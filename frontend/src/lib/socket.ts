import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// Usamos el proxy de Vite para evitar CORS
// Para WebSockets, el cliente de SockJS suele requerir la URL completa, así que usamos window.location.origin
const WS_URL = `${window.location.origin}/ws-tracking`;

export const createStompClient = () => {
  const client = new Client({
    webSocketFactory: () => new SockJS(WS_URL),
    debug: function (str) {
      console.log('STOMP: ' + str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  });

  return client;
};
