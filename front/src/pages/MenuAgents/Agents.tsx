import InternHeader from '../../components/InternHeader'
import TableUsers from '../../components/TableUsers'
import { FilterProvider } from '../../context/FilterUserContext'
import FilterUsers from '../../components/FilterUsers'

const UserAgents = () => {

  return (
    <>
      <InternHeader title='Agents'></InternHeader>
      <div className='w-full bg-gray-50' >
        <FilterProvider>
          <FilterUsers></FilterUsers>
          <div className='w-full mt-36 bg-gray-50'>
            <TableUsers prefilter={{ filter: 'role', value: 'admin' }}></TableUsers>
          </div>

        </FilterProvider>
      </div>
    </>
  )
}

export default UserAgents