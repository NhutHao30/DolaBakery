import axios from 'axios';
axios.defaults.withCredentials = true;

const API_URL = 'http://localhost:8080/api/wishlist';

export const getWishlist = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const addToWishlist = async (masp) => {
    const response = await axios.post(API_URL, { masp });
    return response.data;
};

export const removeFromWishlist = async (masp) => {
    const response = await axios.delete(`${API_URL}/${masp}`);
    return response.data;
};
