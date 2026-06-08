import axios from 'axios';
axios.defaults.withCredentials = true;

const API_URL = 'http://localhost:8080/api/cart';

export const getCart = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const addToCart = async (masp, soluong = 1) => {
    const response = await axios.post(API_URL, { masp, soluong });
    return response.data;
};

export const updateCartQuantity = async (masp, soluong) => {
    const response = await axios.put(`${API_URL}/${masp}`, { masp, soluong });
    return response.data;
};

export const removeFromCart = async (masp) => {
    const response = await axios.delete(`${API_URL}/${masp}`);
    return response.data;
};

export const checkout = async (checkoutData = {}) => {
    const response = await axios.post(`${API_URL}/checkout`, checkoutData);
    return response.data;
};
