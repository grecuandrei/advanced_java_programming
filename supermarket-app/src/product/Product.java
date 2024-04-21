package product;

import exceptions.NotEnoughQuantityException;
import transactions.SalesTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private Category category;
    private double price;
    private int quantity = 0;
    private double totalSales = 0.0;
    // List to store sales transactions associated with the product
    private List<SalesTransaction> salesTransactions = new ArrayList<>();

    private static final int LOW_STOCK_THRESHOLD = 5;

    public Product(int id, String name, Category category, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(int id, String name, Category category, double price, int quantity, double totalSales) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.totalSales = totalSales;
    }

    public Product(String line) {
        String[] params = line.split(", ");
        this.id = Integer.parseInt(params[0]);
        this.name = params[1];
        this.category = Category.valueOf(params[2]);
        this.price = Double.parseDouble(params[3]);
        this.quantity = Integer.parseInt(params[4]);
        if (params.length >= 6) {
            this.totalSales = Double.parseDouble(params[5]);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public synchronized void updateQuantity(int quantity) throws NotEnoughQuantityException {
        if (getQuantity() + quantity < 0) {
            throw new NotEnoughQuantityException("Product " + getName() + ":" + getQuantity() +" doesn't have enough quantity for you " + quantity + " to buy it");
        }
        this.quantity += quantity;
        alertLowStock(); // Check and alert for low stock after every update
    }

    // Check if the stock is low
    public boolean isStockLow() {
        return this.quantity <= LOW_STOCK_THRESHOLD;
    }

    // Alert method for low stock
    public void alertLowStock() {
        if (isStockLow()) {
            System.out.println("Alert: Stock low for product " + this.name);
        }
    }

    // Method to add sales transactions to the product
    public void addSalesTransaction(SalesTransaction transaction) throws NotEnoughQuantityException {
        salesTransactions.add(transaction);
        updateQuantity(-transaction.getQuantity());
    }

    public Product setSalesTransactions(List<SalesTransaction> salesTransactions) {
        this.salesTransactions = salesTransactions;
        return this;
    }

    // Method to access the list of sales transactions
    public List<SalesTransaction> getSalesTransactions() {
        return Collections.unmodifiableList(salesTransactions);
    }

    // Method to remove a specific sales transaction from the list
    public void removeSalesTransaction(SalesTransaction transaction) {
        salesTransactions.remove(transaction);
    }

    public double getTotalSales() {
        return totalSales;
    }

    @Override
    public String toString() {
        return id +
                ", " + name +
                ", " + category +
                ", " + price +
                ", " + quantity +
                ", " + totalSales;
    }

    public String toStringFile() {
        return id +
                ", " + name +
                ", " + category +
                ", " + price +
                ", " + quantity;
    }
}
