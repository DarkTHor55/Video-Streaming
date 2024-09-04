import { Toaster } from "react-hot-toast";
import "./App.css";
import VideoUpload from "./components/VideoUpload";
import { useState } from "react";

function App() {
  const [videoId, setVideoId] = useState(
    "922bc808-2223-440b-9227-ca54514cef9d"
  );
  return (
    <>
      <Toaster />
      <div className="flex flex-col items-center justify-center py-9 bg-gray-900 min-h-screen">
        <h1 className="text-3xl font-bold text-white mb-10">
          Video Streaming App
        </h1>

        <div className="flex w-full max-w-6xl space-x-8 justify-between">
          <div className="w-1/2">
            <h1 className="text-white text-center text-xl mb-4">
              Playing Video
            </h1>filepath
            <video
              className="w-full rounded-lg shadow-lg"
              src={`http://localhost:8080/api/v1/videos/stream/range/${videoId}`}
              controls
            ></video>
          </div>

          <div className="w-1/2">
            <VideoUpload />
          </div>
        </div>
      </div>
    </>
  );
}

export default App;
