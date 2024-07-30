
import { useAuth } from '../../auth/AuthContext'
import FilterTickets from '../../components/FilterTickets'
import InternHeader from '../../components/InternHeader'
import TableTickets from '../../components/TableTickets'
import { FilterProvider } from '../../context/FilterTicketContext'

const ConcludedTickets = () => {
  const { isAdmin } = useAuth();

  const prefilter = isAdmin() ? [{ filter: 'status', value: 'CONCLUDED' }] : [
    { filter: 'status', value: 'CONCLUDED' },
    { filter: 'all', value: false }
  ];

  return (
    <>
      <InternHeader title='All Tickets'></InternHeader>
      <div className='w-full bg-gray-50' >
        <FilterProvider>
          <FilterTickets prefilter={prefilter}></FilterTickets>
          <div className='w-full mt-36 bg-gray-50'>
            <TableTickets prefilter={prefilter}></TableTickets>
          </div>

        </FilterProvider>
      </div>
    </>
  )
}

export default ConcludedTickets