import React, { useState } from "react";
import axios from "axios";
import { Button, Card, Label, Textarea, TextInput, Progress, Alert } from "flowbite-react";
import videoLogo from "../data/upload.png";  // Ensure you have the correct path for your image

function VideoUpload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [meta, setMeta] = useState({
    title: "",
    description: "",
  });
  const [progress, setProgress] = useState(0);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");

  function handleFileChange(event) {
    setSelectedFile(event.target.files[0]);
  }

  function formFieldChange(event) {
    setMeta({
      ...meta,
      [event.target.name]: event.target.value,
    });
  }

  function handleForm(formEvent) {
    formEvent.preventDefault();
    if (!selectedFile) {
      alert("Please select a file!");
      return;
    }
    saveVideoToServer(selectedFile, meta);
  }

  function resetForm() {
    setMeta({
      title: "",
      description: "",
    });
    setSelectedFile(null);
    setUploading(false);
    setMessage("");
  }

  async function saveVideoToServer(video, videoMetaData) {
    setUploading(true);

    try {
      let formData = new FormData();
      formData.append("title", videoMetaData.title);
      formData.append("description", videoMetaData.description);
      formData.append("file", video);

      let response = await axios.post(
        `http://localhost:8080/api/v1/videos`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
          onUploadProgress: (progressEvent) => {
            const progress = Math.round(
              (progressEvent.loaded * 100) / progressEvent.total
            );
            setProgress(progress);
          },
        }
      );

      setMessage("File uploaded successfully! Video ID: " + response.data.videoId);
      setUploading(false);
      resetForm();
    } catch (error) {
      setMessage("Error uploading file.");
      setUploading(false);
    }
  }

  return (
    <div className="text-white">
      <Card className="flex flex-col items-center justify-center">
        <h1>Upload Videos</h1>

        <div>
          <form
            noValidate
            className="flex flex-col space-y-6"
            onSubmit={handleForm}
          >
            <div>
              <div className="mb-2 block">
                <Label htmlFor="title" value="Video Title" />
              </div>
              <TextInput
                value={meta.title}
                onChange={formFieldChange}
                name="title"
                placeholder="Enter title"
                required
              />
            </div>

            <div className="max-w-md">
              <div className="mb-2 block">
                <Label htmlFor="description" value="Video Description" />
              </div>
              <Textarea
                value={meta.description}
                onChange={formFieldChange}
                name="description"
                placeholder="Write video description..."
                required
                rows={4}
              />
            </div>

            <div className="flex items-center space-x-5 justify-center">
              <div className="shrink-0">
                <img
                  className="h-16 w-16 object-cover"
                  src={videoLogo}
                  alt="Current profile photo"
                />
              </div>
              <label className="block">
                <span className="sr-only">Choose video file</span>
                <input
                  name="file"
                  onChange={handleFileChange}
                  type="file"
                  className="block w-full text-sm text-slate-500
                    file:mr-4 file:py-2 file:px-4
                    file:rounded-full file:border-0
                    file:text-sm file:font-semibold
                    file:bg-violet-50 file:text-violet-700
                    hover:file:bg-violet-100"
                  required
                />
              </label>
            </div>

            <div>
              {uploading && (
                <Progress
                  color="green"
                  progress={progress}
                  textLabel="Uploading"
                  size={"lg"}
                  labelProgress
                  labelText
                />
              )}
            </div>

            <div>
              {message && (
                <Alert
                  color={message.includes("successfully") ? "success" : "failure"}
                  rounded
                  withBorderAccent
                  onDismiss={() => setMessage("")}
                >
                  <span className="font-medium">{message}</span>
                </Alert>
              )}
            </div>

            <div className="flex justify-center">
              <Button disabled={uploading} type="submit">
                Submit
              </Button>
            </div>
          </form>
        </div>
      </Card>
    </div>
  );
}

export default VideoUpload;
