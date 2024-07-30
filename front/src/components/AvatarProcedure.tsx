const getInitials = (name: string) => {
    // Separa o nome completo em partes usando espaço como delimitador
    const parts = name.trim().split(/\s+/);

    // Pega a primeira letra do primeiro nome
    const firstInitial = parts[0].charAt(0).toUpperCase();

    let lastInitial = '';

    if (parts.length > 1) {
        // Pega a última letra do último nome
        lastInitial = parts[parts.length - 1].charAt(0).toUpperCase();
    }

    // Retorna as iniciais formatadas
    return firstInitial + lastInitial;
}

interface Props {
    name: string | undefined;
}

const AvatarProcedure = ({ name }: Props) => {
    const initials = name ? getInitials(name) : 'AN';

    return (
        <>
            <div className="avatar placeholder">
                <div className="bg-gray-800 text-gray-500 font-semibold rounded-full w-12 h-12 flex items-center justify-center">
                    <span>{initials}</span>
                </div>
            </div>
        </>
    );
}

export default AvatarProcedure;
