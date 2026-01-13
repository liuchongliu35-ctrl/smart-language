import React, { useEffect, useState } from 'react';
import Logo from '../image/logo.png'
import { UserOutlined,CaretDownFilled } from '@ant-design/icons';
import {  Layout, theme, Avatar, Collapse, Progress, Dropdown, Tag } from 'antd';
import style from './home.module.css';
import Progresses from '../Echarts/progressEchart'
import Hanguo from '../image/韩国.png'
import WordsBook from '../image/单词本.png'
import Communication from '../image/HTSCIT_对话.png'
import Composition from '../image/作文.png'
import Reading from '../image/阅读书本.png'
import Building from '../image/韩国建筑.png'
import Boy from '../image/男孩头像.png'
import Message from '../image/留言建议.png'
import Notice from '../image/通知中心.png'
import Mybook from '../image/book.png'
import { useNavigate } from 'react-router-dom';
import HomeLoading from '../loadingComponent/homeLoading';


const { Header, Content, Sider } = Layout;
const items = [
  {
    key: '1',
    label: '韩语',
  },
  {
    key: '2',
    label: '日语',
  },
  {
    key: '3',
    label: '西班牙语',
  },
];






const HomeAPP = () => {
  const {
    token: { colorBgContainer, borderRadiusLG }
  } = theme.useToken();

  const navigate = useNavigate()

  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 2000);

    return () => clearTimeout(timer);
  }, []);




  return (
    <>

    <Layout>
      {/* <Header
        style={{
          width:'100%',
          height: 50,
          display: 'flex',
          marginBottom: 10,
          background: '#fff',
          boxShadow:'0px 4px 2px #eaeaea',
          position:'fixed',
          zIndex:2,
          top:0,
          left:0
        }}
      >
      
        <img src={Logo} alt=' ' style={{width:100,marginLeft:0}} />
        <div style={{flex:1}}></div>
        <div className={style.titleCard}>服务支持</div>
        <div className={style.titleCard}>关于我们</div>
        <div style={{flex:0.1}}></div>

      </Header> */}
      <Layout>
        <Sider
          height={1200}
          width={250}
          style={{
            background:'#21353c',
            borderRadius: borderRadiusLG,
            position:'fixed',
            height:740,
            top:0,
            left:0
          }}
        >
          <div className={style.homeAvatar}>
            <div style={{ textAlign: 'center', marginBottom: 7 }}>
              <Avatar size={64} icon={<UserOutlined />} src={Boy} />
            </div>
            <Tag color="green" style={{width:'max-content',margin:'auto',marginTop:0,marginBottom:7}}>Lv<span style={{fontSize:15}}>1</span></Tag>
            
            <span style={{ fontSize: 18 }}>爱学习的小奥</span>
            <div style={{width:'100%',display:"flex",marginTop:10}}>
              <div className={style.userIcon}>
                <Avatar src={Mybook} shape='square' size={24} />
              </div>
              <div className={style.userIcon}><Avatar src={Message} size={20} /></div>
              <div className={style.userIcon}><Avatar src={Notice} size={24} /></div>
            </div>
            <div style={{fontSize:13,fontWeight:600,color:'#8b8b8b',marginTop:7,textDecoration:'underline',cursor:'pointer'}} onClick={()=>navigate('/login')}>退出</div>
          </div>

          <div className={style.siderCard}>
          <Collapse
            // ghost
            style={{ width: 200, marginLeft: 10,color:'#fff' }}
            size="small"
            items={[
              {
                key: '1',
                label: '班级',
                children: <p style={{color:'#fff'}}>班级</p>,
              },
            ]}
          />

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
              marginTop:0,
              marginLeft: 250,
              minHeight: 280,
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
              // background:'#f0c2a255',
              position:'relative'
            }}
          >
            <img src={Building} alt='图片'  style={{position:'absolute',width:750,transform:'scaleX(-1)',top:450,left:0}}/>

            <div style={{ display: 'flex' }}>
              <div style={{ flex: 1, height: 660, }}>
                <div className={style.rateBox}>
                <Dropdown
    menu={{
      items,
      selectable: true,
      defaultSelectedKeys: ['3'],
      
    }}
  >
     <div style={{width:165,height:55,borderRadius:30,background:'#fff',display:'flex'}}>
                  <Avatar src={Hanguo} size={57} />
                  <span style={{fontSize:25,fontWeight:700,lineHeight:2 ,marginLeft:6}}>韩语</span>
                  <CaretDownFilled style={{marginLeft:15,fontSize:25,cursor:'pointer'}}/>
                </div>
  </Dropdown>
               
                <div style={{marginTop:15,marginLeft:100,display:'flex',paddingRight:100}}>
                  <span style={{fontSize:20,fontWeight:600,lineHeight:2,marginRight:7}}>阶段</span>
                  <span style={{fontSize:24,background:'#435fff',width:40,height:40,borderRadius:'50%',textAlign:'center',fontWeight:700,color:'#fff',boxShadow:'0px 2px 2px #00000099'}}>1</span>
                  <div style={{flex:1}}></div>
                  <span style={{fontSize:20,fontWeight:600,lineHeight:2,marginRight:7}}>阶段</span>
                  <span style={{fontSize:24,background:'#435fff',width:40,height:40,borderRadius:'50%',textAlign:'center',fontWeight:700,color:'#fff',boxShadow:'0px 2px 2px #00000099'}}>2</span>
                </div>
                <div style={{height:30,marginTop:15,display:'flex'}}>
                  <div style={{flex:1,textAlign:'right'}}>271</div> 
                <Progresses  />
                <div style={{flex:1}}>1000</div> 
                </div>
                </div>
                <div className={style.functionBox}>
                  <div className={style.functionCard} onClick={()=>navigate('/wordsbook')}>
                  <Avatar src={WordsBook} shape='square' size={80} style={{marginLeft:14}} />
                  <div className={style.cardText}>单词本</div>
                  </div>
                  <div className={style.functionCard} onClick={()=>navigate('/communication')}>
                  <Avatar src={Communication} shape='square' size={80} style={{marginLeft:14}} />
                  <div className={style.cardText}>情景对话</div>
                  </div>
                  <div className={style.functionCard} onClick={()=>navigate('/writingCorrect')}>
                  <Avatar src={Composition} shape='square' size={80} style={{marginLeft:14}} />
                  <div className={style.cardText}>作文批改</div>
                  </div>
                  <div className={style.functionCard}>
                  <Avatar src={Reading} shape='square' size={80} style={{marginLeft:14}} onClick={()=>navigate('/reading')}/>
                  <div className={style.cardText}>阅读练习</div>
                  </div>
                </div>
              </div>
              <div style={{ width: 300 }}>

                <div className={style.oneCard}>
                  <div className={style.oneCardTitle}>基础学习</div>
                  <div style={{width:'max-content',height:'max-content',margin:'auto',marginTop:15}}>
                  <Progress
          type="dashboard"
          steps={8}
          percent={40}
          trailColor="rgba(0, 0, 0, 0.06)"
          strokeWidth={20}
          strokeColor='#ea5514'
        />
                  </div>
                  <div className={style.oneCardBtn} onClick={()=>{navigate('/learn')}}>点击继续</div>
                </div>
                <div className={style.oneCard}> <div className={style.oneCardTitle}>进阶学习</div>
                  <div style={{width:'max-content',height:'max-content',margin:'auto',marginTop:15}}>
                  <Progress
          type="dashboard"
          steps={8}
          percent={0}
          trailColor="rgba(0, 0, 0, 0.06)"
          strokeWidth={20}
          strokeColor='#ea5514'
        />
                  </div>
                  <div className={style.oneCardBtn} style={{background:'rgba(62,213,153)'}}>等待解锁</div></div>
                <div className={style.oneCard}>
                <div className={style.oneCardTitle}>实战提升</div>
                  <div style={{width:'max-content',height:'max-content',margin:'auto',marginTop:15}}>
                  <Progress
          type="dashboard"
          steps={8}
          percent={0}
          trailColor="rgba(0, 0, 0, 0.06)"
          strokeWidth={20}
          strokeColor='#ea5514'
        />
                  </div>
                  <div className={style.oneCardBtn} style={{background:'rgba(62,213,153)'}}>等待解锁</div>
                </div>

              </div>
            </div>
          </Content>
        </Layout>
      </Layout>
    </Layout>
    </>
  );
};

const Home = () =>{

  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1000);

    return () => clearTimeout(timer);
  }, []);

  return(
<>
{isLoading?<HomeLoading />:<HomeAPP />}

</>
  )
}


export default Home;