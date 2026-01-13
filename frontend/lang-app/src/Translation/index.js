import React, { useState,useRef,useEffect } from "react";
import style from './translation.module.css'
import { Avatar, Button, Dropdown, Input, Popover, Space, message } from "antd";
import { request } from "../utils/request";
import { AudioOutlined,UploadOutlined } from '@ant-design/icons';
import Right from '../image/右箭头.png'

const {TextArea} = Input





function postAudioAPI(value){
  return request({
    url:'/translate/audio?lang=en',
    method:'POST',
    data:value,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

function postImageAPI(data,from,to){
  return request({
    url:`/translate/photo?from=${from}&to=${to}`,
    method:'POST',
    data:data,
    headers: {'Content-Type': 'multipart/form-data'}
  })
}

function postTextAPI(text,from,to){
  return request({
    url:`/translate/text?text=${text}&from=${from}&to=${to}`,
    method:'GET',
  })
}



      const Translation = () =>{
      const [kind,setKind] = useState('文本翻译')
      const [origin,setOrigin] = useState('汉语')
      const [trans,setTrans] = useState('英语')
      const [clicked, setClicked] = useState(false)
      const [isLoading,setIsloading] = useState(false)
      const [result,setResult] = useState('')
      const [inputFrom,setInputFrom] = useState('cn')
      const [resultTo,setResultTo] = useState('en')

      const handleChange = (e) =>{
        setOrigin(e)
        switch(e){
          case '汉语' :
            setInputFrom('cn')
            break;
          case '韩语' :
            setInputFrom('ko')
            break;
          case '英语' :
            setInputFrom('en')
            break;
          case '日语' :
            setInputFrom('ja')
            break;
          case '法语' :
            setResultTo('fa')
            break;
          default:
            break
        }
      }

      const handleClick = (e) =>{
        setTrans(e)
        switch(e){
          case '汉语' :
            setResultTo('cn')
            break;
          case '韩语' :
            setResultTo('ko')
            break;
          case '英语' :
            setResultTo('en')
            break;
          case '日语' :
            setResultTo('ja')
            break;
          case '法语' :
            setResultTo('fa')
            break;
          default:
            break
        }
      }

      const items = [
        {
          key:'1',
          label:(
            <div onClick={()=>handleChange('汉语')}>汉语</div>
          )
        },{
          key:'2',
          label:(
            <div  onClick={()=>handleChange('韩语')}>韩语</div>
          )
        },{
          key:'3',
          label:(
            <div  onClick={()=>handleChange('英语')}>英语</div>
          )
        },{
          key:'4',
          label:(
            <div  onClick={()=>handleChange('日语')}>日语</div>
          )
        },{
          key:'5',
          label:(
            <div  onClick={()=>handleChange('法语')}>法语</div>
          )
        }
      ]

      const selClick = (e) =>{
          setKind(e)
          setResult('')
      }

      const handleClickChange = (open) => {
        setClicked(open);
      };

      const Aloading = () =>(
        <div style={{display:'flex',marginLeft:10,marginTop:20}}>
        <div className={style.dot1}></div>
        <div className={style.dot2}></div>
        <div className={style.dot3}></div>
        </div>
      )

      const handlepostImg = async(img,from,to) =>{
        setResult('')
        setIsloading(true)
        const imgData = new FormData()
        imgData.append('image', img)
        let {data} = await postImageAPI(imgData,from,to)
        
        
        setTimeout(() => {
          setResult(data)
          setIsloading(false)
        }, 500);
    }

    const handlePostText = async(text,from,to) =>{
      let {data} = await postTextAPI(text,from,to)
      setResult('')
      setIsloading(true)
      setTimeout(() => {
        setResult(data)
        setIsloading(false)
      }, 200);
    }


    const InputComponent = () =>{
      const [text,setText] = useState('')
        
        const onChange = (e) =>{
          setText(e.target.value)
        }

        const handleKeyDown = (e) => {
          if (e.keyCode === 13) { // 检测是否按下回车键
            handlePostText(text,inputFrom,resultTo) // 按下回车键时执行添加消息的操作
            e.preventDefault() //按下回车键不产生\n
          }
        }
  
          return(
            <div>
              <TextArea className={style.input} placeholder="请输入文本" style={{height:250}} value={text} onChange={onChange} onKeyDown={handleKeyDown}/>
              <div style={{display:'flex',justifyContent:'center'}}>
              <button type="submit" className={style.submitBtn} style={{marginTop:10}}  onClick={()=>handlePostText(text,inputFrom,resultTo)}>确 认</button>
              </div>
              </div>
          )
      }

      const ImgComponent = () =>{
        const [selectedFile, setSelectedFile] = useState(null)
        const [imgName,setImgName] = useState('')
        const imgInputRef = useRef(null)
        
        const handleFileChange = (event) => {
          setSelectedFile(event.target.files[0])
          setImgName(event.target.files[0].name)
        }

        console.log(imgName)

        const handleAbandonPost = () =>{
          message.error('请先上传图片！')
        }
        

        const handleUploadClick = () => {
          if (imgInputRef.current) {
            imgInputRef.current.click();
          }
        }
      
        return (
          <div>
            
              <div style={{width:'max-content',margin:'auto',marginTop:30}}>
              <input ref={imgInputRef} type='file' accept="image" className={style.uploadBtn} onChange={handleFileChange}/>
              <Button icon={<UploadOutlined />} onClick={()=>handleUploadClick()}>{imgName === '' ? <p>点击上传图片</p> : <p>{imgName}</p>}</Button>
              </div>
              
              {selectedFile && (<img src={URL.createObjectURL(selectedFile)} alt="Selected file" style={{boxShadow:'0px 0px 3px #3c3c3c',width:200,marginLeft:75,marginTop:30,maxHeight:200}} />)}
              <div style={{width:'max-content',margin:'auto',marginTop:30}}>
              <button type="submit" className={style.submitBtn} onClick={() =>imgName ?  handlepostImg(selectedFile,inputFrom,resultTo) : handleAbandonPost()}>确 认</button>
              </div>
            
            
          </div>
        )
      }

      const SpeechComponent= ()=>{
        const [isSpeak,setIsSpeak] = useState(false)
        const [mediaRecorder, setMediaRecorder] = useState(null);
        const [audioChunks, setAudioChunks] = useState([]);
        const [stream, setStream] = useState(null); // 初始化stream为null,用于处理媒体流
        const [result,setResult] = useState('')
        const audioRef = useRef(null)
        
        
          // 使用useEffect钩子初始化音频元素
        useEffect(() => {
         audioRef.current = new Audio();
          }, []);
  
        const startRecording = () => {
          setIsSpeak(true)
          if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
            alert('MediaDevices API or getUserMedia is not supported in this browser.');
            return;
          }
          navigator.mediaDevices.getUserMedia({ audio: true })
            .then(stream => {
              const mediaRecorder = new MediaRecorder(stream);
              setMediaRecorder(mediaRecorder);
              setStream(stream);
              mediaRecorder.addEventListener('dataavailable', event => {
                setAudioChunks(prev => [...prev, event.data]);
              });
      
              mediaRecorder.addEventListener('stop', () => {
                const audioBlob = new Blob(audioChunks, { type: 'audio/mp3' });
                audioRef.current.src = URL.createObjectURL(audioBlob);
                audioRef.current.controls = true;
              });
      
              mediaRecorder.start();
            })
            .catch(error => console.error('Error:', error));
        }
        
        const stopRecording = () => {
          if (mediaRecorder) {
            setIsSpeak(false)
            mediaRecorder.stop()
            if (stream) {
              // 关闭媒体流
              stream.getTracks().forEach((track) => track.stop())
              setStream(null)
            }
            sendAudioToServer()
          }
          // handlebtnClick()
        }
  
        const sendAudioToServer = async () => {
          const audioBlob = new Blob(audioChunks, { type: 'audio/mp3' });
          const formData = new FormData();
          formData.append('file', audioBlob, 'audio.mp3');  
          postAudio(formData)
         
        }
        
        const postAudio = async (e) =>{
          let {data} = await postAudioAPI(e)
          setResult(data)
          console.log(result)
        }
  
  
  
        
        const FocusText = () =>(
            <>
            <div className={style.audioText}>正在录入语音中</div>
            <div style={{display:'flex',marginLeft:155,marginTop:20}}>
            <div className={style.dot1}></div>
            <div className={style.dot2}></div>
            <div className={style.dot3}></div>
            </div>
            </>
        )
  
        const NoFocusText = () =>(
          <>
          <div className={style.audioText} style={{color:'#000'}}>点击录音</div>
          </>
      )
  
  
  
        return(
            <>
            <div className={isSpeak?style.audioFocus:style.audio} onClick={isSpeak?stopRecording:startRecording}>
                <AudioOutlined  />
            </div>
            {!isSpeak && <NoFocusText />}
            {isSpeak && <FocusText />}     
            </>
        )
    }

      return(
          <>
          <div className={style.selBox}>
              <button onClick={()=>selClick('文本翻译')} className={kind === '文本翻译'?style.btnFocus:style.btn}>文本翻译</button>
              <button onClick={()=>selClick('图像翻译')} className={kind === '图像翻译'?style.btnFocus:style.btn}>图像翻译</button>
              <button onClick={()=>selClick('语音翻译')} className={kind === '语音翻译'?style.btnFocus:style.btn}>语音翻译</button>
          </div>
          <div className={style.langBox}>
          <Dropdown
          menu={{
            items
          }}
        >
          <Button className={style.langBtn}>{origin}</Button>
        </Dropdown>
          <Avatar src={Right} size={20} style={{marginLeft:5,marginRight:5}} />
          <Popover trigger="click" content={<div style={{width:46}}>
               <Space direction="vertical">
                <div className={style.menuSet} onClick={()=>handleClick('英语')}>英语</div>
                <div className={style.menuSet} onClick={()=>handleClick('汉语')}>汉语</div>
                <div className={style.menuSet} onClick={()=>handleClick('韩语')}>韩语</div>
                <div className={style.menuSet} onClick={()=>handleClick('日语')}>日语</div>
                <div className={style.menuSet} onClick={()=>handleClick('法语')}>法 语</div>
               </Space>
               </div>}
               placement="bottom" open={clicked}
               onOpenChange={handleClickChange} >
          <Button className={style.langBtn}>{trans}</Button>
          </Popover>
          </div>
          <div className={style.tranBox}>
          <div style={{flex:1,borderRight:'2px solid #3c3c3c'}} className={style.inputBox}>
              {kind === '文本翻译' &&<InputComponent  />}
              {kind === '图像翻译' && <ImgComponent />}
              {kind === '语音翻译' && <SpeechComponent />}
          </div>
          <div style={{flex:1}} className={style.resultBox}>
          {isLoading && <Aloading />}
          {result}
          </div>
          </div>
          </>
      )
  }


export default Translation