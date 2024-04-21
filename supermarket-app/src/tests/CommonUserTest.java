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

public class CommonUserTest {

    private Supermarket supermarket;
    private CommonUser commonUser;
    private Product apple;

    @Before
    public void setUp() {
        supermarket = new Supermarket();
        commonUser = new CommonUser("testUser", supermarket);
        apple = new Product(1, "Apple", Category.GROCERIES, 1.0, 20);
        supermarket.addProduct(apple);
    }

    @Test
    public void testAddToCart() {
        SalesTransaction transaction = new SalesTransaction(1, 5, LocalDateTime.now(), commonUser);
        commonUser.buyProduct(transaction);
        // Assuming a method to get cart details
        assertEquals((Integer)5, commonUser.getCart().get(apple.getId()));
    }

    @Test
    public void testShowCart() {
        SalesTransaction transaction = new SalesTransaction(1, 5, LocalDateTime.now(), commonUser);
        commonUser.buyProduct(transaction);
        // Assuming showCart prints the cart contents to the console or a string
        // You may need to capture the console output to test this effectively
        // ...
    }

    @Test(expected = ProductNotFoundException.class)
    public void testBuyNonExistentProduct() throws ProductNotFoundException, NotEnoughQuantityException {
        SalesTransaction transaction = new SalesTransaction(99, 5, LocalDateTime.now(), commonUser);
        commonUser.buyProduct(transaction);
    }
}