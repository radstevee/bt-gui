import { useState, useEffect } from "react";
import { invoke } from "@tauri-apps/api/tauri";
import { listen } from "@tauri-apps/api/event";
import "./App.css";
import { ThemeProvider } from "./components/theme-provider";
import { ScrollArea } from "./components/ui/scroll-area";
import { Button } from "./components/ui/button";
import VersionSelection from "./components/ui/version-selection";
import BreakString from "./components/break-string";

function App() {
  const [log, setLog] = useState<string>(
    "Start BuildTools by clicking the button below!"
  );
  const [versions, setVersions] = useState<string[]>([]);

  async function spawn() {
    setLog("");
    await invoke("spawn_buildtools");
  }

  useEffect(() => {
    // invoke("get_versions").then((response) => {
    //   console.log("received response: " + response)
    //   if(response) setVersions(response as string[]);
    // })
    // temporary until cloudflare unblocks me :D
    setVersions([
      "1.10.2",
      "1.10",
      "1.11.1",
      "1.11.2",
      "1.11",
      "1.12.1",
      "1.12.2",
      "1.12",
      "1.13.1",
      "1.13.2",
      "1.13",
      "1.14.1",
      "1.14.2",
      "1.14.3",
      "1.14.4",
      "1.14",
      "1.15.1",
      "1.15.2",
      "1.15",
      "1.16.1",
      "1.16.2",
      "1.16.3",
      "1.16.4",
      "1.16.5",
      "1.17.1",
      "1.17",
      "1.18",
      "1.18.1",
      "1.18.2",
      "1.19.1",
      "1.19.2",
      "1.19.3",
      "1.19.4",
      "1.19",
      "1.20.1",
      "1.20.2",
      "1.20.3",
      "1.20.4",
      "1.20.5",
      "1.20.6",
      "1.20",
      "1.21",
      "1.8.3",
      "1.8.4",
      "1.8.5",
      "1.8.6",
      "1.8.7",
      "1.8.8",
      "1.8",
      "1.9.2",
      "1.9.4",
      "1.9",
      "latest",
    ]);

    const unlisten = listen("log-line", (event) => {
      setLog(log => log + "\n" + event.payload)
    });

    return () => {
      unlisten.then((fn) => fn());
    };
  }, []);

  return (
    <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
      <h1 className="text-xl font-sans grid place-content-center pt-10">
        BuildTools
      </h1>
      <div className="pl-16">
        <VersionSelection versions={versions} />
      </div>
      <h1 className="text-xl font-sans pl-16 pt-10">Logs</h1>
      <div className="pt-5 pl-16">
        <ScrollArea className="pt-2 pl-2 h-96 w-11/12 bg-gray-800 rounded font-mono">
          <BreakString text={log} />
        </ScrollArea>
      </div>
      <div className="pt-5 absolute right-0 m-4 pr-16">
        <Button
          onClick={spawn}
          className="bg-green-400 text-slate-950 hover:bg-green-500"
        >
          Start BuildTools!
        </Button>
      </div>
    </ThemeProvider>
  );
}

export default App;
