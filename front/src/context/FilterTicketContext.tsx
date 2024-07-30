import React, { createContext, useContext, useState, ReactNode } from 'react';

// Definindo a interface do filtro
export interface Filter {
    search: string
    all: boolean;
    title: string;
    department: string;
    status: string;
    clientEmail: string;
    supportEmail: string;
    startDate: Date | null;
    endDate: Date | null;
}

// Estado inicial para o filtro
const initialFilter: Filter = {
    search:'',
    all: true,
    title: '',
    department: '',
    status: '',
    clientEmail: '',
    supportEmail: '',
    startDate: null,
    endDate: null,
};

// Criando o contexto com um valor default
const FilterContext = createContext<{
    filter: Filter;
    setFilter: React.Dispatch<React.SetStateAction<Filter>>;
    resetFilters: () => void;
}>({
    filter: initialFilter,
    setFilter: () => {},
    resetFilters: () => {},  // Função de reset adicionada aqui
});

// Componente provedor
export const FilterProvider: React.FC<{children: ReactNode}> = ({ children }) => {
    const [filter, setFilter] = useState<Filter>(initialFilter);

    // Função para resetar todos os filtros
    const resetFilters = () => {
        setFilter(initialFilter);
    };

    return (
        <FilterContext.Provider value={{ filter, setFilter, resetFilters }}>
            {children}
        </FilterContext.Provider>
    );
};

// Hook customizado para usar o contexto do filtro
export const useFilter = () => useContext(FilterContext);
