import React, { createContext, useState, useEffect, useContext, ReactNode } from 'react';
import { LoginData, loginService } from '../api/AuthService';  // Import your authentication services
import { User, getUser } from '../api/UserService';  // Assume types are defined here
import { useNavigate } from 'react-router-dom';

interface AuthContextType {
    user: User | null;
    isAuthenticated: () => boolean;
    isAdmin: () => boolean;
    login: (data: LoginData) => Promise<void>;
    logout: () => void;
    token: () => string | null;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);

    const navigate = useNavigate();

    useEffect(() => {
        // Tentar carregar o usuário do localStorage ao iniciar
        const initializeAuth = async () => {
            const token = localStorage.getItem('access_token');
            if (token) {
                try {
                    const user = await getUser(token);
                    setUser(user);
                } catch (error) {
                    console.error('Failed to fetch user:', error);
                }
            }
        };
        initializeAuth();
    }, []);

    const login = async (data: LoginData) => {
        try {
            await loginService(data);
            const token = localStorage.getItem('access_token');
            const user = await getUser(token);
                    setUser(user);
            navigate('/dashboard');
        } catch (error:any) {
            console.error('Login failed:', error);
            throw new Error(error);
        }
    };

    const logout = () => {
        localStorage.removeItem('access_token');
        localStorage.removeItem('expires_at');
        localStorage.removeItem('is_admin');
        setUser(null);
        navigate("/login");
    };

    const isAuthenticated = () => {
        const expiresAt = localStorage.getItem('expires_at');
        const isAuthenticated = expiresAt? (new Date().getTime() < Number(expiresAt)) : false;

        if(!isAuthenticated){ // se for false força logout
            logout();
        }
        
        return isAuthenticated;
    };

    const isAdmin = () => {
        const isAdmin = localStorage.getItem('is_admin') === 'admin';
        return isAdmin;
    }

    const token = () => {
        return localStorage.getItem('access_token');
    }


    return (
        <AuthContext.Provider value={{ user, login, logout, isAuthenticated, isAdmin, token }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

