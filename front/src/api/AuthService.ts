
interface LoginResponse {
    access_token: string;
    expires_in: number;
    token_type: string;
}

export interface LoginData {
    email: string;
    password: string;
    isAdmin?: boolean;
}

const apiURL = import.meta.env.VITE_NODE_ENV === 'prod' ? import.meta.env.VITE_API_URL_PROD : import.meta.env.VITE_API_URL_DEV;
// const API_URL = 'https://35.247.244.176:8765/oauth2/token';
const API_URL = `${apiURL}/oauth2/token`;

// Função para codificar os dados como x-www-form-urlencoded
const encodeFormData = (data: any) => {
    return Object.keys(data)
        .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(data[key]))
        .join('&');
};

export const loginService = async ({ email, password, isAdmin }: LoginData): Promise<LoginResponse> => {

    // Substitua 'myclientid' e 'myclientsecret' pelos seus reais Client ID e Client Secret
    const clientId = import.meta.env.VITE_CLIENT_ID;
    const clientSecret = import.meta.env.VITE_CLIENT_SECRET;

    // Codificando clientId e clientSecret em Base64 para autenticação Basic Auth
    const credentials = btoa(`${clientId}:${clientSecret}`);

    const formData = {
        username: email,          // Usar dois pontos para atribuição de valor
        password: password,       // Usar dois pontos para atribuição de valor
        grant_type: "password",   // "password" é comumente usado aqui para fluxos de senha do OAuth
        mode: isAdmin ? "admin" : "user", // "admin" ou "user", dependendo do que você precisa
    };

    const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': `Basic ${credentials}`
        },
        body: encodeFormData(formData),
    });

    // Read the response JSON data just once
    const data = await response.json();

    // Check if the response was not ok after reading the json data
    if (!response.ok) {
        // Handle the error with the data already read
        throw new Error(data.message || 'Invalid credentials');
    }

    //chamar get by user salvar em um contetxo
    console.log(data.access_token)
    // Continue processing with the data
    localStorage.setItem('access_token', data.access_token);
    const expiresAt = new Date().getTime() + data.expires_in * 1000;
    localStorage.setItem('expires_at', expiresAt.toString());
    localStorage.setItem('is_admin', formData.mode);

    return data;
};
