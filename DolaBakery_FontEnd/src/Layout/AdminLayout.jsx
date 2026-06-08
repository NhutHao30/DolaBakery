import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { logout, getCurrentUser } from '../services/authService';
import '../css/admin.css';

const AdminLayout = ({ children }) => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [isAuthorized, setIsAuthorized] = useState(false);
  const location = useLocation();

  const navigate = useNavigate();

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const user = await getCurrentUser();
        if (user.role === 0 || user.role === 1) {
          setIsAuthorized(true);
        } else {
          alert("Bạn không có quyền truy cập vào trang quản trị!");
          window.location.href = "/";
        }
      } catch (error) {
        alert("Vui lòng đăng nhập bằng tài khoản quản trị!");
        window.location.href = "/dang-nhap";
      }
    };
    checkAuth();
  }, []);

  const toggleSidebar = () => {
    setSidebarOpen(!isSidebarOpen);
  };

  const handleLogout = async () => {
    try {
      await logout();
      alert("Đăng xuất thành công!");
      window.location.href = "/dang-nhap";
    } catch (error) {
      console.error("Lỗi đăng xuất:", error);
      alert("Có lỗi xảy ra khi đăng xuất.");
    }
  };

  if (!isAuthorized) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', flexDirection: 'column' }}>
        <i className="fa-solid fa-spinner fa-spin" style={{ fontSize: '40px', color: 'var(--primary-color)', marginBottom: '15px' }}></i>
        <h3>Đang kiểm tra quyền truy cập...</h3>
      </div>
    );
  }

  return (
    <div className="admin-dashboard">
      {/* Mobile Overlay */}
      <div 
        className={`admin-sidebar-overlay ${isSidebarOpen ? 'open' : ''}`}
        onClick={() => setSidebarOpen(false)}
      ></div>

      {/* Sidebar */}
      <aside className={`admin-sidebar ${isSidebarOpen ? 'open' : ''}`} style={{fontSize: '14px'}}>
        <Link to="/admin" className="sidebar-logo">
          Dola Bakery
        </Link>
        <nav>
          <Link 
            to="/admin/san-pham" 
            className={`nav-link ${location.pathname === '/admin/san-pham' ? 'active' : ''}`}
          >
            Quản lý sản phẩm
          </Link>
          <Link 
            to="/admin/khach-hang" 
            className={`nav-link ${location.pathname === '/admin/khach-hang' ? 'active' : ''}`}
          >
            Quản lý khách hàng
          </Link>
          <Link 
            to="/admin/nhan-vien" 
            className={`nav-link ${location.pathname === '/admin/nhan-vien' ? 'active' : ''}`}
          >
            Quản lý nhân viên
          </Link>
          <Link 
            to="/admin/hoa-don" 
            className={`nav-link ${location.pathname === '/admin/hoa-don' ? 'active' : ''}`}
          >
            Quản lý hóa đơn
          </Link>
        </nav>
        <div style={{ padding: '20px', marginTop: 'auto' }}>
          <button 
            onClick={handleLogout}
            style={{ 
              width: '100%', 
              padding: '10px', 
              backgroundColor: '#dc3545', 
              color: 'white', 
              border: 'none', 
              borderRadius: '5px', 
              cursor: 'pointer',
              fontWeight: 'bold',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: '8px'
            }}
          >
            <i className="fa-solid fa-right-from-bracket"></i> Đăng xuất
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="admin-main">
        <button className="admin-mobile-toggle" onClick={toggleSidebar}>
          ☰ Menu
        </button>
        {children}
      </main>
    </div>
  );
};

export default AdminLayout;
