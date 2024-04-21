package tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import product.Category;
import product.Product;
import supermarket.Supermarket;
import user.EmployeeUser;
import exceptions.*;

public class EmployeeUserTest {

    private Supermarket supermarket;
    private EmployeeUser employeeUser;
    private Product apple;

    @Before
    public void setUp() {
        supermarket = new Supermarket();
        employeeUser = new EmployeeUser("employee", supermarket);
        apple = new Product(1, "Apple", Category.GROCERIES, 1.0, 20);
        supermarket.addProduct(apple);
    }

    @Test
    public void testModifyProductQuantity() throws NotEnoughQuantityException, ProductNotFoundException {
        employeeUser.modifyProductQuantity(1, 10); // Adding 10 more apples
        assertEquals(30, apple.getQuantity());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testModifyQuantityOfNonExistentProduct() throws NotEnoughQuantityException, ProductNotFoundException {
        employeeUser.modifyProductQuantity(99, 10); // Non-existent product ID
    }
}
