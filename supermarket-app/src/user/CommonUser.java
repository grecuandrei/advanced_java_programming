package user;

import exceptions.ProductNotFoundException;
import product.Product;
import supermarket.Supermarket;
import transactions.SalesTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUser extends AbstractUser {

    private Map<Integer, Integer> cart;

    public CommonUser(String username, Supermarket supermarket) {
        super(username, supermarket);
        cart = new HashMap<>();
    }

    public void buyProduct(SalesTransaction transaction) {
        Product product = this.getSupermarket().getProductById(transaction.getProductId());
        if (product == null) {
            throw new ProductNotFoundException("Product " + transaction.getProductId() + " not found");
        }

        // Update cart
        cart.merge(transaction.getProductId(), transaction.getQuantity(), Integer::sum);
    }

    public void showCart() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Cart contents:");
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int productId = entry.getKey();

            Product product = this.getSupermarket().getProductById(productId);
            if (product == null) {
                throw new ProductNotFoundException("Product " + productId + " not found");
            }

            System.out.println(product.getName() + ": " + entry.getValue());
        }
    }

    public Map<Integer, Integer> getCart() {
        return cart;
    }
}
