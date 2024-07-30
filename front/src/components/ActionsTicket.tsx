import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form';
import { useAuth } from '../auth/AuthContext';
import { Ticket, getTicket, FormData, updateTicket, concludedTicket, assignTicket, deleteTicket } from '../api/TicketService';
import { LuClipboardSignature, LuClipboardCheck, LuDelete } from "react-icons/lu";
import AvatarProcedure from './AvatarProcedure';
import ActionTicketModal from './modal/ActionTicketModal';
import {useNavigate } from 'react-router-dom';

type Props = {
  ticketId: string | null;
}

const ActionsTicket = ({ ticketId }: Props) => {
  const navigate = useNavigate();
  const { token } = useAuth();
  const [ticket, setTicket] = useState<Ticket>()
  const [render, setRender] = useState(false)
  const { register, handleSubmit, reset } = useForm<FormData>();
  const [departmentOptions, setDepartmentOptions] = useState([
    'DEPARTMENT 1', 'DEPARTMENT 2', 'DEPARTMENT 3', 'DEPARTMENT 4', 'DEPARTMENT 5'
  ]);
  const [modalOpen, setModalOpen] = useState({ show: false, message: {message1:'', message2:''}, onConfirm: () => { } });

  useEffect(() => {
    const fetchTicket = async () => {
      try {
        if (ticketId) {
          const fetchedTicket = await getTicket(ticketId, token());
          setTicket(fetchedTicket);

          // Verifica se o departamento atual está nas opções existentes, se não, adiciona
          if (fetchedTicket?.department && !departmentOptions.includes(fetchedTicket.department)) {
            setDepartmentOptions(prev => [...prev, fetchedTicket.department]);
          }
          // Carrega os dados do ticket no formulário estado inicial
          reset({
            status: fetchedTicket?.status,
            department: fetchedTicket?.department
          });
        }
      } catch (error) {
        console.error('Failed to fetch ticket details:', error);
      }
    };

    fetchTicket();
    setRender(false);
  }, [ticketId, render]);

  const onSubmit = async (data: FormData) => {
    if (data.status === ticket?.status && data.department === ticket?.department) {
      return
    }
    try {
      await updateTicket(data, ticketId, token());
      setRender(true);
    } catch (error) {
      console.error('Failed to fetch ticket details:', error);
    }
  };

  const handleResetTicketInfo = () => {
    reset(ticket);
  };

  const concludedSelectedTicket = async () => {
    try {
      await concludedTicket(ticketId, token());
      setModalOpen({ show: false, message: {message1:'', message2:''}, onConfirm: () => { } });
      setRender(true);
    } catch (error) {
      console.error('Failed to fetch ticket details:', error);
    }
  };

  const assignSelectedTicket = async () => {
    try {
      await assignTicket(ticketId, token());
      setModalOpen({ show: false, message: {message1:'', message2:''}, onConfirm: () => { } });
      setRender(true);
    } catch (error) {
      console.error('Failed to fetch ticket details:', error);
    }
  };

  const removeSelectedTicket = async () => {
    try {
      await deleteTicket(ticketId, token());
      setModalOpen({ show: false, message: {message1:'', message2:''}, onConfirm: () => { } });
      navigate(-1)
    } catch (error) {
      console.error('Failed to fetch ticket details:', error);
    }
  };


  const handleActionTicketModal = (action: string) => {

    let message = { message1: '', message2: '' };
    let onConfirm = () => { };

    switch (action) {
      case 'assign':
        message.message1 = "Are you sure you want to sign?"
        onConfirm = assignSelectedTicket;
        break;
      case 'concluded':
        message.message1 = "Are you sure you want to mark as complete?"
        onConfirm = concludedSelectedTicket;
        break;
      case 'remove':
        message.message1 = "Are you sure you want to remove?"
        onConfirm = removeSelectedTicket;
        break;
      default:
        // Nenhuma ação
        break;
    }
    
    message.message2 = 'Ticket Id:'

    setModalOpen({ show: true, message: message, onConfirm: onConfirm });
  };

  return (
    <>
      {ticket ? (
        <>
          <div className='w-full flex justify-between py-4 bg-gray-400'>
            <form onSubmit={handleSubmit(onSubmit)} className='w-full flex justify-between'>

              <div className='w-full flex flex-col space-y-3 mr-4 bg-gray-50 p-5 rounded-xl shadow'>
                <div className='border-b-[2px] border-gray-700 py-1 mb-1'>
                  <h3 className='text-base font-semibold text-gray-800'>Update ticket information</h3>
                </div>
                <div className="w-72 label flex flex-col items-start space-y-2">
                  <label className="label-text">Set Status</label>
                  <select {...register('status')} className="w-full select select-bordered">
                    <option value="OPEN">OPEN</option>
                    <option value="IN PROGRESS">IN PROGRESS</option>
                    <option value="PAUSED">PAUSED</option>
                    <option value="CONCLUDED" className='hidden'>CONCLUDED</option>
                  </select>
                </div>
                <div className="w-72 label flex flex-col items-start space-y-2">
                  <label className="label-text">Set Department</label>
                  <select {...register('department')} className="w-full select select-bordered">
                    {departmentOptions.map((department, index) => (
                      <option key={index} value={department}>{department}</option>
                    ))}
                  </select>
                </div>
                <div className='w-full flex flex-wrap space-x-5 my-5 items-baseline justify-end  md:space-y-2 '>
                  <button
                    type='button'
                    onClick={handleResetTicketInfo}
                    className='btn btn-default rounded-3xl text-gray-850 w-full 2xl:w-28'>
                    Discard
                  </button>
                  <button
                    type="submit"
                    className='btn btn-700 rounded-3xl text-gray-50 w-full 2xl:w-28'>
                    Update
                  </button>
                </div>
              </div>
            </form>

            <div className='w-3/12 flex flex-col space-y-3 bg-gray-50 p-5 rounded-xl shadow'>
              <div className='border-b-[2px] border-gray-700 py-1'>
                <h3 className='text-base font-semibold text-gray-800'>Advanced Options</h3>
              </div>

              <button
                onClick={() => handleActionTicketModal('assign')}
                className='flex space-x-2 items-center text-sm font-normal  text-gray-50 rounded-lg p-2 bg-blue-600'>
                <LuClipboardSignature size={18} />
                <span>To sign</span>
              </button>
              <button
                onClick={() => handleActionTicketModal('concluded')}
                className='flex space-x-2 items-center text-sm font-normal  text-gray-50 rounded-lg p-2 bg-blue-700'>
                <LuClipboardCheck size={18} />
                <span>Concluded</span>
              </button>
              <button
                onClick={() => handleActionTicketModal('remove')}
                className='flex space-x-2 items-center text-sm font-normal  text-gray-50 rounded-lg p-2 bg-gray-800'>
                <LuDelete size={18} style={{ transform: "rotate(180deg)" }} />
                <span>Remove</span>
              </button>
            </div>

            {modalOpen.show && ticketId && (
              <ActionTicketModal
                ticketId={ticketId}
                message={modalOpen.message}
                onConfirm={modalOpen.onConfirm}
                onClose={() => setModalOpen({ show: false, message: {message1:'', message2:''}, onConfirm: () => { } })}
              />
            )}

          </div>


          {ticket.support && (
            <div className='w-full flex flex-col space-y-3 mr-4 bg-gray-50 p-5 rounded-xl shadow'>
              <div className='border-b-[2px] border-gray-700 py-1 mb-1'>
                <h3 className='text-base font-semibold text-gray-800'>Responsibility</h3>
              </div>
              <div className='flex space-x-4 items-center '>
                <div>
                  <AvatarProcedure name={ticket.support?.name} />
                </div>
                <div className='flex flex-col space-y-0 '>
                  <h3 className='text-base'>{ticket.support?.name}</h3>
                  <span className='text-sm'>{ticket.support?.email}</span>
                </div>
              </div>
            </div>
          )}


        </>


      )
        :
        (<p>Loading ticket details...</p>)}

    </>
  )
}

export default ActionsTicket