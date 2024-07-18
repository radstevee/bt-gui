use std::path::PathBuf;

use to_snake_case::ToSnakeCase;
use tokio::process::Command;

pub struct BuildToolsTask {
    pub jar_file: PathBuf,
    pub args: Vec<BuildToolsArgument>,
}

impl BuildToolsTask {
    pub fn command(&self) -> Command {
        let arg_strings: Vec<String> = self.args.iter().map(|arg| arg.to_string()).collect();
        let mut command = Command::new("java");
        command.arg("-jar");
        command.arg(self.jar_file.to_string_lossy().to_string());
        for arg_string in arg_strings {
            arg_string.split(" ").into_iter().for_each(|arg| {
                command.arg(arg);
            });
        }
        command
    }
}

#[derive(Clone, Debug, PartialEq)]
pub enum CompilationTarget {
    None,
    CraftBukkit,
    Spigot,
}
impl CompilationTarget {
    pub fn from_string(input: String) -> Self {
        match &*input {
            "NONE" => CompilationTarget::None,
            "SPIGOT" => CompilationTarget::Spigot,
            "CRAFTBUKKIT" => CompilationTarget::CraftBukkit,
            _ => CompilationTarget::None
        }
    }
}

#[derive(Clone, Debug, PartialEq)]
pub enum BuildToolsArgument {
    Remapped,
    Rev(String),
    DisableCert,
    DisableJavaCheck,
    DontUpdate,
    SkipCompile,
    GenerateSource,
    GenerateDocs,
    Dev,
    Experimental,
    OutputDir(PathBuf),
    FinalName(String),
    PullRequest(String, u16),
    Compile(Vec<CompilationTarget>),
    CompileIfChanged,
    NoGui
}

impl BuildToolsArgument {
    pub fn to_string(&self) -> String {
        return match self {
            BuildToolsArgument::Rev(rev) => format!("--rev {rev}"),
            BuildToolsArgument::Compile(target) => format!(
                "--compile {}",
                format!("{:?}", target)
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "")
                    .to_uppercase()
            ),
            BuildToolsArgument::OutputDir(output) => format!("--output-dir {}", output.to_string_lossy()),
            BuildToolsArgument::PullRequest(repo, id) => format!("--pull-request {repo}:{id}"),
            BuildToolsArgument::FinalName(name) => format!("--final-name {name}"),
            BuildToolsArgument::NoGui => format!("--nogui"),
            _ => format!("--{:?}", self).to_snake_case().replace("_", "-"),
        };
    }
}
