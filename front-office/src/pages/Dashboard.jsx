import React, { useState, useEffect } from 'react';
import { 
  Menu, 
  Bell, 
  Search, 
  User, 
  Settings, 
  LogOut, 
  Home,
  FileText,
  BarChart3,
  Users,
  Package,
  Truck,
  Ship,
  Plane,
  ChevronDown,
  ChevronRight,
  RefreshCw,
  Shield,
  Globe,
  AlertTriangle,
  Eye,
  Calendar,
  MapPin,
  ExternalLink,
  ArrowLeft,
  AlertCircle,
  Construction
} from 'lucide-react';
import { useAxiosInstance } from '../hook/useAxiosInstance';

const Dashboard = () => {
  // Récupération du code portail depuis l'URL
  const portailCode = window.location.pathname.split('/').pop() || 'IMP_001';

  const axiosInstance = useAxiosInstance();
  
  // États pour l'interface
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [activeMenu, setActiveMenu] = useState('dashboard');
  const [expandedMenus, setExpandedMenus] = useState({});
  const [userMenuOpen, setUserMenuOpen] = useState(false);
  
  // ⭐ NOUVEAUX ÉTATS POUR L'IFRAME ET MESSAGES
  const [currentUrl, setCurrentUrl] = useState(null);
  const [currentMenuTitle, setCurrentMenuTitle] = useState('');
  const [currentMenuData, setCurrentMenuData] = useState(null);
  const [showIframe, setShowIframe] = useState(false);
  const [showEmptyUrlMessage, setShowEmptyUrlMessage] = useState(false);
  const [iframeLoading, setIframeLoading] = useState(false);
  
  // États pour les données RÉELLES
  const [portailData, setPortailData] = useState(null);
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fonction pour récupérer les VRAIES données du portail
  const fetchPortailData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const token = localStorage.getItem('frontoffice_token');
      /*if (!token) {
        setError('Token manquant - Redirection vers login nécessaire');
        return;
      }*/

      console.log('🔄 Chargement des données du portail:', portailCode);

      const response = await axiosInstance.get(`/api/frontoffice/portail/${portailCode}`).then(res => {
        console.log('📥 Données du portail récupérées:', res.data);
        setPortailData(res.data);})
;

      
      
      // Récupérer les données utilisateur
      const userInfo = localStorage.getItem('user_info');
      if (userInfo) {
        const parsedUser = JSON.parse(userInfo);
        setUserData(parsedUser);
        console.log('👤 Données utilisateur:', parsedUser);
      }
      
    } catch (err) {
      console.error('❌ Erreur lors du chargement du portail:', err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    console.log('🎯 Dashboard monté pour le portail:', portailCode);
    
    if (portailCode) {
      fetchPortailData();
    }
  }, [portailCode]);

  // Fonction de déconnexion
  const handleLogout = () => {
    localStorage.removeItem('frontoffice_token');
    localStorage.removeItem('user_info');
    localStorage.removeItem('portail_code');
    
    console.log('🚪 Déconnexion effectuée');
    window.location.href = '/login';
  };

  // Fonction pour obtenir l'icône d'un menu
  const getMenuIcon = (menuCode) => {
    const iconMap = {
      'DASHBOARD': Home,
      'IMPORT': Ship,
      'EXPORT': Plane,
      'TRANSPORT': Truck,
      'DOCUMENTS': FileText,
      'REPORTS': BarChart3,
      'USERS': Users,
      'PROFILE': User,
      'SETTINGS': Settings,
      'OPERATIONS': Package,
      'TRACKING': MapPin,
      'CALENDAR': Calendar
    };
    
    return iconMap[menuCode?.toUpperCase()] || FileText;
  };

  const toggleMenu = (menuId) => {
    setExpandedMenus(prev => ({
      ...prev,
      [menuId]: !prev[menuId]
    }));
  };

  // ⭐ FONCTION POUR VÉRIFIER SI UNE URL EST VIDE OU NON IMPLÉMENTÉE
  const isEmptyOrNotImplemented = (url) => {
    if (!url || url.trim() === '') return true;
    
    // Vérifier si c'est une URL relative qui commence par / mais qui n'est pas complète
    if (url.startsWith('/') && !url.includes('https')) {
      return true;
    }
    
    return false;
  };

