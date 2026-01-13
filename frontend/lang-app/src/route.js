import Login from './Loginer/index'
import Intro from './Intro'
import LanSelect from './Intro_lanSelect'
import MasSelect from './Intro_masSelect'
import MasQuestion from './Intro_masQuestion'
import Introduction from './Intro_introduction'
import Compare from './Intro_Compare'
import Yinbiao from './learn_yinbiao'
import Communication from './Communication'
import Wordsbook from './WordsBook'
import WordsLearn from './WordsBook_learn'
import Writing from './writing'
import Reading from './Reading'
import NewHome from './newHome'
import Teacher from './Teacher'

import { createBrowserRouter } from 'react-router-dom'


const route = createBrowserRouter([
  {
    index: true,
    element: <NewHome />
  },
  {
    path: '/Login',
    element: <Login />
  }, {
    path: '/intro',
    element: <Intro />,
    children:[
      {
        index:true,
        element:<LanSelect />
      },{
        path:'/intro/masSelect',
        element:<MasSelect />
      },{
        path:'/intro/masQuestion',
        element:<MasQuestion />
      },{
        path:'/intro/introduction',
        element:<Introduction />
      },{
        path:'/intro/compare',
        element:<Compare />
      }
    ]
  },{
    path:'/learn',
    element:<Yinbiao />
  },{
    path:'/communication',
    element:<Communication />
  },{
    path:'/wordsbook',
    element:<Wordsbook />
  },{
    path:'/learnWords/:clamp/:cid',
    element:<WordsLearn />
  },{
    path:'/writingCorrect',
    element:<Writing />
  },{
    path:'/reading',
    element:<Reading />
  },{
    path:'/test',
    element:<Teacher />
  }

])

export default route