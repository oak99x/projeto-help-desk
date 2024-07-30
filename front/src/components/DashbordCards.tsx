
import { SiHelpdesk } from "react-icons/si";

type Props = {
    title: string;
    onclick: () => void;
}

const DashbordCards = ({title, onclick }: Props) => {
    return (
        <>
            <div
                onClick={onclick}
                className="md:w-40  md:h-36 flex bg-gray-200 items-center card card-compact shadow-xl">
                <SiHelpdesk className='mt-5' size={40}></SiHelpdesk>
                <div className="card-body text-center flex justify-center items-center text-gray-800 font-bold text-sm">
                    <h2>{title}</h2>
                </div>
            </div>


        </>
    )
}

export default DashbordCards