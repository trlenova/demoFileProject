import './App.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function App() {

  const [file, setFile] = useState()
  const [uploadedFileURL, setUploadedFileURL] = useState(null)
  const [data, setData] = useState([]);


  useEffect(() => {
    const fetchToken = async () => {
      try {
        const response = await axios.get('http://localhost:5000/auth/generateToken', {});
        debugger;
        const token = response.data;
        sessionStorage.setItem("token", token);
        getFileList();
      } catch (error) {
        console.error('Token problems:', error);
      }
    };

    fetchToken();

  }, []);

  function getFileList() {
    const config = {
      headers: {
        Authorization: 'Bearer ' + sessionStorage.getItem("token"),
      },
    };
    axios.get("http://localhost:5000/file/getFileList", config).then((response) => {
      setData(Object.values(response.data.data));
    });
  }



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
        'Authorization': 'Bearer ' + sessionStorage.getItem("token")
      },
    };
    axios.post(url, formData, config).then((response) => {
      setUploadedFileURL(response.data.fileUrl);
    });
  }

 


  return (
    <div className="App">
      <form onSubmit={handleSubmit}>
        <h1>File Upload</h1>
        <input type="file" onChange={handleChange} />
        <button type="submit">Upload</button>
      </form>
      {uploadedFileURL && <img src={uploadedFileURL} alt="Uploaded content" />}

      <table className="table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Extension</th>
            <th>Path</th>
            <th>Size</th>
          </tr>
        </thead>
        <tbody>
          {data && data.map((item) => (
            <tr key={item.id}>
              <td>{item.fileName}</td>
              <td>{item.fileType}</td>
              <td>{item.filePath}</td>
              <td>{item.fileSize}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
export default App;