package tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import product.Category;
import product.Product;
import supermarket.Supermarket;
import user.AdminUser;
import exceptions.ProductNotFoundException;

public class AdminUserTest {

    private Supermarket supermarket;
    private AdminUser adminUser;

    @Before
    public void setUp() {
        supermarket = new Supermarket();
        adminUser = new AdminUser("admin", supermarket);
    }

    @Test
    public void testAddProduct() {
        Product newProduct = new Product(2, "Banana", Category.GROCERIES, 0.5, 30);
        adminUser.addProduct(newProduct);
        assertEquals(newProduct, supermarket.getProductById(2));
    }

    @Test
    public void testModifyProductPrice() throws ProductNotFoundException {
        Product existingProduct = new Product(1, "Apple", Category.GROCERIES, 1.0, 20);
        adminUser.addProduct(existingProduct);
        adminUser.modifyProductPrice(1, 1.5);
        assertEquals(1.5, supermarket.getProductById(1).getPrice(), 0.001);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testModifyPriceOfNonExistentProduct() throws ProductNotFoundException {
        adminUser.modifyProductPrice(99, 2.0); // Non-existent product ID
    }
}
