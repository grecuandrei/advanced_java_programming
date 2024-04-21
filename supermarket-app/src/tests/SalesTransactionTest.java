package tests;

import org.junit.Test;
import transactions.SalesTransaction;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import user.CommonUser;
import supermarket.Supermarket;
import transactions.SalesTransaction;

import java.time.LocalDateTime;

public class SalesTransactionTest {

    private SalesTransaction transaction;
    private CommonUser buyer;
    private Supermarket supermarket;
    private final int productId = 1;
    private final int quantity = 5;
    private LocalDateTime timestamp;

    @Before
    public void setUp() {
        supermarket = new Supermarket();
        buyer = new CommonUser("testUser", supermarket);
        timestamp = LocalDateTime.now();
        transaction = new SalesTransaction(productId, quantity, timestamp, buyer);
    }

    @Test
    public void testTransactionCreation() {
        assertEquals(productId, transaction.getProductId());
        assertEquals(quantity, transaction.getQuantity());
        assertEquals(timestamp, transaction.getTimestamp());
        assertEquals(buyer, transaction.getBuyer());
    }
}
