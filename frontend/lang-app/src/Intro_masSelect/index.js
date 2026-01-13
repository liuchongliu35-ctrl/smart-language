
import { Button,Avatar } from 'antd'
import Right from '../image/右箭头.png'
import Robot from '../image/robot.png'
import Hanyu from '../image/韩国.png'
import style from './masSelect.module.css'
import { useNavigate } from 'react-router-dom'




const MasSelect = () =>{
const navigate = useNavigate()

    return(
        <div>
        <Avatar src={Right} size={64} onClick={() => { navigate('/intro') }} style={{position:'fixed',left:100,top:350,cursor:'pointer',transform:'scaleX(-1)'}}/>
        <img src={Robot} alt='tu' style={{position:'fixed',width:150,left:350,top:15}} />
      <div className={style.langBox} >
        <div style={{height:60}}></div>
        <div className={style.titleText}>您是否了解过这门语言？</div>
        <div className={style.selBox}>
            <div style={{marginTop:70}}>
                <div className={style.selBar} onClick={() => { navigate('/intro/introduction') }}>一点也没了解过</div>
                <div className={style.selBar} onClick={() => { navigate('/intro/introduction') }}>只能理解一些基本的日常用语和简单对话</div>
                <div className={style.selBar} onClick={() => { navigate('/intro/introduction') }}>理解常见的词汇和基本语法规则</div>
                <div className={style.selBar} onClick={() => { navigate('/intro/introduction') }}>理解和使用相对复杂的语法结构和词汇</div>
            </div>
        </div>
      </div>
        </div>
    )
}

export default MasSelect