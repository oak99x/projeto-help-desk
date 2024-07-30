import { useState } from 'react';
import reactLogo from '../assets/logo.svg';
import { DashboardOutlined, ConfirmationNumberOutlined, GroupOutlined, FeedOutlined, LogoutSharp } from '@mui/icons-material';
import { FaUserGear } from "react-icons/fa6";
import { Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';


const Menu = () => {

    const navigate = useNavigate();
    const [activeMenu, setActiveMenu] = useState<string>('');
    const { logout } = useAuth();
    const { isAdmin } = useAuth();

    // useEffect(() => {
    //     //navigate('/dashboard');
    //     setActiveMenu('dashboard');
    //     // // Este código será executado apenas uma vez após o componente ser montado
    //     // if (auth.isAdmin() !== "admin") {
    //     //     navigate('/dashboard'); // Redireciona para o dashboard se não for admin
    //     //     setActiveMenu('dashboard');
    //     // } else {
    //     //     navigate('/tickets'); // Redireciona para tickets se for admin
    //     //     setActiveMenu('tickets');
    //     // }
    // }, []);

    // useEffect(()=>{setActiveMenu('dashboard')}, [])

    // Função para definir o item ativo
    const handleMenuItemClick = (itemName: string) => {
        setActiveMenu(itemName);
        itemName === 'logout' ? logout() : navigate(`/${itemName}`);
    };

    return (
        <>

            <div className='flex w-full h-full'>
                <div className='flex h-full fixed w-24 z-10'>
                    <div className='flex flex-col h-screen justify-between bg-primary py-2 w-full'>
                        <ul className="menu  text-gray-50 space-y-1 text-xs p-0">
                            <li className='active:bg-transparent'>
                                <a onClick={() => handleMenuItemClick('dashboard')} className={`rounded-none flex flex-col justify-center items-center`}>
                                    <img src={reactLogo} className="logo react" alt="React logo" width={40} />
                                </a>
                            </li>

                            {/* {auth.isAdmin() !== "admin" && (
                                <li>
                                    <a
                                        onClick={() => handleMenuItemClick('dashboard')}
                                        className={`rounded-none flex flex-col justify-center items-center ${activeMenu === 'dashboard' ? 'bg-gray-50 text-primary hover:bg-gray-100' : ''}`}
                                    >
                                        <DashboardOutlined />
                                        Dashboard
                                    </a>
                                </li>
                            )} */}

                            <li>
                                <a
                                    onClick={() => handleMenuItemClick('dashboard')}
                                    className={`rounded-none flex flex-col justify-center items-center ${activeMenu === 'dashboard' ? 'bg-gray-50 text-primary font-semibold hover:bg-gray-100' : ''}`}
                                >
                                    <DashboardOutlined />
                                    Dashboard
                                </a>
                            </li>

                            <li>
                                <a onClick={() => handleMenuItemClick('tickets')} className={`rounded-none flex flex-col justify-center items-center ${activeMenu === 'tickets' ? 'bg-gray-50 text-primary font-semibold hover:bg-gray-100' : ''}`}>
                                    <ConfirmationNumberOutlined />
                                    Tickets
                                </a>
                            </li>

                            {isAdmin() && (
                                <>
                                    <li>
                                        <a onClick={() => handleMenuItemClick('agents')} className={`rounded-none flex flex-col justify-center items-center ${activeMenu === 'agents' ? 'bg-gray-50 text-primary font-semibold hover:bg-gray-100' : ''}`}>
                                            <GroupOutlined />
                                            Agents
                                        </a>
                                    </li>
                                    <li>
                                        <a onClick={() => handleMenuItemClick('reports')} className={`rounded-none flex flex-col justify-center items-center ${activeMenu === 'reports' ? 'bg-gray-50 text-primary font-semibold hover:bg-gray-100' : ''}`}>
                                            <FeedOutlined />
                                            Reports
                                        </a>
                                    </li>
                                </>
                            )}
                        </ul>
                        <ul className="menu bg-primary text-gray-50 space-y-1 text-xs p-0">
                            <li>
                                <a onClick={() => handleMenuItemClick('manage')} className={`rounded-none flex flex-col justify-center items-center ${activeMenu === 'manage' ? 'bg-gray-50 text-primary font-semibold hover:bg-gray-100' : ''}`}>
                                    <FaUserGear size={22} />
                                    Manage
                                </a>
                            </li>
                            <li>
                                <a onClick={() => handleMenuItemClick('logout')} className={`rounded-none flex flex-col justify-center items-center `}>
                                    <LogoutSharp />
                                    Logout
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>

                <div className='w-full h-full ml-24'>
                    <Outlet />
                </div>
            </div>


        </>
    )
}

export default Menu