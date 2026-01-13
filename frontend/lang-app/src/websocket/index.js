import { useEffect, useRef, useState } from 'react';
import WebSocketClient from 'websocket';


export function useWebSocket(accessToken, requestName) {
  const clientRef = useRef<WebSocketClient | null>(null);
  const [isActive, setIsActive] = useState<boolean>(false);
  const [socketClient, setSockClient] = useState<WebSocketClient | null>(null);
  // 获取url
  let port = window.location.port;
  let wsUrl = '';

  if (window.location.protocol === 'https:') {
    //如果当前是HTTPS加密的，那么使用wss
    if (!port) {
      port = '4174';
    }
    wsUrl = 'wss:';
  } else {
    if (!port) {
      port = '8080';
    }
    wsUrl = 'ws:';
  }
  wsUrl +=
    `//${window.location.hostname}:${port}/api/ws/plugins/${requestName}?token=` +
    accessToken;
  if (!socketClient) {
    setSockClient(new WebSocketClient(wsUrl, isActive)); // 创建 WebSocketClient 实例并传入 URL 和活动状态 isActive
  }

  useEffect(() => {
    clientRef.current = socketClient;
    if (!socketClient?.socket) {
      socketClient?.start(); // 启动WebSocket连接
    }
    return () => {
      socketClient?.close(); // 组件卸载时关闭WebSocket连接
    };
  }, []);

  // 建立 WebSocket 连接
  const connect = () => {
    const client = clientRef.current;

    if (client) {
      client.connect(); // 建立WebSocket连接
    }
  };

  // 关闭 WebSocket 连接
  const close = () => {
    const client = clientRef.current;

    if (client) {
      client.close(); // 关闭WebSocket连接
    }
  };

  // 订阅消息处理程序
  const subscribe = (handler) => {
    const client = clientRef.current;

    setIsActive(true);
    if (client) {
      client.subscribe(handler);
    }
  };

  // 取消订阅消息
  const unsubscribe = () => {
    const client = clientRef.current;

    if (client && isActive) {
      setIsActive(false);
      client.unsubscribe();
    }
  };

  // 发送消息
  const send = (message) => {
    const client = clientRef.current;

    if (client && client.socket?.readyState === WebSocket.OPEN) {
      client.send(message); // 发送消息
    } else if (client && client.socket?.readyState === WebSocket.CLOSED) {
      // WebSocket连接未建立或已关闭，需要重新建立连接
      client.connect(); // 建立WebSocket连接
    }
  };

  return { connect, close, subscribe, unsubscribe, send };
}
