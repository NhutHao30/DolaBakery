import axios from 'axios';

const API_URL = 'http://localhost:8080/api/customers';

export const getCustomers = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const getCustomerById = async (id) => {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
};

export const createCustomer = async (customer) => {
    const response = await axios.post(API_URL, customer);
    return response.data;
};

export const updateCustomer = async (id, customer) => {
    const response = await axios.put(`${API_URL}/${id}`, customer);
    return response.data;
};

export const deleteCustomer = async (id) => {
    const response = await axios.delete(`${API_URL}/${id}`);
    return response.data;
};
