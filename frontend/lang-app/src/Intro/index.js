// eslint-disable-next-line
import React, { useState } from 'react';
import style from './intro.module.css'
import { Outlet } from 'react-router-dom';


const Intro = () => {



  return ( 
    <>
    <div className={style.box}>

      <Outlet />

    </div>
    </>
  )
}

export default Intro