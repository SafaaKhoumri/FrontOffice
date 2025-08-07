// LoginPage.js - Version propre sans boutons de test
import React, { useState, useEffect } from 'react';
import { Eye, EyeOff, User, Lock, LogIn, Sparkles, Shield, ArrowRight, AlertCircle } from 'lucide-react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import config from '../Config';

const LoginPage = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [focusedField, setFocusedField] = useState('');
  const [error, setError] = useState('');
  const [mousePosition, setMousePosition] = useState({ x: 50, y: 50 });
  const [particles, setParticles] = useState([]);
  
  const navigate = useNavigate();

  // Configuration axios
  const apiClient = axios.create({
    baseURL: 'http://localhost:8087/api/frontoffice',
    withCredentials: true
  });

  useEffect(() => {
    // Vérifier s'il y a déjà un token valide
    const token = localStorage.getItem('frontoffice_token');
    if (token) {
      verifyTokenAndRedirect(token);
    }

    // Générer les particules d'animation
    const newParticles = Array.from({ length: 20 }, (_, i) => ({
      id: i,
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * 4 + 2,
      duration: Math.random() * 10 + 10,
      delay: Math.random() * 5
    }));
    setParticles(newParticles);
  }, []);
  useEffect(() => {
    console.log(" config :",config.baseURL);
  }, []);
  const verifyTokenAndRedirect = async (token) => {
    try {
      const response = await apiClient.get('/auth/verify', {
        headers: { 'Authorization': token }
      });
      
      if (response.data.valid) {
        const user = response.data.user;
        const portailCode = getPortailCodeByRole(user.roleCode);
        navigate(`/portail/${portailCode}`);
      }
    } catch (error) {
      // Token invalide, on reste sur la page de login
      localStorage.removeItem('frontoffice_token');
    }
  };

  const getPortailCodeByRole = (roleCode) => {
    const portailMapping = {
      'TRANSITAIRE': 'TRANS_001',
      'IMPORTATEUR': 'IMP_001',
      'EXPORTATEUR': 'EXP_001'
    };
    return portailMapping[roleCode] || 'TRANS_001';
  };

  const handleMouseMove = (e) => {
    const rect = e.currentTarget.getBoundingClientRect();
    setMousePosition({
      x: ((e.clientX - rect.left) / rect.width) * 100,
      y: ((e.clientY - rect.top) / rect.height) * 100
    });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    // Effacer l'erreur quand l'utilisateur tape
    if (error) setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      // Validation côté client
      if (!formData.email || !formData.password) {
        setError('Veuillez remplir tous les champs');
        setIsLoading(false);
        return;
      }

      console.log('Tentative de connexion pour:', formData.email , formData.password);

      // Appel API de connexion
      const response = await axios.post('http://localhost:8087/api/frontoffice/auth/login', {
        email: formData.email,
        password: formData.password
      },
      {
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true 
      },
    ).then(response =>{
      console.log('Réponse du serveur:', response.data)
      localStorage.setItem('frontoffice_token', response.data.token);
        localStorage.setItem('user_info', JSON.stringify(response.data.user));
        localStorage.setItem('portail_code', response.data.portailCode);
         // Rediriger vers le portail approprié
        navigate(response.data.redirectUrl);
    } );

      

     

    } catch (error) {
      console.error('Erreur de connexion:', error);
      
      if (error.response) {
        console.log('Status:', error.response.status);
        console.log('Data:', error.response.data);
        
        if (error.response.data && error.response.data.message) {
          setError(error.response.data.message);
        } else if (error.response.status === 404) {
          setError('Service non disponible. Vérifiez que le serveur est démarré.');
        } else if (error.response.status === 400) {
          setError('Email ou mot de passe incorrect');
        } else {
          setError('Erreur du serveur. Veuillez réessayer.');
        }
      } else if (error.request) {
        setError('Impossible de contacter le serveur. Vérifiez votre connexion.');
      } else {
        setError('Erreur de connexion. Veuillez réessayer.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const containerStyle = {
    minHeight: '100vh',
    position: 'relative',
    overflow: 'hidden',
    background: `
      radial-gradient(circle at ${mousePosition.x}% ${mousePosition.y}%, rgba(59, 130, 246, 0.3) 0%, transparent 50%),
      linear-gradient(135deg, #0f172a 0%, #1e293b 25%, #334155 50%, #475569 75%, #64748b 100%)
    `,
    fontFamily: 'system-ui, -apple-system, sans-serif'
  };

  const cardStyle = {
    position: 'relative',
    background: 'rgba(255, 255, 255, 0.1)',
    backdropFilter: 'blur(20px)',
    borderRadius: '24px',
    boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
    border: '1px solid rgba(255, 255, 255, 0.2)',
    padding: '32px',
    transition: 'all 0.5s ease',
    maxWidth: '28rem',
    width: '100%'
  };

  const inputStyle = (focused) => ({
    width: '100%',
    paddingLeft: '48px',
    paddingRight: '16px',
    paddingTop: '16px',
    paddingBottom: '16px',
    background: 'rgba(255, 255, 255, 0.1)',
    border: `1px solid ${focused ? '#60a5fa' : 'rgba(255, 255, 255, 0.2)'}`,
    borderRadius: '16px',
    color: 'white',
    fontSize: '16px',
    outline: 'none',
    transition: 'all 0.3s ease',
    backdropFilter: 'blur(8px)'
  });

  const buttonStyle = {
    width: '100%',
    padding: '16px 24px',
    borderRadius: '16px',
    fontWeight: '600',
    color: 'white',
    background: 'linear-gradient(45deg, #3b82f6, #8b5cf6, #ec4899)',
    border: 'none',
    cursor: formData.email && formData.password && !isLoading ? 'pointer' : 'not-allowed',
    opacity: formData.email && formData.password && !isLoading ? 1 : 0.5,
    transition: 'all 0.3s ease',
    position: 'relative',
    overflow: 'hidden'
  };

  return (
    <>
      <style>{`
        @keyframes float {
          0%, 100% { transform: translateY(0px) rotate(0deg); }
          50% { transform: translateY(-20px) rotate(180deg); }
        }
        @keyframes pulse {
          0%, 100% { opacity: 1; }
          50% { opacity: 0.5; }
        }
        @keyframes bounce {
          0%, 100% { transform: translateY(0); }
          50% { transform: translateY(-25%); }
        }
        @keyframes spin {
          from { transform: rotate(0deg); }
          to { transform: rotate(360deg); }
        }
        .login-card:hover {
          transform: scale(1.02) !important;
        }
        .login-button:hover:not(:disabled) {
          transform: scale(1.05);
          box-shadow: 0 0 30px rgba(59, 130, 246, 0.5);
        }
      `}</style>
      
      <div style={containerStyle} onMouseMove={handleMouseMove}>
        {/* Particules animées */}
        <div style={{ position: 'absolute', inset: 0, pointerEvents: 'none' }}>
          {particles.map((particle) => (
            <div key={particle.id} style={{
              position: 'absolute',
              left: `${particle.x}%`,
              top: `${particle.y}%`,
              width: `${particle.size}px`,
              height: `${particle.size}px`,
              borderRadius: '50%',
              background: 'linear-gradient(45deg, #60a5fa, #a855f7)',
              opacity: 0.2,
              animation: `float ${particle.duration}s ease-in-out infinite ${particle.delay}s alternate`
            }} />
          ))}
        </div>

        {/* Grille de fond */}
        <div style={{
          position: 'absolute',
          inset: 0,
          opacity: 0.1,
          backgroundImage: `
            linear-gradient(rgba(255,255,255,0.1) 1px, transparent 1px),
            linear-gradient(90deg, rgba(255,255,255,0.1) 1px, transparent 1px)
          `,
          backgroundSize: '50px 50px'
        }} />

        {/* Conteneur principal */}
        <div style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '100vh',
          padding: '16px',
          position: 'relative',
          zIndex: 10
        }}>
          <div className="login-card" style={cardStyle}>
            {/* Effet de lueur */}
            <div style={{
              position: 'absolute',
              inset: '-4px',
              background: 'linear-gradient(45deg, #3b82f6, #8b5cf6, #ec4899)',
              borderRadius: '24px',
              filter: 'blur(12px)',
              opacity: 0.25,
              zIndex: -1
            }} />
            
            {/* En-tête */}
            <div style={{ textAlign: 'center', marginBottom: '32px' }}>
              <div style={{ position: 'relative', display: 'inline-block', marginBottom: '24px' }}>
                <div style={{
                  position: 'absolute',
                  inset: 0,
                  background: 'linear-gradient(45deg, #3b82f6, #8b5cf6)',
                  borderRadius: '50%',
                  filter: 'blur(16px)',
                  opacity: 0.5,
                  animation: 'pulse 2s infinite'
                }} />
                <div style={{
                  position: 'relative',
                  width: '80px',
                  height: '80px',
                  background: 'linear-gradient(45deg, #3b82f6, #8b5cf6)',
                  borderRadius: '50%',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)'
                }}>
                  <Shield size={40} color="white" style={{ animation: 'pulse 2s infinite' }} />
                </div>
                <Sparkles 
                  size={24} 
                  color="#fbbf24"
                  style={{
                    position: 'absolute',
                    top: '-8px',
                    right: '-8px',
                    animation: 'bounce 1s infinite'
                  }}
                />
              </div>
              
              <h1 style={{
                fontSize: '2.5rem',
                fontWeight: 'bold',
                background: 'linear-gradient(45deg, white, #bfdbfe, #ddd6fe)',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
                marginBottom: '8px'
              }}>
                Connexion
              </h1>
              <p style={{ color: 'rgba(255, 255, 255, 0.7)', fontSize: '1.125rem' }}>
                Accédez à votre portail métier
              </p>
            </div>

            {/* Message d'erreur */}
            {error && (
              <div style={{
                background: 'rgba(239, 68, 68, 0.1)',
                border: '1px solid rgba(239, 68, 68, 0.3)',
                borderRadius: '12px',
                padding: '12px',
                marginBottom: '24px',
                display: 'flex',
                alignItems: 'center',
                color: '#fca5a5'
              }}>
                <AlertCircle size={20} style={{ marginRight: '8px', flexShrink: 0 }} />
                <span style={{ fontSize: '14px' }}>{error}</span>
              </div>
            )}

            {/* Formulaire */}
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
              {/* Email */}
              <div>
                <label style={{
                  display: 'block',
                  color: 'rgba(255, 255, 255, 0.9)',
                  fontSize: '14px',
                  fontWeight: '500',
                  marginBottom: '12px'
                }}>
                  Email
                </label>
                <div style={{ position: 'relative' }}>
                  <User
                    size={20}
                    style={{
                      position: 'absolute',
                      left: '16px',
                      top: '50%',
                      transform: 'translateY(-50%)',
                      color: focusedField === 'email' ? '#60a5fa' : 'rgba(255, 255, 255, 0.5)',
                      transition: 'color 0.3s ease'
                    }}
                  />
                  <input
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                    onFocus={() => setFocusedField('email')}
                    onBlur={() => setFocusedField('')}
                    style={inputStyle(focusedField === 'email')}
                    placeholder="Entrez votre email"
                    disabled={isLoading}
                    autoComplete="email"
                  />
                </div>
              </div>

              {/* Mot de passe */}
              <div>
                <label style={{
                  display: 'block',
                  color: 'rgba(255, 255, 255, 0.9)',
                  fontSize: '14px',
                  fontWeight: '500',
                  marginBottom: '12px'
                }}>
                  Mot de passe
                </label>
                <div style={{ position: 'relative' }}>
                  <Lock
                    size={20}
                    style={{
                      position: 'absolute',
                      left: '16px',
                      top: '50%',
                      transform: 'translateY(-50%)',
                      color: focusedField === 'password' ? '#a855f7' : 'rgba(255, 255, 255, 0.5)',
                      transition: 'color 0.3s ease'
                    }}
                  />
                  <input
                    name="password"
                    type={showPassword ? "text" : "password"}
                    value={formData.password}
                    onChange={handleChange}
                    onFocus={() => setFocusedField('password')}
                    onBlur={() => setFocusedField('')}
                    style={{...inputStyle(focusedField === 'password'), paddingRight: '48px'}}
                    placeholder="Entrez votre mot de passe"
                    disabled={isLoading}
                    autoComplete="current-password"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    style={{
                      position: 'absolute',
                      right: '16px',
                      top: '50%',
                      transform: 'translateY(-50%)',
                      background: 'none',
                      border: 'none',
                      color: 'rgba(255, 255, 255, 0.5)',
                      cursor: 'pointer',
                      transition: 'color 0.2s ease'
                    }}
                  >
                    {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                  </button>
                </div>
              </div>

              {/* Bouton de connexion */}
              <button
                type="submit"
                disabled={isLoading || !formData.email || !formData.password}
                className="login-button"
                style={buttonStyle}
              >
                <div style={{
                  position: 'relative',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  zIndex: 1
                }}>
                  {isLoading ? (
                    <>
                      <div style={{
                        width: '24px',
                        height: '24px',
                        border: '2px solid white',
                        borderTop: '2px solid transparent',
                        borderRadius: '50%',
                        animation: 'spin 1s linear infinite',
                        marginRight: '12px'
                      }} />
                      <span>Connexion...</span>
                    </>
                  ) : (
                    <>
                      <LogIn size={20} style={{ marginRight: '8px' }} />
                      <span>Se connecter</span>
                      <ArrowRight size={20} style={{ marginLeft: '8px' }} />
                    </>
                  )}
                </div>
              </button>
            </form>

            {/* Pied de page */}
            <div style={{ marginTop: '32px', textAlign: 'center' }}>
              <p style={{ color: 'rgba(255, 255, 255, 0.4)', fontSize: '12px' }}>
                © 2025 PortNet S.A. Tous droits réservés.
              </p>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default LoginPage;