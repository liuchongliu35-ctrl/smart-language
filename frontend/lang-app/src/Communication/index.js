// eslint-disable-next-line
import style from './communication.module.css'
import React, { useEffect, useRef, useState } from 'react'
import { Avatar, Popover, message, Modal } from 'antd';
import { RobotOutlined, AudioOutlined, SendOutlined, PlusCircleFilled } from '@ant-design/icons';//创建IconFont 对象需要的方法
import Audio from '../image/喇叭.png'
import Playing from '../image/播放中.png'
import Boy from '../image/男孩头像.png'
import Robot from '../image/robot.png'
import { Input } from "antd"
import Qipao from '../image/气泡.png'
import Quit from '../image/退出橙色.png'
import { useNavigate } from 'react-router-dom'
import Example from '../image/例子音频.mp3'
import Logo from '../image/ZhiDuoYuLogo.png'
import RobotAvatar from '../image/机器人2.png'
import Translation from '../Translation'
import TranRobot from '../image/翻译机器人.png'

const MessageList = [
  {
    replay: '안녕하세요, 무엇을 도와 드릴까요?',
    translation: '您好，请问有什么我能帮助您？',
    replaySoundPath: Example,
    type: 'replay'
  }]

const WaitingReplay = () => (
  <div style={{ display: 'flex', marginBottom: 30 }}>
    <div className={style.avatar} ><Avatar src={RobotAvatar} icon={<RobotOutlined />} size={32} /></div>
    <div className={style.leftMessageBox} style={{ height: 20.6, width: 70 }}>
      <div style={{ display: 'flex', marginTop: 4 }}>
        <div className={style.dot1}></div>
        <div className={style.dot2}></div>
        <div className={style.dot3}></div>
      </div>
    </div>
  </div>
)



