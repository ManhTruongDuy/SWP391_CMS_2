package dao;

import model.History;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO {
    private final Connection conn;

    public HistoryDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    public List<History> searchHistories(String actionType, String entityType, String date, int page, int pageSize) {
        List<History> histories = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, action_type, entity_type, entity_id, details, action_time FROM history WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (actionType != null && !actionType.isEmpty()) {
            sql.append(" AND action_type = ?");
            params.add(actionType);
        }
        if (entityType != null && !entityType.isEmpty()) {
            sql.append(" AND entity_type = ?");
            params.add(entityType);
        }
        if (date != null && !date.isEmpty()) {
            sql.append(" AND DATE(action_time) = ?");
            params.add(date);
        }

        sql.append(" ORDER BY action_time DESC LIMIT ? OFFSET ?");
        params.add(String.valueOf(pageSize));
        params.add(String.valueOf((page - 1) * pageSize));

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    History history = new History();
                    history.setHistoryId(rs.getInt("id"));
                    history.setActionType(rs.getString("action_type"));
                    history.setEntityType(rs.getString("entity_type"));
                    history.setEntityId(rs.getInt("entity_id"));
                    history.setDetails(rs.getString("details"));
                    history.setActionTime(rs.getTimestamp("action_time").toLocalDateTime());
                    histories.add(history);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm lịch sử: " + e.getMessage());
            e.printStackTrace();
        }
        return histories;
    }

    public int countHistories(String actionType, String entityType, String date) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM history WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (actionType != null && !actionType.isEmpty()) {
            sql.append(" AND action_type = ?");
            params.add(actionType);
        }
        if (entityType != null && !entityType.isEmpty()) {
            sql.append(" AND entity_type = ?");
            params.add(entityType);
        }
        if (date != null && !date.isEmpty()) {
            sql.append(" AND DATE(action_time) = ?");
            params.add(date);
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm lịch sử: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addHistory(History history) {
        String sql = "INSERT INTO history (action_type, entity_type, entity_id, details, action_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, history.getActionType());
            ps.setString(2, history.getEntityType());
            ps.setInt(3, history.getEntityId());
            ps.setString(4, history.getDetails());
            ps.setTimestamp(5, Timestamp.valueOf(history.getActionTime()));
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        history.setHistoryId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm lịch sử: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}