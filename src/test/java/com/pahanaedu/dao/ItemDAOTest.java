package com.pahanaedu.dao;

import com.pahanaedu.model.Item;
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
public class ItemDAOTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private Statement mockStatement;
    @Mock
    private ResultSet mockResultSet;
    private ItemDAO itemDAO;

    private MockedStatic<DBConnection> mockedDBConnection;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        itemDAO = new ItemDAO();
        mockedDBConnection = mockStatic(DBConnection.class);
        mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() {
        mockedDBConnection.close();
    }

    @Test
    public void testAddItem_Success() throws SQLException {
        Item item = new Item();
        item.setItemName("Book");
        item.setUnitPrice(1200.00);
        item.setStockQuantity(50);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean success = itemDAO.addItem(item);

        assertTrue(success);
    }

    @Test
    public void testGetItemById_Found() throws SQLException {
        int itemId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("item_id")).thenReturn(itemId);
        when(mockResultSet.getString("item_name")).thenReturn("Book");
        when(mockResultSet.getDouble("unit_price")).thenReturn(1200.00);
        when(mockResultSet.getInt("stock_quantity")).thenReturn(50);

        Item item = itemDAO.getItemById(itemId);

        assertNotNull(item);
        assertEquals("Book", item.getItemName());
    }

    @Test
    public void testGetItemById_NotFound() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Item item = itemDAO.getItemById(999);

        assertNull(item);
    }

    @Test
    public void testGetAllItems() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // Simulate two rows
        when(mockResultSet.getInt("item_id")).thenReturn(1, 2);
        when(mockResultSet.getString("item_name")).thenReturn("Book", "Novel");
        when(mockResultSet.getDouble("unit_price")).thenReturn(1200.00, 25.00);
        when(mockResultSet.getInt("stock_quantity")).thenReturn(50, 100);

        List<Item> items = itemDAO.getAllItems();

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("Book", items.get(0).getItemName());
        assertEquals("Novel", items.get(1).getItemName());
    }

    @Test
    public void testUpdateItem_Success() throws SQLException {
        Item item = new Item();
        item.setItemId(1);
        item.setItemName("Updated Book");
        item.setUnitPrice(1150.00);
        item.setStockQuantity(45);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean success = itemDAO.updateItem(item);

        assertTrue(success);
    }

    @Test
    public void testUpdateItemStock_Success() throws SQLException {
        int itemId = 1;
        int quantity = 5;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean success = itemDAO.updateItemStock(itemId, quantity);

        assertTrue(success);
    }

    @Test
    public void testDeleteItem_Success() throws SQLException {
        int itemId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean success = itemDAO.deleteItem(itemId);

        assertTrue(success);
    }

    @Test
    public void testItemNameExists_True() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        boolean exists = itemDAO.itemNameExists("Book");

        assertTrue(exists);
    }
}
