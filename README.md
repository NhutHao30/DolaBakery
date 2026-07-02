🍰 Dola Bakery - E-Commerce Management System

Dola Bakery là một hệ thống ứng dụng web thương mại điện tử chuyên cung cấp các sản phẩm bánh ngọt, được thiết kế với giao diện hiện đại, ấm cúng và tinh tế. Dự án được xây dựng dựa trên mô hình **Client-Server** (Frontend - Backend tách biệt), đem lại trải nghiệm mượt mà cho khách hàng và bộ công cụ quản lý mạnh mẽ, chặt chẽ cho Admin/Nhân viên.

Đây là project được xây dựng bài bản, áp dụng nhiều công nghệ và tiêu chuẩn thực tế để đáp ứng các yêu cầu về thẩm mỹ UI/UX, bảo mật và toàn vẹn dữ liệu.


## 🚀 Công Nghệ Cốt Lõi (Tech Stack)

### 🎨 Frontend (Client)
- **Framework & Thư viện:** ReactJS (Vite), Axios.
- **Styling:** CSS Vanilla (Áp dụng hệ thống Design System chuyên nghiệp với màu sắc Dusty Rose, Mint Green; phông chữ Literata & Inter).
- **Kiến trúc:** Component-based, định tuyến linh hoạt với React Router.

### ⚙️ Backend (Server)
- **Framework:** Java Spring Boot.
- **Database Access:** Spring Data JPA (Áp dụng ORM cho phép thao tác với DB an toàn, sinh tự động truy vấn, chống SQL Injection).
- **Security:** Spring Security & **BCryptPasswordEncoder** (Băm mật khẩu 1 chiều với Salt, bảo mật tối đa).
- **Database:** Microsoft SQL Server (hoặc MySQL).

### 🔌 Tích hợp Mở rộng (Integrations)
- **JavaMailSender (SMTP):** Hệ thống cấp phát mã OTP gửi trực tiếp qua Email phục vụ tính năng bảo mật tài khoản.
- **VietQR API:** Tự động tạo mã QR chuẩn EMVCo động (chứa số tiền và nội dung) hỗ trợ khách hàng quét thanh toán nhanh chóng trên các App ngân hàng.


## 🌟 Chức Năng Nổi Bật

### 🛒 Dành Cho Khách Hàng
- **Trải nghiệm mua sắm mượt mà:** Xem sản phẩm, phân trang, tìm kiếm hiện đại.
- **Quản lý Giỏ Hàng thông minh:** Đồng bộ giỏ hàng theo Session, tự động tính toán tổng tiền mà không bị nhầm lẫn dữ liệu giữa các khách.
- **Thanh toán An toàn (Checkout):** 
  - Kiểm tra tự động số lượng tồn kho trước khi thanh toán (chặn giao dịch nếu kho không đủ).
  - Hỗ trợ thanh toán COD và **Chuyển khoản VietQR** động tự động điền sẵn số tiền & nội dung.
  - Tự động trừ kho sau khi thanh toán thành công.
- **Bảo mật Tài khoản:** Đăng ký, đăng nhập bảo mật. Hỗ trợ **Quên mật khẩu** xác thực qua mã **OTP (Gửi qua Email)** trong thời gian thực.
- **Wishlist:** Danh sách lưu trữ các sản phẩm yêu thích.

👨‍💻 Dành Cho Quản Trị Viên (Admin & Nhân viên)
- **Phân quyền Role-based:** Hệ thống giới hạn quyền chặt chẽ theo 3 cấp độ (Admin / Nhân viên / Khách hàng).
- **Quản lý Sản phẩm & Kho hàng:** Thêm/sửa/xóa các loại bánh, hình ảnh, cập nhật số lượng tồn kho, đơn giá.
- **Quản lý Đơn hàng:** Theo dõi, kiểm soát và thay đổi trạng thái hóa đơn (Chờ xử lý, Đang giao, Hoàn thành, Đã hủy).
- **Quản lý Khách hàng & Phân hạng:** Theo dõi chi tiêu và lịch sử mua sắm chi tiết của khách, phân hạng thẻ (Đồng/Bạc/Vàng).
- **Quản lý Nhân sự nội bộ:** Admin có quyền cấp tài khoản nội bộ cho nhân viên với mật khẩu mặc định được mã hóa tự động.


