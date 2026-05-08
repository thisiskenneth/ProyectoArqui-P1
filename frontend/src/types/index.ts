export interface User {
  id: string;
  name: string;
  role: 'ADMIN' | 'OPERATOR' | 'CLIENT' | 'FLEET';
}

export interface Shipment {
  id: string;
  trackingNumber: string;
  status: string;
  origin: string;
  destination: string;
}
