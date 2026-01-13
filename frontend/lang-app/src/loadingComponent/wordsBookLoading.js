
// eslint-disable-next-line
import { Layout,theme, Skeleton,Collapse,Tag,Avatar,Popover,Space} from 'antd';
// eslint-disable-next-line
import React, { useEffect, useState } from 'react'
import style from '../WordsBook/wordsbook.module.css'
// eslint-disable-next-line
import Boy from '../image/男孩头像.png'
import Message from '../image/留言建议.png'
import Notice from '../image/通知中心.png'
import Mybook from '../image/book.png'
import Logo from '../image/ZhiDuoYuLogo.png'
// eslint-disable-next-line
import Shezhi from '../image/设置.png'
import Kefu from '../image/客服.png'
// eslint-disable-next-line
import {  UserOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';


const { Header, Content, Sider } = Layout;






const WordsBookLoading = () =>{
  const [clicked, setClicked] = useState(false);
  const handleClickChange = (open) => {
    setClicked(open);
  };
    const {
        token: { borderRadiusLG }
      } = theme.useToken();
      const navigate = useNavigate()

    return(
       <div>
   <Layout>
      <Layout>
      <Sider
          height={1200}
          width={250}
          style={{
            background:'#3c3c3c',
            // background:'#fff',
            borderRight:'3px solid #e1ac5b',
            borderRadius: borderRadiusLG,
            position:'fixed',
            height:740,
            top:0,
            left:0
          }}
        >
          <div style={{width:'max-content',margin:'auto',marginTop:30}}>
            <img src={Logo} alt='1' style={{width:100}}/>
          </div>
          <div className={style.homeAvatar}>
            <div style={{ textAlign: 'center', marginBottom: 7 }}>
              <Avatar size={64} icon={<UserOutlined />} src={Boy} />
            </div>
            <Tag color="green" style={{width:'max-content',margin:'auto',marginTop:0,marginBottom:7}}>Lv<span style={{fontSize:15}}>1</span></Tag>
            
            <span style={{ fontSize: 15,fontWeight:600 }}>爱学习的小奥</span>
            <div style={{width:'100%',display:"flex",marginTop:10}}>
              <div className={style.userIcon}>
                <Avatar src={Mybook} shape='square' size={24} />
              </div>
              <div className={style.userIcon}><Avatar src={Message} size={20} /></div>
              <div className={style.userIcon}><Avatar src={Notice} size={24} /></div>
            </div>
          </div>
          <div style={{width:'max-content',margin:'auto',marginTop:200}}>
            <Avatar src={Kefu} size={50} shape='square' />
          </div>
          <div style={{width:'max-content',margin:'auto',marginTop:10}}>
          <Popover
                content={<div style={{width:'max-content',height:20,textAlign:'center',fontSize:12}}>设置</div>}
            > 
            </Popover> 

            <Popover trigger="click" content={<div style={{width:80}}>
               <Space direction="vertical">
              <div className={style.menuSet} onClick={()=>navigate('/login')} >退出登录</div>
                <div className={style.menuSet}>更多帮助</div>
              <div className={style.menuSet}>关于我们？</div>
               </Space>
               </div>}
               placement="right" open={clicked}
               onOpenChange={handleClickChange} >
            
            <Avatar src={Shezhi} size={40} shape='square' style={{cursor:'pointer'}} />
          
          </Popover>
          </div>

        </Sider>
        <Layout
          style={{
            padding: '0 4px 24px',
          }}
        >

          <Content
             style={{
              height: 'max-content',
              padding: 20,
              marginLeft: 240,
              minHeight: 700,
              borderRadius: borderRadiusLG,
              position:'relative',
              background:'#fff6e87b'
            }}
          >

         <Skeleton active />
         <Skeleton active />
          
          </Content>
        </Layout>
      </Layout>
    </Layout>
       




       </div>
    )
}

export default WordsBookLoading