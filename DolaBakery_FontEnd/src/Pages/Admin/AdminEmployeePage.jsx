import React, { useState, useEffect } from 'react';
import AdminLayout from '../../Layout/AdminLayout';
import { getEmployees, createEmployee, updateEmployee, deleteEmployee } from '../../services/employeeService';

const AdminEmployeePage = () => {
  const [employees, setEmployees] = useState([]);
  const [filteredEmployees, setFilteredEmployees] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchRole, setSearchRole] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState(null);
  const [viewingEmployee, setViewingEmployee] = useState(null);
  
  const [formData, setFormData] = useState({
    userName: '',
    hoTen: '',
    sdt: '',
    ngaySinh: '',
    gioiTinh: '',
    diaChi: '',
    chucVu: '',
    caLamViec: '',
    luong: '',
    trangThai: '',
    maRole: 1
  });

  const fetchEmployees = async () => {
    setIsLoading(true);
    try {
      const data = await getEmployees();
      setEmployees(data);
      setFilteredEmployees(data);
    } catch (error) {
      console.error("Error fetching employees:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  const handleSearch = () => {
    let result = employees;
    if (searchQuery) {
      const lowerQuery = searchQuery.toLowerCase();
      result = result.filter(e => 
        (e.hoTen && e.hoTen.toLowerCase().includes(lowerQuery)) || 
        (e.sdt && e.sdt.includes(lowerQuery)) ||
        (e.userName && e.userName.toLowerCase().includes(lowerQuery))
      );
    }
    if (searchRole) {
      result = result.filter(e => e.chucVu === searchRole);
    }
    setFilteredEmployees(result);
  };

  useEffect(() => {
    handleSearch();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchQuery, searchRole, employees]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const openAddModal = () => {
    setEditingEmployee(null);
    setFormData({
      userName: '',
      hoTen: '',
      sdt: '',
      ngaySinh: '',
      gioiTinh: '',
      diaChi: '',
      chucVu: 'Nhân viên',
      caLamViec: 'Ca Sáng',
      luong: '',
      trangThai: 'Đang làm việc',
      maRole: 1
    });
    setIsModalOpen(true);
  };

  const openEditModal = (employee) => {
    setEditingEmployee(employee);
    setFormData({
      userName: employee.userName || '',
      hoTen: employee.hoTen || '',
      sdt: employee.sdt || '',
      ngaySinh: employee.ngaySinh || '',
      gioiTinh: employee.gioiTinh || '',
      diaChi: employee.diaChi || '',
      chucVu: employee.chucVu || 'Nhân viên',
      caLamViec: employee.caLamViec || 'Ca Sáng',
      luong: employee.luong || '',
      trangThai: employee.trangThai || 'Đang làm việc',
      maRole: 1 // maRole chỉ dùng lúc tạo mới
    });
    setIsModalOpen(true);
  };

  const openDetailModal = (employee) => {
    setViewingEmployee(employee);
    setIsDetailModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setIsDetailModalOpen(false);
    setEditingEmployee(null);
    setViewingEmployee(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        userName: formData.userName,
        hoTen: formData.hoTen,
        sdt: formData.sdt,
        ngaySinh: formData.ngaySinh ? formData.ngaySinh : null,
        gioiTinh: formData.gioiTinh,
        diaChi: formData.diaChi,
        chucVu: formData.chucVu,
        caLamViec: formData.caLamViec,
        luong: formData.luong ? parseFloat(formData.luong) : 0,
        trangThai: formData.trangThai,
        maRole: parseInt(formData.maRole, 10)
      };

      if (editingEmployee) {
        await updateEmployee(editingEmployee.userName, payload);
        alert('Cập nhật nhân viên thành công!');
      } else {
        await createEmployee(payload);
        alert('Thêm nhân viên và tạo tài khoản thành công!');
      }
      closeModal();
      fetchEmployees();
    } catch (error) {
      console.error("Error saving employee:", error);
      if (error.response) {
        console.error("Backend error data:", error.response.data);
        alert(`Lỗi từ máy chủ: ${error.response.data.message || error.response.statusText}`);
      } else {
        alert('Có lỗi xảy ra, vui lòng kiểm tra console!');
      }
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Xác nhận cho nhân viên này nghỉ việc và xóa dữ liệu/tài khoản?")) {
      try {
        await deleteEmployee(id);
        alert('Đã xóa thành công!');
        fetchEmployees();
      } catch (error) {
        console.error("Error deleting employee:", error);
        alert('Có lỗi xảy ra khi xóa!');
      }
    }
  };

  const getStatusBadgeClass = (status) => {
    if (status === 'Đang làm việc') return 'admin-badge-success';
    if (status === 'Nghỉ phép') return 'admin-badge-warning';
    return 'admin-badge-danger'; // Nghỉ việc
  };

  return (
    <AdminLayout>
      <div className="admin-flex-between" style={{ marginBottom: '1.5rem' }}>
        <h1 className="admin-title" style={{ marginBottom: 0 }}>Quản lý nhân viên</h1>
        <button className="admin-btn admin-btn-primary" onClick={openAddModal}>+ Thêm nhân viên</button>
      </div>

      <div className="admin-card">
        <h2 className="admin-card-title">Danh sách nhân viên</h2>
        
        <div className="admin-flex-between" style={{ marginBottom: '1rem' }}>
          <div className="admin-flex-gap">
            <input 
              type="text" 
              className="admin-input" 
              placeholder="Tìm kiếm Tên, Username, SĐT..." 
              style={{ width: '250px', marginBottom: 0 }} 
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <select 
              className="admin-input" 
              style={{ width: '200px', marginBottom: 0 }}
              value={searchRole}
              onChange={(e) => setSearchRole(e.target.value)}
            >
              <option value="">Tất cả chức vụ</option>
              <option value="Quản lý">Quản lý</option>
              <option value="Nhân viên">Nhân viên</option>
              <option value="Thợ làm bánh">Thợ làm bánh</option>
            </select>
          </div>
        </div>

        <div className="admin-table-container">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Username</th>
                <th>Tên nhân viên</th>
                <th>SĐT</th>
                <th>Chức vụ</th>
                <th>Ca làm việc</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {isLoading ? (
                <tr><td colSpan="7" style={{textAlign: 'center', padding: '20px'}}>Đang tải...</td></tr>
              ) : filteredEmployees.length === 0 ? (
                <tr><td colSpan="7" style={{textAlign: 'center', padding: '20px'}}>Không tìm thấy nhân viên</td></tr>
              ) : filteredEmployees.map(employee => (
                <tr key={employee.userName}>
                  <td><strong>{employee.userName}</strong></td>
                  <td style={{ fontWeight: 600 }}>{employee.hoTen}</td>
                  <td>{employee.sdt || '-'}</td>
                  <td>{employee.chucVu}</td>
                  <td>{employee.caLamViec || '-'}</td>
                  <td>
                    <span className={`admin-badge ${getStatusBadgeClass(employee.trangThai)}`}>
                      {employee.trangThai || 'Không rõ'}
                    </span>
                  </td>
                  <td>
                    <div className="admin-flex-gap">
                      <button className="admin-btn admin-btn-info" style={{ padding: '0.25rem 0.75rem', fontSize: '12px' }} onClick={() => openDetailModal(employee)}>Chi tiết</button>
                      <button className="admin-btn admin-btn-secondary" style={{ padding: '0.25rem 0.75rem', fontSize: '12px' }} onClick={() => openEditModal(employee)}>Sửa</button>
                      <button className="admin-btn admin-btn-danger" style={{ padding: '0.25rem 0.75rem', fontSize: '12px' }} onClick={() => handleDelete(employee.userName)}>Nghỉ việc</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal Thêm/Sửa */}
      {isModalOpen && (
        <div className="admin-modal-overlay" style={modalOverlayStyle}>
          <div className="admin-modal" style={modalStyle}>
            <div className="admin-flex-between" style={{ marginBottom: '1rem' }}>
              <h2 className="admin-card-title" style={{ marginBottom: 0 }}>
                {editingEmployee ? 'Sửa thông tin nhân viên' : 'Thêm nhân viên mới'}
              </h2>
              <button type="button" onClick={closeModal} style={closeBtnStyle}>&times;</button>
            </div>
            
            <form onSubmit={handleSubmit}>
              <div className="admin-form-group">
                <label>Tên đăng nhập (Username) *</label>
                <input 
                  type="text" 
                  name="userName" 
                  value={formData.userName} 
                  onChange={handleInputChange} 
                  className="admin-input" 
                  required 
                  disabled={!!editingEmployee} 
                  placeholder={!editingEmployee ? "Dùng để đăng nhập hệ thống" : ""}
                />
              </div>
              <div className="admin-form-group">
                <label>Họ tên nhân viên *</label>
                <input type="text" name="hoTen" value={formData.hoTen} onChange={handleInputChange} className="admin-input" required />
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="admin-form-group">
                  <label>Số điện thoại *</label>
                  <input type="text" name="sdt" value={formData.sdt} onChange={handleInputChange} className="admin-input" required />
                </div>
                <div className="admin-form-group">
                  <label>Ngày sinh</label>
                  <input type="date" name="ngaySinh" value={formData.ngaySinh} onChange={handleInputChange} className="admin-input" />
                </div>
              </div>
              
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="admin-form-group">
                  <label>Giới tính</label>
                  <select name="gioiTinh" value={formData.gioiTinh} onChange={handleInputChange} className="admin-input">
                    <option value="">Chọn giới tính</option>
                    <option value="Nam">Nam</option>
                    <option value="Nữ">Nữ</option>
                  </select>
                </div>
                <div className="admin-form-group">
                  <label>Trạng thái</label>
                  <select name="trangThai" value={formData.trangThai} onChange={handleInputChange} className="admin-input">
                    <option value="Đang làm việc">Đang làm việc</option>
                    <option value="Nghỉ phép">Nghỉ phép</option>
                    <option value="Nghỉ việc">Nghỉ việc</option>
                  </select>
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="admin-form-group">
                  <label>Chức vụ</label>
                  <select name="chucVu" value={formData.chucVu} onChange={handleInputChange} className="admin-input">
                    <option value="Nhân viên">Nhân viên</option>
                    <option value="Quản lý">Quản lý</option>
                    <option value="Thợ làm bánh">Thợ làm bánh</option>
                  </select>
                </div>
                <div className="admin-form-group">
                  <label>Ca làm việc</label>
                  <select name="caLamViec" value={formData.caLamViec} onChange={handleInputChange} className="admin-input">
                    <option value="Ca Sáng">Ca Sáng (6h - 14h)</option>
                    <option value="Ca Chiều">Ca Chiều (14h - 22h)</option>
                    <option value="Hành chính">Hành chính (8h - 17h)</option>
                  </select>
                </div>
              </div>

              {!editingEmployee && (
                <div className="admin-form-group">
                  <label>Quyền hệ thống (Role) *</label>
                  <select name="maRole" value={formData.maRole} onChange={handleInputChange} className="admin-input" required>
                    <option value={1}>Nhân viên (Quyền hạn hạn chế)</option>
                    <option value={0}>Quản trị viên (Toàn quyền)</option>
                  </select>
                  <small style={{color: '#666'}}>Tài khoản tự động được tạo với mật khẩu mặc định là Username.</small>
                </div>
              )}

              <div className="admin-form-group">
                <label>Lương (VNĐ)</label>
                <input type="number" name="luong" value={formData.luong} onChange={handleInputChange} className="admin-input" placeholder="Để trống hệ thống sẽ tính tự động theo chức vụ" />
              </div>

              <div className="admin-form-group">
                <label>Địa chỉ</label>
                <textarea name="diaChi" value={formData.diaChi} onChange={handleInputChange} className="admin-input" rows="2" />
              </div>
              
              <div className="admin-flex-between" style={{ justifyContent: 'flex-end', gap: '1rem', marginTop: '1.5rem' }}>
                <button type="button" className="admin-btn admin-btn-secondary" onClick={closeModal}>Hủy</button>
                <button type="submit" className="admin-btn admin-btn-primary">Lưu</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal Chi tiết */}
      {isDetailModalOpen && viewingEmployee && (
        <div className="admin-modal-overlay" style={modalOverlayStyle}>
          <div className="admin-modal" style={{ ...modalStyle, maxWidth: '600px' }}>
            <div className="admin-flex-between" style={{ marginBottom: '1.5rem' }}>
              <h2 className="admin-card-title" style={{ marginBottom: 0 }}>Hồ sơ Nhân viên</h2>
              <button type="button" onClick={closeModal} style={closeBtnStyle}>&times;</button>
            </div>
            
            <div style={{ padding: '1.5rem', backgroundColor: 'var(--admin-tertiary)', borderRadius: '8px', marginBottom: '1.5rem' }}>
              <h3 style={{ fontSize: '1.1rem', marginBottom: '1rem', borderBottom: '1px solid var(--admin-outline)', paddingBottom: '0.5rem', display: 'flex', justifyContent: 'space-between' }}>
                <span>{viewingEmployee.hoTen}</span>
                <span className={`admin-badge ${getStatusBadgeClass(viewingEmployee.trangThai)}`}>{viewingEmployee.trangThai}</span>
              </h3>
              
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <p><strong>Username:</strong> {viewingEmployee.userName}</p>
                <p><strong>Số điện thoại:</strong> {viewingEmployee.sdt || 'Chưa cập nhật'}</p>
                <p><strong>Ngày sinh:</strong> {viewingEmployee.ngaySinh || 'Chưa cập nhật'}</p>
                <p><strong>Giới tính:</strong> {viewingEmployee.gioiTinh || 'Chưa cập nhật'}</p>
                <p><strong>Chức vụ:</strong> {viewingEmployee.chucVu || 'Chưa cập nhật'}</p>
                <p><strong>Ca làm việc:</strong> {viewingEmployee.caLamViec || 'Chưa cập nhật'}</p>
                <p><strong>Mức lương:</strong> {viewingEmployee.luong ? Number(viewingEmployee.luong).toLocaleString('vi-VN') + ' ₫' : 'Chưa thiết lập'}</p>
              </div>
              <div style={{ marginTop: '1rem' }}>
                <p><strong>Địa chỉ:</strong> {viewingEmployee.diaChi || 'Chưa cập nhật'}</p>
              </div>
            </div>
            
            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '1rem' }}>
              <button type="button" className="admin-btn admin-btn-secondary" onClick={() => { closeModal(); openEditModal(viewingEmployee); }}>Chỉnh sửa</button>
              <button type="button" className="admin-btn admin-btn-primary" onClick={closeModal}>Đóng</button>
            </div>
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
  maxWidth: '600px',
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

export default AdminEmployeePage;
