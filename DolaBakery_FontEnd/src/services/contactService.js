import axios from 'axios';

axios.defaults.withCredentials = true;

const API_URL = 'http://localhost:8080/api/lien-he';

export const sendContactEmail = async (contactData) => {
    try {
        const response = await axios.post(`${API_URL}/send`, contactData);
        return response.data;
    } catch (error) {
        if (error.response && error.response.data) {
            throw new Error(error.response.data);
        }
        throw new Error('Đã xảy ra lỗi khi gửi yêu cầu. Vui lòng thử lại sau.');
    }
};
