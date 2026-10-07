import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080/api/v1.0",
});

// Attach token automatically
API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token"); // or sessionStorage
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const addCategory = async(category)=>{
    return await API.post('/admin/categories', category, {
        headers: {'Content-Type': 'multipart/form-data'}
      }
    );
}

export const deleteCategory = async(categoryId)=>{
    return await API.delete(`/admin/categories/${categoryId}`);
}

export const fetchCategories = async()=>{
    return await API.get('/categories');
}

