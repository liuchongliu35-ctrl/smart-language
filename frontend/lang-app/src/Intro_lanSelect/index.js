import React, { useState } from 'react';
import style from './lanSelect.module.css'
import Hanyu from '../image/韩国.png'
import Taiyu from '../image/泰国.png'
import Xibanya from '../image/西班牙.png'
import Riyu from '../image/日本国旗.png'
import Mianyu from '../image/缅甸.png'
import Eluosi from '../image/俄罗斯.png'
import Arabo from '../image/AR-阿拉伯语.png'
import Yuenan from '../image/越南.png'
import Fayu from '../image/法国.png'
import Deyu from '../image/德国.png'
import Right from '../image/右箭头.png'
import Robot from '../image/robot.png'
import { Avatar, Button } from 'antd';
import { useNavigate } from 'react-router-dom';


const langList = [
  {
    guoqi: Hanyu,
    text: '韩语',
    textBottom: '한국어'
  }, {
    guoqi: Riyu,
    text: '日语',
    textBottom: '日本語'
  }, {
    guoqi: Eluosi,
    text: '俄语',
    textBottom: 'Россия'
  }, {
    guoqi: Xibanya,
    text: '西班牙语言',
    textBottom: 'Español'
  }, {
    guoqi: Arabo,
    text: '阿拉伯语',
    textBottom: 'بالعربية'
  }, {
    guoqi: Taiyu,
    text: '泰语',
    textBottom: 'ภาษาไทย'
  }, {
    guoqi: Mianyu,
    text: '缅甸语',
    textBottom: 'ဗာရမ်'
  }, {
    guoqi: Deyu,
    text: '德语',
    textBottom: 'Deutsch'
  }, {
    guoqi: Fayu,
    text: '法语',
    textBottom: 'Français'
  }, {
    guoqi: Yuenan,
    text: '越南语',
    textBottom: 'Tiếng Việt'
  },
]

const LanSelect = () => {

  const [lang,setLang] = useState('')

  const handleClick = (e) =>{
    setLang(e)
  }

  const navigate = useNavigate()



// eslint-disable-next-line
  return (
    <>
        <Avatar src={Right} size={64} onClick={() => { navigate('/intro/masSelect') }} style={{position:'fixed',right:100,top:350,cursor:'pointer'}}/>
        <img src={Robot} alt='tu' style={{position:'fixed',width:150,left:350,top:15}} />
      <div className={style.langBox} >
        <div style={{height:60}}></div>
        <div className={style.titleText}>请选择您想学习的语言</div>
        <div className={style.selBox}>
          {langList.map((item,index)=>(
            <div className={lang === `${item.text}`?style.selCardFocus:style.selCard} key={index} onClick={()=>handleClick(`${item.text}`)}>
            <div className={style.country}>
              <img src={item.guoqi} alt='图片' style={{width:150}}/>
            </div>
            <div>
              <div style={{marginLeft:15,fontSize:18,marginTop:35}}>{item.text}</div>
              <div style={{marginLeft:15,fontSize:16,fontWeight:600}}>{item.textBottom}</div>
            </div>
          </div>
          ))}
         
        </div>
      </div>
    </>
  )
}

export default LanSelect