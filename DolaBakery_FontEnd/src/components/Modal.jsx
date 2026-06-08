import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const Modal = () => {
    const [searchQuery, setSearchQuery] = useState("");
    const navigate = useNavigate();

    const handleSearchSubmit = () => {
        if (searchQuery.trim()) {
            navigate(`/san-pham?search=${encodeURIComponent(searchQuery.trim())}`);
            const modal = document.querySelector('.modal-js');
            const modalSearch = document.querySelector('.Modal-Search-js');
            if (modalSearch) modalSearch.style.display = 'none';
            if (modal) modal.style.display = 'none';
        }
    };

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            handleSearchSubmit();
        }
    };
    useEffect(() => {
        const handleClick = async (e) => {
            const modal = document.querySelector('.modal-js');
            const modalSearch = document.querySelector('.Modal-Search-js');
            const searchInfor = document.querySelector('.searchInfor-js');
            const addCart = document.querySelector('.addCart-js');
            const menuBar = document.querySelector('.Modal-MenuMobile-block-js');

            // Search Modal
            if (e.target.closest('.search-btn-js')) {
                if(searchInfor) searchInfor.style.display = 'none';
                if(addCart) addCart.style.display = 'none';
                if(menuBar) menuBar.style.display = 'none';
                if(modalSearch) {
                   modalSearch.style.display = 'block';
                   modalSearch.style.animation = 'none';
                   void modalSearch.offsetWidth;
                   modalSearch.style.animation = 'slideIn linear 0.5s forwards';
                }
                if(modal) modal.style.display = 'block';
            }
            // Exit Search
            if (e.target.closest('.exit-btn-js')) {
                if(modalSearch) modalSearch.style.display = 'none';
                if(modal) modal.style.display = 'none';
            }
            // Mobile Menu
            if (e.target.closest('.menuBars')) {
                if(searchInfor) searchInfor.style.display = 'none';
                if(addCart) addCart.style.display = 'none';
                if(modalSearch) modalSearch.style.display = 'none';
                if(modal) modal.style.display = 'block';
                if(menuBar) menuBar.style.display = 'flex';
            }
            // Add Cart Quick View
            if (e.target.closest('.product-sale-tag-item-2-cart-js') || e.target.closest('.searchInfor-item__addCartBtn-js')) {
                e.preventDefault();
                const boxProduct = e.target.closest('.product-sale-item-level2') || document.querySelector('.searchInfor-list-js');
                const masp = boxProduct?.dataset?.masp || "SP01"; // Fallback to SP01 if not found
                
                try {
                    // Call backend API
                    await import('../services/cartService.js').then(m => m.addToCart(masp, 1));
                    
                    // Update header
                    await import('../services/cartService.js').then(async m => {
                        const cart = await m.getCart();
                        const cartCountEls = document.querySelectorAll('#cart-count');
                        cartCountEls.forEach(el => el.textContent = cart.length);
                        document.dispatchEvent(new Event('cartUpdated'));
                    });

                    if(searchInfor) searchInfor.style.display = 'none';
                    if(modalSearch) modalSearch.style.display = 'none';
                    if(menuBar) menuBar.style.display = 'none';
                    if(modal) modal.style.display = 'block';
                    if(addCart) addCart.style.display = 'flex';
                } catch(error) {
                    if (error.response?.status === 401) {
                        alert("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng");
                        window.location.href = "/dang-nhap";
                    } else {
                        console.error("Lỗi thêm giỏ hàng:", error);
                        alert("Không thể thêm vào giỏ hàng");
                    }
                }
            }
            // Search Info Quick View
            if (e.target.closest('.product-sale-tag-item-2-search-js')) {
                if(addCart) addCart.style.display = 'none';
                if(modalSearch) modalSearch.style.display = 'none';
                if(menuBar) menuBar.style.display = 'none';
                
                // Get product info
                const productElement = e.target.closest('.product-sale-item-level2');
                if (productElement) {
                    const img = productElement.querySelector('.product-sale_img-js')?.src;
                    const name = productElement.querySelector('.product-sale__name-js')?.textContent;
                    const priceSale = productElement.querySelector('.product-sale__price-sale-js')?.textContent;
                    const priceOriginal = productElement.querySelector('.product-sale__price-original-js')?.textContent;
                    const masp = productElement.dataset.masp || "SP01";

                    const searchInfoList = document.querySelector('.searchInfor-list-js');
                    if (searchInfoList) searchInfoList.dataset.masp = masp; // Save masp for add to cart inside modal

                    if (img) {
                        const largeImg = document.getElementById('largeImage');
                        if (largeImg) largeImg.src = img;
                        const optImg = document.querySelector('.searchInfor-item__IMG-option-item-img-js');
                        if(optImg) optImg.src = img;
                    }
                    if (name) {
                        const nameEl = document.querySelector('.searchInfor-item__name-js');
                        if (nameEl) nameEl.textContent = name;
                    }
                    if (priceSale) {
                        const priceSaleEl = document.querySelector('.searchInfor-item__priceSale-js');
                        if (priceSaleEl) priceSaleEl.textContent = priceSale;
                    }
                    if (priceOriginal) {
                        const priceOrigEl = document.querySelector('.searchInfor-item__priceOriginal-js');
                        if (priceOrigEl) priceOrigEl.textContent = priceOriginal;
                    }
                }

                if(modal) modal.style.display = 'block';
                if(searchInfor) searchInfor.style.display = 'flex';
                const searchInfoList = document.querySelector('.searchInfor-list-js');
                if (searchInfoList) searchInfoList.style.display = 'flex';
            }

            // Exit Add Cart
            if (e.target.closest('.addCart-item__exit-js') || e.target.closest('.addCart-item__continueBuy')) {
                if(addCart) addCart.style.display = 'none';
                if(modal) modal.style.display = 'none';
            }

            // Exit Search Info
            if (e.target.closest('.searchInfor-list__level2-exit-js')) {
                if(searchInfor) searchInfor.style.display = 'none';
                if(modal) modal.style.display = 'none';
            }

            // Click Overlay to close all
            if (e.target.matches('.modal__overlay') || e.target.matches('.modal-overlay-js')) {
                if(modalSearch) modalSearch.style.display = 'none';
                if(menuBar) menuBar.style.display = 'none';
                if(addCart) addCart.style.display = 'none';
                if(searchInfor) searchInfor.style.display = 'none';
                if(modal) modal.style.display = 'none';
            }

            // Add to wishlist — click vào li.product-bestSale__tag-icon--noLike-js hoặc li.product-sale__tag-icon--noLike-js
            if (e.target.closest('.product-bestSale__tag-icon--noLike-js') || e.target.closest('.product-sale__tag-icon--noLike-js')) {
                e.preventDefault();
                e.stopPropagation();
                const liNoLike = e.target.closest('.product-bestSale__tag-icon--noLike-js') || e.target.closest('.product-sale__tag-icon--noLike-js');
                const boxProduct = e.target.closest('.product-sale-item-level2');
                if (boxProduct && liNoLike) {
                    const masp = boxProduct.dataset.masp;
                    if (!masp) { alert('Không tìm thấy mã sản phẩm!'); return; }
                    try {
                        const { addToWishlist, getWishlist } = await import('../services/wishlistService.js');
                        await addToWishlist(masp);
                        alert("Đã thêm vào danh sách yêu thích!");
                        const wl = await getWishlist();
                        document.querySelectorAll('#wishlist-count').forEach(el => el.textContent = wl.length);
                        document.dispatchEvent(new Event('wishlistUpdated'));
                        // Toggle icon: ẩn noLike, hiện Like
                        liNoLike.style.display = 'none';
                        const liLike = liNoLike.nextElementSibling;
                        if (liLike) liLike.style.display = 'inline-flex';
                    } catch(err) {
                        console.error(err);
                        alert("Sản phẩm đã có trong danh sách yêu thích.");
                    }
                }
            }

            // Remove from wishlist — click vào li.product-bestSale__tag-icon--Like-js hoặc li.product-sale__tag-icon--Like-js
            if (e.target.closest('.product-bestSale__tag-icon--Like-js') || e.target.closest('.product-sale__tag-icon--Like-js')) {
                e.preventDefault();
                e.stopPropagation();
                const liLike = e.target.closest('.product-bestSale__tag-icon--Like-js') || e.target.closest('.product-sale__tag-icon--Like-js');
                const boxProduct = e.target.closest('.product-sale-item-level2');
                if (boxProduct && liLike) {
                    const masp = boxProduct.dataset.masp;
                    if (!masp) return;
                    try {
                        const { removeFromWishlist, getWishlist } = await import('../services/wishlistService.js');
                        await removeFromWishlist(masp);
                        const wl = await getWishlist();
                        document.querySelectorAll('#wishlist-count').forEach(el => el.textContent = wl.length);
                        document.dispatchEvent(new Event('wishlistUpdated'));
                        // Toggle icon: ẩn Like, hiện noLike
                        liLike.style.display = 'none';
                        const liNoLike = liLike.previousElementSibling;
                        if (liNoLike) liNoLike.style.display = 'inline-flex';
                    } catch(err) {
                        console.error(err);
                    }
                }
            }
        };

        // Initialize header counters
        import('../services/wishlistService.js').then(async m => {
            try {
                const wl = await m.getWishlist();
                const countEls = document.querySelectorAll('#wishlist-count');
                countEls.forEach(el => el.textContent = wl.length);
            } catch(e) {}
        });

        import('../services/cartService.js').then(async m => {
            try {
                const cart = await m.getCart();
                const cartCountEls = document.querySelectorAll('#cart-count');
                cartCountEls.forEach(el => el.textContent = cart.length);
            } catch(e) {}
        });

        document.addEventListener('click', handleClick);
        return () => {
            document.removeEventListener('click', handleClick);
        };
    }, []);

    return (
        <>
        <div className="modal modal-js" style={{ display: "none" }}>
            <div className="modal__overlay modal-overlay-js">           
        
                <div className="modal__body modal__body-js">

                    
                    <div className="grid">
                        <div className="row">
                            <div className="Modal-Search Modal-Search-js">
                                <ul className="search-list">
                                    <li className="search-item">
                                        <input 
                                            type="text" 
                                            id="search-input" 
                                            className="Search-input" 
                                            placeholder="Bạn muốn tìm gì?" 
                                            value={searchQuery}
                                            onChange={(e) => setSearchQuery(e.target.value)}
                                            onKeyDown={handleKeyDown}
                                        />
                                        <label htmlFor="search-input" className="search-item-icon" onClick={handleSearchSubmit} style={{cursor: "pointer"}}>
                                            <i className="search-icon fa-solid fa-magnifying-glass"></i> 
                                        </label>
                                    </li>
                                    <p className="exit-btn exit-btn-js">
                                        <i className="exit-icon fa-solid fa-x"></i>
                                    </p>
                                </ul>
                                    
                            </div> 
                        </div>
                    </div>

                    
                    <div className="grid">
                        <div className="row">
                            <div className="addCart addCart-js">
                                <ul className="addCart-list addCart-list-js">
                                    <li className="addCart-item">
                                        <div className="addCart-item__title">
                                            <i className="addCart-item__title-check-icon fa-regular fa-circle-check"></i>
                                            Thêm vào giỏ hàng thành công
                                        </div>
                                        <div className="addCart-item__exit addCart-item__exit-js">
                                            <i className="addCart-item__exit-icon fa-solid fa-xmark"></i>
                                        </div>
                                    </li>
                                    <li className="addCart-item">
                                        <div className="addCart-item__img-block">
                                            <img id="largeImageaddCart" src="../../assets/IMG/productSale_1.webp" alt="" className="addCart-item__img" />
                                        </div>
                                        <div className="addCart-item__des-product">
                                            <div className="addCart-item__name">Bánh Sừng Bò Mini</div>
                                            <div className="addCart-item__price">36.000₫</div>
                                        </div>
                                    </li>
                                    <li className="addCart-item">
                                        <div className="addCart-item__des addCart-item__des-js">
                                            <a href="addCart-item__des-link">
                                                Giỏ hàng của bạn hiện có <span className="highligh">3</span> sản phẩm</a>
                                        </div>
                                        <div className="addCart-item__btn">
                                            <div className="addCart-item__continueBuy">
                                                Tiếp tục mua hàng
                                            </div>
                                            <div className="addCart-item__payNow">
                                                <a href="" className="addCart-item__payNow-link">
                                                    Thanh toán ngay
                                                </a>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    
                    <div className="grid">
                        <div className="row">
                            <div className="searchInfor searchInfor-js">
                                <ul className="searchInfor-list searchInfor-list-js">
                                    <li className="searchInfor-item">
                                        <div className="searchInfor-item__IMG">
                                                <img id="largeImage" src="../../assets/IMG/productSale_1.webp" alt="" className="searchInfor-img  " />
                                                <ul className="searchInfor-item__IMG-option">
                                                    <li className="searchInfor-item__IMG-option-item">
                                                        <img src="../../assets/IMG/productSale_1.webp" alt="" className="searchInfor-item__IMG-option-item-img searchInfor-item__IMG-option-item-img-js" onClick={() => {}} />
                                                    </li>
                                                    <li className="searchInfor-item__IMG-option-item">
                                                        <img src="../../assets/IMG/productInfor0.webp" alt="" className="searchInfor-item__IMG-option-item-img searchInfor-item__IMG-option-item-img1-js" onClick={() => {}} />
                                                    </li>
                                                </ul>
                                        </div>
                                    </li>
                                    <li className="searchInfor-item">
                                        <ul className="searchInfor-list__level2">
                                            <li className="searchInfor-item__level2">
                                                <div className="searchInfor-item__name searchInfor-item__name-js">Bánh Sừng Bò Mini</div>
                                            </li>
                                            <li className="searchInfor-item__level2">
                                                <div className="searchInfor-item__productStatus">
                                                    Tình trạng: <span className="searchInfor-item__productStatus-title">Còn hàng</span>
                                                </div>
                                                <div className="searchInfor-item__productCode">
                                                    Mã sản phẩm: <span className="searchInfor-item__productCode-title">Đang cập nhật</span>
                                                </div>
                                            </li>
                                            <li className="searchInfor-item__level2">
                                                <div className="searchInfor-item__priceSale searchInfor-item__priceSale-js">36.000₫</div>
                                                <div className="searchInfor-item__priceOriginal searchInfor-item__priceOriginal-js">40.000₫</div>
                                            </li>
                                            <li className="searchInfor-item__level2">
                                                <div className="searchInfor-item__productInfor">Thông tin sản phẩm đang cập nhật</div>
                                            </li>
                                            <li className="searchInfor-item__level2">
                                                <div className="searchInfor-item__productQuantity">
                                                    <span className="searchInfor-item__productQuantity-number">Số lượng:</span>
                                                    <ul className="CustomizeQuantity">
                                                        <li className="CustomizeQuantity-remove CustomizeQuantity-remove-js">
                                                            <i className="CustomizeQuantity-icon fa-solid fa-minus"></i>
                                                        </li>
                                                        <li className="CustomizeQuantity-number CustomizeQuantity-number-js">1</li>
                                                        <li className="CustomizeQuantity-add CustomizeQuantity-add-js">
                                                            <i className="CustomizeQuantity-icon fa-solid fa-plus"></i>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </li>
                                            <li className="searchInfor-item__level2">
                                                <div className="searchInfor-item__addCartBtn searchInfor-item__addCartBtn-js">Thêm Vào Giỏ Hàng</div>
                                            </li>
                                            <span className="searchInfor-list__level2-exit searchInfor-list__level2-exit-js">
                                                <i className="searchInfor-list__level2-exit-icon fa-solid fa-x"></i>
                                            </span>
                                        </ul>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                    
                <div className="grid">
                    <div className="row" >
                        <div className="Modal-MenuMobile-block Modal-MenuMobile-block-js" style={{ flexDirection: "column" }}>
                                    <div className="Modal-MenuMobile-block__header">
                                        <div>
                                            <a href=" register.html">Đăng ký</a>
                                        </div>
                                        <div>
                                            <a href=" Login.html">Đăng nhập</a>
                                        </div>
                                        <div>
                                            <a href="">Menu chính</a>
                                        </div>
                                    </div>
                                    <div className="Modal-MenuMobile-block__content" style={{ padding: "0 8px" }}>
                                        <ul className="Modal-MenuMobile-block__content-list" style={{ borderBottom: "1px solid var(--primary-color)" }}>
                                            <li className="Modal-MenuMobile-block__content-item">
                                                <a href=" Trang-Chu.html" className="Modal-MenuMobile-block__content-item-link">Trang chủ</a>
                                            </li>
                                            <li className="Modal-MenuMobile-block__content-item">
                                                <a href=" Gioi-thieu.html" className="Modal-MenuMobile-block__content-item-link">Giới thiệu</a>
                                            </li>
                                            <li className="Modal-MenuMobile-block__content-item">
                                                <a href=" San-Pham.html" className="Modal-MenuMobile-block__content-item-link">Sản phẩm</a>
                                            </li>
                                            <li className="Modal-MenuMobile-block__content-item">
                                                <a href=" Tin-tuc.html" className="Modal-MenuMobile-block__content-item-link">Tin tức</a>
                                            </li>
                                            <li className="Modal-MenuMobile-block__content-item">
                                                <a href=" Lien-he.html" className="Modal-MenuMobile-block__content-item-link">Liên hệ</a>
                                            </li>
                                            <li className="Modal-MenuMobile-block__content-item">
                                                <a href="#" className="Modal-MenuMobile-block__content-item-link">Hệ thống cửa hàng</a>
                                            </li>
                                            <li className="Modal-MenuMobile-block__content-item">
                                                <a href=" Cau-hoi-thuong-gap.html" className="Modal-MenuMobile-block__content-item-link">Câu hỏi thường gặp</a>
                                            </li>
                                        </ul>
                                        <ul className="Modal-MenuMobile-block__content-list">
                                            <li className="Modal-MenuMobile-block__content-item">
                                                <a href=" Yeu-Thich.html" className="Modal-MenuMobile-block__content-item-link">Sản phẩm yêu thích</a>
                                            </li>
                                            <li className="Modal-MenuMobile-block__content-item">
                                                <a href=" Cart.html" className="Modal-MenuMobile-block__content-item-link">Danh sách giỏ hàng</a>
                                            </li>
                                        </ul>
                                    </div>
                        </div>
                        
                    </div>
                </div>
            </div>
       </div>
    
        </>
    );
};

export default Modal;
