
type Props = {
    title: string
}

const InternHeader = ({title}: Props) => {
    return (
        <>
            <div className='fixed w-full z-10 h-16 bg-gray-50 text-lg text-left font-semibold text-gray-850 border-b-4 border-gray-700 px-5 flex items-center'>
                <h1>
                    {title}
                </h1>
            </div>
        </>
    )
}

export default InternHeader;
