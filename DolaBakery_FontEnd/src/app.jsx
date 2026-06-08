import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./Layout/Header";
import HomePage from "./Pages/HomePage";
import AboutPage from "./Pages/AboutPage";
import ProductPage from "./Pages/Productpage";
import PostPage from "./Pages/PostPage";
import ContactPage from "./Pages/ContactPage";
import FAQPage from "./Pages/FAQPage";
import StorePage from "./Pages/StorePage";
import CartPage from "./Pages/CartPage";
import WishlistPage from "./Pages/WishlistPage";
import LoginPage from "./Pages/LoginPage";
import RegisterPage from "./Pages/RegisterPage";
import RegisterGooglePage from "./Pages/RegisterGooglePage";
import Modal from "./components/Modal";
import AdminProductPage from "./Pages/Admin/AdminProductPage";
import AdminCustomerPage from "./Pages/Admin/AdminCustomerPage";
import AdminEmployeePage from "./Pages/Admin/AdminEmployeePage";
import AdminInvoicePage from "./Pages/Admin/AdminInvoicePage";
import MyOrdersPage from "./Pages/MyOrdersPage";

function App() {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/gioi-thieu" element={<AboutPage />} />
        <Route path="/san-pham" element={<ProductPage />} />
        <Route path="/tin-tuc" element={<PostPage />} />
        <Route path="/lien-he" element={<ContactPage />} />
        <Route path="/Cau-hoi-thuong-gap" element={<FAQPage />} />
        <Route path="/he-thong-cua-hang" element={<StorePage />} />
        <Route path="/gio-hang" element={<CartPage />} />
        <Route path="/yeu-thich" element={<WishlistPage />} />
        <Route path="/dang-nhap" element={<LoginPage />} />
        <Route path="/dang-ky" element={<RegisterPage />} />
        <Route path="/dang-ky-google" element={<RegisterGooglePage />} />
        <Route path="/my-orders" element={<MyOrdersPage />} />
        
        {/* Admin Routes */}
        <Route path="/admin" element={<AdminProductPage />} />
        <Route path="/admin/san-pham" element={<AdminProductPage />} />
        <Route path="/admin/khach-hang" element={<AdminCustomerPage />} />
        <Route path="/admin/nhan-vien" element={<AdminEmployeePage />} />
        <Route path="/admin/hoa-don" element={<AdminInvoicePage />} />
      </Routes>
      <Modal />
    </Router>
  );
}

export default App;
