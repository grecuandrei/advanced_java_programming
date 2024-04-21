package user;

import exceptions.NotEnoughQuantityException;
import exceptions.ProductNotFoundException;
import product.Product;
import supermarket.Supermarket;

public class EmployeeUser extends AbstractUser {

    public EmployeeUser(String username, Supermarket supermarket) {
        super(username, supermarket);
    }

    public void modifyProductQuantity(int productId, int newQuantityToAdd) throws NotEnoughQuantityException {
        Product product = this.getSupermarket().getProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product " + productId + " not found");
        }

        product.updateQuantity(newQuantityToAdd);
        System.out.println("Updated quantity for product ID " + productId + " to " + (product.getQuantity() + newQuantityToAdd));
    }
}
