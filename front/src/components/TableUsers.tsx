import React, { useState, useEffect, useRef } from 'react';
import { useFilter } from '../context/FilterUserContext';
// import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import ActionTicketModal from './modal/ActionTicketModal';
import { getUsersByFilter, promoteToAdmin, removeUser, removeRoleAdmin, User } from '../api/UserService';
import AvatarProcedure from './AvatarProcedure';

type Props = {
    prefilter: { filter: string; value: string } | null;
}

const ITEMS_PER_PAGE = 12;

const TableUsers = ({ prefilter }: Props) => {
    // const navigate = useNavigate();
    const { token } = useAuth();

    const [currentPage, setCurrentPage] = useState(1);
    const [pageCount, setPageCount] = useState(1);
    const [itemsPerPage] = useState(ITEMS_PER_PAGE);

    const { filter, setFilter } = useFilter();
    const prefilterApplied = useRef(false);

    const [allUsersCount, setAllUsersCount] = useState(0);
    const [currentUsers, setCurrentUsers] = useState<User[]>([]);
    const [selectedUserEmail, setSelectedUserEmail] = useState<string | null>(null);
    const selectedUserEmailRef = useRef<string | null>(null);

    const [modalOpen, setModalOpen] = useState({ show: false, message: { message1: '', message2: '' }, onConfirm: () => { } });

    useEffect(() => {
        fetchTickets();
    }, [currentPage, filter]);

    const fetchTickets = async () => {

        try {
            if (prefilter && !prefilterApplied.current) {
                setFilter(prev => ({ ...prev, [prefilter.filter]: prefilter.value }));
                prefilterApplied.current = true;
                return; // Importante para evitar aplicação imediata do estado
            }

            console.log(filter)
            const allUsers = await getUsersByFilter(filter, token());
            setAllUsersCount(allUsers.length);

            const searchLower = filter.search?.toLowerCase();
            const filteredUsers = allUsers.filter((user: User) => {
                // Aplica filtro genérico
                return Object.keys(user).some(key => {
                    const value = (user as any)[key];
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
            setPageCount(Math.ceil(filteredUsers.length / itemsPerPage));
            setCurrentUsers(filteredUsers.slice(firstIndex, lastIndex));
        } catch (error) {
            console.error('Erro ao carregar users:', error);
        }
    };

    // Função para mudar a página
    const setPage = (page: number) => {
        if (page < 1 || page > pageCount) return;
        setCurrentPage(page);
    };

    useEffect(() => {
        selectedUserEmailRef.current = selectedUserEmail;
    }, [selectedUserEmail]);

    const resetStateConstants = () => {
        setModalOpen({ show: false, message: { message1: '', message2: '' }, onConfirm: () => { } });
        fetchTickets(); // Refresh list after operation
        setSelectedUserEmail(null); // Reseta o ID selecionado
    }

    const addAdminToUser = async () => {
        const userEmail = selectedUserEmailRef.current; // Usa a ref em vez do estado
        try {
            await promoteToAdmin(userEmail, token());
            resetStateConstants();
        } catch (error) {
            console.error('Error adding admin to user:', error? error : '');
        }
    };

    const removeRoleAdminToUSer = async () => {
        const userEmail = selectedUserEmailRef.current; // Usa a ref em vez do estado
        try {
            await removeRoleAdmin(userEmail, token());
            resetStateConstants();
        } catch (error) {
            console.error('Error remove role user:', error);
        }
    };

    const removeSelectedUser = async () => {
        const userEmail = selectedUserEmailRef.current; // Usa a ref em vez do estado
        try {
            await removeUser(userEmail, token());
            resetStateConstants();
        } catch (error) {
            console.error('Error remove user:', error);
        }
    };

    const handleActionTicketModal = (event: React.ChangeEvent<HTMLSelectElement>, userEmail: string) => {
        event.stopPropagation();
        setSelectedUserEmail(userEmail);

        let message = { message1: '', message2: '' };

        let onConfirm = () => { };

        const action = event.target.value;
        switch (action) {
            case 'add_admin':
                message.message1 = "Are you sure you want to sign?"
                onConfirm = addAdminToUser;
                break;
            case 'remove_admin':
                message.message1 = "Are you sure you want to remove role?"
                onConfirm = removeRoleAdminToUSer;
                break;
            case 'remove':
                message.message1 = "Are you sure you want to remove?"
                onConfirm = removeSelectedUser;
                break;
            default:
                // Nenhuma ação
                break;
        }

        message.message2 = 'User E-mail:'

        setModalOpen({ show: true, message: message, onConfirm: onConfirm });
    };

    // const handleRowClick = (ticketId: string) => {
    //     // navigate(`/tickets/${ticketId}`);
    // };

    return (
        <>
            <div className='z-0 mx-6 overflow-auto flex items-start' style={{ maxHeight: '710px' }}>
                <div className='w-full'>
                    <div className='flex justify-between border-b-[3px] border-gray-500 items-baseline'>
                        <div className='text-sm text-gray-700'><span>{allUsersCount} users</span></div>
                        <div></div>
                    </div>
                    <table className="table bg-gray-50">
                        <thead className="sticky top-0 text-gray-900 border-b-[3px] border-gray-500 bg-gray-200">
                            <tr>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Area</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {/* navlink */}

                            {currentUsers.map(user => (
                                <tr key={user.email} className="hover" onClick={() => {}}>
                                    <td className='w-2/6'>
                                        <div className='flex space-x-4 items-center'>
                                            <AvatarProcedure name={user.name}></AvatarProcedure>
                                            <p className='max-w-64 truncate'>{user.name}</p>
                                        </div>
                                    </td>
                                    <td className='w-2/6'><p className='max-w-64 truncate'>{user.email}</p></td>
                                    <td className='w-2/6'><p className='max-w-64 truncate'>{user.area ? user.area : 'N/A'}</p></td>
                                    <td className='w-16 text-blue-500  font-normal cursor-pointer  hover:bg-gray-200' onClick={(event) => event.stopPropagation()}>
                                        {/* Actions onClick={(event) => { handleActionClick(event, ticket.id) }}*/}
                                        <select
                                            value=""
                                            onChange={(event) => handleActionTicketModal(event, user.email)}
                                            className='text-blue-500 cursor-pointer bg-transparent outline-none'
                                        >
                                            <option value="" disabled>Actions</option>
                                            <option value="add_admin">Add as Admin</option>
                                            <option value="remove_admin">Remove admin</option>
                                            <option value="remove">Remove</option>
                                        </select>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    {modalOpen.show && selectedUserEmail && (
                        <ActionTicketModal
                            ticketId={selectedUserEmail}
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

export default TableUsers;