package dao;

import model.AccountPharmacist;
import model.AccountStaff;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountStaffDAO extends DBContext{
    public List<AccountStaff> getAllAccount() {
        List<AccountStaff> listAccount = new ArrayList<>();
        String sql = "SELECT * FROM AccountStaff";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AccountStaff newAccount = new AccountStaff(
                        rs.getInt("account_staff_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("img"),
                        rs.getString("status")
                );

                listAccount.add(newAccount);
            }

            rs.close();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("Error loading user profiles: " + ex.getMessage());
        }

        return listAccount;
    }


    public AccountStaff getAccountByEmailAndPassword(String email, String password) {
        AccountStaff account = null;
        String sql = "SELECT * FROM AccountStaff WHERE email = ? AND password = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                account = new AccountStaff(
                        rs.getInt("account_staff_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("img"),
                        rs.getString("status")
                );
            }
        } catch (SQLException ex) {
            System.out.println("Error getting account: " + ex.getMessage());
        }

        return account;
    }
    public boolean isEmailExist(String email) {
        String sql = "SELECT 1 FROM AccountStaff WHERE email = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.out.println("Error checking email existence: " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        AccountStaffDAO dao = new AccountStaffDAO();
        List<AccountStaff> list = dao.getAllAccount();

        if (list.isEmpty()) {
            System.out.println("Không có tài khoản staff nào trong CSDL.");
        } else {
            System.out.println("Danh sách tài khoản staff:");
            for (AccountStaff acc : list) {
                System.out.println("Username: " + acc.getUsername() + ", Email: " + acc.getEmail());
            }
        }
    }

}

