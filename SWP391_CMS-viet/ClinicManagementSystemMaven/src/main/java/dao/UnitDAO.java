package dao;

import model.Unit;
import model.History;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UnitDAO {
    private final Connection conn;

    public UnitDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    public List<Unit> getAllUnits() {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT unit_id, unitName FROM Unit ORDER BY unitName";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setIdUnit(rs.getInt("unit_id"));
                unit.setUnitName(rs.getString("unitName"));
                list.add(unit);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách đơn vị: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Unit getUnitById(int id) {
        Unit unit = null;
        String sql = "SELECT unit_id, unitName FROM Unit WHERE unit_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    unit = new Unit();
                    unit.setIdUnit(rs.getInt("unit_id"));
                    unit.setUnitName(rs.getString("unitName"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy đơn vị: " + e.getMessage());
            e.printStackTrace();
        }
        return unit;
    }

    public List<Unit> getUnitsByName(String name) {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT unit_id, unitName FROM Unit WHERE unitName LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Unit unit = new Unit();
                    unit.setIdUnit(rs.getInt("unit_id"));
                    unit.setUnitName(rs.getString("unitName"));
                    list.add(unit);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm đơn vị theo tên: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public boolean addUnit(Unit unit) {
        String sql = "INSERT INTO Unit (unitName) VALUES (?)";
        HistoryDAO historyDAO = new HistoryDAO();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, unit.getUnitName());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        unit.setIdUnit(generatedKeys.getInt(1));
                        // Ghi lịch sử
                        History history = new History(
                                "CREATE",
                                "Unit",
                                unit.getIdUnit(),
                                String.format("Thêm đơn vị: %s", unit.getUnitName())
                        );
                        history.setActionTime(LocalDateTime.now());
                        historyDAO.addHistory(history);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm đơn vị: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUnit(Unit unit) {
        String sql = "UPDATE Unit SET unitName = ? WHERE unit_id = ?";
        HistoryDAO historyDAO = new HistoryDAO();
        // Lấy dữ liệu cũ
        Unit oldUnit = getUnitById(unit.getIdUnit());
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, unit.getUnitName());
            ps.setInt(2, unit.getIdUnit());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                // Ghi lịch sử
                String details = oldUnit != null ?
                        String.format("Cập nhật đơn vị: %s -> %s", oldUnit.getUnitName(), unit.getUnitName())
                        : String.format("Cập nhật đơn vị: ID %d, Tên: %s", unit.getIdUnit(), unit.getUnitName());
                History history = new History(
                        "UPDATE",
                        "Unit",
                        unit.getIdUnit(),
                        details
                );
                history.setActionTime(LocalDateTime.now());
                historyDAO.addHistory(history);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật đơn vị: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUnit(int id) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Medicine WHERE unit_id = ?";
        String sql = "DELETE FROM Unit WHERE unit_id = ?";
        HistoryDAO historyDAO = new HistoryDAO();

        // Kiểm tra ràng buộc khóa ngoại
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, id);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Không thể xóa đơn vị: Có thuốc đang sử dụng đơn vị này.");
                }
            }
        }

        // Lấy thông tin đơn vị trước khi xóa
        Unit unit = getUnitById(id);
        String details = unit != null ?
                String.format("Xóa đơn vị: %s", unit.getUnitName())
                : String.format("Xóa đơn vị: ID %d", id);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                // Ghi lịch sử
                History history = new History(
                        "DELETE",
                        "Unit",
                        id,
                        details
                );
                history.setActionTime(LocalDateTime.now());
                historyDAO.addHistory(history);
                return true;
            }
        }
        return false;
    }
}