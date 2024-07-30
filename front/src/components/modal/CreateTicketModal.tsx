import React, { useEffect } from 'react';
import { Player } from '@lottiefiles/react-lottie-player';
import { IoClose } from "react-icons/io5";

export interface ModalShow {
    show: boolean;
    message: string;
    success: boolean;
}

interface Props {
    modalShow: ModalShow;
    onClose: () => void;
}

const CreateTicketModal: React.FC<Props> = ({ modalShow, onClose }) => {
    // Set up the auto-close functionality
    useEffect(() => {
        if (modalShow.show) {
            const timer = setTimeout(() => {
                onClose();  // Call the onClose function after 5 seconds
            }, 4000);

            // Clean up the timer when the component unmounts or modalShow changes
            return () => clearTimeout(timer);
        }
    }, [modalShow, onClose]);

    return (
        <dialog open={modalShow.show} className="modal" onClick={onClose}>
            <div className="modal-box relative">
                <div className='flex flex-col justify-center items-center m-0 p-0'>
                    {modalShow.success ?
                        (<Player
                            autoplay={true}
                            loop={false}
                            keepLastFrame={true}
                            src="https://lottie.host/b4fc3d5d-39d5-4fb0-a4d0-f2d5a27231dc/a9Pfw4KD3x.json"
                            style={{ width: '100px' }}
                            speed={1}
                        />)
                        :
                        (<Player
                            autoplay={true}
                            loop={false}
                            keepLastFrame={true}
                            src="https://lottie.host/70aa1d15-40e2-486d-a8db-48423ab555d0/rbW3r4FXiz.json"
                            style={{ width: '100px' }}
                            speed={2}
                        />)
                    }

                    <h3 className="font-bold text-sm">{modalShow.message}</h3>
                </div>
                <IoClose color='#7a7a7a' size={28} onClick={onClose} className='absolute top-0 right-0 m-2'/>
            </div>
        </dialog>
    );
};

export default CreateTicketModal;
