export interface User {
    name: string;
    email: string;
    area: string;
    roles: string[];
}

const apiURL = import.meta.env.VITE_NODE_ENV === 'prod' ? import.meta.env.VITE_API_URL_PROD : import.meta.env.VITE_API_URL_DEV;
const API_URL_USER = `${apiURL}/api/user`;
const API_URL_ADMIN = `${apiURL}/api/admin`;

export async function getUsers(filter: any, token: string | null): Promise<any> {
    
    if (filter.role) {
        return getUsersRole(filter.role, token);
    }

    return allUsers(token);
}

export async function allUsers(token: string | null): Promise<any> {

    // const url = 'http://localhost:8765/api/admin/users';
    const url = `${API_URL_ADMIN}/users`;

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

    return response.json();
}

export async function getUser(token: string | null): Promise<any> {

    // const url = 'http://localhost:8765/api/user';
    const url = `${API_URL_USER}`;

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

    return response.json();
}

export async function getUserEmail(email: string | null, token: string | null): Promise<any> {

    // const baseUrl = 'http://localhost:8765/api/user'
    const url = `${API_URL_USER}/email/${email}`;

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

    return response.json();
}

export async function getUsersRole(role: string | null, token: string | null): Promise<any> {

    // const baseUrl = 'http://localhost:8765/api/admin'
    const url = `${API_URL_ADMIN}/users/role/${'role_' + role}`;

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

    return response.json();
}

export async function promoteToAdmin(email: string | null, token: string | null): Promise<any> {

    // const baseUrl = 'http://localhost:8765/api/user'
    const url = `${API_URL_ADMIN}/promoteToAdmin/${email}`;

    const response = await fetch(url, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        const errorData = await response.json(); // Assumindo que a resposta do erro está em formato JSON
        throw new Error(errorData.error); // Lança um erro com a mensagem específica de erro do backend
    }

    return response.json();
}

export async function removeRoleAdmin(email: string | null, token: string | null): Promise<any> {
    const url = `${API_URL_ADMIN}/deleteRoleAdmin/${email}`;

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

    
    console.log(response);

    return response.json();
}

export async function removeUser(email: string | null, token: string | null): Promise<any> {
    console.log("remove" + email + token)
    return null;
}

export async function getUsersByFilter(filter: any, token: string | null): Promise<any> {


    // const baseUrl = 'http://localhost:8765/api/ticket/filter';
    const queryString = buildQueryString(filter);
    const url = `${API_URL_ADMIN}/filter?${queryString}`;

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

    return  response.json();
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


