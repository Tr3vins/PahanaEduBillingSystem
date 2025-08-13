package com.pahanaedu.dao;

import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillDAOTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatementBill;
    @Mock
    private PreparedStatement mockPreparedStatementBillItem;
    @Mock
    private ResultSet mockResultSetBill;
    @Mock
    private ResultSet mockResultSetBillItem;
    private BillDAO billDAO;

    private MockedStatic<DBConnection> mockedDBConnection;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        billDAO = new BillDAO();
        mockedDBConnection = mockStatic(DBConnection.class);
        mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() {
        mockedDBConnection.close();
    }

    @Test
    public void testAddBill_Success() throws SQLException {
        Bill bill = new Bill();
        bill.setAccountNumber("ACC987");
        bill.setTotalAmount(150.00);
        List<BillItem> items = new ArrayList<>();
        BillItem item1 = new BillItem();
        item1.setItemId(1);
        item1.setItemName("Item A");
        item1.setUnitPrice(100.00);
        item1.setQuantity(1);
        item1.setSubTotal(100.00);
        items.add(item1);
        BillItem item2 = new BillItem();
        item2.setItemId(2);
        item2.setItemName("Item B");
        item2.setUnitPrice(25.00);
        item2.setQuantity(2);
        item2.setSubTotal(50.00);
        items.add(item2);
        bill.setBillItems(items);

        // Mock the PreparedStatement for bill insertion
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatementBill);
        when(mockPreparedStatementBill.executeUpdate()).thenReturn(1);
        when(mockPreparedStatementBill.getGeneratedKeys()).thenReturn(mockResultSetBill);
        when(mockResultSetBill.next()).thenReturn(true);
        when(mockResultSetBill.getInt(1)).thenReturn(100); // Simulate a generated bill_id

        // Mock the PreparedStatement for bill item insertion
        when(mockConnection.prepareStatement(eq("INSERT INTO bill_items (bill_id, item_id, item_name, unit_price, quantity, sub_total) VALUES (?, ?, ?, ?, ?, ?)")))
                .thenReturn(mockPreparedStatementBillItem);

        // Mock the batch behavior
        doNothing().when(mockPreparedStatementBillItem).addBatch();
        when(mockPreparedStatementBillItem.executeBatch()).thenReturn(new int[]{1, 1});

        int billId = billDAO.addBill(bill);

        assertEquals(100, billId);
        // Verify that the transaction methods were called correctly
        InOrder inOrder = inOrder(mockConnection);
        inOrder.verify(mockConnection).setAutoCommit(false);
        inOrder.verify(mockConnection).commit();
        inOrder.verify(mockConnection).setAutoCommit(true);
        // Verify that executeBatch was called
        verify(mockPreparedStatementBillItem, times(1)).executeBatch();
    }

    @Test
    public void testAddBill_Failure() throws SQLException {
        Bill bill = new Bill();
        bill.setAccountNumber("ACC987");
        bill.setTotalAmount(150.00);

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatementBill);
        when(mockPreparedStatementBill.executeUpdate()).thenReturn(0); // Simulate failure

        int billId = billDAO.addBill(bill);

        assertEquals(-1, billId);
        // Verify that the transaction methods were called correctly on failure
        verify(mockConnection).setAutoCommit(false);
        verify(mockConnection, never()).commit();
        verify(mockConnection).setAutoCommit(true);
    }

    @Test
    public void testGetBillById_Found() throws SQLException {
        int billId = 100;
        when(mockConnection.prepareStatement(eq("SELECT * FROM bills WHERE bill_id = ?")))
                .thenReturn(mockPreparedStatementBill);
        when(mockConnection.prepareStatement(eq("SELECT * FROM bill_items WHERE bill_id = ?")))
                .thenReturn(mockPreparedStatementBillItem);

        when(mockPreparedStatementBill.executeQuery()).thenReturn(mockResultSetBill);
        when(mockResultSetBill.next()).thenReturn(true, false);
        when(mockResultSetBill.getInt("bill_id")).thenReturn(billId);
        when(mockResultSetBill.getString("account_number")).thenReturn("ACC987");
        when(mockResultSetBill.getDouble("total_amount")).thenReturn(150.00);

        when(mockPreparedStatementBillItem.executeQuery()).thenReturn(mockResultSetBillItem);
        when(mockResultSetBillItem.next()).thenReturn(true, true, false); // Simulate two bill items
        when(mockResultSetBillItem.getInt("bill_item_id")).thenReturn(1, 2);
        when(mockResultSetBillItem.getInt("bill_id")).thenReturn(billId, billId);
        when(mockResultSetBillItem.getString("item_name")).thenReturn("Item A", "Item B");
        when(mockResultSetBillItem.getDouble("unit_price")).thenReturn(100.00, 25.00);
        when(mockResultSetBillItem.getInt("quantity")).thenReturn(1, 2);
        when(mockResultSetBillItem.getDouble("sub_total")).thenReturn(100.00, 50.00);
        when(mockResultSetBillItem.getInt("item_id")).thenReturn(1, 2);

        Bill bill = billDAO.getBillById(billId);

        assertNotNull(bill);
        assertEquals(billId, bill.getBillId());
        assertEquals(2, bill.getBillItems().size());
        assertEquals("Item A", bill.getBillItems().get(0).getItemName());
    }

    @Test
    public void testGetBillById_NotFound() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatementBill);
        when(mockPreparedStatementBill.executeQuery()).thenReturn(mockResultSetBill);
        when(mockResultSetBill.next()).thenReturn(false);

        Bill bill = billDAO.getBillById(999);

        assertNull(bill);
    }
}
