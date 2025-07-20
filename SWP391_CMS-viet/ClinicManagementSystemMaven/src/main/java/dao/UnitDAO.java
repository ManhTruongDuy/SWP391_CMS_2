package dao;

import model.Unit;
import java.sql.*;
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
            e.printStackTrace();
        }
        return list;
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
            e.printStackTrace();
        }
        return list;
    }

    public boolean addUnit(Unit unit) {
        String sql = "INSERT INTO Unit (unitName) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, unit.getUnitName());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        unit.setIdUnit(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}