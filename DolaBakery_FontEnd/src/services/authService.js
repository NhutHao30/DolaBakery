import axios from "axios";

// Cấu hình axios để gửi cookie/session id cho backend
axios.defaults.withCredentials = true;

const API_URL = "http://localhost:8080/api/auth";

export const login = async (username, password) => {
  const response = await axios.post(`${API_URL}/login`, { username, password });
  return response.data;
};

export const register = async (userData) => {
  const response = await axios.post(`${API_URL}/register`, userData);
  return response.data;
};

export const getCurrentUser = async () => {
  const response = await axios.get(`${API_URL}/me`);
  return response.data;
};

export const logout = async () => {
  const response = await axios.post(`${API_URL}/logout`);
  return response.data;
};

export const getGoogleStatus = async () => {
  const response = await axios.get(`${API_URL}/google-status`);
  return response.data;
};

export const googleRegister = async (sdt, gioiTinh) => {
  const response = await axios.post(`${API_URL}/google-register`, { sdt, gioiTinh });
  return response.data;
};

export const forgotPassword = async (email) => {
  const response = await axios.post(`${API_URL}/forgot-password`, { email });
  return response.data;
};

export const resetPassword = async (email, otp, newPassword) => {
  const response = await axios.post(`${API_URL}/reset-password`, { email, otp, newPassword });
  return response.data;
};
