import axios from 'axios';

const API_URL = 'http://localhost:8080/api/employees';

// Quan trọng: Bật withCredentials để gửi kèm session cookie nếu backend yêu cầu
axios.defaults.withCredentials = true;

export const getEmployees = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const getEmployeeById = async (id) => {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
};

export const createEmployee = async (employeeData) => {
    const response = await axios.post(API_URL, employeeData);
    return response.data;
};

export const updateEmployee = async (id, employeeData) => {
    const response = await axios.put(`${API_URL}/${id}`, employeeData);
    return response.data;
};

export const deleteEmployee = async (id) => {
    const response = await axios.delete(`${API_URL}/${id}`);
    return response.data;
};
