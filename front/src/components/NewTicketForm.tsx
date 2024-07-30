import { useForm, SubmitHandler } from "react-hook-form";
import ComboBoxNewTicket from "./ComboBoxNewTicket";
import { createTicket, FormData } from '../api/TicketService'
import { useAuth } from '../auth/AuthContext';
import { useState } from 'react'
import CreateTicketModal from './modal/CreateTicketModal'; // Default import for the component
import { ModalShow } from './modal/CreateTicketModal'; // Named import for the interface



// Definição do tipo dos dados do formulário

const NewTicketForm = () => {
    const { isAdmin } = useAuth();
    const { register, handleSubmit, setValue, formState: { errors }, reset } = useForm<FormData>();
    const { token } = useAuth();
    const [modalShow, setModalShow] = useState<ModalShow>({ show: false, message: 'Ticket created successfully!', success: false });

    // Atualiza o valor do email no formulário
    const handleEmailChange = (newEmail: string) => {
        console.log(newEmail)
        setValue('email', newEmail);
    };

    const onSubmit: SubmitHandler<FormData> = async (data) => {
        try {
            await createTicket(data, token()); // Chamada da API para criar o ticket
            reset(); // Reseta os valores do formulário
            setModalShow({ show: true, message: 'Ticket created successfully!', success: true })
        } catch (error) {
            setModalShow({ show: true, message: 'Failed to create ticket!', success: false })
            console.error("Failed to create ticket:", error);
        }
    };

    return (
        <>
            <div className='w-full mt-5 flex flex-col justify-center items-center'>
                <div className="w-4/6 h-full mb-10 bg-gray-50 rounded-2xl">
                    <div className='text-left font-semibold text-gray-850 border-b-4 border-gray-700 m-6'>
                        <h2 className='my-1'>Fill in the fields</h2>
                    </div>
                    <form className="h-full m-6" onSubmit={handleSubmit(onSubmit)} method="POST">
                        <div className='space-y-1'>
                            <div className="w-full label flex flex-col items-start space-y-2">
                                <span className="label-text font-medium text-sm w-full">Title</span>
                                <input
                                    type="text"
                                    id='title'
                                    {...register("title", {
                                        required: "Title is required",
                                        maxLength: {
                                            value: 100,
                                            message: "Title can have a maximum of 100 characters"  // Mensagem exibida se o limite for excedido
                                        }
                                    })}
                                    placeholder="Title"
                                    className="w-full h-10 text-sm input input-bordered input-focus-outline-none"
                                />
                                {errors.title && <span className="text-error text-xs">{errors.title.message}</span>}
                            </div>

                            <div className="w-full label flex flex-col items-start space-y-2">
                                <span className="label-text font-medium text-sm w-full">Description</span>
                                <textarea
                                    id='description'
                                    {...register("description", {
                                        required: "Description is required",
                                        maxLength: {
                                            value: 1000,
                                            message: "Title can have a maximum of 1000 characters"  // Mensagem exibida se o limite for excedido
                                        }
                                    })}
                                    rows={3}
                                    placeholder="Description"
                                    className="textarea text-sm textarea-bordered w-full h-28"
                                ></textarea>
                                {errors.description && <span className="text-error text-xs">{errors.description.message}</span>}
                            </div>

                            <div className='w-full flex flex-wrap justify-between items-start'>
                                <div className="w-72 label flex flex-col items-start space-y-2">
                                    <span className="label-text">Department</span>
                                    <select
                                        {...register("department", { required: "Please select a department" })}
                                        className="w-full select select-bordered">
                                        <option value=""></option>
                                        <option value="Department 1">Department 1</option>
                                        <option value="Department 2">Department 2</option>
                                        <option value="Department 3">Department 3</option>
                                        <option value="Department 4">Department 4</option>
                                        <option value="Department 5">Department 5</option>
                                    </select>
                                    {errors.department && <span className="text-error text-xs">{errors.department.message}</span>}
                                </div>

                                {isAdmin() && (
                                    <div className="w-96 label flex flex-col items-start space-y-2">
                                        <span className="label-text">Client E-mail</span>
                                        <div className="w-full h-12">
                                            <ComboBoxNewTicket sendEmail={handleEmailChange}></ComboBoxNewTicket>
                                        </div>
                                    </div>
                                )}

                            </div>
                        </div>

                        <div className="mt-7 flex items-center justify-end gap-x-4">
                            <button type="button" className="btn-default h-12 w-28 text-sm font-semibold leading-6 text-gray-850 p-2 rounded-3xl">
                                Cancel
                            </button>
                            <button
                                type="submit"
                                className="btn w-28 text-sm font-semibold leading-6 text-gray-300 p-2 rounded-3xl"
                            >
                                Send
                            </button>
                        </div>
                    </form>
                </div>
            </div>


            <CreateTicketModal
                modalShow={modalShow}
                onClose={() => setModalShow({ show: false, message: '', success: false })}
            />

        </>
    );
};

export default NewTicketForm;