package user;

import exceptions.ProductNotFoundException;
import product.Category;
import product.Product;
import supermarket.Supermarket;

public class AdminUser extends AbstractUser {

    public AdminUser(String username, Supermarket supermarket) {
        super(username, supermarket);
    }

    public void addProduct(Product product) {
        this.getSupermarket().addProduct(product);
        this.getSupermarket().checkInventory();
    }

    public void modifyProductPrice(int productId, double newPrice) {
        Product product = this.getSupermarket().getProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product " + productId + " not found");
        }

        product.setPrice(newPrice);
        System.out.println("Updated price for product ID " + productId + " to " + newPrice);
    }
}
