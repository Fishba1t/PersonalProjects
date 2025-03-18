import axios from "axios";

// Define your API base URL here
export const baseUrl = "http://localhost:8080"; // Update the base URL for your backend

// Add Event
export const addEvent = async (url, eventData) => {
    return await axios.post(url, eventData);
};

// Other existing functions
export const registerUser = async (userData) => {
    return await axios.post(`${baseUrl}/api/user/register`, userData);
};

export const loginUser = async (credentials) => {
    return await axios.post(`${baseUrl}/api/user/login`, credentials);
};

export const getAllUsers = async (token) => {
    return await axios.get(`${baseUrl}/api/user/all`, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });
};

export const updatePassword = async (token, passwordData) => {
    return await axios.put(`${baseUrl}/api/user/newPassword`, passwordData, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });
};
