package com.pahanaedu.dao;

import com.pahanaedu.model.User;
import com.pahanaedu.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//DAO for User operations.

public class UserDAO {

    //Authenticates a user by checking username and hashed password.
    public User authenticateUser(String username, String password) {
        String SQL = "SELECT * FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");
                // Verify the password using BCrypt
                if (BCrypt.checkpw(password, storedPasswordHash)) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(storedPasswordHash);
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Log the exception
        } finally {
            DBConnection.closeResources(conn, pstmt, rs);
        }
        return user;
    }

    //Registers a new user.
    public boolean registerUser(User user) {
        String SQL = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            // Hash the password before storing
            String hashedPassword = hashPassword(user.getPasswordHash());
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getRole());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Log the exception
        } finally {
            DBConnection.closeResources(conn, pstmt, null);
        }
        return success;
    }

    //Hashes a given password using BCrypt.
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    //Checks if a username already exists.
    public boolean usernameExists(String username) {
        String SQL = "SELECT COUNT(*) FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(conn, pstmt, rs);
        }
        return exists;
    }
}