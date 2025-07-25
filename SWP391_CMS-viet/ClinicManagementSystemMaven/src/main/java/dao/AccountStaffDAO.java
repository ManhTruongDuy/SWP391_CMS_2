package dao;

import model.AccountPharmacist;
import model.AccountStaff;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

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
        String sql = "SELECT * FROM AccountStaff WHERE email = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                if (BCrypt.checkpw(password, hashed)) {
                    return new AccountStaff(
                            rs.getInt("account_staff_id"),
                            rs.getString("username"),
                            hashed,
                            rs.getString("role"),
                            rs.getString("email"),
                            rs.getString("img"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException ex) {
            System.out.println("Login error: " + ex.getMessage());
        }
        return null;
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
    public boolean checkCurrentPassword(String email, String oldPassword) {
        String sql = "SELECT password FROM AccountStaff WHERE email = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                return BCrypt.checkpw(oldPassword, hashed);
            }
        } catch (SQLException ex) {
            System.out.println("Check current password error: " + ex.getMessage());
        }
        return false;
    }

    public boolean changePassword(String email, String newPassword) {
        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String sql = "UPDATE AccountStaff SET password = ? WHERE email = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, hashed);
            stm.setString(2, email);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Change password error: " + ex.getMessage());
            return false;
        }
    }
    public boolean updatePassword(String email, String newHashedPassword) {
        String sql = "UPDATE AccountStaff SET password = ? WHERE email = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, newHashedPassword);
            stm.setString(2, email);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error updating password for staff: " + ex.getMessage());
            return false;
        }
    }




}

