
import FilterTickets from '../../components/FilterTickets'
import InternHeader from '../../components/InternHeader'
import TableTickets from '../../components/TableTickets'
import { FilterProvider } from '../../context/FilterTicketContext'

const MyTickets = () => {
    return (
        <>
        <InternHeader title='All Tickets'></InternHeader>
          <div className='w-full bg-gray-50' >
            <FilterProvider>
              <FilterTickets prefilter={[{filter:"all", value:false}]}></FilterTickets>
              <div className='w-full mt-36 bg-gray-50'>
                <TableTickets prefilter={[{filter:"all", value:false}]}></TableTickets> 
              </div>
              
            </FilterProvider>
          </div>
        </>
      )
}

export default MyTickets