// Prevents additional console window on Windows in release, DO NOT REMOVE!!
#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]
#![feature(async_closure)]

use std::{env::temp_dir, error::Error, io::Cursor, path::PathBuf, process::Stdio, sync::{Arc, Mutex}};
use regex::Regex;
use tauri::{AppHandle, Manager, State, Wry};
use tokio::{fs::{create_dir_all, File}, io::{copy, AsyncBufReadExt, AsyncWriteExt, BufReader}};

use buildtools::{BuildToolsArgument, BuildToolsTask, CompilationTarget};

mod buildtools;

const BUILD_TOOLS_LATEST_JAR_URL: &str = "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar";
const SPIGOT_VERSIONS_URL: &str = "https://hub.spigotmc.org/versions/";

#[derive(Clone, Debug)]
struct BuildToolsGUIState {
    args: Arc<Mutex<Vec<BuildToolsArgument>>>,
    working_directory: Arc<Mutex<PathBuf>>
}

#[tauri::command]
async fn spawn_buildtools(app: AppHandle<Wry>, state: State<'_, BuildToolsGUIState>) -> Result<(), ()> {
    let working_directory = state.working_directory.lock().unwrap().clone();
    let mut jar_file = working_directory.clone();
    jar_file.push(PathBuf::from("BuildTools.jar"));
    let task = BuildToolsTask { jar_file, args: state.args.lock().unwrap().clone() };
    let mut command = task.command();
    command.current_dir(state.working_directory.lock().unwrap().clone());
    command.stdout(Stdio::piped());
    let mut child = command.spawn().expect("failed spawning process");
    let stdout = child.stdout.take().expect("failed retrieving child stdout");
    let mut reader = BufReader::new(stdout).lines();

    tokio::spawn(async move {
        while let Ok(Some(line)) = reader.next_line().await {
            app.emit_all("log-line", line).expect("failed sending line to frontend");
        }
    });

    let status = child.wait().await.expect("waiting on child failed");
    println!("buildtools exited with status: {}", status);

    Ok(())
}

fn set_basic_arg(state: State<'_, BuildToolsGUIState>, argument: BuildToolsArgument, enabled: bool) {
    if enabled {
        state.args.lock().unwrap().push(argument);
    } else {
        let mut args = state.args.lock().unwrap();
        *args = args.clone().into_iter().filter(|arg| arg != &argument).collect();
    }
}

#[tauri::command]
fn set_remapped(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::Remapped, enabled)
}

#[tauri::command]
fn set_rev(state: State<'_, BuildToolsGUIState>, rev: String) {
    let mut args = state.args.lock().unwrap();
    *args = args.clone().into_iter().filter(|arg| {
        match arg {
            BuildToolsArgument::Rev(_) => false,
            _ => true
        }
    }).collect::<Vec<BuildToolsArgument>>();
    args.push(BuildToolsArgument::Rev(rev))
}

#[tauri::command]
fn set_disable_cert(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::DisableCert, enabled)
}

#[tauri::command]
fn set_disable_java_check(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::DisableJavaCheck, enabled)
}

#[tauri::command]
fn set_dont_update(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::DontUpdate, enabled)
}

#[tauri::command]
fn set_skip_compile(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::SkipCompile, enabled)
}

#[tauri::command]
fn set_generate_source(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::GenerateSource, enabled)
}

#[tauri::command]
fn set_generate_docs(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::GenerateDocs, enabled)
}

#[tauri::command]
fn set_dev(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::Dev, enabled)
}

#[tauri::command]
fn set_experimental(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::Experimental, enabled)
}

#[tauri::command]
fn set_output_dir(state: State<'_, BuildToolsGUIState>, output_dir_full_path: String) {
    let path = PathBuf::from(output_dir_full_path);

    let mut args = state.args.lock().unwrap();
    *args = args.clone().into_iter().filter(|arg| {
        match arg {
            BuildToolsArgument::OutputDir(_) => false,
            _ => true
        }
    }).collect::<Vec<BuildToolsArgument>>();
    args.push(BuildToolsArgument::OutputDir(path))
}

