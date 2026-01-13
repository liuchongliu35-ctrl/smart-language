import React from 'react';
import ReactDOM from 'react-dom/client';
import route from './route';
import { RouterProvider, HashRouter } from 'react-router-dom'
import './style.css'

const root = ReactDOM.createRoot(
  document.getElementById('root'))
root.render(
  <>
    <RouterProvider router={route} />
  </>
);