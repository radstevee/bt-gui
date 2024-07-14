// Prevents additional console window on Windows in release, DO NOT REMOVE!!
#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]
#![feature(async_closure)]

use std::{path::PathBuf, process::Stdio};
use tauri::{AppHandle, Manager, Wry};
use tokio::io::{AsyncBufReadExt, BufReader};

use buildtools::{BuildToolsArgument, BuildToolsTask, CompilationTarget};

mod buildtools;

#[tauri::command]
async fn spawn_buildtools(app: AppHandle<Wry>) {
    let args = vec![
        BuildToolsArgument::Remapped,
        BuildToolsArgument::Rev("1.21".into()),
        BuildToolsArgument::OutputDir(PathBuf::from(r"/tmp/buildtools")),
        BuildToolsArgument::Compile(vec![CompilationTarget::Spigot, CompilationTarget::CraftBukkit]),
    ];
    let file = PathBuf::from(r"/home/radsteve/buildtools/BuildTools.jar");
    let task = BuildToolsTask { jar_file: file, args };
    let mut command = task.command();
    command.current_dir("/tmp/buildtools");
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
}

#[tokio::main]
async fn main() {
    tauri::Builder::default()
        .invoke_handler(tauri::generate_handler![spawn_buildtools])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
