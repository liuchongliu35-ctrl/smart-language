import React, { useState } from "react";
import { request } from "../utils/request";

function App() {
  const [selectedFile, setSelectedFile] = useState(null);

  function postImageAPI(data,from,to){
    return request({
      url:`/translate/photo?from=en&to=cn`,
      method:'POST',
      data:data,
      headers: {'Content-Type': 'multipart/form-data'}
    })
  }
  
  const selecteFileHandler = (event) => {
    setSelectedFile(event.target.files[0]);  // 选择单个文件
  };

  const uploadHandler = () => {
    console.log("The file to be sent is:", selectedFile.name);
	// 将图像发送到后端，使用 fetch 或者 axios 都行
    const formData = new FormData();
    formData.append('image', selectedFile);
    postImageAPI(formData)
  };

  return (
    <div className="App">
      <input type="file" onChange={selecteFileHandler} />
      <button onClick={uploadHandler}>Upload to Server</button>
    </div>
  );
}
export default App;
