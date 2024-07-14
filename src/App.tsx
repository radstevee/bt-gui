import { useState, useEffect } from "react";
import { invoke } from "@tauri-apps/api/tauri";
import { listen } from "@tauri-apps/api/event";
import "./App.css";

function App() {
  const [log, setLog] = useState("");

  async function spawn() {
    await invoke("spawn_buildtools");
  }
  
  useEffect(() => {
    const unlisten = listen("log-line", (event) => {
      setLog((prevLog) => prevLog + "\n" + event.payload);
    });

    // Clean up the listener on component unmount
    return () => {
      unlisten.then((fn) => fn());
    };
  }, []);

  return (
    <div className="container">
      <button onClick={spawn}>Spawn</button>
      <p>log:</p>
      <br />
      <pre>{log}</pre>
    </div>
  );
}

export default App;
