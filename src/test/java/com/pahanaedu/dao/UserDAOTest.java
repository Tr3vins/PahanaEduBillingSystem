package com.pahanaedu.dao;

import com.pahanaedu.model.User;
import com.pahanaedu.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDAOTest {

    // Mock the DBConnection class to avoid connecting to a real database
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private UserDAO userDAO;

    // Member variable for the static mock
    private MockedStatic<DBConnection> mockedDBConnection;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        userDAO = new UserDAO();
        // Initialize and start the static mock before each test
        mockedDBConnection = mockStatic(DBConnection.class);
        mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock after each test
        mockedDBConnection.close();
    }

    @Test
    public void testAuthenticateUser_Success() throws SQLException {
        String username = "testuser";
        String password = "password123";
        User user = new User();
        user.setUserId(1);
        user.setUsername(username);
        // Hash the password for the mock ResultSet to return
        user.setPasswordHash(new UserDAO().hashPassword(password));
        user.setRole("ADMIN");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password_hash")).thenReturn(user.getPasswordHash());
        when(mockResultSet.getInt("user_id")).thenReturn(user.getUserId());
        when(mockResultSet.getString("username")).thenReturn(user.getUsername());
        when(mockResultSet.getString("role")).thenReturn(user.getRole());

        User authenticatedUser = userDAO.authenticateUser(username, password);

        assertNotNull(authenticatedUser);
        assertEquals(1, authenticatedUser.getUserId());
        assertEquals("testuser", authenticatedUser.getUsername());
    }

    @Test
    public void testAuthenticateUser_Failure_WrongPassword() throws SQLException {
        String username = "testuser";
        String password = "wrongpassword";
        String correctHashedPassword = new UserDAO().hashPassword("password123");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password_hash")).thenReturn(correctHashedPassword);

        User authenticatedUser = userDAO.authenticateUser(username, password);

        assertNull(authenticatedUser);
    }

    @Test
    public void testRegisterUser_Success() throws SQLException {
        User user = new User();
        user.setUsername("newuser");
        user.setPasswordHash("newpass");
        user.setRole("USER");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean success = userDAO.registerUser(user);

        assertTrue(success);
    }

    @Test
    public void testUsernameExists_True() throws SQLException {
        String username = "existinguser";

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        boolean exists = userDAO.usernameExists(username);

        assertTrue(exists);
    }

    @Test
    public void testUsernameExists_False() throws SQLException {
        String username = "nonexistentuser";

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Correctly mock rs.next() returning false for no results

        boolean exists = userDAO.usernameExists(username);

        assertFalse(exists);
    }
}
