import InternHeader from '../../components/InternHeader'
import TableUsers from '../../components/TableUsers'
import { FilterProvider } from '../../context/FilterUserContext'
import FilterUsers from '../../components/FilterUsers'

const AllTickets = () => {

  return (
    <>
      <InternHeader title='All Users'></InternHeader>
      <div className='w-full bg-gray-50' >
        <FilterProvider>
          <FilterUsers></FilterUsers>
          <div className='w-full mt-36 bg-gray-50'>
            <TableUsers prefilter={null}></TableUsers>
          </div>

        </FilterProvider>
      </div>
    </>
  )
}

export default AllTickets