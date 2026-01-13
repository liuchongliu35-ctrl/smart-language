
import {QuestionCircleOutlined } from '@ant-design/icons';
import Right from '../image/右箭头.png'
import Robot from '../image/robot.png'
import style from './introduction.module.css'
import { useNavigate } from 'react-router-dom'
import { Avatar, Popover } from 'antd'


const Introduction = () =>{

    const navigate = useNavigate()

    return(
            <>
        <Avatar src={Right} size={64} onClick={() => { navigate('/intro/masSelect') }} style={{position:'fixed',left:100,top:350,cursor:'pointer',transform:'scaleX(-1)'}}/>
        <button style={{position:'fixed',right:40,top:350,cursor:'pointer'}} className={style.btn} onClick={()=>navigate('/')}>让我们开始学习吧！</button>
        <img src={Robot} alt='tu' style={{position:'fixed',width:150,left:350,top:10}} />
      <div className={style.langBox} >
        <div style={{height:100}}></div>
        {/* <div className={style.titleText}>您是否了解过这门语言？</div> */}
        <div className={style.selBox}>
            <div style={{flex:1,height:'100%'}} className={style.left}>
                <div className={style.title}>汉语</div>
                <div className={style.titleText} style={{marginTop:10,color:'#3c3c3c'}}>文字方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>表意文字，看到字即可知道其意思，每个字有独特的字形。</div>
                <div className={style.titleText} style={{marginTop:15,color:'#3c3c3c'}}>语序方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>句子结构大体上是“主谓宾”，动词通常位于主语之后。</div>
                <div className={style.titleText} style={{marginTop:15,color:'#3c3c3c'}}>语尾方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>语尾变化不明显，通常通过固定的词序和助词来表达语法关系。</div>
                <div className={style.titleText} style={{marginTop:15,color:'#3c3c3c'}}>发音方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>每个字有独立发音，且发音与文字本身无直接关系。</div>
                <div className={style.titleText} style={{marginTop:15,color:'#3c3c3c'}}>助词方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>相比韩语，汉语助词较少，但依然在造句中扮演重要角色。</div>
            </div>
            <div style={{flex:0.3,height:'100%'}} className={style.middle}>
                <div style={{textAlign:'center',fontFamily:'youshe',fontSize:33,color:'#3c3c3c'}}>对</div>
                <div style={{textAlign:'center',fontFamily:'youshe',fontSize:33,color:'#3c3c3c'}}>比</div>
                <Popover
                content={<div style={{width:'max-content',height:20,textAlign:'center',fontSize:12}}>该数据由大模型生产</div>}
            >
              <QuestionCircleOutlined  style={{textAlign:'center',fontFamily:'youshe',fontSize:33,color:'#3c3c3c',marginLeft:38,cursor:'pointer'}} />
            </Popover>
            </div>
            <div style={{flex:1,height:'100%'}} className={style.right}>
            <div className={style.title}>韩语</div>
                <div className={style.textRight} style={{marginTop:10,color:'#3c3c3c'}}>文字方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>表音文字，看到字即可知道其发音，注重发音，字形相对简单。</div>
                <div className={style.textRight} style={{marginTop:15,color:'#3c3c3c'}}>语序方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>句子结构通常为“主语-宾语-谓语”，动词往往放在最后面。</div>
                <div className={style.textRight} style={{marginTop:15,color:'#3c3c3c'}}>语尾方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>具有丰富的语尾变化，这些变化可以表示时间、尊卑等语法关系。</div>
                <div className={style.textRight} style={{marginTop:15,color:'#3c3c3c'}}>发音方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>表音文字的缘故，发音与文字关联紧密，存在连音与变音现象。</div>
                <div className={style.textRight} style={{marginTop:15,color:'#3c3c3c'}}>助词方面</div>
                <div style={{width:'70%',marginLeft:'15%',textIndent:'2em',fontSize:14,fontWeight:600}}>助词系统复杂，包括主格助词、添意助词等，对语法结构起到关键作用。</div>
            </div>
        </div>
      </div>
            </>
    )
}

export default Introduction