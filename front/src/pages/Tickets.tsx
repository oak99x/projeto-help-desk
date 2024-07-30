import { useAuth } from '../auth/AuthContext';
import LateralMenu from '../components/LateralMenu'

const Tickets = () => {
    const { isAdmin } = useAuth();

    const menuItems = [
        ...(isAdmin() ? [{ label: 'All Tickets', link: '/tickets/all-tickets' }] : []),
        { label: 'My Tickets', link: '/tickets/my-tickets' },
        { label: 'Open', link: '/tickets/open' },
        { label: 'Concluded', link: '/tickets/concluded' },
        { label: 'New Ticket', link: '/tickets/new-ticket' }
    ];

    return (
        <>
            <div className='w-full h-full'>
                <LateralMenu title='Tickets' menuItems={menuItems}></LateralMenu>
            </div>
        </>
    )
}

export default Tickets