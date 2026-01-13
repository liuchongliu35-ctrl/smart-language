import * as echarts from 'echarts';
import React from 'react';
import ReactEcharts from 'echarts-for-react';

const option = {
  grid: {
    left: 0,
    top: 0,
    right: '0%',
    bottom: '8%',
    containLabel: true
  },
  xAxis: [{
    show: false,
  }],
  yAxis: [{
    axisTick: 'none',
    axisLine: 'none',
    offset: '27',
    axisLabel: {
      textStyle: {
        color: '#ffffff',
        fontSize: '16',
      }
    },
    show:false,
    data: ['']
  }, {
    axisTick: 'none',
    axisLine: 'none',
    axisLabel: {
      textStyle: {
        color: '#ffffff',
        fontSize: '16',
      }
    },
    data: ['']
  }, {
    name: '分拨延误TOP 10',
    nameGap: '50',
    nameTextStyle: {
      color: '#ffffff',
      fontSize: '16',
    },
    axisLine: {
      lineStyle: {
        color: 'rgba(0,0,0,0)'
      }
    },
    data: [],
  }],
  series: [{
    name: '条',
    type: 'bar',
    yAxisIndex: 0,
    data: [271],
    label: {
      normal: {
        show: false,
        position: 'right',
        textStyle: {
          color: '#ffffff',
          fontSize: '16',
        }
      }
    },
    barWidth: 16,
    itemStyle: {
      normal: {
        color: '#17cd62',
        barBorderRadius: 5,
      }
    },
    z: 2
  }, {
    show: false,
    name: '外框',
    type: 'bar',
    yAxisIndex: 2,
    barGap: '-100%',
    data: [1000],
    barWidth: 16,
    itemStyle: {
      normal: {
        show: false,
        color: '#eaeaea',
        barBorderRadius: 5,
      }
    },
    z: 0
  }
  ]
};



const ProgressEchart = () => {


  return <ReactEcharts option={option} style={{width:620,height:50}}/>;
};

export default ProgressEchart;

