import React from 'react'
import { IoClose } from "react-icons/io5";

interface Props {
    ticketId: string;
    message:{message1:string,message2:string};
    onConfirm: () => void;
    onClose: () => void;
}

const ActionTicketModal: React.FC<Props> = ({ ticketId, message, onConfirm, onClose }) => {
    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-gray-600 bg-opacity-50 backdrop-blur-sm">
            <div className="relative p-4 rounded-lg bg-gray-50 shadow-lg max-w-md w-full">
                <h3 className="font-bold text-lg">{message.message1}</h3>
                <p className="my-10">{message.message2} <span className='font-bold text-lg'>{ticketId} </span></p>
                <button className="btn-700 w-full text-sm font-semibold leading-6 text-gray-300 p-2 rounded-3xl" onClick={onConfirm}>
                    Confirm
                </button>
                <IoClose color='#7a7a7a' size={28} onClick={onClose} className='absolute top-0  right-0 m-2' />
            </div>
        </div>
    );
};

export default ActionTicketModal;