#[tauri::command]
fn set_final_name(state: State<'_, BuildToolsGUIState>, final_name: String) {
    let mut args = state.args.lock().unwrap();
    *args = args.clone().into_iter().filter(|arg| {
        match arg {
            BuildToolsArgument::FinalName(_) => false,
            _ => true
        }
    }).collect::<Vec<BuildToolsArgument>>();
    args.push(BuildToolsArgument::FinalName(final_name))
}

#[tauri::command]
fn set_pull_request(state: State<'_, BuildToolsGUIState>, repo: String, id: u16) {
    let mut args = state.args.lock().unwrap();
    *args = args.clone().into_iter().filter(|arg| {
        match arg {
            BuildToolsArgument::PullRequest(_, _) => false,
            _ => true
        }
    }).collect::<Vec<BuildToolsArgument>>();
    args.push(BuildToolsArgument::PullRequest(repo, id))
}

#[tauri::command]
fn set_compile(state: State<'_, BuildToolsGUIState>, targets: Vec<String>) {
    let target_enums = targets.into_iter().map(|target| CompilationTarget::from_string(target)).collect::<Vec<CompilationTarget>>();
    let mut args = state.args.lock().unwrap();
    *args = args.clone().into_iter().filter(|arg| {
        match arg {
            BuildToolsArgument::Compile(_) => false,
            _ => true
        }
    }).collect::<Vec<BuildToolsArgument>>();
    args.push(BuildToolsArgument::Compile(target_enums))
}

#[tauri::command]
fn set_compile_if_changed(state: State<'_, BuildToolsGUIState>, enabled: bool) {
    set_basic_arg(state, BuildToolsArgument::CompileIfChanged, enabled)
}

#[tauri::command]
fn set_working_directory(state: State<'_, BuildToolsGUIState>, full_path: String) {
    let mut working_directory = state.working_directory.lock().unwrap();
    *working_directory = PathBuf::from(full_path)
}

async fn download_buildtools_jar(path: PathBuf) -> Result<(), Box<dyn Error>> {
    let response = reqwest::get(BUILD_TOOLS_LATEST_JAR_URL).await?;
    let text = response.text().await;
    println!("response: {:#?}", text);
    //let mut file = File::create(path).await?;
    //let bytes = response.bytes().await?;
    //let mut slice = bytes.as_ref();
    
    //copy(&mut slice, &mut file).await?;
    
    Ok(())
}

#[tauri::command]
async fn get_versions() -> Result<Vec<String>, ()> {
    let response = reqwest::get(SPIGOT_VERSIONS_URL).await.expect("failed retrieving version response").text().await.unwrap();
    let pattern = Regex::new(r"([0-9]+\\.[0-9]+\\.[0-9]+|[0-9]+\\.[0-9]+)").unwrap();
    let matches = pattern.find_iter(&*response).map(|regex_match| regex_match.as_str().into()).collect::<Vec<String>>();
    println!("found matches: {:#?} | response: {response}", matches);
    
    Ok(matches)
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn Error>> {
    let mut temp_dir = temp_dir();
    temp_dir.push(PathBuf::from("buildtools-gui"));
    create_dir_all(temp_dir.clone()).await?;
    let mut buildtools_jar = temp_dir.clone();
    buildtools_jar.push(PathBuf::from("BuildTools.jar"));
    //download_buildtools_jar(buildtools_jar).await?;

    tauri::Builder::default()
        .invoke_handler(tauri::generate_handler![
            spawn_buildtools,
            set_remapped,
            set_rev,
            set_disable_cert,
            set_disable_java_check,
            set_dont_update,
            set_skip_compile,
            set_generate_source,
            set_generate_docs,
            set_dev,
            set_experimental,
            set_output_dir,
            set_final_name,
            set_pull_request,
            set_compile,
            set_compile_if_changed,
            set_working_directory,
            get_versions
        ])
        .manage(BuildToolsGUIState {
            args: Arc::new(Mutex::new(vec![BuildToolsArgument::NoGui])),
            working_directory: Arc::new(Mutex::new(temp_dir))
        })
        .run(tauri::generate_context!())
        .expect("error while running tauri application");

    Ok(())
}
