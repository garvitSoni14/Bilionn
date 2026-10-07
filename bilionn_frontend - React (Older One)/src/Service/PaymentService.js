import axios from "axios";

const API = axios.create({
    baseURL: "http://localhost:8080/api/v1.0",
});

API.interceptors.request.use((config) => {
    const token = localStorage.getItem("token"); // or sessionStorage
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const createRazorpayOrder = async(data)=>{
    return await API.post('/payments/create-order', data);
}

export const verifyPayment = async(paymentData)=>{
    return await API.post('/payments/verify', paymentData);
}