package util;

import dao.AccountPharmacistDAO;
import dao.AccountStaffDAO;
import model.AccountPharmacist;
import model.AccountStaff;
import org.mindrot.jbcrypt.BCrypt;
import dao.DBContext;
import java.util.List;

public class HashAllAccounts {
    public static void main(String[] args) {
        hashPharmacists();
        hashStaff();
        System.out.println("✅ Tất cả mật khẩu đã được mã hóa.");
    }

    private static void hashPharmacists() {
        AccountPharmacistDAO dao = new AccountPharmacistDAO();
        List<AccountPharmacist> accounts = dao.getAllAccount(); // chuẩn rồi nè

        for (AccountPharmacist acc : accounts) {
            String plainPassword = acc.getPassword();
            if (plainPassword != null && !plainPassword.startsWith("$2a$")) {
                String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                boolean updated = dao.updatePassword(acc.getEmail(), hashed); // dùng theo email như bạn đang có
                if (updated) {
                    System.out.println("🔒 Đã mã hóa: " + acc.getEmail());
                }
            }
        }
    }

    private static void hashStaff() {
        AccountStaffDAO dao = new AccountStaffDAO();
        List<AccountStaff> accounts = dao.getAllAccount();

        for (AccountStaff acc : accounts) {
            String plainPassword = acc.getPassword();
            if (plainPassword != null && !plainPassword.startsWith("$2a$")) {
                String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                String email = acc.getEmail();
                boolean updated = dao.updatePassword(email, hashed);
                if (updated) {
                    System.out.println("🔒 Đã mã hóa (staff): " + email);
                } else {
                    System.out.println("❌ Lỗi mã hóa (staff): " + email);
                }
            }
        }
    }

}
