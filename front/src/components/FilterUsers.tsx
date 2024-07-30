import { useState, useEffect, useRef } from 'react';
import { useForm } from 'react-hook-form';
import Search from './Search';
import { IoAddOutline } from "react-icons/io5";
import { useFilter } from '../context/FilterUserContext'

const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

const FilterUsers = () => {
    // Estado para controlar a visibilidade do filtro lateral
    const [lateralFilter, setLateralFilter] = useState(false);
    const lateralFilterRef = useRef<HTMLDivElement>(null);  // Referência ao painel lateral do filtro

    // Função para alternar a visibilidade do filtro lateral
    const toggleLateralFilter = () => {
        setLateralFilter(!lateralFilter);
    };

    const handleClickOutside = (event: MouseEvent) => {
        if (lateralFilterRef.current && !lateralFilterRef.current.contains(event.target as Node)) {
            setLateralFilter(false);
        }
    };

    useEffect(() => {
        if (lateralFilter) {
            document.addEventListener('mousedown', handleClickOutside);
        } else {
            document.removeEventListener('mousedown', handleClickOutside);
        }

        // Limpar o evento ao desmontar o componente
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [lateralFilter]);

    const [search, setSearch] = useState('');
    const { filter, setFilter, resetFilters } = useFilter();

    const { register, handleSubmit, reset, formState: { errors } } = useForm({
        defaultValues: {
            area: filter.area,
            userEmail: filter.userEmail
        }
    });

    const onSubmit = (data: any) => {
        setFilter(prev => ({ ...prev, ...data }));
    };

    const handleReset = () => {
        resetFilters();
        reset();
        setFilter(prev => ({ ...prev, ['search']: search }));
    };

    const handleSearch = (value: string) => {
        setSearch(value);
        setFilter(prev => ({ ...prev, ['search']: value }));
    };


    return (
        <>
            <div className='fixed top-16 w-full z-10 flex items-center px-5 h-16 bg-gray-50 text-sm text-left font-semibold text-gray-850 border-b-2 border-gray-700'>
                <div className='w-full h-full flex items-center space-x-3'>
                    <div>
                        <p>Search for:</p>
                    </div>
                    <div className='w-3/6 h-10 flex  items-center space-x-3 bg-blue-700 rounded-3xl'>
                        <Search value={(value) => { handleSearch(value) }}></Search>
                        <button onClick={(toggleLateralFilter)} className="h-full w-28 pr-5 flex items-center justify-between font-semibold text-gray-50">
                            <IoAddOutline size={16} />
                            Filters
                        </button>
                    </div>
                </div>
            </div>

            <form onSubmit={handleSubmit(onSubmit)}>
                {lateralFilter && (
                    <div ref={lateralFilterRef} className="z-10 fixed right-0 top-0 w-1/4 h-screen bg-gray-400 shadow-xl shadow-gray-700 p-5 overflow-y-auto">
                        <div className='space-y-2'>

                            <div className="label flex flex-col items-start">
                                <span className="label-text">Area</span>
                                <select {...register("area")} className="w-full select select-bordered">
                                    <option value=""></option>
                                    <option value="Area 1">Area 1</option>
                                    <option value="Area 2">Area 2</option>
                                    <option value="Area 3">Area 3</option>
                                    <option value="Area 4">Area 4</option>
                                    <option value="Area 5">Area 5</option>
                                </select>
                            </div>

                            <div className="label flex flex-col items-start">
                                <span className="label-text">Email</span>
                                <input
                                    {...register("userEmail", {
                                        validate: {
                                            isEmailValid: value => value === '' || emailRegex.test(value) || "Email inválido"
                                        }
                                    })}
                                    type="text" placeholder="Type here" className="input input-bordered w-full" />
                                {errors.userEmail && <p className="text-error text-sm">{errors.userEmail.message}</p>}
                            </div>
                            
                        </div>

                        <div className='w-full flex flex-wrap my-5 justify-between md:space-y-2 items-baseline'>
                            <button
                                type='button'
                                onClick={handleReset}
                                className='btn btn-default text-gray-850 w-full 2xl:w-40'>
                                Clean filter
                            </button>
                            <button
                                type="submit"
                                className='btn btn-700 text-gray-50 w-full 2xl:w-40'>
                                Search
                            </button>
                        </div>
                    </div>
                )}
            </form>
        </>
    )
}

export default FilterUsers;