
import { Procedure } from '../api/TicketService'
import DOMPurify from 'dompurify';
import AvatarProcedure from './AvatarProcedure';

const HtmlContent = ({ html }: any) => {
    const cleanHtml = DOMPurify.sanitize(html);
    return (
        <div
            className="w-full label flex flex-col items-start space-y-2 whitespace-pre-wrap"
            dangerouslySetInnerHTML={{ __html: cleanHtml }}
        />
    );
};

type Props = {
    procedure: Procedure
}

const ProcedureCard = ({ procedure }: Props) => {
    return (
        <>
            <div className='flex flex-col my-6'>
                <div className='w-full bg-gray-50 p-4 rounded-xl'>
                    <div className='border-b-2 border-gray-700 pb-2'>
                        <div className='w-full flex justify-between'>
                            <div className='flex space-x-4 items-center '>
                                <div>
                                    <AvatarProcedure name={procedure.sender.name} />
                                </div>
                                <div className='flex flex-col space-y-0 '>
                                    <h3 className='text-base'>{procedure.sender.name}</h3>
                                    <span className='text-sm'>{procedure.sender.email}</span>
                                </div>
                            </div>
                            <div className='flex flex-col text-sm'>
                                <span>{procedure.date}</span>
                                <span>{procedure.time}</span>
                            </div>
                        </div>
                    </div>

                    <div>
                        <div>
                            <HtmlContent html={procedure.content} />
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default ProcedureCard