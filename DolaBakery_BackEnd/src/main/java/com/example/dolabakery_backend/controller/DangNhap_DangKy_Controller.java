package com.example.dolabakery_backend.controller;

import com.example.dolabakery_backend.Models.KhachHang;
import com.example.dolabakery_backend.Models.TaiKhoan;
import com.example.dolabakery_backend.repository.KhachHangRepository;
import com.example.dolabakery_backend.repository.TaiKhoanRepository;

import com.example.dolabakery_backend.service.OTP_Email;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DangNhap_DangKy_Controller {



    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private OTP_Email emailService;

    //TRANG LOGIN
    @GetMapping("/login")
    public String loginPage() {

        return "DangNhap/DangNhap";
    }

    // XỬ LÝ ĐĂNG NHẬP
    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model
    ) {
        TaiKhoan tk = taiKhoanRepository.findByUserNameAndPassword(username, password);

        // Sai tài khoản
        if (tk == null) {
            model.addAttribute(
                    "error",
                    "Sai tài khoản hoặc mật khẩu!"
            );
            return "DangNhap/DangNhap";
        }
        // Kiểm tra đăng nhập thông thường
        // Lưu session
        session.setAttribute("user", tk);

        // ADMIN
        if (tk.getMaRole() == 0) {
            return "Welcome";
        }
        // NHÂN VIÊN
        else if (tk.getMaRole() == 1) {
            return "redirect:/nhanvien";
        }
        // KHÁCH HÀNG
        else if(tk.getMaRole() == 2){
            return "redirect:/khachhang";
        }
        else{
            return "redirect:/";
        }
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
    //----------------------------------------------------------------------//

    // TRANG ĐỔI MẬT KHẨU
    @GetMapping("/changePassword")
    public String doiMatKhauPage() {
        return "DangNhap/DoiMatKhau";
    }


    // XỬ LÝ ĐỔI MẬT KHẨU
    @PostMapping("/changePassword")
    public String doiMatKhau(@RequestParam String newPassword,
                             @RequestParam String rePassword,
                             HttpSession session,
                             Model model)
    {
        //Kiểm tra xác thực OTP
        Boolean otpVerified =
                (Boolean) session.getAttribute(
                        "otpVerified"
                );

        if(otpVerified == null || !otpVerified){

            model.addAttribute(
                    "error",
                    "Bạn chưa xác thực OTP!"
            );

            return "DangNhap/DoiMatKhau";
        }

        // Kiểm tra nhập lại mật khẩu
        if (!newPassword.equals(rePassword)) {
            model.addAttribute(
                    "error",
                    "Mật khẩu nhập lại không khớp!"
            );
            return "DangNhap/DoiMatKhau";
        }

        // Tìm tài khoản
        String email =
                (String) session.getAttribute("email");

        TaiKhoan tk =
                taiKhoanRepository.findByEmail(email);

        if(tk == null){

            model.addAttribute(
                    "error",
                    "Không tìm thấy tài khoản!"
            );

            return "DangNhap/DoiMatKhau";
        }

        tk.setPassword(newPassword);

        taiKhoanRepository.save(tk);

        session.removeAttribute("otp");
        session.removeAttribute("email");
        session.removeAttribute("otpVerified");

        model.addAttribute(
                "success",
                "Đổi mật khẩu thành công!"
        );

        return "DangNhap/DangNhap";
    }
    //Xử lí API gửi OTP
    @PostMapping("/sendOTP")
    @ResponseBody
    public String sendOTP(@RequestParam("email") String email, HttpSession session, Model model){
        TaiKhoan tk = taiKhoanRepository.findByEmail(email);

        //Xử lí không tìm thấy email
        if(tk == null){
            model.addAttribute("error", "Email không tồn tại!");
            return "email_not_found";
        }

        //Xử lí nhận mã OTP
        String otp = String.valueOf((int)(Math.random()*900000)+100000);
        session.setAttribute("otp", otp);
        session.setAttribute("email", email);

        emailService.sendOTP(email, otp);

        return "success";
    }

    //Xử lí xác nhận OTP
    @PostMapping("/verify-otp")
    @ResponseBody
    public String verifyOTP(
            @RequestParam String otp,
            HttpSession session
    ) {

        String sessionOTP =
                (String) session.getAttribute("otp");

        if(sessionOTP == null){
            return "expired";
        }

        if(sessionOTP.equals(otp)){
            // ĐÁNH DẤU ĐÃ XÁC THỰC OTP
            session.setAttribute(
                    "otpVerified",
                    true
            );

            return "success";
        }

        return "failed";
    }
//----------------------------------------------------------------------//

    //Trang Đăng Ký
    private String generateMaKH(){
        long count = khachHangRepository.count() + 1;
        return String.format("KH%03d", count);
    }

    @GetMapping("/register")
    public String registerPage() {

        return "DangKy/DangKy";
    }
    //Xử lý đăng ký tài khoản
    @PostMapping("/register")
    public String register(  @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("repassword") String repassword,
                             @RequestParam("HOTEN") String hoTen,
                             @RequestParam("GioiTinh") String gioiTinh,
                             @RequestParam("SDT") String sdt,
                             @RequestParam("EMAIL") String email,
                             Model model){

        //Check password
        if(!password.equals(repassword)){
            model.addAttribute("error", "Mật khẩu nhập lại không chính xác!");
            return "DangKy/DangKy";
        }

        //Check username đã tồn tại
        if(taiKhoanRepository.findByUserName(username) !=null){
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "DangKy/DangKy";
        }

        //Check email tồn tại
        if(taiKhoanRepository.findByEmail(email) != null){
            model.addAttribute("error", "Email đã tồn tại!");
            return "DangKy/DangKy";
        }

        //Tạo tài khoản
        TaiKhoan tk = new TaiKhoan();
        tk.setUserName(username);
        tk.setPassword(password);
        tk.setEmail(email);
        //Set mã role mặc định = 2 cho khách hàng
        tk.setMaRole(2);
        taiKhoanRepository.save(tk);

        //Tạo khách hàng
        KhachHang kh = new KhachHang();
        kh.setMaKH(generateMaKH());
        kh.setHoTen(hoTen);
        kh.setSdt(sdt);
        kh.setDiaChi("");
        kh.setDiemTichLuy(0);
        kh.setNgaySinh(null);
        kh.setGioiTinh(gioiTinh);
        kh.setUserName(username);

        khachHangRepository.save(kh);
        model.addAttribute(
                "success",
                "Đăng ký thành công!"
        );

        return "DangNhap/DangNhap";
    }
//----------------------------------------------------------------------//
    //Xử lý đăng ký bằng GooGle

    //Hiển thị form
    @GetMapping("/google-register-form")
    public String googleRegisterForm(HttpSession session, Model model){
        model.addAttribute("email", session.getAttribute("email"));
        model.addAttribute("name", session.getAttribute("name"));
        return "DangKy/DangKyGoogle";
    }

    //Đưa dữ liệu xuống Db
    @PostMapping("/google-register")
    public String googleRegister(@RequestParam String sdt, @RequestParam String gioiTinh, HttpSession session, Model model){
        String email = (String) session.getAttribute("email");
        String name = (String) session.getAttribute("name");

        if(taiKhoanRepository.findByEmail(email) != null) {
            return "redirect:/login";
        }
                                            //d: Ký tự số, 10: đúng 10 số
        if(sdt == null || !sdt.matches("\\d{10}")){

            model.addAttribute(
                    "error",
                    "Vui lòng nhập số điện thoại đủ 10 chữ số!"
            );

            model.addAttribute("email", session.getAttribute("email"));
            model.addAttribute("name", session.getAttribute("name"));

            return "DangKy/DangKyGoogle";
        }

        String username = email.split("@")[0];
        TaiKhoan tk = new TaiKhoan();
        tk.setUserName(username);
        tk.setPassword(null);
        tk.setEmail(email);
        //Set mã role mặc định = 2 cho khách hàng
        tk.setMaRole(2);
        taiKhoanRepository.save(tk);

        //Tạo khách hàng
        KhachHang kh = new KhachHang();
        kh.setMaKH(generateMaKH());
        kh.setHoTen(name);
        kh.setSdt(sdt);
        kh.setDiaChi("");
        kh.setDiemTichLuy(0);
        kh.setNgaySinh(null);
        kh.setGioiTinh(gioiTinh);
        kh.setUserName(username);

        khachHangRepository.save(kh);
        session.setAttribute("user", tk);

        return "redirect:/WelcomeKhachHang";
    }

    //Trả về tên Email và tên User đã mapping qua form Đăng ký của Google
    @GetMapping("/google-success")
    public String googleSuccess(
            OAuth2AuthenticationToken authentication, HttpSession session, Model model
    ) {
        String email =
                authentication.getPrincipal()
                        .getAttribute("email");

        String name =
                authentication.getPrincipal()
                        .getAttribute("name");

        //Kiểm tra tài khoản tồn tại chưa
        TaiKhoan tk = taiKhoanRepository.findByEmail(email);
        if(tk != null)
        {
            session.setAttribute("user", tk);
            return "redirect:http://localhost:5173/";
        }
        session.setAttribute("email", email);
        session.setAttribute("name", name);
        return "redirect:http://localhost:5173/dang-ky-google";
    }



    //Test Giao Diện Với WelcomeKhachHang
    @GetMapping("/WelcomeKhachHang")
    public String welcome(
            HttpSession session,
            Model model
    ){
        TaiKhoan tk =
                (TaiKhoan) session.getAttribute("user");
        KhachHang kh =
                khachHangRepository.findByUserName(
                        tk.getUserName()
                );

        model.addAttribute(
                "name",
                kh.getHoTen()
        );

        return "WelcomeKhachHang";
    }


//----------------------------------------------------------------------------------//

}

