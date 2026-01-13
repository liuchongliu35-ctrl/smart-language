import { Avatar, Button, Input, Popover, Upload, message } from "antd";
import React, { useState,useRef } from "react";
import style from './writing.module.css'
import { useNavigate } from "react-router-dom";
import Quit from '../image/退出.png'
import { request } from "../utils/request";
import { UploadOutlined } from '@ant-design/icons';




  function postCompositionAPI(value) {
    return request({
      url: '/write/result',
      method: 'POST',
      data:value
    })
  }

  function postImgAPI(value) {
    return request({
      url: '/write/photo',
      method: 'POST',
      data:value,
      headers: {'Content-Type': 'multipart/form-data'}
    })
  }


const Writing = () =>{

const { TextArea } = Input;
const navigate = useNavigate()
const [sel,setSel] = useState(true)
const [composition,setComposition] = useState('')
const [corretResult,setCorrectResult] = useState('')
const [loadings, setLoadings] = useState(false)
const [selectedFile, setSelectedFile] = useState(null)




const onChange = (e) =>{
  setComposition(e.target.value)
}


  const TextInput = () => (
    <TextArea 
    className={style.input}
     placeholder="请将您的文章复制至此处..."
    value={composition}
    onChange={onChange}
     style={{
        minHeight: 360,
        resize: 'none',
      }}
/>
  )

  const ImgInput = () => {
    const [imgName,setImgName] = useState('')
    const imgInputRef = useRef(null)
    

    const handleFileChange = (event) => {
      setSelectedFile(event.target.files[0])
      setImgName(event.target.files[0].name)
    }

    const handleUploadClick = () => {
      if (imgInputRef.current) {
        imgInputRef.current.click();
      }
    };
    return (
    <div style={{width:600,height:'max-content',paddingBottom:30}}>
        <div style={{width:'max-content',margin:'auto',marginTop:30}}>
              <input ref={imgInputRef} type='file' accept="image" className={style.uploadBtn}  onChange={handleFileChange}/>
              <Button icon={<UploadOutlined />} onClick={()=>handleUploadClick()}>{imgName === '' ? <p>点击上传图片</p> : <p>{imgName}</p>}</Button>
              </div>
              <div style={{width:'max-content',margin:'auto'}}>
              {selectedFile && (<img src={URL.createObjectURL(selectedFile)} alt="Selected file" className={style.preImg} />)}
              </div>
  </div>
  )}

  const handleClick = (e) =>{
    setLoadings(true)
    if(sel){
    handlePostComponent(e)}
    else{
    handlepostImg(e)}
  }

  const handlepostImg = async(img) =>{
    const imgData = new FormData()
    imgData.append('file', img)
    let {data} = await postImgAPI(imgData)
    console.log('成功发送')
    setCorrectResult(data)        
    setLoadings(false)
}

  const handlePostComponent = async (value) =>{
        const item = {
          text:value
        }
        let {data} = await postCompositionAPI(item)
        setCorrectResult(data)        
        setLoadings(false)
  }

    return(
        <div style={{background:'#fff6e87b'}}>
        <div className={style.title}>
        <span className={style.changjing}>作文批改</span>
        <Popover
            content={<div style={{width:60,height:20,textAlign:'center',fontSize:18}}>退出</div>}
        >
        <Avatar src={Quit} size={40}shape='square' className={style.titleBtn} onClick={()=>{navigate('/')}} />
        </Popover>
    </div>

    <div style={{height:160}}></div>
    <div className={style.inputBox}>
        <div style={{flex:1}}>
            <button className={sel?style.focusBtn:style.typeBtn} style={{marginTop:100}} onClick={()=>setSel(true)} >文本输入</button>
            <button className={sel?style.typeBtn:style.focusBtn} onClick={()=>setSel(false)}>图片输入</button>
        </div>
        
        {sel?<TextInput />:<ImgInput />}

 


        <div style={{flex:1}}></div>

    </div>
    <div style={{width:'max-content',margin:'auto'}}>
    {!loadings && <Button className={style.btn} onClick={()=>handleClick(sel?composition:selectedFile)}>点击批阅</Button>}
    {loadings && <Button className={style.btn} loading>AI正在批阅中</Button> }
    </div>

    <div className={style.correctContent}>
     <div>{corretResult[0]}</div>
     <div>{corretResult[1]}</div>
     <div>{corretResult[2]}</div>
     <div>{corretResult[3]}</div>
     <div>{corretResult[4]}</div>
     <div>{corretResult[5]}</div>
     <div>{corretResult[6]}</div>
     <div>{corretResult[7]}</div>
     <div>{corretResult[8]}</div>
     <div>{corretResult[9]}</div>
     <div>{corretResult[10]}</div>
     <div>{corretResult[11]}</div>
     <div>{corretResult[12]}</div>
     <div>{corretResult[13]}</div>
     <div>{corretResult[14]}</div>
     <div>{corretResult[15]}</div>
    </div>
    <div style={{height:40}}></div>
    </div>
    )
}

export default Writing