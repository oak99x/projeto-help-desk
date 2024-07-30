import RichTextEditor from './RichTextEditor';
import AvatarProcedure from './AvatarProcedure';
import { useAuth } from '../auth/AuthContext';
import { useState } from 'react'
import { addProcedure } from '../api/TicketService'

type Props ={
    ticketId: string | null;
    onClose: ()=>void;
}

const NewProcedureForm = ({ticketId, onClose}:Props) => {

    const [procedure, setProcedure] = useState('');
    const { token, user } = useAuth();

    const handleSendClick = async () => {
        if (!procedure || procedure === "<p><br></p>") {
            return; 
        }
        try {
            await addProcedure(ticketId, procedure, token());
            setProcedure('');
        } catch (error) {
            console.error('Failed to send procedure:', error);
            alert('Failed to send procedure. Please try again.');
        }
    };

    const handleClosedClick = () => {
        setProcedure('');
        onClose();
    }

    return (
        <>
            <div className='w-full bg-gray-50 p-2 rounded-xl'>
                <div className='flex space-x-4 items-center border-b-2 border-gray-700 pb-2'>
                    <div>
                        <AvatarProcedure name={user?.name} />
                    </div>
                    <div className='flex flex-col space-y-0 '>
                        <h3 className='text-base'>{user?.name}</h3>
                        <span className='text-sm'>{user?.email}</span>
                    </div>
                </div>

                <div className="w-full label flex flex-col items-start space-y-2">
                    <RichTextEditor value={procedure} onChange={setProcedure}></RichTextEditor>
                </div>

                <div className="flex items-center justify-end gap-x-4">
                    <button type="button" onClick={handleClosedClick} className="btn-default w-28 text-sm font-semibold leading-6 text-gray-300 p-2 rounded-3xl">
                        Cancel
                    </button>
                    <button
                        type="button"
                        onClick={handleSendClick}
                        className="btn-700 w-28 text-sm font-semibold leading-6 text-gray-300 p-2 rounded-3xl"
                    >
                        Send
                    </button>
                </div>
            </div>
        </>
    )
}

export default NewProcedureForm