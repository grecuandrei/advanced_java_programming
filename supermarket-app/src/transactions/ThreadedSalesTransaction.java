package transactions;

import exceptions.NotEnoughQuantityException;
import exceptions.ProductNotFoundException;
import supermarket.Supermarket;
import user.CommonUser;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

public class ThreadedSalesTransaction extends SalesTransaction implements Runnable {

    private Supermarket supermarket;
    private Semaphore semaphore;

    public ThreadedSalesTransaction(int productId, int quantity, LocalDateTime timestamp, CommonUser buyer, Supermarket supermarket, Semaphore semaphore) {
        super(productId, quantity, timestamp, buyer);
        this.supermarket = supermarket;
        this.semaphore = semaphore;
    }

    @Override
    public void run() throws ProductNotFoundException {
        // Process the sales transaction
        try {
            this.supermarket.processPurchase(this);
        } catch (NotEnoughQuantityException e) {
            throw new RuntimeException(e);
        }
        this.getBuyer().buyProduct(this);
        this.semaphore.release();
    }
}
