import style from './wordsbook.module.css'
// eslint-disable-next-line
import { Layout, theme, Avatar, Modal, Tag, Checkbox, Input, Form, Button, Popover, Spin, Space, Progress } from 'antd';
// eslint-disable-next-line
import React, { useEffect, useRef, useState } from 'react'
import Nature from '../image/nature.png'
import Daliy from '../image/日常.png'
import Mai from '../image/麦克风.png'
import Boy from '../image/男孩头像.png'
import Message from '../image/留言建议.png'
import Notice from '../image/通知中心.png'
import Mybook from '../image/book.png'
import Science from '../image/科学.png'
import Shezhi from '../image/设置.png'
import Right from '../image/右箭头.png'
import Kefu from '../image/客服.png'
import OneBook from '../image/oneBook.png'
import TwoBook from '../image/twoBook.png'
import ThreeBook from '../image/threeBook.png'
import FourBook from '../image/fourBook.png'
import FiveBook from '../image/fiveBook.png'
import SixBook from '../image/sixBook.png'
import SevenBook from '../image/sevenBook.png'
import EightBook from '../image/eightBook.png'
import NineBook from '../image/nineBook.png'
import TenBook from '../image/tenBook.png'
import ElevenBook from '../image/elevenBook.png'
import Twelve from '../image/12.png'
import Thirteen from '../image/13.png'
import Fourteen from '../image/14.png'
import Fifteen from '../image/15.png'
// eslint-disable-next-line
import { useNavigate } from 'react-router-dom';
import Logo from '../image/ZhiDuoYuLogo.png'
// eslint-disable-next-line
import { PlusCircleOutlined, UserOutlined, QuestionCircleOutlined, LoadingOutlined, SearchOutlined, CaretDownOutlined } from '@ant-design/icons';
import { request } from '../utils/request';
import WordsBookLoading from '../loadingComponent/wordsBookLoading';
const { Content, Sider } = Layout;


const natureList = [
  {
    text: '植物'
  }, {
    text: '水果'
  }, {
    text: '蔬菜'
  }, {
    text: '天气'
  }
]

const daliyList = [
  {
    text: '食物'
  }, {
    text: '服装'
  }, {
    text: '交通'
  }, {
    text: '职业'
  }, {
    text: '购物'
  }
]

const entertamentList = [
  {
    text: '音乐'
  }, {
    text: '节日'
  }, {
    text: '娱乐'
  }
]

const scienceList = [
  {
    text: '健康'
  }, {
    text: '学科'
  }, {
    text: '工具'
  }
]

const animalWordList = [
  {
    text: '',
    translation: '',
    state: 1,
    mp3Url: ''
  },
]


const WordListLoading = () => {
  const [grammarLoadingPercent, setGrammarLoadingPercent] = useState(0)

  useEffect(() => {
    const interval = setInterval(() => {
      setGrammarLoadingPercent((prevProgress) => {
        if (prevProgress >= 100) {
          clearInterval(interval)
          return 100
        } else {
          return prevProgress + 1
        }
      });
    }, 6000 / 100); // 将7秒分成100份，每份间隔的时间
    return () => {
      clearInterval(interval)
    };
  }, [])

  return (
    <div className={style.wordLoading}>
      <div style={{ textAlign: "center", marginTop: 100, fontFamily: 'youshe', fontSize: 24 }}>{grammarLoadingPercent >= 20 ? '正在生成语法中' : '单词玩命加载中'}</div>
      <div style={{ width: 'max-content', margin: 'auto', marginTop: 20 }}><Progress type="circle" percent={grammarLoadingPercent} /></div>

      <div style={{ display: 'flex', width: 'max-content', margin: 'auto', marginTop: 30 }}>
        <div class={style.dot1}></div>
        <div class={style.dot2}></div>
        <div class={style.dot3}></div>
      </div>
    </div>
  )
}

