import { useEffect, useState } from 'react'
import { Procedure, getProcedures } from '../api/TicketService';
import { useAuth } from '../auth/AuthContext';
import ProcedureCard from './ProcedureCard';

type Props = {
    ticketId: string | null;
    activeNewProcedure: boolean;
}

const ProceduresList = ({ ticketId, activeNewProcedure}: Props) => {
    const [procedures, setProcedures] = useState<Procedure[]>([])
    const { token } = useAuth();

    useEffect(() => {
        const fetchProcedures = async () => {
            try {
                if (ticketId) {
                    const fetchProcedures = await getProcedures(ticketId, token());
                    setProcedures(fetchProcedures);
                }
            } catch (error) {
                console.error('Failed to fetch ticket details:', error);
            }
        };
        fetchProcedures();
    }, [procedures]);



    return (
        <>
            {procedures.length ?
                (procedures.map(procedure => (<ProcedureCard key={procedure.dateTime} procedure={procedure} />)))
                :
                (
                    !activeNewProcedure && (
                        <div className='flex justify-center text-base font-semibold text-gray-700'>
                            <p>Nothing to show</p>
                        </div>
                    )
                   
                )

            }
        </>
    )
}

export default ProceduresList