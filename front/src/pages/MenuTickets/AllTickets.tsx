import InternHeader from '../../components/InternHeader'
import TableTickets from '../../components/TableTickets'
import { FilterProvider } from '../../context/FilterTicketContext'
import FilterTickets from '../../components/FilterTickets'

const AllTickets = () => {

  return (
    <>
    <InternHeader title='All Tickets'></InternHeader>
      <div className='w-full bg-gray-50' >
        <FilterProvider>
          <FilterTickets prefilter={null}></FilterTickets>
          <div className='w-full mt-36 bg-gray-50'>
            <TableTickets prefilter={null}></TableTickets> 
          </div>
        </FilterProvider>
      </div>
    </>
  )
}

export default AllTickets