import LateralMenu from '../components/LateralMenu'

const Manage = () => {

    const menuItems = [
        { label: 'My account', link: '/manage/#' },
        { label: 'Notifications', link: '/manage/#' },
    ];

    return (
        <>
        <div className='w-full h-full'>
            <LateralMenu title='Manage' menuItems={menuItems}></LateralMenu>
        </div>
        </>
    )
}

export default Manage