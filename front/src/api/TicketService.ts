
export interface Ticket {
    id: string;
    title: string;
    description: string;
    department: string;
    generator: {
        name: string;
        email: string;
    };
    client: {
        name: string;
        email: string;
    };
    support: null | {
        name: string;
        email: string;
    };
    status: string;
    startDate: string | null;
    endDate: string | null;
}

export interface Procedure {
    content: string;
    sender: {
        name: string;
        email: string;
    };
    dateTime: string;
    date: string;
    time: string;
}

export type FormData = {
    title: string;
    description: string;
    department: string;
    email: string;
    status: string;
};

const apiURL = import.meta.env.VITE_NODE_ENV === 'prod' ? import.meta.env.VITE_API_URL_PROD : import.meta.env.VITE_API_URL_DEV;
const API_URL = `${apiURL}/api/ticket`;

function formatDate(dateString: string) {
    if (!dateString) return null;
    return dateString.split('T')[0];
}

function formatDateTime(dateTime: string, index: number) {

    if (!dateTime) return null;
    const parts = dateTime.split('T');
    if (index === 1 && parts[1]) {
        // Separa a parte do tempo em hora, minutos, segundos e milissegundos
        const timeParts = parts[1].split(':');
        // Retorna apenas a hora e os minutos, assumindo que timeParts[0] é a hora e timeParts[1] são os minutos
        return `${timeParts[0]}:${timeParts[1]}`;
    }

    return parts[index];
}

export async function getTickets(filter: any, token: string | null): Promise<any> {


    // const baseUrl = 'http://localhost:8765/api/ticket/filter';
    const queryString = buildQueryString(filter);
    const url = `${API_URL}/filter?${queryString}`;

    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    const tickets = await response.json();

    const formattedTickets = tickets.map((ticket: Ticket) => ({
        ...ticket,
        status: ticket.status === 'IN_PROGRESS' ? 'IN PROGRESS' : ticket.status,
        startDate: ticket.startDate ? formatDate(ticket.startDate) : null,
        endDate: ticket.endDate ? formatDate(ticket.endDate) : null
    }));

    return formattedTickets;
}

function buildQueryString(filter: any): string {
    const queryParts: string[] = [];
    Object.keys(filter).forEach(key => {
        const value = (filter as any)[key];
        if (value !== undefined && value !== null) {
            queryParts.push(`${encodeURIComponent(key)}=${encodeURIComponent(value.toString())}`);
        }
    });
    return queryParts.join('&');
}

export async function getTicket(ticketId: string | null, token: string | null): Promise<any> {
    // const baseUrl = 'http://localhost:8765/api/ticket/';
    const url = `${API_URL}/${ticketId}`;

    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    const ticket = await response.json();

    return {
        ...ticket,
        status: ticket.status === 'IN_PROGRESS' ? 'IN PROGRESS' : ticket.status,
        startDate: formatDate(ticket.startDate),
        endDate: ticket.endDate ? formatDate(ticket.endDate) : null
    };
}


export async function createTicket(data: FormData, token: string | null): Promise<any> {
    // const baseUrl = 'http://localhost:8765/api/ticket/create';
    const url = `${API_URL}/create`;

    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title: data.title,
            description: data.description,
            department: data.department,
            clientEmail: data.email
        })
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
}

export async function assignTicket(ticketId: string | null, token: string | null): Promise<any> {
    // const baseUrl = 'http://localhost:8765/api/ticket/assign';
    const url = `${API_URL}/assign/${ticketId}`;

    const response = await fetch(url, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
}

export async function deleteTicket(ticketId: string | null, token: string | null): Promise<any> {
    const url = `${API_URL}/delete/${ticketId}`;

    const response = await fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
}

export async function addProcedure(ticketId: string | null, content: string, token: string | null): Promise<any> {
    // const baseUrl = 'http://localhost:8765/api/ticket';
    const url = `${API_URL}/${ticketId}/respond`;

    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            content: content
        })
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
}

export async function getProcedures(ticketId: string | null, token: string | null): Promise<any> {
    // const baseUrl = 'http://localhost:8765/api/ticket';
    const url = `${API_URL}/${ticketId}/procedures`;

    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    const procedures = await response.json();

    const formattedProcedures = procedures.map((procedure: Procedure) => ({
        ...procedure,
        date: formatDateTime(procedure.dateTime, 0),
        time: formatDateTime(procedure.dateTime, 1),
    }));

    // Ordenando os procedimentos em ordem decrescente por dateTime
    formattedProcedures.sort((a: Procedure, b: Procedure) => {
        return new Date(b.dateTime).getTime() - new Date(a.dateTime).getTime();
    });

    return formattedProcedures;
}

export async function updateTicket(data: FormData, ticketId: string | null, token: string | null): Promise<any> {
    // const baseUrl = 'http://localhost:8765/api/ticket';
    const url = `${API_URL}/${ticketId}/update`;

    const response = await fetch(url, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            status: data.status.replace(' ', '_'),
            department: data.department,
        })
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    const ticket = await response.json();

    return {
        ...ticket,
        status: ticket.status === 'IN_PROGRESS' ? 'IN PROGRESS' : ticket.status,
    };
}

export async function concludedTicket(ticketId: string | null, token: string | null): Promise<any> {
    // const baseUrl = 'http://localhost:8765/api/ticket';
    const url = `${API_URL}/${ticketId}/concluded`;

    const response = await fetch(url, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
}


