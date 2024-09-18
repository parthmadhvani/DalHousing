import axios from 'axios';

const API_URL = 'http://172.17.0.139:8080/properties';

export const getProperties = async (filters, token) => {
    const { pageNumber, pageSize, sortBy, sortDir, ...otherFilters } = filters;
    const params = new URLSearchParams({
        pageNumber,
        pageSize,
        sortBy,
        sortDir,
        ...otherFilters
    }).toString();
    try {
        const response = axios.get(`${API_URL}/filter?${params}`, {
        })
        return response;
    } catch (error) {
        console.error('Failed to get properties', error);
        throw error;
    }
};



export const saveProperty = async (userId, propertyId, token) => {
    try {
        const response = await axios.post(`${API_URL}/${userId}/save-property/${propertyId}`, null, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        return response;
    } catch (error) {
        let errorMessage = 'Failed to save property';
        if (error.response) {
            if (error.response.status === 400) {
                errorMessage = error.response.data || 'Property already saved';
            }
        }
        console.error(errorMessage);
        throw new Error(errorMessage);
    }
};



export const getSavedProperties = (userId, pageNumber = 0, pageSize = 6) => {
    const token = localStorage.getItem('token');  // Fetch the token from localStorage
    return axios.get(`${API_URL}/${userId}/saved-properties`, {
        headers: {
            'Authorization': `Bearer ${token}`
        },
        params: { pageNumber, pageSize }
    });
};

export const removeSavedProperty = (userId, propertyId) => {
    const token = localStorage.getItem('token');  // Fetch the token from localStorage
    return axios.delete(`${API_URL}/${userId}/saved-properties/${propertyId}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        },
        params: { userId, propertyId }
    });
};

export const getCities = () => {
    return axios.get(`${API_URL}/cities`);
};

export const getPropertyTypes = () => {
    return axios.get(`${API_URL}/propertyTypes`)
}


export const searchProperties = async (keyword, filters) => {
    const { pageNumber, pageSize, sortBy, sortDir, ...otherFilters } = filters;
    const params = new URLSearchParams({
        keyword,
        pageNumber,
        pageSize,
        sortBy,
        sortDir,
        ...otherFilters
    }).toString();
    try {
        const response = await axios.get(`${API_URL}/search-filter?${params}`);
        return response;
    } catch (error) {
        console.error('Failed to search properties', error);
        throw error;
    }
};

