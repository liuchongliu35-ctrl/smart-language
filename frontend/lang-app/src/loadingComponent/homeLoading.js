
// eslint-disable-next-line
import { Layout,theme, Skeleton } from 'antd';
// eslint-disable-next-line
import React, { useEffect, useState } from 'react'

import { useNavigate } from 'react-router-dom';

// eslint-disable-next-line

const { Header, Content, Sider } = Layout;






const HomeLoading = () =>{
    const {
        token: { colorBgContainer, borderRadiusLG }
      } = theme.useToken();


    return(
       <div>
   <Layout>
      <Header
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
      
     
      </Header>
      <Layout>
        <Sider
          height={1200}
          width={250}
          style={{
            background:'#21353c',
            borderRadius: borderRadiusLG,
            position:'fixed',
            height:680,
            top:60
          }}
        >
         

         <Skeleton active />
          

        
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
              marginTop:50,
              marginLeft: 250,
              minHeight: 680,
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
        
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

export default HomeLoading