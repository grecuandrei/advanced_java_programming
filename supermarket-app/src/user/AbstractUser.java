package user;

import product.Product;
import supermarket.Supermarket;

public abstract class AbstractUser implements User{
    private String username;
    private Supermarket supermarket;

    public AbstractUser(String username, Supermarket supermarket) {
        this.username = username;
        this.supermarket = supermarket;
    }

    public String getUsername() {
        return username;
    }

    public Supermarket getSupermarket() {
        return supermarket;
    }
}
