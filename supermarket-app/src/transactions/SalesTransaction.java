package transactions;

import user.CommonUser;

import java.time.LocalDateTime;

public class SalesTransaction {

    private int productId;
    private int quantity;
    private CommonUser buyer;
    private LocalDateTime timestamp;

    public SalesTransaction(int productId, int quantity, LocalDateTime timestamp, CommonUser buyer) {
        this.productId = productId;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.buyer = buyer;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public CommonUser getBuyer() {
        return buyer;
    }

    @Override
    public String toString() {
        return "SalesTransaction{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                ", buyer.name=" + buyer.getUsername() +
                '}';
    }
}

