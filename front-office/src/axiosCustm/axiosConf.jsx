// axiosConf.js
import axios from 'axios';
import config from '../Config';

// Créer une instance personnalisée d'axios
const createAxiosInstance = (navigate) => {
  const axiosInstance = axios.create({
    baseURL: config.baseURL, // URL de votre backend
    withCredentials: true // Permettre l'envoi des cookies si nécessaire
  });

  return axiosInstance;
};

export default createAxiosInstance;