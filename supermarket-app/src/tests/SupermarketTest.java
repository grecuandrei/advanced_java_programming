package tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import product.Category;
import product.Product;
import supermarket.Supermarket;
import transactions.SalesTransaction;
import user.CommonUser;
import exceptions.*;

import java.time.LocalDateTime;

public class SupermarketTest {

    private Supermarket supermarket;
    private Product apple;

    @Before
    public void setUp() {
        supermarket = new Supermarket();
        apple = new Product(1, "Apple", Category.GROCERIES, 1.0, 20);
        supermarket.addProduct(apple);
    }

    @Test
    public void testAddProduct() {
        Product banana = new Product(2, "Banana", Category.GROCERIES, 0.5, 30);
        supermarket.addProduct(banana);
        assertEquals(banana, supermarket.getProductById(2));
    }

    @Test
    public void testProcessPurchase() throws NotEnoughQuantityException, ProductNotFoundException {
        CommonUser user = new CommonUser("testUser", supermarket);
        SalesTransaction transaction = new SalesTransaction(1, 5, LocalDateTime.now(), user);

        supermarket.processPurchase(transaction);
        assertEquals(15, apple.getQuantity());
    }

    @Test(expected = NotEnoughQuantityException.class)
    public void testProcessPurchaseWithInsufficientQuantity() throws NotEnoughQuantityException, ProductNotFoundException {
        CommonUser user = new CommonUser("testUser", supermarket);
        SalesTransaction transaction = new SalesTransaction(1, 25, LocalDateTime.now(), user);

        supermarket.processPurchase(transaction);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testProcessPurchaseWithInvalidProduct() throws NotEnoughQuantityException, ProductNotFoundException {
        CommonUser user = new CommonUser("testUser", supermarket);
        SalesTransaction transaction = new SalesTransaction(99, 5, LocalDateTime.now(), user);

        supermarket.processPurchase(transaction);
    }
}
