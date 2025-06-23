package dao;

import model.Medicine;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class WarehouseDAO {
    public boolean addMedicine(Medicine med) {
        String sql = "INSERT INTO Medicine (medicine_id, name, unit_id, category_id, ingredient, usage, preservation, manuDate, expDate, quantity, price, warehouse_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, med.medicine_id);
            stmt.setString(2, med.name);
            stmt.setInt(3, med.unit_id);
            stmt.setInt(4, med.category_id);
            stmt.setString(5, med.ingredient);
            stmt.setString(6, med.usage);
            stmt.setString(7, med.preservation);
            stmt.setDate(8, Date.valueOf(med.manuDate));
            stmt.setDate(9, Date.valueOf(med.expDate));
            stmt.setInt(10, med.quantity);
            stmt.setFloat(11, med.price);
            stmt.setInt(12, med.warehouse_id);


            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm thuốc theo ID
    public Medicine findMedicineById(int id) {
        String sql = "SELECT * FROM Medicine WHERE medicine_id = ?";
        try (Connection conn = DBContext.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToMedicine(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm thuốc theo tên hoặc ID (có phân trang)
    public List<Medicine> searchMedicines(String keyword, int limit, int offset) {
        List<Medicine> results = new ArrayList<>();
        String sql = "SELECT * FROM Medicine WHERE name LIKE ? OR CAST(medicine_id AS VARCHAR) LIKE ? ORDER BY medicine_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBContext.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);
            stmt.setInt(3, offset);
            stmt.setInt(4, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(mapResultSetToMedicine(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Danh sách thuốc (có phân trang)
    public List<Medicine> listMedicines(int limit, int offset) {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM Medicine ORDER BY medicine_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBContext.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, offset);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToMedicine(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách thuốc hết hạn
    public List<Medicine> getExpiredMedicines() {
        List<Medicine> expiredList = new ArrayList<>();
        String sql = "SELECT * FROM Medicine WHERE expDate < GETDATE()";
        try (Connection conn = DBContext.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                expiredList.add(mapResultSetToMedicine(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expiredList;
    }

    // Map dữ liệu từ ResultSet sang đối tượng Medicine
    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException {
        return new Medicine(
                rs.getInt("medicine_id"),
                rs.getString("name"),
                rs.getInt("unit_id"),
                rs.getInt("category_id"),
                rs.getString("ingredient"),
                rs.getString("usage"),
                rs.getString("preservation"),
                rs.getDate("manuDate").toLocalDate(),
                rs.getDate("expDate").toLocalDate(),
                rs.getInt("quantity"),
                rs.getFloat("price"),
                rs.getInt("warehouse_id")
        );
    }
}
