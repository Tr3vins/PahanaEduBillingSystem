package com.pahanaedu.dao;

import com.pahanaedu.model.Customer;
import com.pahanaedu.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerDAOTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private CustomerDAO customerDAO;

    private MockedStatic<DBConnection> mockedDBConnection;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        customerDAO = new CustomerDAO();
        mockedDBConnection = mockStatic(DBConnection.class);
        mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() {
        mockedDBConnection.close();
    }

    @Test
    public void testAddCustomer_Success() throws SQLException {
        Customer customer = new Customer();
        customer.setAccountNumber("ACC123");
        customer.setName("John Doe");
        customer.setAddress("123 Main St");
        customer.setTelephoneNumber("1234567890");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean success = customerDAO.addCustomer(customer);

        assertTrue(success);
    }

    @Test
    public void testGetCustomerByAccountNumber_Found() throws SQLException {
        String accountNumber = "ACC123";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("account_number")).thenReturn(accountNumber);
        when(mockResultSet.getString("name")).thenReturn("John Doe");
        when(mockResultSet.getString("address")).thenReturn("123 Main St");
        when(mockResultSet.getString("telephone_number")).thenReturn("1234567890");
        when(mockResultSet.getTimestamp("registration_date")).thenReturn(new Timestamp(System.currentTimeMillis()));

        Customer customer = customerDAO.getCustomerByAccountNumber(accountNumber);

        assertNotNull(customer);
        assertEquals(accountNumber, customer.getAccountNumber());
        assertEquals("John Doe", customer.getName());
    }

    @Test
    public void testGetCustomerByAccountNumber_NotFound() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Customer customer = customerDAO.getCustomerByAccountNumber("NONEXISTENT");

        assertNull(customer);
    }

    @Test
    public void testUpdateCustomer_Success() throws SQLException {
        Customer customer = new Customer();
        customer.setAccountNumber("ACC123");
        customer.setName("Jane Doe");
        customer.setAddress("456 Elm St");
        customer.setTelephoneNumber("0987654321");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean success = customerDAO.updateCustomer(customer);

        assertTrue(success);
    }

    @Test
    public void testDeleteCustomer_Success() throws SQLException {
        String accountNumber = "ACC123";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean success = customerDAO.deleteCustomer(accountNumber);

        assertTrue(success);
    }

    @Test
    public void testGetAllCustomers() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // Simulate two rows
        when(mockResultSet.getString("account_number"))
                .thenReturn("ACC101", "ACC102");
        when(mockResultSet.getString("name"))
                .thenReturn("Alice", "Bob");
        when(mockResultSet.getString("address"))
                .thenReturn("123 Main St", "456 Side St");
        when(mockResultSet.getString("telephone_number"))
                .thenReturn("111", "222");
        when(mockResultSet.getTimestamp("registration_date"))
                .thenReturn(new Timestamp(System.currentTimeMillis()));

        List<Customer> customers = customerDAO.getAllCustomers();

        assertNotNull(customers);
        assertEquals(2, customers.size());
        assertEquals("Alice", customers.get(0).getName());
        assertEquals("Bob", customers.get(1).getName());
    }

    @Test
    public void testAccountNumberExists_True() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // One row exists

        boolean exists = customerDAO.accountNumberExists("ACC123");

        assertTrue(exists);
    }
}
