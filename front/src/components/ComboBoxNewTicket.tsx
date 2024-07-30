import { useState, useEffect } from 'react'
import { Combobox } from '@headlessui/react'
import { HiMiniChevronUpDown } from "react-icons/hi2";
import { LuDelete } from "react-icons/lu";
import { useAuth } from '../auth/AuthContext';
import {User, allUsers} from '../api/UserService'

type Props = {
  sendEmail: (email: string) => void;
};

function ComboBoxNewTicket({ sendEmail }: Props) {

  const [users, setUsers] = useState<User[]>([]);
  const [selected, setSelected] = useState<User | null>(null);
  const [query, setQuery] = useState('')
  const {token} = useAuth();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const fetchedUsers = await allUsers(token());
        setUsers(fetchedUsers);
      } catch (error) {
        console.error("Failed to fetch users:", error);
      }
    };

    fetchUsers();
  }, []);

  const filteredPeople =
    query === ''
      ? users
      : users.filter((person) =>
        person.email
          .toLowerCase()
          .replace(/\s+/g, '')
          .includes(query.toLowerCase().replace(/\s+/g, ''))
      )

  const handleSelect = (person: User | null) => {
    if (person == null) {
      setSelected(null);
      sendEmail('')
    } else {
      setSelected(person);
      sendEmail(person.email)
    }
  };

  return (
    <div className="w-full h-full">
      <Combobox value={selected} onChange={handleSelect}>
        <div className="relative h-full">
          <div className="relative w-full h-full cursor-default overflow-hidden border border-gray-600 rounded-lg bg-white text-left focus:outline-none focus-visible:ring-2 focus-visible:ring-white/75 focus-visible:ring-offset-2 focus-visible:ring-offset-teal-300 sm:text-sm">
            <Combobox.Input
              placeholder='Digite o email'
              className="w-full h-full py-2 pl-3 pr-10 text-sm leading-5 text-gray-900 focus:ring-0  focus:outline-none"
              displayValue={(person: User) => person ? person.email : ''}
              onChange={(event) => setQuery(event.target.value)}
            />

            <Combobox.Button onClick={() => { handleSelect(null) }} className="absolute inset-y-0 right-0 flex items-center pr-2">
              {selected === null ? (
                <HiMiniChevronUpDown
                  className="h-5 w-5 text-gray-800"
                  size={24}
                />
              ) : (
                <LuDelete
                  className="h-5 w-5 text-gray-800"
                  size={24}
                />
              )}
            </Combobox.Button>
          </div>

          <Combobox.Options className="absolute mt-1  max-h-44 w-full bg-gray-50 border border-gray-900 overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black/5 focus:outline-gray-900 sm:text-sm">
            {filteredPeople.length === 0 && query !== '' ? (
              <div className="relative cursor-default select-none px-4 py-2 text-gray-700">
                Nothing found.
              </div>
            ) : (
              filteredPeople.map((person) => (
                <Combobox.Option
                  // key={person.id}
                  value={person}
                  className={({ active }) =>
                    `relative cursor-default select-none py-2 px-4 ${active ? 'text-white bg-gray-400' : 'text-gray-900 '
                    }`
                  }
                >
                  {({ selected, active }) => (
                    <>
                      <span
                        className={`block truncate ${selected ? 'font-medium' : 'font-normal'
                          }`}
                      >
                        {person.email}
                      </span>
                      {selected ? (
                        <span
                          className={`absolute inset-y-0 left-0 flex items-center pl-3 ${active ? 'text-white' : 'text-teal-600'
                            }`}
                        >
                        </span>
                      ) : null}
                    </>
                  )}
                </Combobox.Option>
              ))
            )}
          </Combobox.Options>
        </div>
      </Combobox>
    </div>
  )
}
export default ComboBoxNewTicket;