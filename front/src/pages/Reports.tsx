
import LateralMenu from '../components/LateralMenu'

const Reports = () => {

    const menuItems = [
        { label: 'Last 7 days', link: '/reports/#' },
        { label: 'New tickets', link: '/reports/#' },
        { label: 'Resolution time', link: '/reports/#' },
        { label: 'Ticket satisfaction', link: '/reports/#' }
    ];

    return (
        <>
        <div className='w-full h-full'>
            <LateralMenu title='Reports' menuItems={menuItems}></LateralMenu>
        </div>
        </>
    )
}

export default Reports