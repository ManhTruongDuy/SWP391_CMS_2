package dao;

import model.PrescriptionChat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionChatDAO extends DBContext {

    public List<PrescriptionChat> getChatsByPrescriptionId(int prescriptionId) {
        List<PrescriptionChat> chats = new ArrayList<>();
        String sql = "SELECT * FROM PrescriptionChat WHERE prescription_id = ? ORDER BY sent_time ASC";

        try {
            Connection conn = new DBContext().getConnection();

            if (conn == null || conn.isClosed()) {
                System.err.println("❌ Connection is null or closed in getChatsByPrescriptionId");
                return chats;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, prescriptionId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    PrescriptionChat chat = new PrescriptionChat();
                    chat.setId(rs.getInt("chat_id"));
                    chat.setPrescriptionId(rs.getInt("prescription_id"));
                    chat.setSenderId(rs.getInt("sender_account_id"));
                    chat.setSenderType(rs.getString("sender_role"));
                    chat.setMessage(rs.getString("message_text"));
                    chat.setTimestamp(rs.getTimestamp("sent_time"));
                    chats.add(chat);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in getChatsByPrescriptionId:");
            e.printStackTrace();
        }

        return chats;
    }

    public boolean insertChat(PrescriptionChat chat) {
        String sql = "INSERT INTO PrescriptionChat (prescription_id, sender_account_id, sender_role, message_text, sent_time) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, chat.getPrescriptionId());
            ps.setInt(2, chat.getSenderId());
            ps.setString(3, chat.getSenderType());
            ps.setString(4, chat.getMessage());
            ps.setTimestamp(5, chat.getTimestamp());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ SQL Error while inserting chat:");
            System.out.println("prescription_id = " + chat.getPrescriptionId());
            System.out.println("sender_account_id = " + chat.getSenderId());
            System.out.println("sender_role = " + chat.getSenderType());
            System.out.println("message_text = " + chat.getMessage());
            System.out.println("sent_time = " + chat.getTimestamp());
            e.printStackTrace();
            return false;
        }
    }
}
