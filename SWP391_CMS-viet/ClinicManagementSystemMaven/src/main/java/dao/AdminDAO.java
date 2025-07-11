package dao;

import model.Medicine;
import model.Pharmacist;
import model.accounts.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private final Connection conn;

    public AdminDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    // Lấy tất cả thuốc
    public List<Medicine> getAllMedicines() {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM Medicine " +
                "ORDER BY medicine_id ";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medicine m = new Medicine(
                            rs.getInt("medicine_id"),
                            rs.getString("name"),
                            rs.getInt("unit_id"),
                            rs.getInt("category_id"),
                            rs.getString("ingredient"),
                            rs.getString("usage"),
                            rs.getString("preservation"),
                            rs.getDate("manuDate") != null ? rs.getDate("manuDate").toLocalDate() : null,
                            rs.getDate("expDate") != null ? rs.getDate("expDate").toLocalDate() : null,
                            rs.getInt("quantity"),
                            rs.getFloat("price"),
                            rs.getInt("warehouse_id")
                    );
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy thuốc theo ID
    public Medicine getMedicineById(int id) {
        String sql = "SELECT * FROM Medicine WHERE medicine_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicine(
                            rs.getInt("medicine_id"),
                            rs.getString("name"),
                            rs.getInt("unit_id"),
                            rs.getInt("category_id"),
                            rs.getString("ingredient"),
                            rs.getString("usage"),
                            rs.getString("preservation"),
                            rs.getDate("manuDate") != null ? rs.getDate("manuDate").toLocalDate() : null,
                            rs.getDate("expDate") != null ? rs.getDate("expDate").toLocalDate() : null,
                            rs.getInt("quantity"),
                            rs.getFloat("price"),
                            rs.getInt("warehouse_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả tk dược sĩ
    public List<PharmacistAccount> getAllPharmacists() {
        List<PharmacistAccount> list = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM Pharmacist p JOIN AccountPharmacist ap" +
                "ON p.account_pharmacist_id = ap.account_pharmacist_id" +
                "ORDER BY p.pharmacist_id " ;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PharmacistAccount pa = new PharmacistAccount(
                            rs.getInt("pharmacist_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    list.add(pa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
