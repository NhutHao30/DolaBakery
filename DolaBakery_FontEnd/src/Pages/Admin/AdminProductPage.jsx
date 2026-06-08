import React, { useState, useEffect } from 'react';
import AdminLayout from '../../Layout/AdminLayout';
import { getProducts, createProduct, updateProduct, deleteProduct } from '../../services/productService';

const AdminProductPage = () => {
  const [products, setProducts] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchCategory, setSearchCategory] = useState('');
  const [searchStatus, setSearchStatus] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  
  // Phân trang
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  
  const [formData, setFormData] = useState({
    MaSP: '',
    TenSP: '',
    GIABAN: '',
    LOAISP: '',
    HINHANH: '',
    GHICHU: ''
  });

  const fetchProducts = async () => {
    setIsLoading(true);
    try {
      const params = {
        page: currentPage,
        size: pageSize
      };
      if (searchQuery) params.keyword = searchQuery;
      if (searchCategory) params.category = searchCategory;
      if (searchStatus) params.status = searchStatus;
      
      const data = await getProducts(params);
      
      // Backend trả về PageResponse hoặc List
      const productList = data.content || data;
      setProducts(productList);
      
      if (data.totalPages !== undefined) {
        setTotalPages(data.totalPages);
      } else {
        setTotalPages(1);
      }
    } catch (error) {
      console.error("Error fetching products:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, pageSize]);

  // Handle Search submit
  const handleSearch = () => {
    setCurrentPage(0); // Reset về trang đầu khi tìm kiếm
    fetchProducts();
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const openAddModal = () => {
    setEditingProduct(null);
    setFormData({ MaSP: '', TenSP: '', GIABAN: '', LOAISP: '', HINHANH: '', GHICHU: '' });
    setIsModalOpen(true);
  };

  const openEditModal = (product) => {
    setEditingProduct(product);
    setFormData({
      MaSP: product.maSP || product.MaSP || product.MASP || '',
      TenSP: product.tenSP || product.TenSP || '',
      GIABAN: product.giaban || product.GIABAN || '',
      LOAISP: product.loaisp || product.LOAISP || '',
      HINHANH: product.hinhanh || product.HINHANH || '',
      GHICHU: product.ghichu || product.GHICHU || ''
    });
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setEditingProduct(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        ...formData,
        maSP: formData.MaSP,
        tenSP: formData.TenSP,
        giaban: formData.GIABAN,
        loaisp: formData.LOAISP,
        soluong: formData.SOLUONG,
        hinhanh: formData.HINHANH,
        ghichu: formData.GHICHU
      };

      if (editingProduct) {
        const id = editingProduct.maSP || editingProduct.MaSP || editingProduct.MASP;
        await updateProduct(id, payload);
        alert('Cập nhật thành công!');
      } else {
        await createProduct(payload);
        alert('Thêm sản phẩm thành công!');
      }
      closeModal();
      fetchProducts();
    } catch (error) {
      console.error("Error saving product:", error);
      alert('Có lỗi xảy ra!');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
      try {
        await deleteProduct(id);
        alert('Xóa thành công!');
        fetchProducts();
      } catch (error) {
        console.error("Error deleting product:", error);
        alert('Có lỗi xảy ra khi xóa!');
      }
    }
  };

  return (
    <AdminLayout>
      <div className="admin-flex-between" style={{ marginBottom: '1.5rem' }}>
        <h1 className="admin-title" style={{ marginBottom: 0 }}>Quản lý sản phẩm</h1>
        <button className="admin-btn admin-btn-primary" onClick={openAddModal}>+ Thêm sản phẩm</button>
      </div>

      <div className="admin-card">
        <h2 className="admin-card-title">Danh sách sản phẩm</h2>
        
        <div className="admin-flex-between" style={{ marginBottom: '1rem' }}>
          <div className="admin-flex-gap">
            <input 
              type="text" 
              className="admin-input" 
              placeholder="Tìm kiếm sản phẩm..." 
              style={{ width: '250px', marginBottom: 0 }} 
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyDown={(e) => { if(e.key === 'Enter') handleSearch(); }}
            />
            <select 
              className="admin-input" 
              style={{ width: '150px', marginBottom: 0 }}
              value={searchCategory}
              onChange={(e) => setSearchCategory(e.target.value)}
            >
              <option value="">Tất cả danh mục</option>
              <option value="LSP001">Bánh kem</option>
              <option value="LSP002">Bánh ngọt</option>
              <option value="LSP003">Bánh mì</option>
              <option value="LSP004">Bánh tráng miệng</option>
              <option value="LSP005">Bánh khô</option>
              <option value="LSP006">Bánh đông lạnh</option>
              <option value="LSP007">Bánh theo mùa</option>
            </select>
            <select 
              className="admin-input" 
              style={{ width: '150px', marginBottom: 0 }}
              value={searchStatus}
              onChange={(e) => setSearchStatus(e.target.value)}
            >
              <option value="">Tất cả trạng thái</option>
              <option value="In Stock">Còn hàng</option>
              <option value="Out of Stock">Hết hàng</option>
            </select>
            <button className="admin-btn admin-btn-secondary" onClick={handleSearch}>Lọc</button>
          </div>
          
          <div className="admin-flex-gap">
            <span>Hiển thị: </span>
            <select 
              className="admin-input" 
              style={{ width: 'auto', marginBottom: 0, padding: '0.25rem 0.5rem' }}
              value={pageSize}
              onChange={(e) => {
                setPageSize(Number(e.target.value));
                setCurrentPage(0);
              }}
            >
              <option value={5}>5</option>
              <option value={10}>10</option>
              <option value={20}>20</option>
              <option value={50}>50</option>
            </select>
          </div>
        </div>

        <div className="admin-table-container">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Mã SP</th>
                <th>Tên sản phẩm</th>
                <th>Mã Danh mục</th>
                <th>Giá bán</th>
                <th>Tồn kho</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {isLoading ? (
                <tr><td colSpan="7" style={{textAlign: 'center', padding: '20px'}}>Đang tải...</td></tr>
              ) : products.length === 0 ? (
                <tr><td colSpan="7" style={{textAlign: 'center', padding: '20px'}}>Không tìm thấy dữ liệu</td></tr>
              ) : products.map(product => {
                const id = product.maSP || product.MaSP || product.MASP;
                const name = product.tenSP || product.TenSP;
                const category = product.loaisp || product.LOAISP;
                const price = product.giaban || product.GIABAN;
                const stock = product.soluong || product.SOLUONG || 0;
                const status = stock > 0 ? 'In Stock' : 'Out of Stock';
                
                return (
                  <tr key={id}>
                    <td><strong>{id}</strong></td>
                    <td style={{ fontWeight: 600 }}>{name}</td>
                    <td>{category}</td>
                    <td>{Number(price).toLocaleString('vi-VN')}₫</td>
                    <td>{stock}</td>
                    <td>
                      <span className={`admin-badge ${status === 'In Stock' ? 'admin-badge-success' : 'admin-badge-warning'}`}>
                        {status === 'In Stock' ? 'Còn hàng' : 'Hết hàng'}
                      </span>
                    </td>
                    <td>
                      <div className="admin-flex-gap">
                        <button className="admin-btn admin-btn-secondary" style={{ padding: '0.25rem 0.75rem', fontSize: '12px' }} onClick={() => openEditModal(product)}>Sửa</button>
                        <button className="admin-btn admin-btn-danger" style={{ padding: '0.25rem 0.75rem', fontSize: '12px' }} onClick={() => handleDelete(id)}>Xóa</button>
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
        
        {/* Phân trang */}
        {totalPages > 1 && (
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '0.5rem', marginTop: '1.5rem' }}>
            <button 
              className="admin-btn admin-btn-secondary" 
              disabled={currentPage === 0} 
              onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
            >
              &laquo; Trang trước
            </button>
            <span style={{ fontSize: '14px', fontWeight: '600' }}>
              Trang {currentPage + 1} / {totalPages}
            </span>
            <button 
              className="admin-btn admin-btn-secondary" 
              disabled={currentPage >= totalPages - 1} 
              onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
            >
              Trang sau &raquo;
            </button>
          </div>
        )}
      </div>

      {/* Modal Thêm/Sửa */}
      {isModalOpen && (
        <div className="admin-modal-overlay" style={modalOverlayStyle}>
          <div className="admin-modal" style={modalStyle}>
            <div className="admin-flex-between" style={{ marginBottom: '1rem' }}>
              <h2 className="admin-card-title" style={{ marginBottom: 0 }}>
                {editingProduct ? 'Sửa sản phẩm' : 'Thêm sản phẩm mới'}
              </h2>
              <button type="button" onClick={closeModal} style={closeBtnStyle}>&times;</button>
            </div>
            
            <form onSubmit={handleSubmit}>
              {editingProduct && (
                <div className="admin-form-group">
                  <label>Mã sản phẩm</label>
                  <input type="text" name="MaSP" value={formData.MaSP} className="admin-input" disabled />
                </div>
              )}
              <div className="admin-form-group">
                <label>Tên sản phẩm</label>
                <input type="text" name="TenSP" value={formData.TenSP} onChange={handleInputChange} className="admin-input" required />
              </div>
              <div className="admin-form-group">
                <label>Giá bán</label>
                <input type="number" name="GIABAN" value={formData.GIABAN} onChange={handleInputChange} className="admin-input" required />
              </div>
              <div className="admin-form-group">
                <label>Mã Danh mục (MALOAI)</label>
                <input type="text" name="LOAISP" value={formData.LOAISP} onChange={handleInputChange} className="admin-input" required />
              </div>
              <div className="admin-form-group">
                <label>Số lượng (Tồn kho)</label>
                <input type="number" name="SOLUONG" value={formData.SOLUONG} onChange={handleInputChange} className="admin-input" required />
              </div>
              <div className="admin-form-group">
                <label>Link Hình ảnh</label>
                <input type="text" name="HINHANH" value={formData.HINHANH} onChange={handleInputChange} className="admin-input" />
              </div>
              <div className="admin-form-group">
                <label>Ghi chú</label>
                <textarea name="GHICHU" value={formData.GHICHU} onChange={handleInputChange} className="admin-input" rows="3" />
              </div>
              
              <div className="admin-flex-between" style={{ justifyContent: 'flex-end', gap: '1rem', marginTop: '1.5rem' }}>
                <button type="button" className="admin-btn admin-btn-secondary" onClick={closeModal}>Hủy</button>
                <button type="submit" className="admin-btn admin-btn-primary">Lưu</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminLayout>
  );
};

const modalOverlayStyle = {
  position: 'fixed',
  top: 0, left: 0, right: 0, bottom: 0,
  backgroundColor: 'rgba(0,0,0,0.5)',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  zIndex: 1000
};

const modalStyle = {
  backgroundColor: '#fff',
  padding: '2rem',
  borderRadius: '0.5rem',
  width: '100%',
  maxWidth: '500px',
  boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
  maxHeight: '90vh',
  overflowY: 'auto'
};

const closeBtnStyle = {
  background: 'none',
  border: 'none',
  fontSize: '1.5rem',
  cursor: 'pointer'
};

export default AdminProductPage;
