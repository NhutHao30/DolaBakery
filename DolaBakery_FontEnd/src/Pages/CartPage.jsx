import React, { useEffect, useState } from "react";
import Footer from "../Layout/Footer";
import { getCart, removeFromCart, updateCartQuantity, checkout } from "../services/cartService";

const getImageSrc = (imageUrl) => {
    if (!imageUrl) return "../../assets/IMG/productnew2.webp";
    if (imageUrl.startsWith("http")) return imageUrl;
    return `../../assets/IMG/${imageUrl.split('/').pop()}`;
};

const getProductName = (product) => {
    if (!product) return "Sản phẩm";
    return product.tenSP || product.TenSP || product.name || "Sản phẩm";
};

const getProductPrice = (product) => {
    if (!product) return 0;
    return Number(product.price || product.GIABAN || 0);
};

const getMasp = (item) => {
    return item.masp || item.MASP || item.MaSP || "";
};

const CartPage = () => {
    const [cartItems, setCartItems] = useState([]);
    const [total, setTotal] = useState(0);
    const [paymentMethod, setPaymentMethod] = useState("COD");

    const loadCart = async () => {
        try {
            const data = await getCart();
            const items = Array.isArray(data) ? data : (data ? [data] : []);
            setCartItems(items);
            const sum = items.reduce((acc, item) => acc + (getProductPrice(item.product) * (item.soluong || 1)), 0);
            setTotal(sum);
        } catch (e) {
            if (e.response?.status === 401) {
                alert("Vui lòng đăng nhập để xem giỏ hàng");
                window.location.href = "/dang-nhap";
            } else {
                console.error("Lỗi load cart:", e);
                setCartItems([]);
            }
        }
    };

    useEffect(() => {
        loadCart();
        const handleUpdate = () => loadCart();
        document.addEventListener('cartUpdated', handleUpdate);
        return () => document.removeEventListener('cartUpdated', handleUpdate);
    }, []);

    const handleRemove = async (masp) => {
        if (!masp) { console.error("masp undefined!"); return; }
        try {
            await removeFromCart(masp);
            await loadCart();
            document.dispatchEvent(new Event('cartUpdated'));
        } catch (e) {
            console.error("Lỗi xóa:", e);
            alert("Không thể xóa sản phẩm: " + e.message);
        }
    };

    const handleUpdateQty = async (masp, newQty) => {
        if (!masp) return;
        if (newQty <= 0) {
            handleRemove(masp);
            return;
        }
        try {
            await updateCartQuantity(masp, newQty);
            await loadCart();
            document.dispatchEvent(new Event('cartUpdated'));
        } catch (e) {
            console.error("Lỗi cập nhật số lượng:", e);
        }
    };

    const handleCheckout = async () => {
        if (cartItems.length === 0) {
            alert("Giỏ hàng của bạn đang trống!");
            return;
        }

        if (window.confirm("Bạn có chắc chắn muốn thanh toán đơn hàng này bằng hình thức " + (paymentMethod === "COD" ? "Thanh toán khi nhận hàng" : "Chuyển khoản") + "?")) {
            try {
                // Có thể thu thập thêm ghi chú, đơn vị vận chuyển ở đây nếu muốn
                await checkout({ ghiChu: "Phương thức thanh toán: " + (paymentMethod === "COD" ? "COD" : "Chuyển khoản"), donViVanChuyen: "Giao hàng tiêu chuẩn" });
                alert("Thanh toán thành công! Hóa đơn của bạn đã được tạo và đang chờ xử lý.");
                setCartItems([]);
                setTotal(0);
                document.dispatchEvent(new Event('cartUpdated'));
                window.location.href = "/my-orders"; // Chuyển hướng sang trang lịch sử đơn hàng
            } catch (error) {
                console.error("Lỗi thanh toán:", error);
                const errorMsg = error.response?.data?.error || "Có lỗi xảy ra khi thanh toán. Vui lòng thử lại!";
                alert(errorMsg);
            }
        }
    };

    return (
        <>
            <main>
                <div className="grid wide">
                    <div className="row">
                        <div className="cart-container mt-30" style={{ margin: "auto", marginBottom: "50px", marginTop: "30px" }}>
                            <div className="cart-container-detail col-lg-10 col-md-12 col-10" style={{ margin: "auto" }}>
                                {/* Header bảng */}
                                <div className="hide-on-mobile" style={{ display: "flex", justifyContent: "space-between", borderBottom: "2px solid var(--primary-color)", padding: "12px", marginBottom: "5px", fontWeight: "bold", color: "#555" }}>
                                    <div style={{ flex: 2, fontSize: "14px" }}>Thông tin sản phẩm</div>
                                    <div style={{ flex: 1, textAlign: "center" }}>Đơn giá</div>
                                    <div style={{ flex: 1, textAlign: "center" }}>Số lượng</div>
                                    <div style={{ flex: 1, textAlign: "right" }}>Thành tiền</div>
                                    <div style={{ width: "60px" }}></div>
                                </div>

                                {/* Danh sách sản phẩm */}
                                <div className="cart-container-detail__product-list">
                                    {cartItems.map((item, index) => {
                                        const masp = getMasp(item);
                                        const price = getProductPrice(item.product);
                                        const qty = item.soluong || 1;
                                        return (
                                            <div
                                                key={index}
                                                style={{ display: "flex", justifyContent: "space-between", alignItems: "center", borderBottom: "1px solid #eee", padding: "15px 0", gap: "10px" }}
                                            >
                                                {/* Ảnh + tên */}
                                                <div style={{ display: "flex", alignItems: "center", flex: 2, gap: "15px", marginLeft: "10px" }}>
                                                    <img
                                                        src={getImageSrc(item.product?.imageUrl || item.product?.HINHANH)}
                                                        alt={getProductName(item.product)}
                                                        style={{ width: "90px", height: "90px", objectFit: "cover", borderRadius: "8px", flexShrink: 0 }}
                                                    />
                                                    <div>
                                                        <div style={{ fontSize: "15px", fontWeight: "600", color: "#333" }}>
                                                            {getProductName(item.product)}
                                                        </div>
                                                        <div style={{ fontSize: "12px", color: "#999", marginTop: "4px" }}>
                                                            Mã SP: {masp}
                                                        </div>
                                                    </div>
                                                </div>

                                                {/* Đơn giá */}
                                                <div style={{ flex: 1, fontSize: "14px", textAlign: "center", color: "#555" }}>
                                                    {price.toLocaleString('vi-VN')}₫
                                                </div>

                                                {/* Số lượng: nút - / số / nút + */}
                                                <div style={{ flex: 1, display: "flex", justifyContent: "center", alignItems: "center", gap: "0" }}>
                                                    <button
                                                        onClick={() => handleUpdateQty(masp, qty - 1)}
                                                        style={{ width: "30px", height: "30px", border: "1px solid #ccc", background: "#f5f5f5", cursor: "pointer", fontSize: "16px", borderRadius: "4px 0 0 4px", display: "flex", alignItems: "center", justifyContent: "center" }}
                                                    >
                                                        <i className="fa-solid fa-minus" style={{ fontSize: "10px" }}></i>
                                                    </button>
                                                    <div style={{ width: "40px", height: "30px", border: "1px solid #ccc", borderLeft: "none", borderRight: "none", display: "flex", alignItems: "center", justifyContent: "center", fontSize: "14px", fontWeight: "600" }}>
                                                        {qty}
                                                    </div>
                                                    <button
                                                        onClick={() => handleUpdateQty(masp, qty + 1)}
                                                        style={{ width: "30px", height: "30px", border: "1px solid #ccc", background: "#f5f5f5", cursor: "pointer", fontSize: "16px", borderRadius: "0 4px 4px 0", display: "flex", alignItems: "center", justifyContent: "center" }}
                                                    >
                                                        <i className="fa-solid fa-plus" style={{ fontSize: "10px" }}></i>
                                                    </button>
                                                </div>

                                                {/* Thành tiền */}
                                                <div style={{ flex: 1, textAlign: "right", color: "var(--primary-color)", fontWeight: "bold" }}>
                                                    {(price * qty).toLocaleString('vi-VN')}₫
                                                </div>

                                                {/* Nút xóa */}
                                                <div style={{ width: "60px", textAlign: "center" }}>
                                                    <button
                                                        onClick={() => handleRemove(masp)}
                                                        title="Xóa sản phẩm"
                                                        style={{ background: "none", border: "none", cursor: "pointer", color: "#ff4d4d", fontSize: "18px", padding: "5px" }}
                                                    >
                                                        <i className="fa-solid fa-trash"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        );
                                    })}

                                    {cartItems.length === 0 && (
                                        <div style={{ textAlign: "center", padding: "50px", fontSize: "16px", color: "#888" }}>
                                            <i className="fa-solid fa-cart-shopping" style={{ fontSize: "48px", marginBottom: "15px", display: "block", color: "#ccc" }}></i>
                                            Chưa có sản phẩm nào trong giỏ hàng.
                                        </div>
                                    )}
                                </div>

                                {/* Tổng tiền + thanh toán */}
                                {cartItems.length > 0 && (
                                    <div style={{ borderTop: "2px solid var(--primary-color)", marginTop: "20px", paddingTop: "20px", textAlign: "right" }}>
                                        <p style={{ fontSize: "18px", marginBottom: "15px", marginRight: "10px", }}>
                                            <b>Tổng tiền ({cartItems.length} sản phẩm):</b>
                                            <span style={{ marginLeft: "12px", color: "var(--primary-color)", fontSize: "24px", fontWeight: "bold" }}>
                                                {total.toLocaleString('vi-VN')}₫
                                            </span>
                                        </p>

                                        <div style={{ marginBottom: "20px", marginRight: "10px" }}>
                                            <b style={{ fontSize: "16px", display: "block", marginBottom: "10px" }}>Hình thức thanh toán:</b>
                                            <div style={{ display: "flex", justifyContent: "flex-end", gap: "20px", fontSize: "15px" }}>
                                                <label style={{ cursor: "pointer", display: "flex", alignItems: "center", gap: "8px" }}>
                                                    <input
                                                        type="radio"
                                                        name="paymentMethod"
                                                        value="COD"
                                                        checked={paymentMethod === "COD"}
                                                        onChange={(e) => setPaymentMethod(e.target.value)}
                                                    />
                                                    Thanh toán khi nhận hàng (COD)
                                                </label>
                                                <label style={{ cursor: "pointer", display: "flex", alignItems: "center", gap: "8px" }}>
                                                    <input
                                                        type="radio"
                                                        name="paymentMethod"
                                                        value="BANK"
                                                        checked={paymentMethod === "BANK"}
                                                        onChange={(e) => setPaymentMethod(e.target.value)}
                                                    />
                                                    Chuyển khoản
                                                </label>
                                            </div>
                                            {paymentMethod === "BANK" && (
                                                <div style={{ marginTop: "15px", padding: "15px", backgroundColor: "#f9f9f9", border: "1px solid #ddd", borderRadius: "8px", textAlign: "center", display: "inline-block" }}>
                                                    <p style={{ margin: "0 0 10px 0", fontWeight: "bold", color: "var(--primary-color)" }}>Quét mã QR để thanh toán:</p>
                                                    <img
                                                        src={`https://img.vietqr.io/image/vietcombank-1031367128-compact2.png?amount=${total}&addInfo=Thanh toan don hang Dola Bakery&accountName=DOLA BAKERY`}
                                                        alt="Mã QR Thanh Toán"
                                                        style={{ width: "200px", height: "200px", objectFit: "contain", borderRadius: "8px", border: "1px solid #ccc", margin: "0 auto" }}
                                                    />
                                                    <p style={{ margin: "10px 0 5px 0" }}>Ngân hàng: <b>Vietcombank</b></p>
                                                    <p style={{ margin: "0" }}>Số tài khoản: <b>1031367128</b></p>
                                                    <p style={{ margin: "5px 0" }}>Chủ tài khoản: <b>DOLA BAKERY</b></p>
                                                    <p style={{ margin: "5px 0 0 0", fontStyle: "italic", fontSize: "13px", color: "#666" }}>Vui lòng chụp lại màn hình giao dịch nếu cần thiết.</p>
                                                </div>
                                            )}
                                        </div>

                                        <button
                                            id="btn-thanh-toan"
                                            onClick={handleCheckout}
                                            style={{ padding: "12px 30px", marginRight: "10px", backgroundColor: "var(--primary-color)", color: "white", border: "none", borderRadius: "6px", fontSize: "16px", cursor: "pointer", fontWeight: "bold" }}
                                        >
                                            Thanh toán ngay
                                        </button>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </main>
            <Footer />
        </>
    );
};

export default CartPage;
