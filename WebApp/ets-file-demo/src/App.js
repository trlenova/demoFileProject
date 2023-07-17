import logo from './logo.svg';
import './App.css';
import React, { useState,useEffect } from 'react';
import axios from 'axios';

function App() {

  const [file, setFile] = useState()
  const [uploadedFileURL, setUploadedFileURL] = useState(null)
  const [data, setData] = useState([]);
  
  

  useEffect(() => {
    axios.get("http://localhost:5000/file/getFileList", {}, {}).then((response) => {
      setData(Object.values(response.data.data));
    });
  }, []);
  
  
  
  function handleChange(event) {
    setFile(event.target.files[0])
  }

  function handleSubmit(event) {
    debugger;
    event.preventDefault()
    const url = 'http://localhost:5000/file/uploadFile';
    const formData = new FormData();
    formData.append('file', file);
    formData.append('fileName', file.name);
    formData.append('fileType', file.type);
    const config = {
      headers: {
        'content-type': 'multipart/form-data',
      },
    };
    axios.post(url, formData, config).then((response) => {
      setUploadedFileURL(response.data.fileUrl);
    });


  }

  return (
    <div className="App">
      <form onSubmit={handleSubmit}>
        <h1>React File Upload</h1>
        <input type="file" onChange={handleChange} />
        <button type="submit">Upload</button>
      </form>
      {uploadedFileURL && <img src={uploadedFileURL} alt="Uploaded content" />}

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
          </tr>
        </thead>
        <tbody>
          {data && data.map((item) => (
            <tr key={item.id}>
              <td>{item.fileName}</td>
              <td>{item.fileType}</td>
              <td>{item.filePath}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
export default App;