import { useState } from 'react'
import InternHeader from '../../../components/InternHeader'
import { useParams } from 'react-router-dom';
import NewProcedureForm from '../../../components/NewProcedureForm';
import TicketDetails from '../../../components/TicketDetails';
import ProceduresList from '../../../components/ProceduresList';
import ActionsTicket from '../../../components/ActionsTicket';
import { useAuth } from '../../../auth/AuthContext';

const Ticket = () => {
    const { isAdmin } = useAuth();
    const { id } = useParams();
    const [activeMenu, setActiveAction] = useState('ticket')
    const [activeNewProcedure, setActiveNewProcedure] = useState(false)

    return (
        <>
            {/* <div className='absolute w-5/6 h-full bg-gray-400 z-0'></div> */}
            <InternHeader title={`Ticket #${id ? id.slice(-4) : id}`}></InternHeader>
            <div className='relative z-100 w-full flex justify-center pt-20 pb-10 bg-gray-400 min-h-screen'>

                <div className='w-5/6'>
                    <div className='border-b-[3px] border-gray-700 py-1'>
                        <button
                            onClick={() => { setActiveAction('ticket') }}
                            className={`w-20 text-sm font-semibold  text-gray-850 p-2 rounded-md ${activeMenu === 'ticket' ? 'bg-gray-600 ' : ''}`}>
                            Ticket
                        </button>
                        {isAdmin() && (
                                <button
                                    onClick={() => { setActiveAction('actions') }}
                                    className={`w-20 text-sm font-semibold  text-gray-850 p-2 rounded-md ${activeMenu === 'actions' ? 'bg-gray-600 ' : ''}`}>
                                    Actions
                                </button>
                            )
                        }
                    </div>

                    {activeMenu === 'ticket' && (
                        <>
                            <TicketDetails ticketId={id ? id : null} />

                            <div className='w-full my-4'>
                                <div className='w-full my-4 border-b-[3px] border-gray-700 py-1 flex justify-between items-baseline'>
                                    <h3 className='text-xl font-semibold'>Procedures</h3>
                                    <button
                                        onClick={() => { setActiveNewProcedure(!activeNewProcedure) }}
                                        className='text-sm font-normal  text-gray-50 border-2 border-blue-700 rounded-xl p-2 bg-blue-700'>
                                        New Procedure
                                    </button>
                                </div>

                                {activeNewProcedure && <NewProcedureForm ticketId={id ? id : null} onClose={() => { setActiveNewProcedure(false) }} />}

                                <ProceduresList ticketId={id ? id : null} activeNewProcedure={activeNewProcedure} />
                            </div>
                        </>

                    )}


                    {activeMenu === 'actions' && (
                        <ActionsTicket ticketId={id ? id : null} />
                    )}

                </div>
            </div>
        </>
    )
}

export default Ticket