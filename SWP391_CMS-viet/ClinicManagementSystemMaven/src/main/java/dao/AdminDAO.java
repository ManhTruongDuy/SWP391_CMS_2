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

    // Cập nhật giá thuốc
    public Boolean updateMedicinePriceById(int id, float price) {
        String sql = "UPDATE Medicine SET price=? WHERE medicine_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setFloat(1, price);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