//添加场景加载页面
const AddLoading = () => {
  const [grammarLoadingPercent, setGrammarLoadingPercent] = useState(0)

  useEffect(() => {
    const interval = setInterval(() => {
      setGrammarLoadingPercent((prevProgress) => {
        if (prevProgress >= 100) {
          clearInterval(interval)
          return 100
        } else {
          return prevProgress + 1
        }
      });
    }, 15000 / 100); // 将7秒分成100份，每份间隔的时间
    return () => {
      clearInterval(interval)
    };
  }, [])
  return (
    <div className={style.addLoading}>
      <div style={{ width: 'max-content', height: 'max-content', margin: 'auto', marginTop: 80 }}>
        <div style={{ textAlign: "center", marginBottom: 20, fontFamily: 'youshe', fontSize: 22 }}>AI大模型生成单词中</div>

        <div style={{ width: 'max-content', margin: 'auto', marginTop: 20 }}><Progress type="circle" percent={grammarLoadingPercent} /></div>
      </div>
      <div style={{ display: 'flex', width: 'max-content', marginLeft: 170, marginTop: 40 }}>
        <div class={style.dot1}></div>
        <div class={style.dot2}></div>
        <div class={style.dot3}></div>
      </div>

    </div>
  )
}


//添加场景API
// eslint-disable-next-line
function postClampAPI(value) {
  return request({
    url: '/wordBook/addClamp',
    method: 'POST',
    data: value
  })
}

//拓展单词API
function postAddWordsAPI(value) {
  return request({
    url: `extend/words?cid=${value}`,
    method: 'POST',
  })
}



const userCurrentScene = []



