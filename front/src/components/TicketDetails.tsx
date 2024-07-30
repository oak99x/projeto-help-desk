import { useEffect, useState } from 'react'
import { useAuth } from '../auth/AuthContext';
import { Ticket, getTicket } from '../api/TicketService';

type Props = {
    ticketId: string | null;
}

const TicketDetails = ({ ticketId }: Props) => {
    const { token } = useAuth();
    const [ticket, setTicket] = useState<Ticket>()

    useEffect(() => {
        fetchTicket();
    }, [ticketId]);

    const fetchTicket = async () => {
        try {
            if (ticketId) {
                const fetchedTicket = await getTicket(ticketId, token());
                setTicket(fetchedTicket);
            }
        } catch (error) {
            console.error('Failed to fetch ticket details:', error);
        }
    };

    return (
        <>
            {ticket ? (
                <div className='w-full my-4 p-5 rounded-xl shadow bg-gray-50'>

                    <div className='border-b-[3px] border-gray-700 py-1'>
                        <h3 className='text-xl font-semibold'>#{ticket.id.slice(-4)} - {ticket.title}</h3>
                    </div>

                    <div className='space-y-3 my-5'>
                        <div className='flex justify-between'>
                            <div className='space-y-3'>
                                <p><strong>Generator:</strong> {ticket.generator?.name}</p>
                                <p><strong>Requester:</strong> {ticket.client?.name}</p>
                                <p><strong>Responsible:</strong> {ticket.support?.name || 'N/A'}</p>
                                <p><strong>Department:</strong> {ticket.department}</p>
                            </div>
                            <div className='space-y-3'>
                                <p className='space-x-2'>
                                    <strong>Status:</strong>
                                    <span className={`font-semibold py-0.5 ${ticket.status === 'OPEN' ? 'text-status-OPEN' :
                                        ticket.status === 'CONCLUDED' ? 'text-status-CONCLUDED' :
                                            ticket.status === 'PAUSED' ? 'text-status-PAUSED' :
                                                ticket.status === 'IN PROGRESS' ? 'text-status-IN_PROGRESS' : ''}`}>
                                        {ticket.status}
                                    </span>
                                </p>
                                <p><strong>Opening Date:</strong> {ticket.startDate}</p>
                                <p><strong>Closing Date:</strong> {ticket.endDate || '-'}</p>
                            </div>
                        </div>

                        <div>
                            <p><strong>Description:</strong></p>
                            <p className='whitespace-pre-line border-2 border-gray-600  rounded-xl p-4'>{ticket.description}</p>
                        </div>
                    </div>
                </div>
            )
                :
                (<p>Loading ticket details...</p>)
            }

        </>
    )
}

export default TicketDetails