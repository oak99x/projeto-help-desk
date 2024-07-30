import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from './AuthContext'; // adjust the path according to your structure

const PrivateRoute = () => {
    const { isAuthenticated} = useAuth();

    return isAuthenticated() ? <Outlet /> : <><Navigate to="/login" /></>;
};

export default PrivateRoute;

