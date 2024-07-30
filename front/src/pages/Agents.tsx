
import LateralMenu from '../components/LateralMenu'

const Tickets = () => {

    const menuItems = [
        { label: 'All Users', link: '/agents/all-users' },
        { label: 'Clients', link: '/agents/clients' },
        { label: 'Agents', link: '/agents/agents' }
    ];

    return (
        <>
        <div className='w-full h-full'>
            <LateralMenu title='Agents' menuItems={menuItems}></LateralMenu>
        </div>
        </>
    )
}

export default Tickets