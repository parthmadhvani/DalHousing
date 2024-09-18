import React, { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import { Autocomplete as GoogleAutocomplete } from '@react-google-maps/api';
import Sidebar from './Sidebar';
import { useLocation, useParams, useNavigate } from 'react-router-dom';


export default function EditProperty() {

    const [files, setFiles] = useState([]);

    const location = useLocation();
    const navigate = useNavigate();

    const { userId, property_id } = location.state || {};

    useEffect(() => {
        if (!userId || !property_id) {
            navigate('/login');
        }
    }, [userId, property_id, navigate]);

    console.log(location.state);

    console.log("prop_ID in edit page:" + property_id);

    const getURL = `http://172.17.0.139:8080/properties/edit_property/${property_id}`;
    const putURL = `http://172.17.0.139:8080/properties/edit_property/${property_id}`;

    const [data, setData] = useState({
        street_address: "",
        property_type: "",
        unit_number: "",
        city: "",
        province: "",
        postal_code: "",
        email: "",
        phone: "",
        monthly_rent: "",
        security_deposite: "",
        availability: "",
        property_heading: "",
        full_description: "",
        latitude: "",
        longitude: "",
        bedrooms: "",
        bathrooms: "",
        furnishing: "",
        parking: "",
        images: {},
        youtube: ""
    })
    const [unformatedDate, setUnformatedDate] = useState(null);
    const [loading, setLoading] = useState(false);

    const token = localStorage.getItem('token')

    useEffect(() => {
        axios.get(getURL, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                console.log("ni axios");
                console.log(response.data);
                const { availability } = response.data;
                setUnformatedDate(availability);
                setData({
                    ...response.data,
                    availability: [formatDate(response.data.availability)]
                })
            })
            .catch(error => console.log(error))
    }, [getURL])

    function formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = ('0' + (date.getMonth() + 1)).slice(-2);
        const day = ('0' + date.getDate()).slice(-2);
        return `${year}-${month}-${day}`;
    }

    const handleChange = (e) => {
        const { id, value } = e.target;
        setData({
            ...data,
            [id]: value
        })
    }

    const handleFileChange = (e) => {
        const selectedFiles = Array.from(e.target.files);
        setFiles(selectedFiles);
    };

    const submitHandler = async (e) => {
        e.preventDefault();
        setLoading(true);

        const formData = new FormData();

        formData.append('property', new Blob([JSON.stringify({ ...data, availability: unformatedDate })], { type: 'application/json' }));
        let isPhotoPresent = false

        const validTypes = ["image/jpeg", "image/png", "image/jpg", "image/png"];

        Array.from(files).forEach(file => {
            isPhotoPresent = true
            if (!validTypes.includes(file.type)) {
                alert("Invalid file type. Please upload a JPEG, JPG or PNG image.")
                console.log("Invalid file type. Please upload a JPEG, JPG or PNG image.");
                return;
            }
            formData.append('files', file);
        });

        if (isPhotoPresent) {
            try {
                const response = await axios.post(putURL, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        'Authorization': `Bearer ${token}`
                    }
                });
                console.log(response);
                alert(response.data)
            } catch (error) {
                alert(error.response.data)
            } finally {
                setLoading(false); // -
            }
        }
        else {
            try {
                const response = await axios.post(putURL, formData, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                console.log(response);
                alert(response.data)
            }
            catch (error) {
                alert(error.response.data)
            } finally {
                setLoading(false);
            }
        }
    };

    const [autocomplete, setAutocomplete] = useState(null);
    const inputRef = useRef(null);

    const onLoad = (autocompleteInstance) => {
        setAutocomplete(autocompleteInstance);
    };

    const onPlaceChanged = () => {
        if (autocomplete !== null) {
            const place = autocomplete.getPlace();
            const location = place.geometry.location;

            const addressComponents = place.address_components;
            let street_address = '';
            let city = '';
            let province = '';
            let postal_code = '';
            let latitude = location.lat();
            let longitude = location.lng()

            addressComponents.forEach(component => {
                const types = component.types;
                if (types.includes('route')) {
                    street_address += component.long_name + ' ';
                }
                if (types.includes('street_number')) {
                    street_address = component.long_name + ' ' + street_address;
                }
                if (types.includes('locality')) {
                    city = component.long_name;
                }
                if (types.includes('administrative_area_level_1')) {
                    province = component.short_name;
                }
                if (types.includes('postal_code')) {
                    postal_code = component.long_name;
                }
            });

            setData({

                ...data,
                street_address: street_address.trim(),
                city: city,
                province: province,
                postal_code: postal_code,
                latitude: latitude,
                longitude: longitude

            });

        } else {
            console.log('Autocomplete is not loaded yet!');
        }
    };

    console.log(userId);

    return (
        <div className="">
            <Sidebar userId={userId} />
            <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
                <form className="ml-80 bg-slate-100 text-left" onSubmit={submitHandler}>
                    <div className="m-auto bor ">
                        <div className="border-b border-gray-900/10 py-12 lg:px-32 px-12">
                            <h2 className="text-2xl mb-6 font-bold leading-7 text-gray-900">Property address</h2>
                            <div className=" grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                                <div className="col-span-2">
                                    <label htmlFor="street-address" className="block text-lg font-medium leading-6 text-gray-900">
                                        Street address
                                    </label>
                                    <div className="mt-2">

                                        <GoogleAutocomplete
                                            onLoad={onLoad}
                                            onPlaceChanged={onPlaceChanged}
                                        >
                                            <input
                                                type="text"
                                                name="street-address"
                                                id="street_address"
                                                ref={inputRef}
                                                autoComplete="street-address"
                                                className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                                onChange={handleChange}
                                                value={data.street_address}
                                            />
                                        </GoogleAutocomplete>

                                    </div>
                                </div>

                                <div className="col-span-2">
                                    <label htmlFor="street-address" className="block text-lg font-medium leading-6 text-gray-900">
                                        Property type
                                    </label>
                                    <div className="mt-2">
                                        <select onChange={handleChange} value={data.property_type} className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" name="cars" id="property_type">
                                            <option value="Townhouse">Townhouse</option>
                                            <option value="Condo">Condo</option>
                                            <option value="Duplex">Duplex</option>
                                            <option value="Basement">Basement</option>
                                            <option value="Apartment">Apartment</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="col-span-2">
                                    <label htmlFor="street-address" className="block text-lg font-medium leading-6 text-gray-900">
                                        Unit number
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            type="number"
                                            name="street-address"
                                            id="unit_number"
                                            autoComplete="street-address"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.unit_number}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-2 sm:col-start-1">
                                    <label htmlFor="city" className="block text-lg font-medium leading-6 text-gray-900">
                                        City
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            type="text"
                                            name="city"
                                            id="city"
                                            autoComplete="address-level2"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.city}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-2">
                                    <label htmlFor="region" className="block text-lg font-medium leading-6 text-gray-900">
                                        State / Province
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            type="text"
                                            name="region"
                                            id="province"
                                            autoComplete="address-level1"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.province}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-2">
                                    <label htmlFor="postal-code" className="block text-lg font-medium leading-6 text-gray-900">
                                        ZIP / Postal code
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            type="text"
                                            name="postal-code"
                                            id="postal_code"
                                            autoComplete="postal-code"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.postal_code}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="border-b border-gray-900/10 py-12 px-32 py-12 lg:px-32 px-12">
                            <h2 className="text-2xl mb-6 font-bold leading-7 text-gray-900">Contact details</h2>
                            <div className=" grid grid-cols-1 gap-x-6 gap-y-8">
                                <div className="sm:col-span-4">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Email address
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            id="email"
                                            name="email"
                                            type="email"
                                            autoComplete="email"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.email}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-2 w-1/4">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Mobile number
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            id="mobile"
                                            name="mobile"
                                            type="text"
                                            autoComplete="mobile"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.mobile}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="border-b border-gray-900/10 py-12 px-32 py-12 lg:px-32 px-12">
                            <h2 className="text-2xl mb-6 font-bold leading-7 text-gray-900">Costing</h2>
                            <div className=" grid grid-cols-6 gap-x-6 gap-y-8">

                                <div className="sm:col-span-3">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Monthly rent
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            id="monthly_rent"
                                            name="email"
                                            type="number"
                                            autoComplete="email"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.monthly_rent}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-3">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Security deposite
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            id="security_deposite"
                                            name="mobile"
                                            type="number"
                                            autoComplete="mobile"
                                            className="block w-full rounded-md border-0 py-1.5  text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.security_deposite}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-3 col-span-6">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Availability
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            id="availability"
                                            name="mobile"
                                            type="date"
                                            autoComplete="mobile"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.availability}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="border-b border-gray-900/10 py-12 px-32 py-12 lg:px-32 px-12">
                            <h2 className="text-2xl mb-6 font-bold leading-7 text-gray-900">Description</h2>

                            <div className=" grid grid-cols-6 gap-x-6 gap-y-8">

                                <div className="sm:col-span-6">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Property heading
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            id="property_heading"
                                            name="email"
                                            type="text"
                                            autoComplete="email"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.property_heading}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-6">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Full description
                                    </label>
                                    <div className="mt-2">
                                        <textarea
                                            id="full_description"
                                            name="email"
                                            type="text"
                                            autoComplete="email"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.full_description}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-3">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Bathrooms
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            id="bathrooms"
                                            name="email"
                                            type="number"
                                            autoComplete="email"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.bathrooms}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-3">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Bedrooms
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            id="bedrooms"
                                            name="mobile"
                                            type="number"
                                            autoComplete="mobile"
                                            className="block w-full rounded-md border-0 py-1.5  text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.bedrooms}
                                        />
                                    </div>
                                </div>

                                <div className="sm:col-span-3">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Parking
                                    </label>
                                    <div className="mt-2">
                                        <select onChange={handleChange} value={data.parking} className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" name="cars" id="parking">
                                            <option value="Available">Available</option>
                                            <option value="Unavailable">Unavailable</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="sm:col-span-3">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        furnishing
                                    </label>
                                    <div className="mt-2">
                                        <select onChange={handleChange} value={data.furnishing} className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" name="cars" id="furnishing">
                                            <option value="Furnished">Furnished</option>
                                            <option value="Unfurnished">Unfurnished</option>
                                            <option value="Semi-furnished">Semi-furnished</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="sm:col-span-6">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Youtube link
                                    </label>
                                    <div className="mt-2">
                                        <input
                                            id="property_heading"
                                            name="youtube"
                                            type="text"
                                            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                            onChange={handleChange}
                                            value={data.youtube}
                                        />
                                    </div>
                                </div>


                                <div className="sm:col-span-6">
                                    <label htmlFor="email" className="block text-lg font-medium leading-6 text-gray-900">
                                        Upload multiple images
                                    </label>
                                    <div className="mt-2">

                                        <div class="font-[sans-serif] ">
                                            <input type="file"
                                                class="w-full text-gray-400 font-semibold text-sm bg-white border file:cursor-pointer cursor-pointer file:border-0 file:py-3 file:px-4 file:mr-4 file:bg-gray-100 file:hover:bg-gray-200 file:text-gray-500 rounded "
                                                id="file"
                                                onChange={handleFileChange}
                                                multiple
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="mt-6 flex items-center justify-end gap-x-6">
                                <button
                                    type="submit"
                                    className="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                                >
                                    {loading ? 'Saving...' : 'Save'}
                                    {/* Save */}
                                </button>
                            </div>
                        </div>

                    </div>
                </form>
            </div>
        </div>
    )
}