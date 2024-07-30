import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from "./pages/Login";
import DefaultLayout from "./pages/DefaultLayout";
import PrivateRoute from './auth/PrivateRoute';
import { Dashboard } from './pages/Dashboard';
import { AuthProvider } from './auth/AuthContext';

import Tickets from './pages/Tickets';
import NewTicket from './pages/MenuTickets/NewTicket';
import AllTickets from './pages/MenuTickets/AllTickets';
import OpenTickets from './pages/MenuTickets/OpenTickets';
import ConcludedTickets from './pages/MenuTickets/ConcludedTickets';
import MyTickets from './pages/MenuTickets/MyTickets';
import Ticket from './pages/MenuTickets/Ticket/Ticket';
import Agents from './pages/Agents';
import AllUsers from './pages/MenuAgents/AllUsers';
import Clients from './pages/MenuAgents/Clients';
import UserAgents from './pages/MenuAgents/Agents';
import Reports from './pages/Reports';
import Manage from './pages/Manage';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route element={<PrivateRoute />}>
            <Route path="/" element={<DefaultLayout />}>
              <Route path="dashboard" element={<Dashboard />} />
              <Route path="tickets" element={<Tickets />}>
                <Route path="my-tickets" element={<MyTickets />} />
                <Route path="new-ticket" element={<NewTicket />} />
                <Route path="all-tickets" element={<AllTickets />} />
                <Route path="open" element={<OpenTickets />} />
                <Route path="concluded" element={<ConcludedTickets />} />
                <Route path=":id" element={<Ticket />} />
              </Route>
              <Route path="agents" element={<Agents />}>
                <Route path="all-users" element={<AllUsers />} />
                <Route path="clients" element={<Clients />} />
                <Route path="agents" element={<UserAgents />} />
              </Route>
              <Route path="reports" element={<Reports />}>
                <Route path="#" element={<Reports />} />
              </Route>
              <Route path="manage" element={<Manage />}>
                <Route path="#" element={<Manage />} />
              </Route>
            </Route>
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
