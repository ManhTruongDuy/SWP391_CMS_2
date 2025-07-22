package dao;

import model.AccountPatient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountPatientDAO extends DBContext {

    /* ---------------- GET ALL ---------------- */
    public List<AccountPatient> getAllAccount() {
        List<AccountPatient> listAccount = new ArrayList<>();
        String sql = "SELECT * FROM AccountPatient";
        try (PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                AccountPatient account = new AccountPatient(
                        rs.getInt("account_patient_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("status")
                );
                listAccount.add(account);
            }
        } catch (SQLException ex) {
            System.out.println("Error loading patient accounts: " + ex.getMessage());
        }
        return listAccount;
    }

    /* ---------------- GET BY EMAIL AND PASSWORD ---------------- */
    public AccountPatient getAccountByEmailAndPassword(String email, String password) {
        AccountPatient account = null;
        String sql = "SELECT * FROM AccountPatient WHERE email = ? AND password = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            stm.setString(2, password);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    account = new AccountPatient(
                            rs.getInt("account_patient_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error getting account: " + ex.getMessage());
        }
        return account;
    }

    /* ---------------- CHECK EMAIL EXISTENCE ---------------- */
    public boolean isEmailExist(String email) {
        String sql = "SELECT 1 FROM AccountPatient WHERE email = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.out.println("Error checking email existence: " + ex.getMessage());
            return false;
        }
    }

    /* ---------------- MAIN TEST METHOD ---------------- */
    public static void main(String[] args) {
        AccountPatientDAO dao = new AccountPatientDAO();
        List<AccountPatient> list = dao.getAllAccount();

        if (list.isEmpty()) {
            System.out.println("Không có tài khoản bệnh nhân nào trong CSDL.");
        } else {
            System.out.println("Danh sách tài khoản bệnh nhân:");
            for (AccountPatient acc : list) {
                System.out.println("Username: " + acc.getUsername() + ", Email: " + acc.getEmail());
            }
        }
    }
}