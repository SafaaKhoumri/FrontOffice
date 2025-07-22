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
  MapPin
} from 'lucide-react';

const Dashboard = () => {
  // Récupération du code portail depuis l'URL
  const portailCode = window.location.pathname.split('/').pop() || 'IMP_001';
  
  // États pour l'interface
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [activeMenu, setActiveMenu] = useState('dashboard');
  const [expandedMenus, setExpandedMenus] = useState({});
  const [userMenuOpen, setUserMenuOpen] = useState(false);
  
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
      if (!token) {
        setError('Token manquant - Redirection vers login nécessaire');
        return;
      }

      console.log('🔄 Chargement des données du portail:', portailCode);

      const response = await fetch(`http://localhost:8085/api/frontoffice/portail/${portailCode}`, {
        headers: {
          'Authorization': token,
          'Content-Type': 'application/json'
        }
      });

      console.log('📡 Status API:', response.status);

      if (!response.ok) {
        throw new Error(`Erreur ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      console.log('📊 Données du portail reçues:', data);
      
      setPortailData(data);
      
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

  const handleMenuClick = (menuItem) => {
    setActiveMenu(menuItem.id || menuItem.code);
    if (menuItem.menus && menuItem.menus.length > 0) {
      toggleMenu(menuItem.id || menuItem.code);
    }
    console.log('📍 Navigation vers:', menuItem);
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
            onClick={() => setActiveMenu('dashboard')}
            className={`w-full flex items-center px-3 py-2 rounded-lg text-left transition-colors mb-1 ${
              activeMenu === 'dashboard'
                ? 'bg-blue-50 text-blue-700 border-r-2 border-blue-700' 
                : 'text-gray-700 hover:bg-gray-100'
            }`}
          >
            <Home size={20} />
            {sidebarOpen && <span className="ml-3 font-medium">Tableau de bord</span>}
          </button>

          {/* Groupes de menus RÉELS */}
          {portailData.groupMenu && portailData.groupMenu.map((group) => {
            const isExpanded = expandedMenus[group.id];
            const hasMenus = group.menus && group.menus.length > 0;

            return (
              <div key={group.id} className="mb-1">
                <button
                  onClick={() => toggleMenu(group.id)}
                  className="w-full flex items-center px-3 py-2 rounded-lg text-left transition-colors text-gray-700 hover:bg-gray-100"
                >
                  <Package size={20} />
                  {sidebarOpen && (
                    <>
                      <span className="ml-3 font-medium">{group.nom}</span>
                      {hasMenus && (
                        <div className="ml-auto">
                          {isExpanded ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
                        </div>
                      )}
                    </>
                  )}
                </button>

                {hasMenus && isExpanded && sidebarOpen && (
                  <div className="ml-6 mt-1 space-y-1">
                    {group.menus.map((menu) => {
                      const MenuIcon = getMenuIcon(menu.code);
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
                          <span className="ml-3">{menu.nom}</span>
                        </button>
                      );
                    })}
                  </div>
                )}
              </div>
            );
          })}

          {/* Menus directs RÉELS */}
          {portailData.menus && portailData.menus.map((menu) => {
            const MenuIcon = getMenuIcon(menu.code);
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
                  {sidebarOpen && <span className="ml-3 font-medium">{menu.nom}</span>}
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
              <h2 className="text-xl font-semibold text-gray-900">
                {portailData.nom}
              </h2>
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

        {/* Contenu principal - VRAIES données */}
        <main className="flex-1 overflow-auto p-6">
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
              <p className="text-gray-600 text-sm mt-1">Fonctionnalités accessibles dans ce portail</p>
            </div>
            
            <div className="p-6">
              {/* Groupes de menus */}
              {portailData.groupMenu && portailData.groupMenu.length > 0 && (
                <div className="mb-6">
                  <h4 className="text-md font-medium text-gray-900 mb-3">Groupes de menus</h4>
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {portailData.groupMenu.map((group) => (
                      <div key={group.id} className="border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow">
                        <div className="flex items-center justify-between mb-2">
                          <h5 className="font-medium text-gray-900">{group.nom}</h5>
                          <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">
                            {group.menus?.length || 0} menus
                          </span>
                        </div>
                        {group.menus && group.menus.length > 0 && (
                          <div className="space-y-1">
                            {group.menus.map((menu) => {
                              const MenuIcon = getMenuIcon(menu.code);
                              return (
                                <div key={menu.id} className="flex items-center text-sm text-gray-600">
                                  <MenuIcon size={14} className="mr-2" />
                                  {menu.nom}
                                </div>
                              );
                            })}
                          </div>
                        )}
                      </div>
                    ))}
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
                      return (
                        <div key={menu.id} className="flex items-center p-3 border border-gray-200 rounded-lg hover:shadow-sm transition-shadow">
                          <MenuIcon size={18} className="mr-3 text-gray-600" />
                          <span className="font-medium text-gray-900">{menu.nom}</span>
                        </div>
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
        </main>
      </div>
    </div>
  );
};

export default Dashboard;