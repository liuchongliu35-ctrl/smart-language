import React, { useEffect, useState } from 'react';
import Logo from '../image/ZhiDuoYuLogo.png'
import { UserOutlined, CaretDownFilled, PlusCircleOutlined } from '@ant-design/icons';
import { Layout, theme, Avatar, Dropdown, Tag, Modal, Popover, Space, Input, Button, message } from 'antd';
import style from './newHome.module.css';
import Hanguo from '../image/韩国Logo.png'
import Riben from '../image/日本.png'
import Faguo from '../image/法国（圆）.png'
import WordsBook from '../image/书本.png'
import Shezhi from '../image/设置.png'
import Kefu from '../image/客服.png'
import ReadingMan from '../image/看书的人.jpg'
import WritingImg from '../image/写作.jpg'
import Homework from '../image/作业.png'
import Piyue from '../image/批阅.png'
import Fayin from '../image/发音练习.png'
import LeftRobot from '../image/边框机器人.png'
import Boy from '../image/男孩头像.png'
import Message from '../image/留言建议.png'
import Notice from '../image/通知中心.png'
import Mybook from '../image/book.png'
import TranRobot from '../image/翻译机器人.png'
import { useNavigate } from 'react-router-dom';
import HomeLoading from '../loadingComponent/homeLoading';
import Coffee from '../image/coffee.jpg'
import Translation from '../Translation';

const { Content, Sider } = Layout;






