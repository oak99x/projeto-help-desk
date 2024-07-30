import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Search from '../components/Search';
import DashbordCards from '../components/DashbordCards';

interface Option {
  name: string;
  url: string;
}

const optionsMap: Record<string, Option[]> = {
  'VPN FortiClient': [
    { name: 'Option 1', url: '' },
    { name: 'Option 2', url: '' },
    { name: 'Option 3', url: '' },
  ],
  'Wi-Fi configuration': [
    { name: 'Option A', url: '' },
    { name: 'Option B', url: '' },
  ],
  'IT Processes': [
    { name: 'Option A', url: '' },
    { name: 'Option B', url: '' },
    { name: 'Option C', url: '' },
  ],
  'HR Department': [
    { name: 'Option X', url: '' },
    { name: 'Option Y', url: '' },
    { name: 'Option Z', url: '' },
  ],
  'Authorization W-SMART': [
    { name: 'Option X', url: '' },
    { name: 'Option X', url: '' },
    { name: 'Option Z', url: '' },
  ],
  'Others': [
    { name: 'Option X', url: '' },
    { name: 'Option Y', url: '' },
    { name: 'Option Z', url: '' },
  ],
  // Adicione mais conforme necessário
};

export const Dashboard = () => {
  const navigate = useNavigate();
  const [openOptionsFor, setOpenOptionsFor] = useState<string | null>(null);
  const [searchText, setSearchText] = useState<string>('');

  const handleSearch = (search: string) => {
    setSearchText(search);
    setOpenOptionsFor(null);
  };

  const handleOptions = (title: string) => {
    if (openOptionsFor === title) {
      setOpenOptionsFor(null);
    } else {
      setOpenOptionsFor(title);
    }
  };

  return (
    <>
      <div className='bg-gray-400'>
        <div className='flex flex-col  bg-gradient-to-r from-blue-600 via-blue-600 to-blue-900 h-60 justify-center items-center'>
          <div className='justify-center w-2/6 text-center font-bold   space-y-10'>
            <h1 className='text-3xl font-bold text-gray-50'>
              Como podemos ajudar?
            </h1>
            <div className='h-10'>
              <Search value={handleSearch}></Search>
            </div>
          </div>
        </div>

        <div className='flex flex-col justify-center items-center  my-3'>
          <div>
            <div className='border-b-4 border-gray-600 text-gray-800 font-bold text-base my-10 mx-1'>
              <h1>Choose a category below to find out more</h1>
            </div>
            <div className='flex space-x-10 items-center my-4'>
              {Object.keys(optionsMap).map((title, index) => (
                <DashbordCards
                  key={index}
                  title={title}
                  onclick={() => { handleOptions(title) }}
                />
              ))}
            </div>

            {openOptionsFor && (
              <div className='flex flex-col space-y-2 mt-14'>
                <div className='border-b-4 border-gray-600 text-gray-800 font-bold text-base'>
                  <h1>Options for {openOptionsFor}</h1>
                </div>
                {optionsMap[openOptionsFor]
                  .filter(option => option.name.toLowerCase().includes(searchText.toLowerCase()))
                  .map((option, index) => (
                    <div
                      key={index}
                      onClick={() => navigate(option.url)}
                      className='bg-gray-200 p-2 rounded-md border-b-4 border-gray-600 text-gray-800 font-bold text-base cursor-pointer hover:bg-gray-500'>
                      <h2>{option.name}</h2>
                    </div>
                  ))}
              </div>
            )}

            {
              searchText && !openOptionsFor && (
                <>
                <div className='mt-14 mb-14'>

                
                  {Object.keys(optionsMap).map((title, index) => {
                    // Filtra as opções que correspondem ao texto de pesquisa
                    const filteredOptions = optionsMap[title].filter(option =>
                      option.name.toLowerCase().includes(searchText.toLowerCase())
                    );

                    // Se não houver opções correspondentes, não renderize o título
                    if (filteredOptions.length === 0) {
                      return null;
                    }

                    return (
                      <>
                        <div key={index} className='mb-5'>
                          <div className='border-b-4 border-gray-600 text-gray-800 font-bold text-base'>
                            <h1>Options for {title}</h1>
                          </div>
                          <div className='flex flex-col space-y-2 mt-4'>
                            {filteredOptions.map((option, idx) => (
                              <div
                                key={idx}
                                onClick={() => navigate(option.url)}
                                className='bg-gray-200 p-2 rounded-md border-b-4 border-gray-600 text-gray-800 font-bold text-base cursor-pointer hover:bg-gray-500'>
                                <h2>{option.name}</h2>
                              </div>
                            ))}
                          </div>
                        </div>

                      </>
                    );
                  })}
                  </div>
                </>
              )
            }
          </div>
        </div>
      </div>

    </>
  );
};
