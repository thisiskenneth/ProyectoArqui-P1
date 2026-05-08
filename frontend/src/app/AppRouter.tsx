import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import TrackingPage from '../pages/tracking/TrackingPage';
import LoginPage from '../pages/login/LoginPage';
import OperatorDashboard from '../pages/operator/OperatorDashboard';
import ClientDashboard from '../pages/client/ClientDashboard';
import FleetDashboard from '../pages/fleet/FleetDashboard';

const AppRouter = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/tracking" replace />} />
        <Route path="/tracking" element={<TrackingPage />} />
        <Route path="/login" element={<LoginPage />} />
        
        {/* Protected Routes (Placeholders for now) */}
        <Route path="/operator/*" element={<OperatorDashboard />} />
        <Route path="/client/*" element={<ClientDashboard />} />
        <Route path="/fleet/*" element={<FleetDashboard />} />

        {/* Fallback */}
        <Route path="*" element={<div className="p-10 text-center">404 - Not Found</div>} />
      </Routes>
    </Router>
  );
};

export default AppRouter;