// 1. Modifier la fonction handleMenuClick pour gérer la nouvelle structure
const handleMenuClick = (menuItem) => {
  setActiveMenu(menuItem.id || menuItem.code);
  setCurrentMenuData(menuItem);
  
  //Vérifier les sous_menus ET menus
  const hasSubMenus = (menuItem.sous_menus && menuItem.sous_menus.length > 0) || 
                     (menuItem.menus && menuItem.menus.length > 0);
  
  if (hasSubMenus) {
    // Si c'est un groupe de menus, toggle l'expansion
    toggleMenu(menuItem.id || menuItem.code);
    return;
  }
  
  const menuTitle = menuItem.title || menuItem.nom;
  const menuUrl = menuItem.url;
  
  // Vérifier si l'URL est vide ou non implémentée
  if (isEmptyOrNotImplemented(menuUrl)) {
    console.log('⚠️ URL vide ou non implémentée pour le menu:', menuTitle);
    setCurrentMenuTitle(menuTitle);
    setCurrentUrl(menuUrl);
    setShowEmptyUrlMessage(true);
    setShowIframe(false);
  } else {
    // URL valide, afficher dans l'iframe
    console.log('🌐 Navigation vers URL:', menuUrl, 'pour le menu:', menuTitle);
    setCurrentUrl(menuUrl);
    setCurrentMenuTitle(menuTitle);
    setShowIframe(true);
    setShowEmptyUrlMessage(false);
    setIframeLoading(true);
  }
  
  console.log('📍 Menu cliqué:', menuItem);
};

  // FONCTION POUR RETOURNER AU DASHBOARD
  const handleBackToDashboard = () => {
    setShowIframe(false);
    setShowEmptyUrlMessage(false);
    setCurrentUrl(null);
    setCurrentMenuTitle('');
    setCurrentMenuData(null);
    setActiveMenu('dashboard');
  };

  // ⭐ FONCTION POUR GÉRER LE CHARGEMENT DE L'IFRAME
  const handleIframeLoad = () => {
    setIframeLoading(false);
    console.log('✅ Iframe chargée pour:', currentUrl);
  };

  const handleIframeError = () => {
    setIframeLoading(false);
    console.log('❌ Erreur de chargement iframe pour:', currentUrl);
  };

  // États de chargement et d'erreur
  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <RefreshCw size={48} className="text-blue-500 animate-spin mx-auto mb-4" />
          <p className="text-gray-600 text-lg">Chargement du portail...</p>
          <p className="text-sm text-gray-400 mt-2">Code: {portailCode}</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center max-w-md">
          <AlertTriangle size={48} className="text-red-500 mx-auto mb-4" />
          <h2 className="text-xl font-semibold text-gray-900 mb-2">Erreur de chargement</h2>
          <p className="text-gray-600 mb-6">{error}</p>
          <div className="space-x-4">
            <button 
              onClick={fetchPortailData}
              className="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
            >
              Réessayer
            </button>
            <button 
              onClick={handleLogout}
              className="px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors"
            >
              Se déconnecter
            </button>
          </div>
        </div>
      </div>
    );
  }

  if (!portailData) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <Globe size={48} className="text-gray-400 mx-auto mb-4" />
          <h2 className="text-xl font-semibold text-gray-900 mb-2">Portail non trouvé</h2>
          <p className="text-gray-600 mb-4">Le portail {portailCode} n'existe pas.</p>
          <button 
            onClick={handleLogout}
            className="px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors"
          >
            Retour au login
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      {/* Sidebar avec données RÉELLES */}
      <div className={`bg-white shadow-lg transition-all duration-300 ${sidebarOpen ? 'w-64' : 'w-16'}`}>
        {/* En-tête du portail */}
        <div className="flex items-center p-4 border-b border-gray-200">
          <div 
            className="w-8 h-8 rounded-lg flex items-center justify-center"
            style={{ 
              background: portailData.couleurTheme || '#3B82F6'
            }}
          >
            <Shield size={20} className="text-white" />
          </div>
          {sidebarOpen && (
            <div className="ml-3">
              <h1 className="font-semibold text-gray-900 text-sm">{portailData.nom}</h1>
              <p className="text-xs text-gray-500">{portailData.code}</p>
            </div>
          )}
        </div>

        {/* Navigation avec VRAIES données */}
        <nav className="mt-6 px-3">
          {/* Dashboard */}
          <button
            onClick={() => handleBackToDashboard()}
            className={`w-full flex items-center px-3 py-2 rounded-lg text-left transition-colors mb-1 ${
              activeMenu === 'dashboard'
                ? 'bg-blue-50 text-blue-700 border-r-2 border-blue-700' 
                : 'text-gray-700 hover:bg-gray-100'
            }`}
          >
            <Home size={20} />
            {sidebarOpen && <span className="ml-3 font-medium">Tableau de bord</span>}
          </button>

          {/* Groupes de menus RÉELS - STRUCTURE CORRIGÉE */}
{portailData.groupMenu && portailData.groupMenu.map((group) => {
  const isExpanded = expandedMenus[group.id];
  
  //Gérer sous_menus de l'API
  const subMenus = group.sous_menus || group.menus || [];
  const hasMenus = subMenus && subMenus.length > 0;

  return (
    <div key={group.id} className="mb-1">
      <button
        onClick={() => handleMenuClick(group)}
        className="w-full flex items-center px-3 py-2 rounded-lg text-left transition-colors text-gray-700 hover:bg-gray-100"
      >
        <Package size={20} />
        {sidebarOpen && (
          <>
            <span className="ml-3 font-medium">{group.nom || group.title}</span>
            {hasMenus && (
              <div className="ml-auto">
                {isExpanded ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
              </div>
            )}
          </>
        )}
      </button>

      {/* FONCTION RÉCURSIVE pour afficher les sous-menus */}
      {hasMenus && isExpanded && sidebarOpen && (
        <div className="ml-6 mt-1 space-y-1">
          {subMenus.map((menu) => {
            // Gérer les menus et groupes imbriqués
            if (menu.type === 'groupMenu') {
              // C'est un sous-groupe
              const isSubExpanded = expandedMenus[menu.id];
              const subSubMenus = menu.sous_menus || [];
              
              return (
                <div key={menu.id}>
                  <button
                    onClick={() => handleMenuClick(menu)}
                    className={`w-full flex items-center px-3 py-2 rounded-lg text-left text-sm transition-colors ${
                      activeMenu === menu.id
                        ? 'bg-blue-50 text-blue-700'
                        : 'text-gray-600 hover:bg-gray-50'
                    }`}
                  >
                    <Package size={16} />
                    <span className="ml-3 font-medium">{menu.nom || menu.title}</span>
                    {subSubMenus.length > 0 && (
                      <div className="ml-auto">
                        {isSubExpanded ? <ChevronDown size={14} /> : <ChevronRight size={14} />}
                      </div>
                    )}
                  </button>
                  
                  {/* Menus du sous-groupe */}
                  {subSubMenus.length > 0 && isSubExpanded && (
                    <div className="ml-6 mt-1 space-y-1">
                      {subSubMenus.map((subMenu) => {
                        const MenuIcon = getMenuIcon(subMenu.code);
                        const hasValidUrl = !isEmptyOrNotImplemented(subMenu.url);
                        
                        return (
                          <button
                            key={subMenu.id}
                            onClick={() => handleMenuClick(subMenu)}
                            className={`w-full flex items-center px-3 py-2 rounded-lg text-left text-xs transition-colors ${
                              activeMenu === subMenu.id
                                ? 'bg-blue-50 text-blue-700'
                                : 'text-gray-600 hover:bg-gray-50'
                            }`}
                          >
                            <MenuIcon size={14} />
                            <span className="ml-3">{subMenu.nom || subMenu.title}</span>
                            {hasValidUrl ? (
                              <ExternalLink size={10} className="ml-auto text-gray-400" />
                            ) : (
                              <Construction size={10} className="ml-auto text-orange-400" />
                            )}
                          </button>
                        );
                      })}
                    </div>
                  )}
                </div>
              );
            } else {
              // C'est un menu simple
              const MenuIcon = getMenuIcon(menu.code);
              const hasValidUrl = !isEmptyOrNotImplemented(menu.url);
              
              return (
                <button
                  key={menu.id}
                  onClick={() => handleMenuClick(menu)}
                  className={`w-full flex items-center px-3 py-2 rounded-lg text-left text-sm transition-colors ${
                    activeMenu === menu.id
                      ? 'bg-blue-50 text-blue-700'
                      : 'text-gray-600 hover:bg-gray-50'
                    }`}
                >
                  <MenuIcon size={16} />
                  <span className="ml-3">{menu.nom || menu.title}</span>
                  {hasValidUrl ? (
                    <ExternalLink size={12} className="ml-auto text-gray-400" />
                  ) : (
                    <Construction size={12} className="ml-auto text-orange-400" />
                  )}
                </button>
              );
            }
          })}
        </div>
      )}
    </div>
  );
})}

          {/* Menus directs RÉELS */}
          {portailData.menus && portailData.menus.map((menu) => {
            const MenuIcon = getMenuIcon(menu.code);
            const hasValidUrl = !isEmptyOrNotImplemented(menu.url);
            
            return (
              <div key={menu.id} className="mb-1">
                <button
                  onClick={() => handleMenuClick(menu)}
                  className={`w-full flex items-center px-3 py-2 rounded-lg text-left transition-colors ${
                    activeMenu === menu.id
                      ? 'bg-blue-50 text-blue-700 border-r-2 border-blue-700' 
                      : 'text-gray-700 hover:bg-gray-100'
                  }`}
                >
                  <MenuIcon size={20} />
                  {sidebarOpen && (
                    <>
                      <span className="ml-3 font-medium">{menu.nom}</span>
                      {hasValidUrl ? (
                        <ExternalLink size={12} className="ml-auto text-gray-400" />
                      ) : (
                        <Construction size={12} className="ml-auto text-orange-400" />
                      )}
                    </>
                  )}
                </button>
              </div>
            );
          })}
        </nav>
      </div>

      {/* Contenu principal */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Header */}
        <header className="bg-white shadow-sm border-b border-gray-200 px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <button
                onClick={() => setSidebarOpen(!sidebarOpen)}
                className="p-2 rounded-lg hover:bg-gray-100 transition-colors"
              >
                <Menu size={20} />
              </button>
              
              {/* ⭐ AFFICHAGE CONDITIONNEL DU TITRE */}
              {(showIframe || showEmptyUrlMessage) && currentMenuTitle ? (
                <div className="flex items-center space-x-3">
                  <button
                    onClick={handleBackToDashboard}
                    className="p-1 rounded hover:bg-gray-100 transition-colors"
                    title="Retour au tableau de bord"
                  >
                    <ArrowLeft size={18} className="text-gray-600" />
                  </button>
                  <h2 className="text-xl font-semibold text-gray-900">
                    {currentMenuTitle}
                  </h2>
                  {showIframe && currentUrl && (
                    <div className="flex items-center text-sm text-gray-500">
                      <ExternalLink size={14} className="mr-1" />
                      {currentUrl}
                    </div>
                  )}
                  {showEmptyUrlMessage && (
                    <div className="flex items-center text-sm text-orange-600">
                      <Construction size={14} className="mr-1" />
                      Non implémenté
                    </div>
                  )}
                </div>
              ) : (
                <h2 className="text-xl font-semibold text-gray-900">
                  {portailData.nom}
                </h2>
              )}
            </div>

            <div className="flex items-center space-x-4">
              <div className="relative">
                <Search size={20} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
                <input
                  type="text"
                  placeholder="Rechercher..."
                  className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              <button className="relative p-2 rounded-lg hover:bg-gray-100 transition-colors">
                <Bell size={20} />
              </button>

              {/* Menu utilisateur avec VRAIES données */}
              <div className="relative">
                <button
                  onClick={() => setUserMenuOpen(!userMenuOpen)}
                  className="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-100 transition-colors"
                >
                  <div 
                    className="w-8 h-8 rounded-full flex items-center justify-center"
                    style={{ 
                      background: portailData.couleurTheme || '#8B5CF6'
                    }}
                  >
                    <User size={16} className="text-white" />
                  </div>
                  <div className="text-left">
                    <p className="text-sm font-medium text-gray-900">
                      {userData?.nom || 'Utilisateur'}
                    </p>
                    <p className="text-xs text-gray-500">
                      {userData?.roleCode || 'Rôle'}
                    </p>
                  </div>
                  <ChevronDown size={16} />
                </button>

                {userMenuOpen && (
                  <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 py-1 z-50">
                    <div className="px-4 py-2 border-b border-gray-100">
                      <p className="text-xs text-gray-500">Connecté en tant que</p>
                      <p className="text-sm font-medium text-gray-900">{userData?.roleCode}</p>
                    </div>
                    <a href="#" className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                      <User size={16} className="mr-3" />
                      Mon Profil
                    </a>
                    <a href="#" className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                      <Settings size={16} className="mr-3" />
                      Paramètres
                    </a>
                    <hr className="my-1" />
                    <button 
                      onClick={handleLogout}
                      className="w-full flex items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                    >
                      <LogOut size={16} className="mr-3" />
                      Déconnexion
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>
        </header>

        {/* ⭐ CONTENU PRINCIPAL AVEC IFRAME ET MESSAGE D'URL VIDE */}
        <main className="flex-1 overflow-hidden">
          {showIframe && currentUrl ? (
            <div className="h-full relative">
              {/* Indicateur de chargement */}
              {iframeLoading && (
                <div className="absolute inset-0 bg-white bg-opacity-75 flex items-center justify-center z-10">
                  <div className="text-center">
                    <RefreshCw size={32} className="text-blue-500 animate-spin mx-auto mb-2" />
                    <p className="text-gray-600">Chargement de {currentMenuTitle}...</p>
                  </div>
                </div>
              )}
              
              {/* ⭐ IFRAME POUR AFFICHER LE CONTENU DU MENU */}
              <iframe
                src={currentUrl}
                title={currentMenuTitle}
                className="w-full h-full border-0"
                onLoad={handleIframeLoad}
                onError={handleIframeError}
                sandbox="allow-same-origin allow-scripts allow-forms allow-navigation"
              />
            </div>
          ) : showEmptyUrlMessage ? (
            /* ⭐ MESSAGE POUR URL VIDE OU NON IMPLÉMENTÉE */
            <div className="flex-1 flex items-center justify-center bg-gray-50">
              <div className="text-center max-w-lg mx-auto p-8">
                <div className="mb-6">
                  <Construction size={64} className="text-orange-400 mx-auto mb-4" />
                  <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                    Fonctionnalité en développement
                  </h2>
                </div>
                
                <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200 mb-6">
                  <div className="flex items-start space-x-3">
                    <AlertCircle size={20} className="text-orange-500 mt-0.5 flex-shrink-0" />
                    <div className="text-left">
                      <p className="text-gray-700 mb-2">
                        Le menu <strong>"{currentMenuTitle}"</strong> contient une URL{' '}
                        {!currentUrl || currentUrl.trim() === '' ? (
                          <span className="text-red-600 font-mono">vide</span>
                        ) : (
                          <>
                            relative <span className="text-blue-600 font-mono">({currentUrl})</span>
                          </>
                        )} qui n'est pas encore implémentée.
                      </p>
                      
                      {currentMenuData && (
                        <div className="mt-4 p-3 bg-gray-10 rounded border text-sm">
                          <h4 className="font-medium text-gray-900 mb-2">Informations du menu :</h4>
                          <div className="space-y-1 text-gray-600">
                            <div><span className="font-medium">Code:</span> {currentMenuData.code || 'N/A'}</div>
                            <div><span className="font-medium">ID:</span> {currentMenuData.id || 'N/A'}</div>
                            <div><span className="font-medium">URL:</span> {currentUrl || 'Vide'}</div>
                          </div>
                        </div>
                      )}
                    </div>
                  </div>
                </div>

                <div className="flex justify-center space-x-4">
                  <button
                    onClick={handleBackToDashboard}
                    className="flex items-center px-6 py-3 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
                  >
                    <ArrowLeft size={18} className="mr-2" />
                    Retour au tableau de bord
                  </button>
                  
                  <button
                    onClick={() => window.location.reload()}
                    className="flex items-center px-6 py-3 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors"
                  >
                    <RefreshCw size={18} className="mr-2" />
                    Actualiser
                  </button>
                </div>

                <div className="mt-6 text-sm text-gray-500">
                  <p>Cette fonctionnalité sera disponible dans une prochaine mise à jour.</p>
                </div>
              </div>
            </div>
          ) : (
            /* Contenu du dashboard par défaut */
            <div className="p-6 overflow-auto">
              {/* Message de bienvenue */}
              <div className="mb-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-2">
                  Bienvenue, {userData?.nom?.split(' ')[0] || 'Utilisateur'} ! 👋
                </h1>
                <p className="text-gray-600 text-lg">
                  {portailData.description || `Portail ${portailData.nom}`}
                </p>
              </div>

              {/* Informations du portail */}
              <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
                {/* Carte principale du portail */}
                <div className="lg:col-span-2">
                  <div 
                    className="rounded-xl p-8 text-white"
                    style={{ 
                      background: `linear-gradient(135deg, ${portailData.couleurTheme || '#3B82F6'}, ${portailData.couleurTheme ? portailData.couleurTheme + 'CC' : '#8B5CF6'})` 
                    }}
                  >
                    <div className="flex items-center justify-between mb-6">
                      <div>
                        <h2 className="text-2xl font-bold mb-2">{portailData.nom}</h2>
                        <p className="text-white opacity-90">
                          Code portail: <span className="font-mono">{portailData.code}</span>
                        </p>
                      </div>
                      <div className="text-right">
                        <div className="text-sm opacity-75">Statut</div>
                        <div className="text-lg font-semibold">
                          {portailData.actif ? '✅ Actif' : '❌ Inactif'}
                        </div>
                      </div>
                    </div>
                    
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <div className="text-sm opacity-75">Groupes de menus</div>
                        <div className="text-2xl font-bold">
                          {portailData.groupMenu?.length || 0}
                        </div>
                      </div>
                      <div>
                        <div className="text-sm opacity-75">Menus directs</div>
                        <div className="text-2xl font-bold">
                          {portailData.menus?.length || 0}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Informations utilisateur */}
                <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
                  <h3 className="text-lg font-semibold text-gray-900 mb-4">Votre Profil</h3>
                  <div className="space-y-4">
                    <div>
                      <div className="text-sm text-gray-500">Nom</div>
                      <div className="font-medium">{userData?.nom || 'N/A'}</div>
                    </div>
                    <div>
                      <div className="text-sm text-gray-500">Email</div>
                      <div className="font-medium text-sm">{userData?.email || 'N/A'}</div>
                    </div>
                    <div>
                      <div className="text-sm text-gray-500">Rôle</div>
                      <div className="font-medium">{userData?.roleCode || 'N/A'}</div>
                    </div>
                    <div>
                      <div className="text-sm text-gray-500">Dernière connexion</div>
                      <div className="font-medium">Aujourd'hui</div>
                    </div>
                  </div>
                </div>
              </div>

              {/* Menus disponibles */}
              <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                <div className="p-6 border-b border-gray-100">
                  <h3 className="text-lg font-semibold text-gray-900">Menus disponibles</h3>
                  <p className="text-gray-600 text-sm mt-1">Cliquez sur un menu pour accéder à ses fonctionnalités</p>
                </div>
                
                <div className="p-6">
                 {/* Groupes de menus dans le dashboard principal */}
{portailData.groupMenu && portailData.groupMenu.length > 0 && (
  <div className="mb-6">
    <h4 className="text-md font-medium text-gray-900 mb-3">Groupes de menus</h4>
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      {portailData.groupMenu.map((group) => {
        // 🔥 CORRECTION : Compter tous les menus récursivement
        const countAllMenus = (menuGroup) => {
          const subMenus = menuGroup.sous_menus || [];
          let count = 0;
          
          subMenus.forEach(subMenu => {
            if (subMenu.type === 'menu') {
              count++;
            } else if (subMenu.type === 'groupMenu') {
              count += countAllMenus(subMenu);
            }
          });
          
          return count;
        };
        
        const totalMenus = countAllMenus(group);
        
        return (
          <div key={group.id} className="border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow">
            <div className="flex items-center justify-between mb-2">
              <h5 className="font-medium text-gray-900">{group.nom || group.title}</h5>
              <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">
                {totalMenus} menus
              </span>
            </div>
            
            {/* Affichage récursif des menus */}
            <div className="space-y-1">
              {(group.sous_menus || []).map((menu) => {
                if (menu.type === 'groupMenu') {
                  return (
                    <div key={menu.id} className="ml-2">
                      <div className="text-sm font-medium text-gray-700 mb-1">
                        📁 {menu.nom || menu.title}
                      </div>
                      {(menu.sous_menus || []).map((subMenu) => {
                        const MenuIcon = getMenuIcon(subMenu.code);
                        const hasValidUrl = !isEmptyOrNotImplemented(subMenu.url);
                        return (
                          <button
                            key={subMenu.id}
                            onClick={() => handleMenuClick(subMenu)}
                            className="w-full flex items-center text-sm text-gray-600 hover:text-blue-600 hover:bg-blue-50 p-2 rounded transition-colors ml-4"
                          >
                            <MenuIcon size={14} className="mr-2" />
                            {subMenu.nom || subMenu.title}
                            {hasValidUrl ? (
                              <ExternalLink size={12} className="ml-auto" />
                            ) : (
                              <Construction size={12} className="ml-auto text-orange-400" />
                            )}
                          </button>
                        );
                      })}
                    </div>
                  );
                } else {
                  const MenuIcon = getMenuIcon(menu.code);
                  const hasValidUrl = !isEmptyOrNotImplemented(menu.url);
                  return (
                    <button
                      key={menu.id}
                      onClick={() => handleMenuClick(menu)}
                      className="w-full flex items-center text-sm text-gray-600 hover:text-blue-600 hover:bg-blue-50 p-2 rounded transition-colors"
                    >
                      <MenuIcon size={14} className="mr-2" />
                      {menu.nom || menu.title}
                      {hasValidUrl ? (
                        <ExternalLink size={12} className="ml-auto" />
                      ) : (
                        <Construction size={12} className="ml-auto text-orange-400" />
                      )}
                    </button>
                  );
                }
              })}
            </div>
          </div>
        );
      })}
    </div>
  </div>
)}
              {/* Menus directs */}
                  {portailData.menus && portailData.menus.length > 0 && (
                    <div>
                      <h4 className="text-md font-medium text-gray-900 mb-3">Menus directs</h4>
                      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-3">
                        {portailData.menus.map((menu) => {
                          const MenuIcon = getMenuIcon(menu.code);
                          const hasValidUrl = !isEmptyOrNotImplemented(menu.url);
                          return (
                            <button
                              key={menu.id}
                              onClick={() => handleMenuClick(menu)}
                              className="flex items-center p-3 border border-gray-200 rounded-lg hover:shadow-sm hover:bg-blue-50 hover:border-blue-200 transition-all"
                            >
                              <MenuIcon size={18} className="mr-3 text-gray-600" />
                              <span className="font-medium text-gray-900">{menu.nom}</span>
                              {hasValidUrl ? (
                                <ExternalLink size={12} className="ml-auto text-gray-400" />
                              ) : (
                                <Construction size={12} className="ml-auto text-orange-400" />
                              )}
                            </button>
                          );
                        })}
                      </div>
                    </div>
                  )}

                  {/* Aucun menu */}
                  {(!portailData.groupMenu || portailData.groupMenu.length === 0) && 
                   (!portailData.menus || portailData.menus.length === 0) && (
                    <div className="text-center py-8">
                      <Eye size={48} className="text-gray-300 mx-auto mb-4" />
                      <p className="text-gray-500">Aucun menu configuré pour ce portail</p>
                    </div>
                  )}
                </div>
              </div>
            </div>
          )}
        </main>
      </div>
    </div>    
  );
};

export default Dashboard;