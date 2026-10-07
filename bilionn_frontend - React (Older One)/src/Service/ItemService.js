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

export const addItem = async(item)=>{
    return await API.post('/admin/items', item,{
        headers: {'Content-Type': 'multipart/form-data'}
    });
}

export const deleteItem = async(itemId)=>{
    return await API.delete(`/admin/items/${itemId}`);
}

export const fetchItems = async()=>{
    return await API.get('/items');
}