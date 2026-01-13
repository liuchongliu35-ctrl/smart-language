// eslint-disable-next-line
import React, { useEffect, useRef, useState } from 'react';
import style from './yinbiao.module.css'
import Fu from '../image/例子音频.mp3'
import Audio from '../image/喇叭.png'
import { Avatar, Popover, } from 'antd';
import { request } from '../utils/request';
import {HeatMapOutlined} from '@ant-design/icons'
import Book from '../image/book.png'
import Quit from '../image/退出.png'
import { useNavigate } from 'react-router-dom';


const yinbiaoList = [
    {
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },{
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },{
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },{
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },{
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },{
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },{
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },{
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },{
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },{
        symbol:'ㅢ',
        pronunciation:'o (vowel)',
        waveUrl:Fu,
        example:'한',
        translation:'韩'
    },
]

function langChange(e){
    switch(e){
        case '1' :
            return 'en'
        case '2' :
            return 'ko'
        case '3' :
            return 'ja'
        case '4' :
            return 'fr'
        default :
        break
    }
}

//请求音标数据
function getYinBiaoAPI(e) {
    return request({
      url: `/student/primerWords?lang=${langChange(e)}`,
      method: 'GET'
    })
  }

const Yinbiao = () => {
    const [yinbiaoData, setYinbiaoData] = useState(yinbiaoList);
    const [top,setTop]=useState(0)
    const listBackgroundMusic = useRef(null);
    const frameY = useRef(null)
    const uid = localStorage.getItem('userID')

    
    useEffect(() => {
        // 在这里可以执行其他需要在状态更新之后执行的命令
        frameY.current.style.transform = `translateY(${top}px)`;
        getYinBiao()
    }, [top]);

    // eslint-disable-next-line
    const getYinBiao = async () =>{
        let {data} = await getYinBiaoAPI(uid)
        setYinbiaoData(data)
        console.log(data)
    }

    const navigate = useNavigate()

    //下一页
    const handleFrameNext = () =>{
        setTop(prevTop => prevTop - 380)
    }
  
    //上一页
    const handleFramePrevious = () =>{
        setTop(prevTop => prevTop + 380)
    }

    //播放音频函数
    const playAudio = (symbol,index) =>{
        listBackgroundMusic.current.src = `http://10.100.85.4:8080/student/mp3?word=${symbol}`
        listBackgroundMusic.current.play()
        // setIsPlaying(true)
        const updatedYinbiaoData = yinbiaoData.map((item, i) => {
            if (i === index) {
                return { ...item, isPlaying: true };
            }
            return { ...item, isPlaying: false };
        })
        setYinbiaoData(updatedYinbiaoData);
        console.log(yinbiaoData)
    } 

    // 音频结束后 
    const handleAudioEnded = () => {
        const updatedYinbiaoData = yinbiaoData.map(item => ({ ...item, isPlaying: false }));
        setYinbiaoData(updatedYinbiaoData);
    };

    //暂停音频逻辑
    const pauseAudio = () => {
        listBackgroundMusic.current.pause();
        const updatedYinbiaoData = yinbiaoData.map(item => ({ ...item, isPlaying: false }));
        setYinbiaoData(updatedYinbiaoData);
      };

  return ( 
    <>
    <div className={style.screen}>
    {/* <div className={style.title}></div> */}
    <div className={style.title}>
        <div className={style.titleBtnBox}>
            <div style={{flex:1}}></div>
            <Popover
                content={<div style={{width:70,height:20,textAlign:'center',fontSize:14}}>重点单词本</div>}
            >
            <Avatar src={Book} size={40}shape='square' className={style.titleBtn} />

            </Popover>
            <Popover
                content={<div style={{width:60,height:20,textAlign:'center',fontSize:18}}>退出</div>}
            >
            <Avatar src={Quit} size={40}shape='square' className={style.titleBtn} onClick={()=>{navigate('/')}}/>
            </Popover>
        </div>
        <div className={style.titleBox}>
            <div style={{flex:1}}></div>
            <div className={style.titleB}>音标学习</div>
            <div style={{flex:1}}></div>

        </div>
        <div style={{color:'#fff',position:'fixed',left:630,top:155}}>Phonetic Alphabet Learning</div>
    </div>
    <div style={{display:'flex'}}>
    <div style={{flex:1,position:'relative'}}>
{top !== 0 && <button onClick={handleFramePrevious} className={style.pageBtn} style={{right:100}}>上一页</button>}
    </div>
    <div className={style.box}>
    <div className={style.frame} ref={frameY}>
    {yinbiaoData.map((i,index)=>(
<div className={style.yinbiaoCard} key={index}>
        <div className={style.koreanBox}>
            <div style={{fontSize:14,textAlign:'center',marginTop:10}}>拼音</div>
            <div style={{fontSize:30,textAlign:'center',lineHeight:2}}>{i.symbol}</div>
        </div>  
        <div className={style.englishBox}>
            <div style={{fontSize:11,color:'rgba(80,80,80)',fontWeight:600}}>发音</div>
            <div style={{fontSize:11,height:48,overflow:'hidden'}}>{i.pronunciation}</div>
        </div>
        <div className={style.exampleBox}>
            <div style={{fontSize:11,color:'rgba(80,80,80)',fontWeight:600}}>例子</div>
            <div style={{fontSize:13,marginTop:2}}>{i.example}(<span style={{fontSize:11,color:'rgba(56,56,56'}}>{i.translation}</span>)</div>
        </div>
        <div className={style.essentialBox}>
        <Popover
                content={<div style={{width:'max-content',height:20,textAlign:'center',fontSize:12}}>标记为重点</div>}
            > 
        <HeatMapOutlined  className={style.btn} />
            </Popover>
        </div>
        <div className={style.audioBox}>
            <div  className={i.isPlaying?style.audioPlay:style.audioPause}  onClick={() => i.isPlaying ? pauseAudio() : playAudio(i.symbol,index)}>
            <Avatar src={Audio} size={30} style={{cursor:'pointer'}} />
            <audio
            ref={listBackgroundMusic}
            preload='auto'
            onEnded={handleAudioEnded}
    />
            </div>
        </div>
    </div>
    ))}
   
    </div>
    </div>
    <div style={{flex:1,position:'relative'}}>
    {top !== (-Math.floor(yinbiaoData.length/6)*380) && <button className={style.pageBtn} onClick={handleFrameNext}>下一页</button>}
    </div>
    </div>
    </div>
    </>
  )
}

export default Yinbiao