// axiosConf.js
import axios from 'axios';
import config from '../Config';

// Créer une instance personnalisée d'axios
const createAxiosInstance = (navigate) => {
  const axiosInstance = axios.create({
    baseURL: config.baseURL, // URL de votre backend
    withCredentials: true // Permettre l'envoi des cookies si nécessaire
  });

  

  // Intercepter chaque requête
 /* axiosInstance.interceptors.request.use(
    (config) => {
      const token = sessionStorage.getItem('token');
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  // Intercepter les réponses
  axiosInstance.interceptors.response.use(
    (response) => {
      return response;
    },
    (error) => {
      if (error.response) {
        // Gérer l'erreur 401 (non authentifié) ou 500 (erreur serveur)
        if (error.response.status === 401 || error.response.status === 500) {
          console.log("Erreur d'authentification ou serveur:", error.response.status);
          sessionStorage.removeItem('token');
          navigate('/Signin');
        }
      }
      return Promise.reject(error);
    }
  );*/

  return axiosInstance;
};

export default createAxiosInstance;