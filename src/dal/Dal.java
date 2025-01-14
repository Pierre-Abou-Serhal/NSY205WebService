package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dal {

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    
    /**
     * Basic constructor. This assumes you'll have separate user/password 
     * for connecting to the DB. If you have an "all in one" connection string, 
     * you can adapt accordingly.
     */
    public Dal(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    /**
     * Execute a query that returns multiple rows of type T.
     * 
     * - query: The SQL statement (e.g. "{call YourStoredProcedure(?)}" if using SP)
     * - parameters: A map of parameter name -> parameter value (or you can use a list).
     * - queryType: SELECT/UPDATE/DELETE
     * - rowMapper: A small functional interface for mapping from ResultSet -> T
     */
    public <T> List<T> executeSqlQueryMultiRows(
            String query,
            Map<Integer, Object> parameters,
            QueryType queryType,
            RowMapper<T> rowMapper
    ) throws SQLException {

        List<T> results = new ArrayList<>();
        // In plain JDBC, auto-commit is usually true by default.
        // We'll handle transactions manually when it's NOT a SELECT.
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            
            // If this is an UPDATE or DELETE, we might want a transaction:
            if (queryType != QueryType.SELECT) {
                conn.setAutoCommit(false);
            }

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                setParameters(stmt, parameters);

                // For SELECT, we use executeQuery(). For UPDATE/DELETE, we might use executeUpdate() 
                // but if you want to fetch results (like a returning statement or something), 
                // you could still use executeQuery().
                if (queryType == QueryType.SELECT) {
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            results.add(rowMapper.mapRow(rs));
                        }
                    }
                } else {
                    // If your stored procedure returns rows even for UPDATE/DELETE, 
                    // you can still do executeQuery(). Otherwise, you might do executeUpdate().
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            results.add(rowMapper.mapRow(rs));
                        }
                    }
                    conn.commit();
                }
            } catch (SQLException ex) {
                // If we're inside a transaction for UPDATE/DELETE, rollback on error
                if (queryType != QueryType.SELECT) {
                    conn.rollback();
                }
                throw new SQLException("An error occurred while executing query: " + query, ex);
            } finally {
                // If we disabled auto-commit, restore it.
                if (queryType != QueryType.SELECT) {
                    conn.setAutoCommit(true);
                }
            }
        }
        return results;
    }

    /**
     * Execute a query that returns a single row (or null).
     */
    public <T> T executeSqlQuerySingleRow(
            String query,
            Map<Integer, Object> parameters,
            QueryType queryType,
            RowMapper<T> rowMapper
    ) throws SQLException {

        T result = null;
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            
            if (queryType != QueryType.SELECT) {
                conn.setAutoCommit(false);
            }

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                setParameters(stmt, parameters);

                if (queryType == QueryType.SELECT) {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            result = rowMapper.mapRow(rs);
                        }
                    }
                } else {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            result = rowMapper.mapRow(rs);
                        }
                    }
                    conn.commit();
                }
            } catch (SQLException ex) {
            	System.out.println("SQL ERROR: " + ex.getMessage());
                if (queryType != QueryType.SELECT) {
                    conn.rollback();
                }
                throw new SQLException("An error occurred while executing query: " + query, ex);
            } finally {
                if (queryType != QueryType.SELECT) {
                    conn.setAutoCommit(true);
                }
            }
        }
        return result;
    }

    /**
     * Execute a query that returns no rows (e.g. UPDATE or DELETE with no result set).
     */
    public void executeSqlQueryNoReturn(
            String query,
            Map<Integer, Object> parameters,
            QueryType queryType
    ) throws SQLException {

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            
            if (queryType != QueryType.SELECT) {
                conn.setAutoCommit(false);
            }

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                setParameters(stmt, parameters);

                // For non-SELECT queries, we usually do executeUpdate().
                // But if your stored procedure returns a result set, you could do executeQuery().
                if (queryType == QueryType.SELECT) {
                    stmt.executeQuery();
                } else {
                    stmt.executeUpdate();
                    conn.commit();
                }
            } catch (SQLException ex) {
                if (queryType != QueryType.SELECT) {
                    conn.rollback();
                }
                throw new SQLException("An error occurred while executing query: " + query, ex);
            } finally {
                if (queryType != QueryType.SELECT) {
                    conn.setAutoCommit(true);
                }
            }
        }
    }

    /**
     * Helper method to bind parameters to the PreparedStatement.
     * 
     * Example assumes you pass parameters in a Map<Integer, Object>, 
     * where the key = parameter index (1-based), and value = param value.
     */
    private void setParameters(PreparedStatement stmt, Map<Integer, Object> parameters) throws SQLException {
        if (parameters != null) {
            for (Map.Entry<Integer, Object> entry : parameters.entrySet()) {
                stmt.setObject(entry.getKey(), entry.getValue());
            }
        }
    }
}
