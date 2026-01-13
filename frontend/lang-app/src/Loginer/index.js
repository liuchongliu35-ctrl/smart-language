import { Checkbox, Input, message } from 'antd'
import BackImg from '../image/首页.jpg'
import Logo from '../image/logo.png'
import style from './loginer.module.css'
import { useNavigate } from 'react-router-dom'

const Login = () => {

  const navigate = useNavigate()
  
  const handleClick = () =>{
    navigate('/')
    message.success('登陆成功')
  }

  return (
    <>
      <div className={style.body}>
        <img alt='1' src={BackImg} className={style.backImg} />
        <div className={style.loginBox}>
          <div className={style.logoBar}>
        <img alt='2' src={Logo} className={style.logo} />
          </div>
          <div className={style.textBar}>
            <div className={style.cnText}>欢迎登录</div>
            <div className={style.enText}>Welcome Login</div>
          </div>
          <div className={style.contentBox}>
            <div>用户名</div>
            <Input style={{background:'#eaeaea',fontSize:12,height:35}} placeholder='请输入用户名'/>
            <div style={{marginTop:20}}>密码</div>
            <Input.Password style={{background:'#eaeaea',fontSize:12,height:35}} placeholder='请输入密码' />
            <div style={{marginTop:10,display:'flex'}}>
            <Checkbox style={{fontFamily:'none',fontSize:13,color:'#21353c'}}>记住密码</Checkbox>
            <div style={{flex:1}}></div>
            <div style={{fontFamily:'none',fontSize:13,color:'#21353c',textDecoration:'underline',cursor:'pointer'}} >忘记密码?</div>
            </div>
            <div style={{width:'max-content',margin:'auto'}}>
            <button className={style.btn} onClick={()=>navigate('/intro')}>登 录</button>
            <div style={{textAlign:'center',fontSize:25,marginTop:10,height:20}}>And</div>
            <button className={style.btn} onClick={()=>handleClick()}>游 客   登 录</button>
            </div>
          </div>
          <div style={{fontSize:12,color:"#21353c",textAlign:'center'}}>还没有账号？<span style={{color:'rgba(165,220,160)',textDecoration:'underline',cursor:'pointer'}}>立即注册</span></div>
        </div>
      </div>
    </>
  )

}

export default Login