const HomeAPP = () => {
  const {
    token: { borderRadiusLG }
  } = theme.useToken()

  const navigate = useNavigate()
  const [isJionClassOpen, setIsJionClassOpen] = useState(false)
  const [isListOpen, setIsListOpen] = useState(false)
  const [clicked, setClicked] = useState(false)
  const [uid, setUid] = useState(localStorage.getItem('userID'))
  const [isJion, setIsJion] = useState(false)
  const handleClickChange = (open) => {
    setClicked(open)
  };

  const handleJionClassCancel = () => {
    setIsJionClassOpen(false)
  }

  const handleListCancel = () => {
    setIsListOpen(false)
  }


  const handleChangeUid = (e) => {
    localStorage.setItem('userID', e)
    setUid(localStorage.getItem('userID'))
  }



  function langLogo() {
    switch (uid) {
      case '2':
        return Hanguo
      case '3':
        return Riben
      case '4':
        return Faguo
      default:
        break;
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

  const items = [
    {
      key: '1',
      label: (
        <div onClick={() => handleChangeUid(2)} className={style.subOption}><Avatar src={Hanguo} size={25} /><span style={{ marginLeft: 10 }}>韩语</span></div>
      ),
    },
    {
      key: '2',
      label: (
        <div onClick={() => handleChangeUid(3)} className={style.subOption}><Avatar src={Riben} size={25} /><span style={{ marginLeft: 10 }}>日语</span></div>
      ),
    },
    {
      key: '3',
      label: (
        <div onClick={() => handleChangeUid(4)} className={style.subOption}><Avatar src={Faguo} size={25} /><span style={{ marginLeft: 10 }}>法语</span></div>
      ),
    }, {
      key: '4',
      label: (<div style={{ fontWeight: 600 }}>添加新语言 <PlusCircleOutlined /></div>)
    }
  ]

  const JionClass = () => (
    <>

      {/* <div style={{width:100,height:'100%'}}>
        <div style={{textAlign:'center',marginTop:20,fontFamily:'youshe'}}>班级</div>
        <div style={{textAlign:'center',fontWeight:600,marginTop:5,color:'#3c3c3c'}}>韩语小分队</div>
      </div>
      <div style={{flex:1}}>
        <div style={{marginLeft:10,marginTop:23}}><span style={{fontFamily:'youshe',}}>老师：</span><span style={{fontWeight:600,color:'#3c3c3c'}}>王大锤</span></div>
        <div style={{marginLeft:10}}><span style={{fontFamily:'youshe',}}>人数：</span><span style={{fontWeight:600,color:'#3c3c3c'}}>26</span></div>
      </div> */}
      <div className={style.classBox}>
        <div className={style.classBoxTitle}>我的班级</div>
        <div className={style.class1}>
          <div style={{ textAlign: 'center', fontFamily: 'youshe', fontSize: 24, marginTop: 35 }}>暂无信息</div>
        </div>
        <div style={{ display: 'flex', width: 250, marginLeft: 20, height: 80, marginTop: 20 }}>
          <div style={{ width: 110, height: 80, lineHeight: 2, background: '#ffecaf', color: '#4f4032', borderRadius: 10, marginRight: 30, fontFamily: 'youshe', textAlign: 'center', cursor: 'pointer' }}>查看作业
            <div><Avatar src={Homework} shape='square' size={36} /></div>
          </div>
          <div style={{ width: 110, height: 80, lineHeight: 2, background: '#ffecaf', color: '#4f4032', borderRadius: 10, fontFamily: 'youshe', textAlign: 'center', cursor: 'pointer' }}>查看批阅
            <div><Avatar src={Piyue} shape='square' size={40} /></div>
          </div>
        </div>
        <div className={style.quitClass} onClick={() => setIsJionClassOpen(true)}>加 入 班 级</div>
      </div>
    </>
  )

  const HavaClass = () => (
    <>
      {/* <div className={style.classBox}>
            <div className={style.classBoxTitle}>我的班级</div>
              <div style={{width:100,height:'100%'}}>
        <div style={{textAlign:'center',marginTop:20,fontFamily:'youshe'}}>班级</div>
        <div style={{textAlign:'center',fontWeight:600,marginTop:5,color:'#3c3c3c'}}>韩语小分队</div>
      </div>
      <div style={{flex:1}}>
        <div style={{marginLeft:10,marginTop:23}}><span style={{fontFamily:'youshe',}}>老师：</span><span style={{fontWeight:600,color:'#3c3c3c'}}>王大锤</span></div>
        <div style={{marginLeft:10}}><span style={{fontFamily:'youshe',}}>人数：</span><span style={{fontWeight:600,color:'#3c3c3c'}}>26</span></div>
      </div>
            <div className={style.class1}>
            </div>
            <div style={{display:'flex',width:250,marginLeft:20,height:80,marginTop:20}}>
              <div style={{width:110,height:80,lineHeight:2,background:'#ffecaf',color:'#4f4032',borderRadius:10,marginRight:30,fontFamily:'youshe',textAlign:'center',cursor:'pointer'}}>查看作业
                <div><Avatar src={Homework} shape='square' size={36} /></div>
              </div>
              <div style={{width:110,height:80,lineHeight:2,background:'#ffecaf',color:'#4f4032',borderRadius:10,fontFamily:'youshe',textAlign:'center',cursor:'pointer'}}>查看批阅
              <div><Avatar src={Piyue} shape='square' size={40} /></div>
              </div>
            </div>
            <div className={style.quitClass}>更 换 班 级</div>
          </div> */}
      <div className={style.classBox}>
        <div className={style.classBoxTitle}>我的班级</div>
        <div className={style.class1}>
          <div style={{ width: 100, height: '100%' }}>
            <div style={{ textAlign: 'center', marginTop: 20, fontFamily: 'youshe' }}>班级</div>
            <div style={{ textAlign: 'center', fontWeight: 600, marginTop: 5, color: '#3c3c3c' }}>韩语小分队</div>
          </div>
          <div style={{ flex: 1 }}>
            <div style={{ marginLeft: 10, marginTop: 23 }}><span style={{ fontFamily: 'youshe', }}>老师：</span><span style={{ fontWeight: 600, color: '#3c3c3c' }}>王大锤</span></div>
            <div style={{ marginLeft: 10 }}><span style={{ fontFamily: 'youshe', }}>人数：</span><span style={{ fontWeight: 600, color: '#3c3c3c' }}>26</span></div>
          </div>
        </div>
        <div style={{ display: 'flex', width: 250, marginLeft: 20, height: 80, marginTop: 20 }}>
          <div style={{ width: 110, height: 80, lineHeight: 2, background: '#ffecaf', color: '#4f4032', borderRadius: 10, marginRight: 30, fontFamily: 'youshe', textAlign: 'center', cursor: 'pointer' }}>查看作业
            <div><Avatar src={Homework} shape='square' size={36} /></div>
          </div>
          <div style={{ width: 110, height: 80, lineHeight: 2, background: '#ffecaf', color: '#4f4032', borderRadius: 10, fontFamily: 'youshe', textAlign: 'center', cursor: 'pointer' }}>查看批阅
            <div><Avatar src={Piyue} shape='square' size={40} /></div>
          </div>
        </div>
        <div className={style.quitClass} onClick={() => setIsJionClassOpen(true)}>更 换 班 级</div>
      </div>
    </>
  )

  const handleJion = () => {
    setIsJionClassOpen(false)
    message.success('加入成功')
    setIsJion(true)
  }


  return (
    <>

      <Layout>

        <Layout>
          <Sider

            width={250}
            style={{
              background: '#3c3c3c',
              // background:'#fff',
              borderRight: '3px solid #e1ac5b',
              borderRadius: borderRadiusLG,
              position: 'fixed',
              height: '100vh',
              top: 0,
              left: 0
            }}
          >
            <div style={{ width: 'max-content', margin: 'auto', marginTop: 30 }}>
              <img src={Logo} alt='1' style={{ width: 100 }} />
            </div>
            <div className={style.homeAvatar}>
              <div style={{ textAlign: 'center', marginBottom: 7 }}>
                <Avatar size={64} icon={<UserOutlined />} src={Boy} />
              </div>
              <Tag color="green" style={{ width: 'max-content', margin: 'auto', marginTop: 0, marginBottom: 7 }}>Lv<span style={{ fontSize: 15 }}>1</span></Tag>

              <span style={{ fontSize: 15, fontWeight: 600 }}>爱学习的小奥</span>
              <div style={{ width: '100%', display: "flex", marginTop: 10 }}>
                <div className={style.userIcon}>
                  <Avatar src={Mybook} shape='square' size={24} />
                </div>
                <div className={style.userIcon}><Avatar src={Message} size={20} /></div>
                <div className={style.userIcon}><Avatar src={Notice} size={24} /></div>
              </div>
            </div>
            <div style={{ width: 'max-content', margin: 'auto', marginTop: 200 }}>
              <Popover
                content={<div style={{ width: 'max-content', height: 20, textAlign: 'center', fontSize: 12 }}>联系客服</div>}
              >
                <Avatar src={Kefu} size={50} shape='square' style={{ cursor: 'pointer' }} />
              </Popover>
            </div>
            <div style={{ width: 'max-content', margin: 'auto', marginTop: 10 }}>
              <Popover
                content={<div style={{ width: 'max-content', height: 20, textAlign: 'center', fontSize: 12 }}>设置</div>}
              >
              </Popover>

              <Popover trigger="click" content={<div style={{ width: 80 }}>
                <Space direction="vertical">
                  <div className={style.menuSet} onClick={() => navigate('/login')} >退出登录</div>
                  <div className={style.menuSet}>更多帮助</div>
                  <div className={style.menuSet}>关于我们？</div>
                </Space>
              </div>}
                placement="right" open={clicked}
                onOpenChange={handleClickChange} >

                <Avatar src={Shezhi} size={40} shape='square' style={{ cursor: 'pointer' }} />

              </Popover>
            </div>

          </Sider>
          <Layout
            style={{
              padding: '0 4px 0px',
            }}
          >

            <Content
              style={{
                height: '100vh',
                padding: 20,
                marginLeft: 240,
                borderRadius: borderRadiusLG,
                position: 'relative',
                display: 'flex',
                background: '#fff6e87b'
              }}
            >
              <img src={LeftRobot} alt='tu' className={style.leftRobot} />
              <div className={style.content}>
                <div>
                  {/* <div style={{fontFamily:'youshe',fontSize:30,paddingLeft:30}}><HomeFilled />&nbsp;首页</div> */}
                  <div className={style.current}>
                    <Dropdown
                      menu={{
                        items,
                        selectable: true,
                        defaultSelectedKeys: [1],

                      }}
                    >
                      <div style={{ width: 140, height: 45, borderRadius: 30, background: '#fff', display: 'flex' }}>
                        <Avatar src={langLogo()} size={45} />
                        <span style={{ fontSize: 25, lineHeight: 2, marginLeft: 6 }}>{langText()}</span>
                        <CaretDownFilled style={{ marginLeft: 15, fontSize: 25, cursor: 'pointer' }} />
                      </div>
                    </Dropdown>
                  </div>
                </div>
                <div style={{ fontWeight: 600, marginTop: 30, fontSize: 20, marginLeft: 25 }}>基础学习</div>
                <div style={{ fontWeight: 600, fontSize: 18, marginLeft: 25 }}>Basic Learning</div>
                <div className={style.basicBox}>
                  <div className={style.basicCard} onClick={() => navigate(`/wordsbook`)}>
                    <div style={{ width: 86, height: 120, background: '#000', borderRadius: 10 }}>
                      <Avatar src={WordsBook} size={60} shape='square' style={{ marginLeft: 12, marginTop: 24 }} />
                    </div>
                    <div style={{ flex: 1 }}>
                      <div style={{ fontFamily: 'youshe', fontSize: 25, color: '#4f4032', textAlign: 'center', marginTop: 17 }}>单词本</div>
                      <div style={{ fontSize: 13, color: '#4f4032', marginTop: 17, marginLeft: 8 }}>场景分类</div>
                      <div style={{ fontSize: 13, color: '#4f4032', marginLeft: 8 }}>支持自定义添加</div>
                    </div>
                  </div>
                  <div className={style.basicCard} onClick={() => navigate('/learn')}>
                    <div style={{ width: 86, height: 120, background: '#000', borderRadius: 10 }}>
                      <Avatar src={Fayin} size={60} shape='square' style={{ marginLeft: 12, marginTop: 24 }} />
                    </div>
                    <div style={{ flex: 1 }}>
                      <div style={{ fontFamily: 'youshe', fontSize: 25, color: '#4f4032', textAlign: 'center', marginTop: 17 }}>发音表</div>
                      <div style={{ fontSize: 13, color: '#4f4032', marginTop: 17, marginLeft: 8 }}>标记重难点</div>
                    </div>
                  </div>
                </div>

                <div style={{ fontWeight: 600, fontSize: 20, marginTop: 60, marginLeft: 25 }}>智能学习</div>
                <div style={{ fontWeight: 600, fontSize: 18, marginLeft: 25 }}>Intelligent Learning</div>
                <div className={style.basicBox}>
                  <div className={style.functionCard} onClick={() => navigate('/communication')}>
                    <div className={style.cardImg}>
                      <img src={Coffee} alt='图' style={{ width: 200 }} />
                    </div>
                    <div style={{ marginTop: 20, fontSize: 21, fontFamily: 'youshe' }} >情景对话</div>
                    <div style={{ marginTop: 2, fontSize: 12, textAlign: 'center' }}>选择想要的场景，模拟真实对话</div>
                  </div>
                  <div className={style.functionCard} onClick={() => navigate('/writingCorrect')}>
                    <div className={style.cardImg} >
                      <img src={WritingImg} alt='图' style={{ width: 200 }} />
                    </div>
                    <div style={{ marginTop: 20, fontSize: 21, fontFamily: 'youshe' }} >写作练习</div>
                    <div style={{ marginTop: 2, fontSize: 12, }}>让AI将您的作文批阅并改进</div>
                  </div>
                  <div className={style.functionCard} onClick={() => navigate('/reading')}>
                    <div className={style.cardImg}>
                      <img src={ReadingMan} alt='图' style={{ width: 200 }} />
                    </div>
                    <div style={{ marginTop: 20, fontSize: 21, fontFamily: 'youshe' }} >阅读训练</div>
                    <div style={{ marginTop: 2, fontSize: 12 }}>选择感兴趣话题，提高阅读能力</div>
                  </div>
                </div>
              </div>
              <div className={style.classContent}>
                {/* <div style={{fontFamily:'youshe',fontSize:26}}>我的班级</div> */}

                {/* {isJion?<HavaClass />:<JionClass />}   */}
                <HavaClass />

                <div className={style.everythingBox} onClick={() => setIsListOpen(true)}>
                  <div style={{ textAlign: 'center', fontFamily: 'youshe', fontSize: 24 }}>
                    <div style={{ marginTop: 6 }}>机器翻译</div>
                    <img src={TranRobot} alt='图片' style={{ width: 80, right: 150 }} />
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

              <Modal
                width={550}
                open={isJionClassOpen}
                onCancel={handleJionClassCancel}
                cancelButtonProps={{ style: { display: 'none' } }}
                okButtonProps={{ style: { display: 'none' } }}
              >
                <div style={{ textAlign: 'center', fontFamily: 'youshe', fontSize: 24 }}>加入班级</div>
                <div style={{ width: 'max-content', margin: "auto" }}>
                  <Input placeholder='输入班级号' style={{ width: 200, height: 40, marginTop: 30 }} />
                </div>
                <div style={{ width: 'max-content', margin: "auto" }}>
                  <Button style={{ marginTop: 30, background: '#3c3c3c', color: '#fff' }} onClick={() => handleJion()}>确定加入</Button>
                </div>
              </Modal>
            </Content>
          </Layout>
        </Layout>
      </Layout>
    </>
  );
};

const NewHome = () => {

  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1000);

    return () => clearTimeout(timer);
  }, []);

  return (
    <>
      {isLoading ? <HomeLoading /> : <HomeAPP />}

    </>
  )
}


export default NewHome;