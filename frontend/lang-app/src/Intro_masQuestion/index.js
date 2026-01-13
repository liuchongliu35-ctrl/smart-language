import { useNavigate } from 'react-router-dom'
import style from './masQuestion.module.css'
import { Button } from 'antd'


const MasQuestion = () =>{
    const navigate = useNavigate()

    return(
        <div>
            <div style={{width:'100%',height:200,border:'1px solid black',display:'flex'}}>
                <Button onClick={()=>{navigate('/intro/masSelect')}}>上一步</Button>
                <div>您是否了解这门语言及这个国家的礼仪</div>
                <Button onClick={()=>{navigate('/intro/introduction')}}>下一步</Button>

            </div>
            <div style={{width:'100%',height:400,border:"1px solid black"}}>
                <div style={{margin:'auto',width:'max-content',marginTop:50}}>
                    <div className={style.selectCard}>你好属地化监管技术考核</div>
                    <div className={style.selectCard}>你好</div>
                    <div className={style.selectCard}>你好</div>
                    <div className={style.selectCard}>你好</div>
                </div>
            </div>
        </div>
    )
}

export default MasQuestion