const Communication = () => {
  const [messageData, setMessageData] = useState(MessageList);
  const [inputValue, setInputValue] = useState('')
  const [ws, setWs] = useState(null)
  const [identity, setIdentity] = useState('')
  const [isWait, setIsWait] = useState(false)
  const [isListOpen, setIsListOpen] = useState(false)

  const listRef = useRef(null);

  const navigate = useNavigate()

  const listBackgroundMusic = useRef(null)
  const { TextArea } = Input

  const handleListCancel = () => {
    setIsListOpen(false)
  }

  useEffect(() => {
    //监听窗口
    if (listRef.current) {
      const list = listRef.current;
      list.scrollTop = list.scrollHeight;
    }
  }, [isWait])

  useEffect(() => {

    // 创建一个新的WebSocket连接
    const socket = new WebSocket('ws://10.33.37.106:8086/websocket');
    // 设置WebSocket状态
    setWs(socket)

    // 当WebSocket连接打开时的处理程序
    socket.onopen = (event) => {
      console.log('WebSocket连接已打开', event);

    }
    socket.addEventListener('message',
      (event) => {
        // console.log('收到来自服务器的消息：', event.data.replaySoundPath); 
        const data = JSON.parse(event.data)
        setMessageData(prevMessageData => [...prevMessageData, data])
        setIsWait(false)
      })


    // 当WebSocket连接关闭时的处理程序
    socket.onclose = (event) => {
      console.log('WebSocket连接已关闭', event);
    };

    // 当发生错误时的处理程序
    socket.onerror = (error) => {
      console.error('WebSocket错误：', error);
    };

    // 返回一个清理函数，用于在组件卸载时关闭WebSocket连接
    return () => {
      if (socket) {
        socket.close();
      }
    }
  }, []);

  const sendMessage = (i) => {
    setIsWait(true)
    const sendmessages = {
      uid: '1',
      text: i,
      system: `${identity}`,
      to: 'ko'
    }
    const jsonData = JSON.stringify(sendmessages);
    if (ws) {
      ws.send(jsonData)
    } else {
      console.error('WebSocket连接未建立');
    }
  }


  const onChange = (e) => {
    setIdentity(e.target.value)
  }

  //获取输入框的值
  const handleInputChange = (e) => {
    setInputValue(e.target.value);
  }

  const handleRecreate = () => {
    window.location.reload()
  }

  //新增内容
  const handleAddMessage = () => {
    if (identity === '') {
      message.error('请先选择身份！')
    } else {
      const newMessage = {
        type: 'text',
        text: inputValue
      };
      if (inputValue === '') {
        message.error('输入内容不能为空！')
      } else {
        setMessageData(prevMessageData => [...prevMessageData, newMessage])
        setInputValue(''); // 清空Input的值 
        sendMessage(inputValue);
      }
    }
  }

  //回车发送
  const handleKeyDown = (e) => {
    if (e.keyCode === 13) { // 检测是否按下回车键
      handleAddMessage() // 按下回车键时执行添加消息的操作
      e.preventDefault() //按下回车键不产生\n
    }
  }

  //播放音频函数
  const playAudio = (audioSrc, index) => {
    console.log(audioSrc)
    listBackgroundMusic.current.src = `http://10.33.37.106:8080/student/mp3?soundName=${audioSrc}`;
    listBackgroundMusic.current.play()
    // setIsPlaying(true)
    const updatedMessageData = messageData.map((item, i) => {
      if (i === index) {
        return { ...item, isPlaying: true };
      }
      return { ...item, isPlaying: false };
    });
    setMessageData(updatedMessageData);
  }

  // 音频结束后 
  const handleAudioEnded = () => {
    const updatedYinbiaoData = messageData.map(item => ({ ...item, isPlaying: false }));
    setMessageData(updatedYinbiaoData);
  };

  //暂停音频逻辑
  const pauseAudio = () => {
    listBackgroundMusic.current.pause();
    const updatedYinbiaoData = messageData.map(item => ({ ...item, isPlaying: false }));
    setMessageData(updatedYinbiaoData);
  }



  return (
    <>
      <div style={{ width: '100%', height: 724, background: '#fff', display: 'flex' }}>

        <Popover
          content={<div style={{ width: 60, height: 20, textAlign: 'center', fontSize: 18 }}>退出</div>}
        >
          <Avatar src={Quit} size={40} shape='square' className={style.titleBtn} onClick={() => { navigate('/') }} />
        </Popover>
        <div style={{ flex: 1, padding: 15, borderRadius: 10, background: '#3c3c3c', borderRight: '3px solid rgba(255,151,75)', height: '100vh' }}>
          <div style={{ width: 'max-content', margin: 'auto', marginTop: 30 }}>
            <img src={Logo} alt='1' style={{ width: 100 }} />
          </div>
          <div className={style.translateBox} onClick={() => setIsListOpen(true)}>
            <img src={TranRobot} alt='图片' style={{ width: 100 }} className={style.robotTran} />
          </div>

          <div className={style.idyBox}>
            <div className={style.idySel}>输入身份</div>
            <Input className={style.input} placeholder='请输入...' onChange={onChange} value={identity} />
            <div className={style.example}>例如：你是一位医生.</div>
          </div>
          <div className={style.xinjian} onClick={() => handleRecreate()}><PlusCircleFilled style={{ fontSize: 23 }} /> 新建对话</div>
        </div>
        <div className={style.communicationBody}>
          <div className={style.title}>
            情景对话
          </div>
          <div style={{ width: '100%', position: 'relative' }}>
            <img src={Robot} className={style.robot} alt='#'></img>
            <img src={Qipao} className={style.qipao} alt='#'></img>
            <div className={style.qipaoText}>Hello!! Start talking!</div>
            <div className={style.box}>
              <div className={style.dialogueBox} ref={listRef}>
                {messageData.map((item, index) => (
                  <>
                    {item.type === 'replay' && <div style={{ display: 'flex', marginBottom: 30 }} key={index}>
                      <div className={style.avatar} ><Avatar src={RobotAvatar} icon={<RobotOutlined />} size={32} /></div>
                      <div className={style.leftMessageBox}>
                        <span>{item.replay}</span>
                        <div className={style.translation}>{item.translation}</div>
                        <div className={style.audio}>
                          <Avatar src={item.isPlaying ? Playing : Audio} style={{ cursor: 'pointer' }} size={20} onClick={() => item.isPlaying ? pauseAudio() : playAudio(item.replaySoundPath, index)} />
                          <audio
                            ref={listBackgroundMusic}
                            preload='auto'
                            onEnded={handleAudioEnded}
                          />
                        </div>
                      </div>
                    </div>}
                    {item.type === 'text' && <>
                      <div style={{ display: 'flex', marginBottom: 30, justifyContent: 'flex-end' }} key={index}>
                        <div className={style.rightMessageBox}>
                          <span>{item.text}</span>

                        </div>
                        <div className={style.avatar}><Avatar src={Boy} size={32} /></div>

                      </div>
                    </>}
                  </>
                ))}
                {isWait && <WaitingReplay />}
              </div>
            </div>
            <div className={style.textBox}>
              <TextArea rows={4} style={{ padding: 10, height: 80, width: 730, margin: 'auto' }} placeholder='请输入韩语或汉语 · · ·' value={inputValue} onChange={handleInputChange} onKeyDown={handleKeyDown} />
              <div style={{ height: 50, width: 730, margin: 'auto', display: 'flex', marginTop: 10 }}>
                <div style={{ flex: 1 }}></div>
                <button className={style.btn} onClick={handleAddMessage}><SendOutlined /></button>
                <button className={style.btn}><AudioOutlined style={{ fontSize: 14 }} /></button>
              </div>
            </div>
          </div>
        </div>
      </div>


      <Modal
        width={850}
        open={isListOpen}
        onCancel={handleListCancel}
        cancelButtonProps={{ style: { display: 'none' } }}
        okButtonProps={{ style: { display: 'none' } }}
      >
        <Translation />
      </Modal>
    </>
  )
}

export default Communication