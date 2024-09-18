import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { FaSignOutAlt } from 'react-icons/fa'; 

export default function Sidebar(props) {
    const navigate = useNavigate();
    const { id: paramId } = useParams();

    const id = localStorage.getItem('userId');


    const my_listing_path = `/dashboard`
    const remove_account_path = `/remove_account`;
    const change_password_path = '/change-password';
    const logout_path = `/logout`; 
    const add_properties_path = `/add_properties` 

    return (
        <div className="w-1/4 col-span-2 fixed z-20 flex flex-col bg-clip-border h-screen rounded-xl bg-white text-gray-700 h-[calc(100vh-2rem)] w-full max-w-[20rem] p-4 shadow-xl shadow-blue-gray-900/5">
            <div className="mb-2 p-4">
                <h5 className="block antialiased tracking-normal font-sans text-xl font-semibold leading-snug text-gray-900">My Account</h5>
            </div>
            <nav className="flex flex-col gap-1 min-w-[240px] p-2 font-sans text-base font-normal text-gray-700">
                <div
                    role="button"
                    tabIndex="0"
                    className="flex items-center w-full p-3 rounded-lg text-start leading-tight transition-all hover:bg-blue-50 hover:bg-opacity-80 focus:bg-blue-50 focus:bg-opacity-80 active:bg-gray-50 active:bg-opacity-80 hover:text-blue-900 focus:text-blue-900 active:text-blue-900 outline-none"
                    onClick={() => navigate(my_listing_path,{state:{userId:id}})}
                >
                    My Listings
                </div>
                <div
                    role="button"
                    tabIndex="0"
                    className="flex items-center w-full p-3 rounded-lg text-start leading-tight transition-all hover:bg-blue-50 hover:bg-opacity-80 focus:bg-blue-50 focus:bg-opacity-80 active:bg-blue-50 active:bg-opacity-80 hover:text-blue-900 focus:text-blue-900 active:text-blue-900 outline-none"
                    onClick={() => navigate(add_properties_path,{state:{userId:id}})}
                >
                    Add Property
                </div>
                <div
                    role="button"
                    tabIndex="0"
                    className="flex items-center w-full p-3 rounded-lg text-start leading-tight transition-all hover:bg-blue-50 hover:bg-opacity-80 focus:bg-blue-50 focus:bg-opacity-80 active:bg-blue-50 active:bg-opacity-80 hover:text-blue-900 focus:text-blue-900 active:text-blue-900 outline-none"
                    onClick={() => navigate(change_password_path,{state:{userId:id}})}
                >
                    Change Password
                </div>
               
            </nav>
            <div className="p-2 mt-auto">
                <div
                    role="button"
                    tabIndex="0"
                    className="flex items-center justify-center w-3/4 p-2 rounded-lg text-center font-bold transition-all bg-green-600 text-white hover:bg-green-700 focus:bg-green-700 active:bg-green-800 outline-none"
                    onClick={() => navigate(logout_path)}
                >
                    <FaSignOutAlt className="mr-2" />
                    Logout
                </div>
            </div>
        </div>
    );
}
