import React, { useState, useEffect, useRef } from 'react';
import { useFilter } from '../context/FilterTicketContext';
import { useNavigate } from 'react-router-dom';
import { getTickets, Ticket, assignTicket, deleteTicket } from '../api/TicketService'
import { useAuth } from '../auth/AuthContext';
import ActionTicketModal from './modal/ActionTicketModal';

type FilterItem = {
    filter: string;
    value: string | boolean | Date;
};

type Props = {
    prefilter: FilterItem[] | null;
};

const ITEMS_PER_PAGE = 12;

const TableTickets = ({ prefilter }: Props) => {
    const navigate = useNavigate();
    const { token } = useAuth();
    const { isAdmin } = useAuth();

    const [currentPage, setCurrentPage] = useState(1);
    const [pageCount, setPageCount] = useState(1);
    const [itemsPerPage] = useState(ITEMS_PER_PAGE);

    const { filter, setFilter } = useFilter();
    const prefilterApplied = useRef(false);

    const [allTicketsCount, setAllTicketsCount] = useState(0);
    const [currentTickets, setCurrentTickets] = useState<Ticket[]>([]);
    const [selectedTicketId, setSelectedTicketId] = useState<string | null>(null);
    const selectedTicketIdRef = useRef<string | null>(null);

    const [modalOpen, setModalOpen] = useState({ show: false, message: { message1: '', message2: '' }, onConfirm: () => { } });
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (!loading) {
            fetchTickets();
        }
    }, [currentPage, filter]); // Remova 'loading' das dependências

    const fetchTickets = async () => {
        if (loading) return; // Evita múltiplas chamadas se já estiver carregando
        setLoading(true); // Inicia o carregamento

        try {
            if (prefilter && !prefilterApplied.current) {
                prefilter.forEach(item => {
                    setFilter(prev => ({ ...prev, [item.filter]: item.value }));
                });
                
                prefilterApplied.current = true;
                // Não faça mais nada neste ciclo de renderização.
                setLoading(false);
                return; // Importante para evitar aplicação imediata do estado
            }

            const allTickets = await getTickets(filter, token());
            setAllTicketsCount(allTickets.length);

            const searchLower = filter.search?.toLowerCase();
            const filteredTickets = allTickets.filter((ticket: Ticket) => {
                // Aplica filtro genérico
                return Object.keys(ticket).some(key => {
                    if (key === 'generator') {
                        // Ignora o campo 'generator'
                        return false;
                    }

                    const value = (ticket as any)[key];
                    if (typeof value === 'string') {
                        return value.toLowerCase().includes(searchLower);
                    } else if (value && typeof value === 'object' && !Array.isArray(value)) {
                        // Se for um objeto, verificar cada propriedade do objeto
                        return Object.values(value).some(innerValue =>
                            typeof innerValue === 'string' && innerValue.toLowerCase().includes(searchLower)
                        );
                    }
                    return false;
                });
            });

            const lastIndex = currentPage * ITEMS_PER_PAGE;
            const firstIndex = lastIndex - ITEMS_PER_PAGE;

            // Calcular o número de páginas
            setPageCount(Math.ceil(filteredTickets.length / itemsPerPage));
            setCurrentTickets(filteredTickets.slice(firstIndex, lastIndex));
        } catch (error) {
            console.error('Erro ao carregar tickets:', error);
        } finally {
            setLoading(false); // Termina o carregamento
        }
    };

    // Função para mudar a página
    const setPage = (page: number) => {
        if (page < 1 || page > pageCount) return;
        setCurrentPage(page);
    };

    useEffect(() => {
        selectedTicketIdRef.current = selectedTicketId;
    }, [selectedTicketId]);

    const assignSelectedTicket = async () => {
        const ticketId = selectedTicketIdRef.current; // Usa a ref em vez do estado
        if (ticketId) {
            try {
                await assignTicket(ticketId, token());
                setModalOpen({ show: false, message: { message1: '', message2: '' }, onConfirm: () => { } });
                fetchTickets(); // Refresh list after operation
                setSelectedTicketId(null); // Reseta o ID selecionado
            } catch (error) {
                console.error('Error assigning ticket:', error);
            }
        }
    };

    const removeSelectedTicket = async () => {
        const ticketId = selectedTicketIdRef.current; // Usa a ref em vez do estado
        if (ticketId) {
            try {
                await deleteTicket(ticketId, token());
                setModalOpen({ show: false, message: { message1: '', message2: '' }, onConfirm: () => { } });
                fetchTickets(); // Refresh list after operation
                setSelectedTicketId(null); // Reseta o ID selecionado
            } catch (error) {
                console.error('Error assigning ticket:', error);
            }
        }
    };

    const handleActionTicketModal = (event: React.ChangeEvent<HTMLSelectElement>, ticketId: string) => {
        event.stopPropagation();
        setSelectedTicketId(ticketId);

        let message = { message1: '', message2: '' };
        let onConfirm = () => { };

        const action = event.target.value;
        switch (action) {
            case 'assign':
                message.message1 = "Are you sure you want to sign?"
                onConfirm = assignSelectedTicket;
                break;
            case 'remove':
                message.message2 = "Are you sure you want to remove?"
                onConfirm = removeSelectedTicket;
                break;
            default:
                // Nenhuma ação
                break;
        }

        message.message2 = 'Ticket Id:'

        setModalOpen({ show: true, message: message, onConfirm: onConfirm });
    };

    const handleRowClick = (ticketId: string) => {
        navigate(`/tickets/${ticketId}`);
    };

    return (
        <>
            <div className='z-0 mx-6 overflow-auto flex items-start' style={{ maxHeight: '710px' }}>
                <div className='w-full'>
                    <div className='flex justify-between border-b-[3px] border-gray-500 items-baseline'>
                        <div className='text-sm text-gray-700'><span>{allTicketsCount} tickets</span></div>
                        <div></div>
                    </div>

                    <table className="table bg-gray-50" >
                        <thead className="sticky top-0 bg-gray-200 text-gray-900 border-b-[3px] border-gray-500">
                            <tr>
                                <th>ID</th>
                                <th>Requester</th>
                                <th>Title</th>
                                <th>Assignee</th>
                                <th>Department</th>
                                <th>Status</th>
                                <th>Opening Date</th>
                                <th>Closing Date</th>
                                {isAdmin() && <th></th>}
                            </tr>
                        </thead>
                        <tbody>
                            {/* navlink */}

                            {currentTickets.map(ticket => (
                                <tr key={ticket.id} className="hover" onClick={() => handleRowClick(ticket.id)}>
                                    <td><p className='max-w-40 truncate'>{ticket.id.slice(-4)}</p></td>
                                    <td><p className='max-w-40 truncate'>{ticket.client.email}</p></td>
                                    <td><p className='max-w-36 truncate'>{ticket.title}</p></td>
                                    <td><p className='max-w-40 truncate'>{ticket.support ? ticket.support.email : 'N/A'}</p></td>
                                    <td><p className='max-w-40 truncate'>{ticket.department}</p></td>
                                    <td>
                                        <p className={`border-2 rounded-2xl text-center py-0.5 
                                    ${ticket.status === 'OPEN' ? 'border-status-OPEN text-status-OPEN' :
                                                ticket.status === 'CONCLUDED' ? 'border-status-CONCLUDED text-status-CONCLUDED' :
                                                    ticket.status === 'PAUSED' ? 'border-status-PAUSED text-status-PAUSED' :
                                                        ticket.status === 'IN PROGRESS' ? 'border-status-IN_PROGRESS text-status-IN_PROGRESS' : ''}`}>
                                            {ticket.status}
                                        </p>
                                    </td>
                                    <td><p>{ticket.startDate || '-'}</p></td>
                                    <td>{ticket.endDate || '-'}</td>
                                    {isAdmin() && (
                                        <td className='w-16 text-blue-500  font-normal cursor-pointer  hover:bg-gray-200' onClick={(event) => event.stopPropagation()}>
                                            {/* Actions onClick={(event) => { handleActionClick(event, ticket.id) }}*/}
                                            <select
                                                value=""
                                                onChange={(event) => handleActionTicketModal(event, ticket.id)}
                                                className='text-blue-500 cursor-pointer bg-transparent outline-none'
                                            >
                                                <option value="" disabled>Actions</option>
                                                <option value="assign">To Sign</option>
                                                <option value="remove">Remove</option>
                                            </select>
                                        </td>
                                    )}

                                </tr>
                            ))}
                        </tbody>
                    </table>
                    {modalOpen.show && selectedTicketId && (
                        <ActionTicketModal
                            ticketId={selectedTicketId}
                            message={modalOpen.message}
                            onConfirm={modalOpen.onConfirm}
                            onClose={() => setModalOpen({ show: false, message: { message1: '', message2: '' }, onConfirm: () => { } })}
                        />
                    )}

                </div>

            </div>


            <div className="h-24 mx-6 flex justify-center items-center space-x-5 mb-5 text-sm">
                <button
                    onClick={() => setPage(currentPage - 1)}
                    disabled={currentPage === 1}
                    className="w-24 h-10 p-2 bg-blue-700 text-gray-50 rounded-3xl hover:bg-blue-700 disabled:opacity-50">
                    Previous
                </button>

                <span>Page {currentPage} of {pageCount}</span>

                <button
                    onClick={() => setPage(currentPage + 1)}
                    disabled={currentPage === pageCount}
                    className="w-24 h-10 p-2 bg-blue-700 text-gray-50 rounded-3xl hover:bg-blue-700  disabled:opacity-50">
                    Next
                </button>
            </div>
        </>
    );
};

export default TableTickets;