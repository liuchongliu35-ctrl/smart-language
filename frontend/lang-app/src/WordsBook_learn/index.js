import React, { useEffect, useRef, useState } from "react";
import style from './wordLearn.module.css'
import Quit from '../image/退出.png'
import { Avatar, Popover, Progress, Modal, Form, Input, Button } from "antd";
import { useNavigate } from "react-router-dom";
import Audio from '../image/喇叭.png'
import { CheckCircleOutlined, StarOutlined } from '@ant-design/icons';
import { useParams } from 'react-router-dom'
import { request } from "../utils/request";
import Plus from '../image/加.png'




const list = [
  {
    text: '韩文',
    motherText: '中文',
    grammar: {
      graText: ''
    },
    mp3Url: ''
  }
]

//添加自定义单词
function postAddDefineWordAPI(value) {
  return request({
    url: `extend/addWord`,
    method: 'POST',
    data: value
  })
}

const WordsLearn = () => {
  const uid = localStorage.getItem('userID')
  const [wordsList, setWordsList] = useState(list)
  const listBackgroundMusic = useRef(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isPlaying, setIsPlaying] = useState(false)
  const [isListOpen, setIsListOpen] = useState(false)
  const [click, setClick] = useState(true)
  const [refresh, setRefresh] = useState(false)
  // const [form] = Form.useForm()

  const handleListCancel = () => {
    setIsListOpen(false)
  }

  const handleClick = async () => {
    await putWordStatus()
    setClick(prevClick => !prevClick)
  }

  //修改单词的掌握状态
  // eslint-disable-next-line
  function putWordStatus() {
    return request({
      url: `/wordBook/status`,
      method: 'POST',
      data: {
        cid: `${wordsList[currentIndex].cid}`,
        state: `${wordsList[currentIndex].state}`,
        text: `${wordsList[currentIndex].text}`,
      }
    })
  }

  function langChange() {
    switch (uid) {
      case '1':
        return 'ENGLISH'
      case '2':
        return 'KOREAN'
      case '3':
        return 'JAPANESE'
      case '4':
        return 'FRENCH'
      default:
        break
    }
  }

  function langText() {
    switch (uid) {
      case '2':
        return '韩语'
      case '3':
        return '日语'
      case '4':
        return '法语'
      default:
        break;
    }
  }

  function getWordsListAPI() {
    return request({
      url: `/wordBook/words?uid=${uid}&type=${clamp}`,
      method: 'GET'
    })
  }

  useEffect(() => {
    // 在这里可以执行其他需要在状态更新之后执行的命令
    setTimeout(() => {
      setRefresh(prevClick => !prevClick)
    }, 5000)
    getList()
  }, [click, refresh])

  // eslint-disable-next-line
  const getList = async () => {
    let { data } = await getWordsListAPI()
    setWordsList(data)
  }

  //获取路由传的参
  let { clamp } = useParams()
  let { cid } = useParams()

  const navigate = useNavigate()
  const [percent, setPercent] = useState(0)
  const increase = () => {
    console.log(wordsList[currentIndex].grammar)
    setPercent((prevPercent) => {
      const newPercent = prevPercent + 100 / wordsList.length;

      if (newPercent > 100 - 100 / wordsList.length) {
        return 0;
      }
      return newPercent;
    });
    setCurrentIndex(currentIndex === wordsList.length - 1 ? 0 : currentIndex + 1);
  };
  const decline = () => {
    setPercent((prevPercent) => {
      const newPercent = prevPercent - 100 / wordsList.length;
      if (newPercent < 100 / wordsList.length) {
        return 100 - 100 / wordsList.length;
      }
      return newPercent;
    });
    setCurrentIndex(currentIndex === 0 ? wordsList.length - 1 : currentIndex - 1);
  }


  //自定义添加单词表单提交
  const handleAddWordFinish = async (values) => {
    const obj = {
      cid: cid,
      text: values.text,
      motherText: values.motherText
    }
    await postAddDefineWordAPI(obj)
    setTimeout(() => {
      handleListCancel()
      window.location.reload()
    }, 5000);

  }

  //播放音频函数
  const playAudio = () => {
    listBackgroundMusic.current.src = `http://10.33.37.106:8080/student/mp3?word=${wordsList[currentIndex].text}&cid=${cid}&lang=${wordsList[currentIndex].lang}`
    listBackgroundMusic.current.play()
    setIsPlaying(true)
  }

  // 音频结束后 
  const handleAudioEnded = () => {
    setIsPlaying(false)
  };

  //暂停音频逻辑
  const pauseAudio = () => {
    listBackgroundMusic.current.pause();
    setIsPlaying(false)
  };

  return (
    <>
      <div className={style.title}>
        <span className={style.changjing}>场景 —— </span><span style={{ fontSize: 30, fontFamily: 'youshe', color: 'rgba(255,151,75)' }}>{clamp}</span>
        <span style={{ fontSize: 40 }}>&nbsp; /</span><span className={style.changjing} style={{ marginLeft: 10, fontSize: 20 }}>{langChange()}</span>
        <Popover
          content={<div style={{ width: 60, height: 20, textAlign: 'center', fontSize: 18 }}>退出</div>}
        >
          <Avatar src={Quit} size={40} shape='square' className={style.titleBtn} onClick={() => { navigate('/wordsbook') }} />
        </Popover>
        <Popover
          content={<div style={{ width: 'max-content', height: 20, textAlign: 'center', fontSize: 15 }}>自定义添加单词</div>}
        >
          <Avatar src={Plus} size={40} shape='square' className={style.titleBtn} style={{ marginRight: 80 }} onClick={() => setIsListOpen(true)} />
        </Popover>
      </div>
      <div className={style.box}>
        <div className={style.progressBox}>
          <Progress percent={percent} showInfo={false} type="line" style={{ width: '90%' }} size={[750, 20]} strokeColor="rgba(228,167,2)" />
        </div>
        <div style={{ display: 'flex' }}>
          <div className={style.displayArea}>
            <div className={style.dataBox}>
              <div>
                <div style={{ fontSize: 18, color: 'rgba(55,55,55)' }}>{langText()}/{langChange()}</div>
                <div style={{ fontSize: 55, fontWeight: 600, marginBottom: 30 }}>
                  {wordsList[currentIndex].text}
                  <Avatar src={Audio} size={24} style={{ marginLeft: 50 }}
                    className={isPlaying ? style.audioPlay : style.audioPause}
                    onClick={() => isPlaying ? pauseAudio() : playAudio()}
                  />
                  <audio
                    ref={listBackgroundMusic}
                    preload='auto'
                    onEnded={handleAudioEnded}
                  />
                </div>
                <div style={{ fontSize: 16, color: 'rgba(55,55,55)', marginBottom: 10 }}>中文/CHINESE</div>
                <div style={{ fontSize: 30, fontWeight: 600, marginBottom: 30 }}>{wordsList[currentIndex].motherText}</div>
                <div style={{ width: 400, height: 3, background: '#eaeaea' }}></div>
                <div>

                </div>
              </div>

            </div>
          </div>

          <div className={style.functionArea}>
            <div style={{ width: 'max-content', margin: 'auto' }}>
              <button className={style.btn} onClick={decline} style={{ marginBottom: 20 }}>上一个</button>
            </div>
            <div style={{ width: 'max-content', margin: 'auto' }}>
              <button className={style.btn} onClick={increase}>下一个</button>
            </div>
            <div>
              <Popover
                content={<div style={{ width: 'max-content', height: 20, textAlign: 'center', fontSize: 12 }}>标记为已掌握</div>}
              >
                <CheckCircleOutlined className={wordsList[currentIndex].state === 0 ? style.noRemember : style.remember} onClick={() => handleClick()} />
              </Popover>
              <Popover
                content={<div style={{ width: 60, height: 20, textAlign: 'center', fontSize: 12 }}>收藏该单词</div>}
              >
                <StarOutlined className={style.shoucang} />
              </Popover>
            </div>
          </div>
        </div>
        {/* <div className={style.grammerBar}><span className={style.grammerLable}>词义：</span><span className={style.grammerContent}>依赖，依靠；取决于，视…而定</span></div> */}
        <div className={style.grammarBox}>

          {wordsList[currentIndex]?.grammar?.length > 0 && wordsList[currentIndex].grammar[0] ? (
            wordsList[currentIndex].grammar.map((grammarItem, index) => (
              <div key={index} className={style.grammerBar}>
                {grammarItem}
              </div>
            ))
          ) : (
            <>
              <div className={style.grammerBarLoading}>等待AI生成语法</div>
              <div style={{ display: 'flex', width: 'max-content', margin: 'auto', marginTop: 30 }}>
                <div class={style.dot1}></div>
                <div class={style.dot2}></div>
                <div class={style.dot3}></div>
              </div>
            </>
          )}



        </div>
      </div>
      <Modal
        width={400}
        open={isListOpen}
        onCancel={handleListCancel}
        cancelButtonProps={{ style: { display: 'none' } }}
        okButtonProps={{ style: { display: 'none' } }}
      >
        <div style={{ fontFamily: 'youshe', width: 'max-content', margin: 'auto', fontSize: 24 }}>自定义添加单词</div>
        <div style={{ height: 30 }}></div>
        <Form
          onFinish={handleAddWordFinish}
          name="basic"
          style={{ width: 200, margin: 'auto', marginTop: 30, }}

        >
          <Form.Item
            label={`${langText()}`}
            name="text"
            rules={[
              {
                required: true,
                message: `必需填写${langText()}`,
              }]}
          >
            <Input placeholder={`请输入${langText()}`} />
          </Form.Item>
          <Form.Item
            label="中文"
            name="motherText"
            rules={[
              {
                required: true,
                message: '必需填写中文',
              }]}
          >
            <Input placeholder="请输入中文" />
          </Form.Item>
          <Form.Item
            label="语法"
            name="grammar"
          >
            <Input placeholder="请输入该单词相关语法" />
          </Form.Item>
          <Form.Item
            label="发音"
            name="voice"
          >
            <Input placeholder="请输入发音" />
          </Form.Item>
          <Form.Item>
            <div style={{ width: 'max-content', margin: 'auto' }}>
              <Button type="primary" htmlType="submit" style={{ width: 120, height: 40, borderRadius: 10, background: 'rgba(255,187,0)', color: '#fff', fontFamily: 'youshe', fontSize: 20, border: 'none', cursor: 'pointer' }}>确定添加</Button>
            </div>
          </Form.Item>
        </Form>

      </Modal>
    </>
  )
}


export default WordsLearn