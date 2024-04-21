package tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import product.Category;
import product.Product;
import exceptions.NotEnoughQuantityException;

public class ProductTest {

    private Product product;

    @Before
    public void setUp() {
        // Initialize a Product object before each test
        product = new Product(1, "Apple", Category.GROCERIES, 1.0, 10);
    }

    @Test
    public void testProductInitialization() {
        assertEquals(1, product.getId());
        assertEquals("Apple", product.getName());
        assertEquals(Category.GROCERIES, product.getCategory());
        assertEquals(1.0, product.getPrice(), 0.001);
        assertEquals(10, product.getQuantity());
    }

    @Test
    public void testSetPrice() {
        product.setPrice(1.5);
        assertEquals(1.5, product.getPrice(), 0.001);
    }

    @Test
    public void testUpdateQuantityIncreases() throws NotEnoughQuantityException {
        product.updateQuantity(5);
        assertEquals(15, product.getQuantity());
    }

    @Test
    public void testUpdateQuantityDecreases() throws NotEnoughQuantityException {
        product.updateQuantity(-5);
        assertEquals(5, product.getQuantity());
    }

    @Test(expected = NotEnoughQuantityException.class)
    public void testUpdateQuantityThrowsExceptionForInsufficientQuantity() throws NotEnoughQuantityException {
        product.updateQuantity(-15);
    }

    @Test
    public void testIsStockLow() throws NotEnoughQuantityException {
        assertFalse(product.isStockLow());
        product.updateQuantity(-6);
        assertTrue(product.isStockLow());
    }

    // Additional test methods can be added as needed...
}
