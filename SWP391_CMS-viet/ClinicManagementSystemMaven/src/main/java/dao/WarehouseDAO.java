package dao;

import model.Warehouse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO {
    private final Connection conn;

    public WarehouseDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    // Lấy thông tin kho hàng theo ID
    public Warehouse getWarehouseById(int id) {
        String sql = "SELECT warehouse_id, name, location FROM Warehouse WHERE warehouse_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Warehouse warehouse = new Warehouse();
                    warehouse.setId(rs.getInt("warehouse_id"));
                    warehouse.setName(rs.getString("name"));
                    warehouse.setLocation(rs.getString("location"));
                    return warehouse;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả kho hàng (tùy chọn nếu cần mở rộng)
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> list = new ArrayList<>();
        String sql = "SELECT warehouse_id, name, location FROM Warehouse";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Warehouse warehouse = new Warehouse();
                warehouse.setId(rs.getInt("warehouse_id"));
                warehouse.setName(rs.getString("name"));
                warehouse.setLocation(rs.getString("location"));
                list.add(warehouse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}