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

export const latestOrder = async()=>{
    return await API.get('/orders/latest' );
}

export const createOrder = async(order)=>{
    return await API.post('/orders', order);
}

export const deleteOrder = async(orderId)=>{
    return await API.delete(`/orders/${orderId}`);
}