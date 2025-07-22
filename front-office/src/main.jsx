import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './index.css'

import App from './App.jsx'
import LoginPage from './pages/LoginPage'
import Dashboard from './pages/Dashboard'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/login" element={<LoginPage />} />
        {/* ✅ AJOUT : Route avec paramètre pour le code portail */}
        <Route path="/portail/:portailCode" element={<Dashboard />} />
        {/* Garde l'ancienne route pour compatibilité */}
        <Route path="/dashboard" element={<Dashboard />} />
        {/* Route par défaut pour les URLs non trouvées */}
        <Route path="*" element={<LoginPage />} />
      </Routes>
    </Router>    
  </StrictMode>
)