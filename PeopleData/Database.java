import java.sql.*;

public class Database {
    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/db_people", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // SELECT
    public ResultSet selectQuery(String sql, Object... params) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            bind(ps, params);
            ps.closeOnCompletion();
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // INSERT, UPDATE, DELETE
    public int insertUpdateDeleteQuery(String sql, Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            bind(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void bind(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
    }
}