const WordsbookApp = () => {
  const uid = localStorage.getItem('userID')
  const [isGrammarCom, setIsGrammarCom] = useState('505')
  const [sceneClamp, setSceneClamp] = useState('')
  const [wordsList, setWordsList] = useState(animalWordList)
  const [natureClampList, setNatureClampList] = useState(userCurrentScene)
  const [dailyClampList, setDailyClampList] = useState([])
  const [studyClampList, setStudyClampList] = useState([])
  const [scienceClampList, setScienceClampList] = useState([])
  const [kind, setKind] = useState('')
  const [understood, setUnderstood] = useState(0)
  const [cid, setCid] = useState('')
  const [isAddLoading, setIsAddLoading] = useState(false)
  const [isWordLoading, setIsWordLoading] = useState(false)
  // const [isExtend,setIsExtend] = useState(false)
  // const [wordsInterval,setWordsInterval] = useState(null)
  const [form] = Form.useForm()
  const navigate = useNavigate()
  const [clicked, setClicked] = useState(false)
  const handleClickChange = (open) => {
    setClicked(open);
  };

  const {
    token: { borderRadiusLG }
  } = theme.useToken();


  //当前场景预览
  // eslint-disable-next-line
  function getWordsListAPI(e) {
    return request({
      url: `/wordBook/words?uid=${uid}&type=${e}`,
      method: 'GET'
    })
  }

  const getWordList = async (e) => {
    let { data, message } = await getWordsListAPI(e)
    setWordsList(data)
    setUnderstood(message)
  }

  const getCampCode = async (e) => {
    let { code } = await getWordsListAPI(e)
    setIsGrammarCom(code)
  }


  // eslint-disable-next-line
  useEffect(() => {
    // 执行其他需要在状态更新之后执行的命令
    // eslint-disable-next-line
    getClampList()
  }, []);

  const getClampList = async () => {
    let { data } = await getClampListAPI();

    const updateListByKind = (kind, item) => {
      switch (kind) {
        case '自然':
          setNatureClampList(prevList => [...prevList, item])
          break
        case '日常':
          setDailyClampList(prevList => [...prevList, item])
          break
        case '学习娱乐':
          setStudyClampList(prevList => [...prevList, item])
          break
        case '科学':
          setScienceClampList(prevList => [...prevList, item])
          break
        default:
          break
      }
    };

    data.forEach(item => {
      updateListByKind(item.kind, item)
    })
  }


  //获取该用户存在的场景
  // eslint-disable-next-line
  function getClampListAPI() {
    return request({
      url: `/wordBook/getClamp?uid=${uid}`,
      method: 'GET',
    })
  }

  //添加场景弹窗控制
  const [isModalOpen, setIsModalOpen] = useState(false);


  const showModal = (e) => {
    setIsModalOpen(true);
    setKind(e)
    // console.log(e)
  };

  const handleOk = () => {
    setIsModalOpen(false);
  };
  const handleCancel = () => {
    setIsModalOpen(false);
  };

  //单词表预览弹窗设置
  const [isListOpen, setIsListOpen] = useState(false)
  const showList = async (e, cid) => {
    let { code } = await getWordsListAPI(e)
    getWordList(e)
    setSceneClamp(e)
    setCid(cid)
    setIsListOpen(true)
    setIsWordLoading(true)
    const delay = code === '504' ? 2000 : 6000
    setTimeout(() => {
      setIsWordLoading(false)
      getWordList(e)
    }, delay);
  }


  const handleListCancel = () => {
    setIsListOpen(false)
    // clearInterval(wordsInterval)
  }


  //表单提交
  const handleFinish = async (values) => {
    const typeStr = values.scene + ',' + values.define
    const arr = typeStr.split(',')
    const clamp = {
      uid: uid,
      type: arr,
      kind: kind
    }
    await postClampAPI(clamp)
    setIsAddLoading(true)
    setTimeout(() => {
      setIsAddLoading(false)
      setIsModalOpen(false)
      window.location.reload()
    }, 15000);
  }



  const resetFormFields = () => {
    form.resetFields(); // 清空表单状态
  };

  const extendMethod = async (cid) => {
    await postAddWordsAPI(cid)
    setTimeout(() => {
      setIsWordLoading(false)
      window.location.reload()
    }, 6000)
  }


  //拓展单词
  const handleAddWords = async (cid) => {
    setIsWordLoading(true)
    extendMethod(cid)
  }



  const ListContentComponent = () => (
    <>
      <div className={style.listTitle}>{sceneClamp}</div>
      <div style={{ display: 'flex', marginTop: 30 }}>
        <div style={{ display: 'flex' }}>未掌握:&nbsp;&nbsp;&nbsp;<span style={{ textAlign: 'center', fontWeight: 600, width: 25, height: 20, background: '#e5a84b', borderRadius: '10%' }}> {wordsList.length - understood} </span>&nbsp;&nbsp;&nbsp;个</div>
        <div style={{ flex: 1 }}></div>
        <div>已掌握: &nbsp;&nbsp;<span style={{ textAlign: 'center', fontWeight: 700, color: '#4f4032', height: 20, background: '#fedc5e', borderRadius: '10%' }}> {understood} </span>&nbsp;&nbsp; 个</div>
      </div>
      <div className={style.wordListBox}>
        {wordsList.map((i, index) => (
          <div className={i.state === 0 ? style.wordBar : style.wordBarUnderstood} key={index}>
            <div className={i.state === 0 ? style.wordIndex : style.wordIndexUnderstood}>{index + 1}</div>
            <div style={{ flex: 1 }}></div>
            <div className={style.wordText}>{i.text}</div>
            <div style={{ flex: 1 }}></div>
            <div className={style.wordTranslation}>{i.motherText}</div>
          </div>
        ))}
        <Popover
          content={<div style={{ width: 'max-content', height: 20, textAlign: 'center', fontSize: 12 }}>AI大模型继续拓展单词</div>}
        >
          <div style={{ width: 'max-content', margin: 'auto' }}><Button onClick={() => handleAddWords(cid)}>扩展单词<CaretDownOutlined /></Button></div>
        </Popover>

      </div>
      <div style={{ width: 'max-content', margin: 'auto' }}>
        <Button className={style.listBtn} onClick={() => navigate(`/learnWords/${sceneClamp}/${cid}`)}>开始学习</Button>
      </div>
    </>
  )

  function selBookMethod(e) {
    switch (e) {
      case 0:
        return OneBook
      case 1:
        return TwoBook;
      case 2:
        return ThreeBook;
      case 3:
        return FourBook;
      case 4:
        return FiveBook;
      case 5:
        return SixBook;
      case 6:
        return SevenBook;
      case 7:
        return EightBook;
      case 8:
        return NineBook;
      case 9:
        return TenBook;
      case 10:
        return ElevenBook;
      case 11:
        return Twelve;
      case 12:
        return Thirteen;
      case 13:
        return Fourteen;
      case 14:
        return Fifteen;
      default:
        break;
    }
  }

  return (
    <div>
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
              padding: '0 4px 24px',
            }}
          >

            <Content
              style={{
                height: 'max-content',
                padding: 20,
                marginLeft: 240,
                minHeight: 600,
                borderRadius: borderRadiusLG,
                position: 'relative',
                background: '#fff6e87b'
              }}
            >
              <div className={style.contentTitle}>
                单词本&nbsp;<Avatar src={Right} style={{ cursor: 'pointer' }} onClick={() => navigate('/')} />
                <div className={style.searchBox}>
                  <input className={style.searchBar} placeholder='搜索已添加的场景' />
                  <div style={{ width: 40, height: 40, textAlign: 'center', lineHeight: 1, background: 'rgba(255,187,0)' }}>
                    <SearchOutlined style={{ fontSize: 24 }} />
                  </div>
                </div>
              </div>

              <div className={style.bookBox}>
                <div className={style.wordsbookBox}>

                  <div className={style.bookTitle}>自然 <Avatar src={Nature} shape='square' /> </div>
                  <div className={style.bookList}>
                    {natureClampList.map((item, index) => (
                      <div className={style.sceneCard} key={index} >
                        <div className={style.cardContent} onClick={() => showList(`${item.type}`, `${item.clampId}`)}>
                          <div>
                            <Avatar src={selBookMethod(item.random)} shape='square' size={72} style={{ borderBottom: '2px solid #dbdbdb' }} />
                          </div>
                          <div style={{ fontFamily: 'youshe', color: '#4f4032', fontSize: 22 }}>
                            {item.type}
                          </div>
                        </div>
                      </div>
                    ))}
                    <div className={style.sceneCard} onClick={() => showModal('自然')}>
                      <div style={{ width: 'max-content', height: 'maxcontent', margin: 'auto', lineHeight: 7 }}>
                        <PlusCircleOutlined style={{ fontSize: 40, marginTop: 35 }} />

                      </div>
                    </div>
                  </div>
                </div>

                <div className={style.wordsbookBox}>
                  <div className={style.bookTitle}>日常  <Avatar src={Daliy} shape='square' /></div>
                  <div className={style.bookList}>
                    {dailyClampList.map((item, index) => (
                      <div className={style.sceneCard} key={index} >
                        <div className={style.cardContent} onClick={() => showList(`${item.type}`, `${item.clampId}`)}>
                          <div>
                            <Avatar src={selBookMethod(item.random)} shape='square' size={72} style={{ borderBottom: '2px solid #dbdbdb' }} />
                          </div>
                          <div style={{ fontFamily: 'youshe', color: '#4f4032', fontSize: 22 }}>
                            {item.type}
                          </div>
                        </div>
                      </div>
                    ))}
                    <div className={style.sceneCard} onClick={() => showModal('日常')}>
                      <div style={{ width: 'max-content', height: 'maxcontent', margin: 'auto', lineHeight: 7 }}>
                        <PlusCircleOutlined style={{ fontSize: 40, marginTop: 35 }} />
                      </div>
                    </div>
                  </div>
                </div>

                <div className={style.wordsbookBox}>
                  <div className={style.bookTitle}>学习娱乐  <Avatar src={Mai} shape='square' /></div>
                  <div className={style.bookList}>
                    {studyClampList.map((item, index) => (
                      <div className={style.sceneCard} key={index} >
                        <div className={style.cardContent} onClick={() => showList(`${item.type}`, `${item.clampId}`)}>
                          <div>
                            <Avatar src={selBookMethod(item.random)} shape='square' size={72} style={{ borderBottom: '2px solid #dbdbdb' }} />
                          </div>
                          <div style={{ fontFamily: 'youshe', color: '#4f4032', fontSize: 22 }}>
                            {item.type}
                          </div>
                        </div>
                      </div>
                    ))}
                    <div className={style.sceneCard} onClick={() => showModal('学习娱乐')} >
                      <div style={{ width: 'max-content', height: 'maxcontent', margin: 'auto', lineHeight: 7 }}>
                        <PlusCircleOutlined style={{ fontSize: 40, marginTop: 35 }} />
                      </div>
                    </div>
                  </div>
                </div>

                <div className={style.wordsbookBox}>
                  <div className={style.bookTitle}>科学  <Avatar src={Science} shape='square' /></div>
                  <div className={style.bookList}>
                    {scienceClampList.map((item, index) => (
                      <div className={style.sceneCard} key={index} >
                        <div className={style.cardContent} onClick={() => showList(`${item.type}`, `${item.clampId}`)}>
                          <div>
                            <Avatar src={selBookMethod(item.random)} shape='square' size={72} style={{ borderBottom: '2px solid #dbdbdb' }} />
                          </div>
                          <div style={{ fontFamily: 'youshe', color: '#4f4032', fontSize: 22 }}>
                            {item.type}
                          </div>
                        </div>
                      </div>
                    ))}

                    <div className={style.sceneCard} onClick={() => showModal('科学')} >
                      <div style={{ width: 'max-content', height: 'maxcontent', margin: 'auto', lineHeight: 7 }}>
                        <PlusCircleOutlined style={{ fontSize: 40, marginTop: 35 }} />

                      </div>
                    </div>
                  </div>
                </div>


              </div>

            </Content>
          </Layout>
        </Layout>
      </Layout>

      <Modal
        afterClose={resetFormFields}
        cancelButtonProps={{ style: { display: 'none' } }}
        okButtonProps={{ style: { display: 'none' } }}
        title="选择场景" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>

        {isAddLoading && <AddLoading />}
        <Form
          name="basic"
          onFinish={handleFinish}
          form={form}
        >
          <div className={style.modalBox}>
            {kind === '自然' && <div className={style.modalTitle}>
              自然 <Avatar src={Nature} shape='square' />
            </div>}
            {kind === '日常' && <div className={style.modalTitle}>
              日常 <Avatar src={Daliy} shape='square' />
            </div>}
            {kind === '学习娱乐' && <div className={style.modalTitle} style={{ width: 'max-content' }}>
              学习娱乐 <Avatar src={Mai} shape='square' />
            </div>}
            {kind === '科学' && <div className={style.modalTitle}>
              科学 <Avatar src={Science} shape='square' />
            </div>}

            <div className={style.sceneSelBox}>

              <Form.Item
                name='scene'
                valuePropName="checked"
              >
                <Checkbox.Group>
                  {kind === '自然' && natureList.map((i, index) => (
                    <Checkbox value={i.text} className={style.checkbox} key={index}>
                      <div className={style.sceneSelCard}>
                        {i.text}
                      </div>
                    </Checkbox>
                  ))}
                  {kind === '日常' && daliyList.map((i, index) => (
                    <Checkbox value={i.text} className={style.checkbox} key={index}>
                      <div className={style.sceneSelCard}>
                        {i.text}
                      </div>
                    </Checkbox>
                  ))}
                  {kind === '学习娱乐' && entertamentList.map((i, index) => (
                    <Checkbox value={i.text} className={style.checkbox} key={index}>
                      <div className={style.sceneSelCard}>
                        {i.text}
                      </div>
                    </Checkbox>
                  ))}
                  {kind === '科学' && scienceList.map((i, index) => (
                    <Checkbox value={i.text} className={style.checkbox} key={index}>
                      <div className={style.sceneSelCard}>
                        {i.text}
                      </div>
                    </Checkbox>
                  ))}
                </Checkbox.Group>
              </Form.Item>

            </div>
            <div className={style.modalTitle} style={{ width: 'max-content', marginBottom: 20 }}>
              自定义添加 &nbsp;
              <Popover
                content={<div style={{ width: 'max-content', height: 20, textAlign: 'center', fontSize: 12 }}>AI大模型会根据您自定义的场景给您推送相关单词</div>}
              >
                <QuestionCircleOutlined className={style.wenhao} />
              </Popover>
            </div>
            <div style={{ width: 'max-content', margin: 'auto' }}>
              <Form.Item
                name='define'>
                <Input style={{ width: 130, height: 30, margin: 'auto' }} placeholder='输入文字' ></Input>
              </Form.Item>
            </div>

            <Form.Item style={{ marginTop: 40 }}>
              <Button type="primary" htmlType="submit" style={{ marginLeft: 200, background: '#ffc10b', fontFamily: 'youshe', fontSize: 18 }}>
                确定
              </Button>
            </Form.Item>
          </div>
        </Form>
      </Modal>

      <Modal
        open={isListOpen}
        onCancel={handleListCancel}
        cancelButtonProps={{ style: { display: 'none' } }}
        okButtonProps={{ style: { display: 'none' } }}
      >
        {isWordLoading ? <WordListLoading /> : <ListContentComponent />}
        {/* {isExtend && <ExtendWordLoading />} */}
      </Modal>

    </div>
  )
}




export default WordsbookApp