## 🛡 Điểm Nhấn Kiến Trúc & Bảo Mật

Dự án chú trọng rất nhiều vào bảo mật và tính toàn vẹn dữ liệu:
1. **Mã hóa mật khẩu 1 chiều (BCrypt):** Tránh hoàn toàn việc lộ mật khẩu, kể cả khi database bị tấn công. Việc thêm ngẫu nhiên "Salt" khiến các cuộc tấn công bằng Rainbow Table hoặc Dictionary Attack trở nên vô hiệu.
2. **Session / Cookie Security:** Thay vì JWT, dự án dùng `HttpSession` truyền thống kết hợp cấu hình CORS nghiêm ngặt (`axios.defaults.withCredentials = true`) để duyệt qua các luồng Authentication, giỏ hàng và thanh toán khép kín.
3. **Bảo mật OTP & Quên mật khẩu:** Mã OTP sinh ngẫu nhiên, lưu trực tiếp trên RAM máy chủ và cấu hình tự động hủy sau 5 phút. Đảm bảo OTP không bao giờ xuất hiện ở phía giao diện hay truyền tải không bảo mật.
4. **Data Integrity & Validation:** Xác thực từ FrontEnd cho đến BackEnd (JPA / Spring Validation) đảm bảo dữ liệu "sạch" khi vào Database, không có giá trị âm bất hợp lý hoặc lỗi nghiệp vụ trừ kho.


## 🛠 Hướng Dẫn Cài Đặt (Setup & Installation)

### 1. Yêu cầu môi trường
- **Node.js** (Phiên bản v16 trở lên)
- **Java JDK** (Phiên bản 11 hoặc 17)
- **IDE (Khuyên dùng):** IntelliJ IDEA / Eclipse (Cho Backend) và VS Code (Cho Frontend).
- **Hệ quản trị CSDL:** Microsoft SQL Server (Hoặc cấu hình sang MySQL tương ứng).

### 2. Cài đặt Database
1. Sử dụng script CSDL có sẵn hoặc cấu hình Hibernate `spring.jpa.hibernate.ddl-auto=update` để tự động tạo bảng.
2. Cập nhật thông tin kết nối trong Backend (`DolaBakery_BackEnd/src/main/resources/application.properties`):
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=Ten_Cua_Database_SQL_Server
   spring.datasource.username=sa
   spring.datasource.password=mat_khau_cua_ban
   ```

### 3. Khởi động Backend (Spring Boot Server)
1. Mở thư mục `DolaBakery_BackEnd` trong IDE.
2. Tải (Sync) các dependencies trong tệp `pom.xml`.
3. Cập nhật thông tin tài khoản SMTP Gmail gửi OTP (`application.properties`):
   ```properties
   spring.mail.username=email_cua_ban@gmail.com
   spring.mail.password=mat_khau_ung_dung_google_cua_ban
   ```
4. Chạy file main `Application.java`. Server sẽ hoạt động ở port `8080`.

### 4. Khởi động Frontend (ReactJS Client)
1. Mở Terminal tại thư mục `DolaBakery_FontEnd`.
2. Chạy lệnh cài đặt các gói (packages) cần thiết:
   ```bash
   npm install
   ```
3. Chạy server phát triển (Development Server):
   ```bash
   npm run dev
   ```
4. Truy cập giao diện ứng dụng trên trình duyệt qua địa chỉ: `http://localhost:5173`

---
*Cảm ơn bạn đã xem qua dự án này!*
