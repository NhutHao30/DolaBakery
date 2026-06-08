document.addEventListener("DOMContentLoaded", function () {

    const form = document.querySelector("form");

    if (form) {

        form.addEventListener("submit", function (e) {

            const username =
                document.querySelector('input[name="username"]').value.trim();

            const password =
                document.querySelector('input[name="password"]').value.trim();

            const hoTen =
                document.querySelector('input[name="HOTEN"]').value.trim();

            const sdt =
                document.querySelector('input[name="SDT"]').value.trim();

            const email =
                document.querySelector('input[name="EMAIL"]').value.trim();

            // Kiểm tra rỗng
            if (
                username === "" ||
                password === "" ||
                hoTen === "" ||
                sdt === "" ||
                email === ""
            ) {
                e.preventDefault();
                alert("Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // Kiểm tra email
            const emailRegex =
                /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            if (!emailRegex.test(email)) {
                e.preventDefault();
                alert("Email không hợp lệ!");
                return;
            }

            // Kiểm tra số điện thoại
            const phoneRegex = /^[0-9]{10,11}$/;

            if (!phoneRegex.test(sdt)) {
                e.preventDefault();
                alert("Số điện thoại không hợp lệ!");
                return;
            }

            // Kiểm tra mật khẩu
            if (password.length < 6) {
                e.preventDefault();
                alert("Mật khẩu phải từ 6 ký tự trở lên!");
                return;
            }

        });

    }

});