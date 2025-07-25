package dao;

import model.Category;
import model.History;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private final Connection conn;

    public CategoryDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, categoryName FROM Category ORDER BY categoryName";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategory_id(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("categoryName"));
                list.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách danh mục: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Category getCategoryById(int id) {
        Category category = null;
        String sql = "SELECT category_id, categoryName FROM Category WHERE category_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    category = new Category();
                    category.setCategory_id(rs.getInt("category_id"));
                    category.setCategoryName(rs.getString("categoryName"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh mục: " + e.getMessage());
            e.printStackTrace();
        }
        return category;
    }

    public boolean addCategory(Category category) {
        String sql = "INSERT INTO Category (categoryName) VALUES (?)";
        HistoryDAO historyDAO = new HistoryDAO();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getCategoryName());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setCategory_id(generatedKeys.getInt(1));
                        // Ghi lịch sử
                        History history = new History(
                                "CREATE",
                                "Category",
                                category.getCategory_id(),
                                String.format("Thêm danh mục: %s", category.getCategoryName())
                        );
                        history.setActionTime(LocalDateTime.now());
                        historyDAO.addHistory(history);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm danh mục: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCategory(Category category) {
        String sql = "UPDATE Category SET categoryName = ? WHERE category_id = ?";
        HistoryDAO historyDAO = new HistoryDAO();
        // Lấy dữ liệu cũ
        Category oldCategory = getCategoryById(category.getCategory_id());
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getCategoryName());
            ps.setInt(2, category.getCategory_id());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                // Ghi lịch sử
                String details = oldCategory != null ?
                        String.format("Cập nhật danh mục: %s -> %s", oldCategory.getCategoryName(), category.getCategoryName())
                        : String.format("Cập nhật danh mục: ID %d, Tên: %s", category.getCategory_id(), category.getCategoryName());
                History history = new History(
                        "UPDATE",
                        "Category",
                        category.getCategory_id(),
                        details
                );
                history.setActionTime(LocalDateTime.now());
                historyDAO.addHistory(history);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật danh mục: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCategory(int id) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Medicine WHERE category_id = ?";
        String sql = "DELETE FROM Category WHERE category_id = ?";
        HistoryDAO historyDAO = new HistoryDAO();

        // Kiểm tra ràng buộc khóa ngoại
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, id);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Không thể xóa danh mục: Có thuốc đang sử dụng danh mục này.");
                }
            }
        }

        // Lấy thông tin danh mục trước khi xóa
        Category category = getCategoryById(id);
        String details = category != null ?
                String.format("Xóa danh mục: %s", category.getCategoryName())
                : String.format("Xóa danh mục: ID %d", id);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                // Ghi lịch sử
                History history = new History(
                        "DELETE",
                        "Category",
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