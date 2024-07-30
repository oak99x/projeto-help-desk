import React, { useState, useEffect } from 'react';
import { BsSearch } from 'react-icons/bs';

type Props = {
    value: (value: string) => void;
};

const Search = ({ value }: Props) => {
    // Estado para armazenar o valor do input
    const [inputValue, setInputValue] = useState('');

    // Efeito para chamar a função 'value' sempre que 'inputValue' mudar
    useEffect(() => {
        value(inputValue);
    }, [inputValue]);

    // Função para lidar com mudanças no input
    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setInputValue(event.target.value);
    };

    return (
        <>
            <label className="h-full w-full input flex items-center gap-2 rounded-3xl text-gray-850 border border-gray-500 p-2 px-4">
                <input
                    value={inputValue}
                    onChange={handleInputChange}
                    type="text"
                    className="grow text-sm placeholder-gray-800"
                    placeholder="Search"
                />
                <BsSearch />
            </label>
        </>
    );
};

export default Search;