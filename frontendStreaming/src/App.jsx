import "./App.css";
import VideoUpload from "./components/VideoUpload";

function App() {
  return (
    <>
      <div className="flex flex-col items-center space-y-5 justify-center py-9">
        <h1 className="text-2xl font-extrabold text-gray-600 dark:text-gray-100">
           Video Streaming App
        </h1>
        <VideoUpload/>
      </div>
    </>
  );
}

export default App;
