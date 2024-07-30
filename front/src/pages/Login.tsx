import { useForm, SubmitHandler } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { LoginData } from '../api/AuthService';
import { useAuth } from '../auth/AuthContext';
import { FaUnlockKeyhole } from "react-icons/fa6";
import { MdEmail } from "react-icons/md";
import { useState } from 'react';

const schema = yup.object().shape({
    email: yup.string().email('Email inválido').required('Email é obrigatório'),
    password: yup.string().required('Senha é obrigatória'),
    isAdmin: yup.boolean(),
});

const Login = () => {
    const { login } = useAuth();
    const { register, handleSubmit, formState: { errors } } = useForm<LoginData>({
        resolver: yupResolver(schema),
    });
    const [error, setError] = useState<string | null>(null);

    const onSubmit: SubmitHandler<LoginData> = async (data) => {
        try {
            await login(data);
        } catch (error: any) {
            setError(error.message);
            console.error(error.message);
        }
    };

    return (
        <div className="flex flex-col md:flex-row h-screen w-screen">
            <div className="w-full md:w-2/3 lg:w-4/6 bg-gradient-to-b from-[#0575E6] via-blue-600 to-blue-900 flex items-center">
                <div className='flex flex-col justify-center ml-40'>
                    <h1 className="my-2 text-xl md:text-7xl text-left text-gray-50 font-extrabold tracking-tigh">Assistly</h1>
                    <p className="my-2 text-xl md:text-xl text-left text-gray-50 font-normal tracking-tigh">Customer support and IT management tool, aiming for agility and cost reduction.</p>
                    <div>
                        <button 
                            onClick={()=>{}}
                            className="btn-500 my-2 border-none rounded-3xl px-10 py-2.5 text-gray-50 text-sm font-normal hover:bg-[#0575E6]">
                            Read More
                        </button>
                    </div>
                </div>
            </div>

            <div className="h-full flex flex-1 flex-col justify-center items-center px-4 md:px-10 lg:px-8 bg-gray-50">
                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                    <h2 className="m-0 text-xl md:text-2xl text-left text-primary font-extrabold tracking-tigh">
                        Hello Again!
                    </h2>
                    <p className='md:text-left text-primary font-medium'>Welcome Back</p>
                </div>

                <div className="mt-6 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form className="space-y-4 md:space-y-5" onSubmit={handleSubmit(onSubmit)} method="POST">
                        <div>
                            {error && <span className="text-sm font-semibold text-error">{error}. Try again.</span>}
                            <div className="mt-1 relative">
                                <span className="absolute inset-y-0 left-0 flex items-center pl-3">
                                    <MdEmail color="#b4b4b4" />
                                </span>
                                <input
                                    id="email"
                                    {...register('email')}
                                    type="email"
                                    autoComplete="email"
                                    placeholder="Email"
                                    className="block w-full rounded-3xl border border-gray-500 p-2 pl-10 outline-blue-400 text-sm placeholder-gray-700"
                                />
                            </div>
                            {errors.email && <span className="text-sm font-semibold text-error">{errors.email.message}</span>}
                        </div>

                        <div>
                            <div className="mt-1 relative">
                                <span className="absolute inset-y-0 left-0 flex items-center pl-3">
                                    <FaUnlockKeyhole color="#b4b4b4" />
                                </span>
                                <input
                                    id="password"
                                    {...register('password')}
                                    type="password"
                                    autoComplete="current-password"
                                    placeholder="Password"
                                    className="block w-full rounded-3xl border border-gray-500 p-2 pl-10 outline-blue-400 text-sm placeholder-gray-700"
                                />
                            </div>
                            {errors.password && <span className="text-sm font-semibold text-error">{errors.password.message}</span>}

                            <div className="flex mt-1 justify-end">
                                <a href="#" className="font-normal text-sm text-secondary">
                                    Forgot password?
                                </a>
                            </div>
                        </div>

                        <div className="form-control">
                            <label className="flex items-center space-x-2">
                                <span className="label-text text-sm font-semibold">Is admin?</span>
                                <input
                                    type="checkbox"
                                    id="isAdmin"
                                    {...register('isAdmin')}
                                    className="toggle toggle-secondary"
                                />
                            </label>
                        </div>

                        <div className='flex justify-center items-center '>
                            <button
                                type="submit"
                                className="btn mt-5 w-full flex justify-center text-gray-100 rounded-3xl px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-blue-500"
                            >
                                Sign in
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Login;

