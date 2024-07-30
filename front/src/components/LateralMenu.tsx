import React from 'react'
import { useState, useEffect } from 'react';
import { Outlet, useNavigate, NavLink } from 'react-router-dom';

// Definindo a interface para um item do menu
interface MenuItem {
  label: string;
  link: string;
}

// Definindo a interface para as props do componente LateralMenu
interface LateralMenuProps {
  title: string;
  menuItems: MenuItem[];
}

const LateralMenu: React.FC<LateralMenuProps> = ({ title, menuItems }) => {

  const navigate = useNavigate();

  const [activeMenu, setActiveMenu] = useState<number>(-1);

  const handleMenuItemClick = (index: number) => {
    setActiveMenu(index);
  };

  useEffect(() => {
        setActiveMenu(0);
        navigate(menuItems[0].link);
    }, []);
  

  return (
    <>
      <div className='flex w-full h-full'>
        <div className='fixed w-48 z-10 '>
          <ul className="menu rounded-none bg-base-200 w-full h-screen p-0">
            <li className="border-b-2 border-gray-800 ">
              <h3 className='menu-title text-gray-800 font-bold text-base my-4 mx-1'>{title}</h3>
            </li>

            {menuItems.map((item, index) => (
              <li
                key={index}
                onClick={() => handleMenuItemClick(index)}
                className={`text-gray-800 font-semibold border-b-2 border-gray-800 mx-2  ${activeMenu === index ? 'bg-gray-700 text-gray-850 hover:bg-gray-700' : ''}`}>
                {/* <a className='my-4 hover:bg-gray-900'> */}
                <NavLink to={item.link} className={`my-4 hover:bg-transparent`}>{item.label}</NavLink>
                {/* </a> */}
              </li>
            ))}

          </ul>
        </div>
        <div className='w-full h-full ml-48 bg-gray-50'>
          <Outlet />
        </div>
      </div>

    </>
  )
}

export default LateralMenu