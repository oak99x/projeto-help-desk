
import NewTicketForm from '../../components/NewTicketForm'
import InternHeader from '../../components/InternHeader'

// import { FilterProvider } from '../../context/FilterTicketContext'

const NewTicket = () => {
  return (
    <>

      <InternHeader title='Create new Ticket'></InternHeader>
      <div className='w-full pt-20 bg-gray-400 min-h-screen' >
          <NewTicketForm></NewTicketForm>     
      </div>


    </>
  )
}

export default NewTicket