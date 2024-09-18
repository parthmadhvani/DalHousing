import { useEffect, useState } from "react"
import axios from "axios";
import Sidebar from "./Sidebar";
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import NoDataFound from "./NoDataFound";

export default function PropertyTable() {


    const location = useLocation();

    const navigate = useNavigate();
    
    const userId = location.state?.userId;
    console.log("userID" + userId);
    const url = `http://172.17.0.139:8080/properties/get_properties/${userId}`
    const [properties, setProperties] = useState([]);
    const [loading, setLoading] = useState(true);

    console.log("pID" + userId);

    const token = localStorage.getItem('token')
    console.log(token);


    useEffect(() => {
        if (!userId || !token) {
            console.error("No userId or token found, redirecting to login.");
            navigate("/login");
        } else {
            const url = `http://172.17.0.139:8080/properties/get_properties/${userId}`;
            axios.get(url, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
                .then(response => {
                    setProperties(response.data);
                })
                .catch(error => console.log(error))
                .finally(() => setLoading(false)); 
        }
    }, [userId, token, navigate]);


    const EditClickHandler = (property_id) => {
        navigate(`/edit_properties`, { state: { userId: userId, property_id: property_id } })
    }

    const RemoveClickHandler = (property_id) => {

        // eslint-disable-next-line no-restricted-globals
        var ans = confirm("Are you sure you want to delete the property?");

        if (ans) {
            deleteProperty(property_id);
        }
    }

    var deleteProperty = async (property_id) => {
        axios.get(url)
            .then(response => {
                setProperties(response.data)
            })
            .catch(error => console.log(error))

        await axios.delete(`http://172.17.0.139:8080/properties/delete_property/${property_id}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })

        const response = await axios.get(url, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                setProperties(response.data)
            })
            .catch(error => console.log(error))

        console.log(response);
    }

    return (

        <div className="">
            <Sidebar />
            <div className="ml-80 w-auto p-4 col-span-10">
                {properties.length === 0 ? (
                    <NoDataFound />
                ) : (
                    <div className="flex flex-wrap -mx-2">
                        {properties.map((property) => {
                            let statusContent;
                            let statusIndicator;

                            switch (property.status) {
                                case 'Rejected':
                                    statusContent = "Rejected";
                                    statusIndicator = "text-red-500";
                                    break;
                                case 'Pending':
                                    statusContent = "Pending";
                                    statusIndicator = "text-yellow-500";
                                    break;
                                case 'Approved':
                                    statusContent = "Approved";
                                    statusIndicator = "text-green-500";
                                    break;
                                default:
                                    statusContent = "Pending";
                                    statusIndicator = "text-yellow-500";
                                    break;
                            }

                            return (
                                <div className="w-full md:w-1/2 lg:w-1/3 px-2 mb-4" key={property.propertyId}>
                                    <div className="w-full max-w-md p-3 grid gap-2 border rounded-md shadow-sm">
                                        <div className="flex items-center justify-between">
                                            <div className="text-sm text-muted-foreground">ID: {property.propertyId}</div>
                                            <div className="flex items-center gap-2">
                                                <div className={`rounded-full w-2 h-2 ${statusIndicator}`} />
                                                <span className={`text-sm font-medium ${statusIndicator}`}>{statusContent}</span>
                                            </div>
                                        </div>
                                        <div>
                                            <h3 className="text-xl font-semibold">{property.property_heading}</h3>
                                            <p className="text-muted-foreground">{property.street_address}</p>
                                        </div>
                                        <div className="flex justify-end gap-4">
                                            <button
                                                className="border border-muted-foreground rounded-md px-4 py-2 text-muted-foreground hover:bg-muted/50 transition-colors"
                                                onClick={() => EditClickHandler(property.propertyId)}
                                            >
                                                Edit
                                            </button>
                                            <button
                                                className="bg-red-500 text-white rounded-md px-4 py-2 hover:bg-red-600 transition-colors"
                                                onClick={() => RemoveClickHandler(property.propertyId)}
                                            >
                                                Remove
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}

            </div>
        </div>
    )
}