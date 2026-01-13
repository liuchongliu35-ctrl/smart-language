import style from './reading.module.css'
import React, { useEffect, useState } from "react";
import Quit from '../image/退出.png'
import { Avatar, Button, Input, InputNumber, Popover, Radio,} from "antd";
import { useNavigate } from "react-router-dom";
import { QuestionCircleOutlined, } from '@ant-design/icons';


const items = [
    {type:'新闻报道'},
    {type:'分析解释'},
    {type:'政治评论'},
    {type:'经济商业'},
    {type:'文化艺术'},
    {type:'科技趋势'},
    {type:'生活方式'},
    {type:'教育培训'},
]

const Reading = () =>{
    const navigate = useNavigate()
    const [ws, setWs] = useState(null)
    const [value, setValue] = useState('')
    const [minValue, setMinValue] = useState(0);
    const [maxValue, setMaxValue] = useState(500)
    const [isCompelete,setIsCompelete] = useState(false)
    const [composition,setComposition] = useState('')
    const [isTranslate,setIsTranslate] = useState('')

    const onChange = (e) => {
        setValue(e.target.value);
      };

      const handleMinChange = (v) => {
        setMinValue(v)
      };
    
      const handleMaxChange = (v) => {
        setMaxValue(v);
      }

      const handlePost = () =>{
        const values ={
            uid:'2',
            type:value,
            range:maxValue,
            to:'韩语'
        }
        const jsonData = JSON.stringify(values)
        console.log(jsonData)
        if (ws) {
          ws.send(jsonData);
        } else {
          console.error('WebSocket连接未建立');
        }
        // setIsAddLoading(true)
        setTimeout(() => {
            // setIsAddLoading(false)
            setIsCompelete(true)
        }, 2000);
      }

      useEffect(() => {
        // 创建一个新的WebSocket连接
        const socket = new WebSocket('ws://10.100.85.4:8087/websocket');
    
        // 设置WebSocket状态
        setWs(socket);
    
        // 当WebSocket连接打开时的处理程序
        socket.onopen = (event) => {
          console.log('WebSocket连接已打开', event);
    
        };
    
        socket.addEventListener('message', 
          (event) => { 
            console.log(event.data)
            const data = event.data
            if(data.startsWith('翻译:')) {
              setIsTranslate(data)
            }else{
              setComposition(prevData => [...prevData, data])
            }
          });
    
    
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
        };
      }, []);

      const handleAgain = () =>{
        window.location.reload()
      }

      const CompositionContent = () =>{
        return(
          <div style={{padding:30,paddingBottom:150}}>
            <div className={style.textTitle}>韩语文章</div>
            <div className={style.comContent} >
              {composition}
              <div className={style.comContent} style={{marginTop:20,color:'#7c7c7c'}}>{isTranslate}</div>
            </div>
            
          </div>
        )
      }


    return(
        <div style={{display:'flex',background:'#fff6e87b'}}>
         <div className={style.title}>
            <div className={style.titleText}>阅读练习</div>
            <Popover
                content={<div style={{width:60,height:20,textAlign:'center',fontSize:18}}>退出</div>}
            >
            <Avatar src={Quit} size={40}shape='square' className={style.titleBtn} onClick={()=>{navigate('/')}} />
            </Popover>
        </div>
        <div style={{flex:1}}></div>
        <div style={{paddingBottom:50}}>
        <div className={style.requirement}>
            <div style={{flex:1,height:'100%'}}>
                <div className={style.requireTitle}>类型选择</div>
                <div className={style.radioBox}>
                    <Radio.Group onChange={onChange}>
                       {items.map((i,index)=>(
                        <Radio  value={i.type} key={index} className={style.checkStyle} >{i.type}</Radio>
                       ))}
                    </Radio.Group>
                </div>
                <div style={{width:'max-content',fontFamily:'youshe',fontSize:15,margin:'auto'}}>
                    自定义输入 <Popover
                content={<div style={{width:'max-content',height:20,textAlign:'center',fontSize:12}}>AI大模型会您输入的关键字推送相关文章</div>}
            >
              <QuestionCircleOutlined className={style.wenhao} style={{cursor:'pointer'}} />
            </Popover>
                </div>
                <div style={{width:'max-content',margin:'auto'}}>
                <Input style={{width:130,height:30,marginTop:10}} placeholder='输入文字...' onChange={onChange} value={value}></Input>
                </div>
            </div>
            <div style={{flex:1,height:'100%',borderLeft:'3px solid #eaeaea'}}>
            <div style={{width:'max-content',fontFamily:'youshe',fontSize:18,margin:'auto',marginTop:30}}>
                    字数限制
                </div>
                <div>
                <div style={{marginTop:10,marginBottom:10   }}>
                <label style={{marginLeft:130,fontWeight:600,fontSize:14}} >最小值:&nbsp;</label>
                <InputNumber min={0} max={10000} value={minValue} onChange={handleMinChange} />
                </div>
                <label style={{marginLeft:130,fontWeight:600,fontSize:14}}>最大值:&nbsp;</label>
                <InputNumber min={0} max={20000} value={maxValue} onChange={handleMaxChange} />
                </div>

                <div style={{width:'max-content',margin:'auto'}}>
                {isCompelete?<Button className={style.again} onClick={handleAgain}>再来一篇</Button>: <Button className={style.btn} onClick={handlePost}  >点击生成</Button>} 
                </div>
            </div>
        </div>
        <div className={style.content}>
          {/* {isAddLoading && <AddLoading />}      */}
          {composition && <CompositionContent />}         
        </div>
        </div>
        <div style={{flex:1}}></div>
        </div>
    )
}

export default Reading