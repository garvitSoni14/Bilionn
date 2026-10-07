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
export const addUser = async(user)=>{
    return await API.post('/admin/register', user);
}

export const deleteUser = async(userId)=>{
    return await API.delete(`/admin/users/${userId}`);
}
export const fetchUsers = async()=>{
    return await API.get('/admin/users');
}