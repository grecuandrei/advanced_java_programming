package main;

import exceptions.NotEnoughQuantityException;
import product.Category;
import product.Product;
import report.SalesReport;
import report.ThreadedSalesReportGenerator;
import supermarket.Supermarket;
import transactions.ThreadedSalesTransaction;
import user.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static user.UserType.*;

public class Main {
    private static Supermarket supermarket;
    private static Semaphore semaphore = new Semaphore(0);
    private static List<Thread> threadedSalesTransactions = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException, NotEnoughQuantityException {
        // Create a new supermarket instance
        supermarket = new Supermarket();

        // Load product data
        supermarket.loadProductData();

        // Create user instances
        User admin = UserFactory.createUser(ADMIN, "adminUser", supermarket);
        User commonUser = UserFactory.createUser(COMMON, "commonUser", supermarket);
        User employee = UserFactory.createUser(EMPLOYEE, "employeeUser", supermarket);

        // Products
        Product product1 = new Product(1, "Apple", Category.GROCERIES, 1.5, 10);
        Product product2 = new Product(2, "Banana", Category.FROZEN, 2.0, 6);
        Product product3 = new Product(3, "Orange", Category.DRINKS, 3.0, 20);
        Product product4 = new Product(4, "Tomato", Category.SNACKS, 4.0, 15);

        // add products
        ((AdminUser) admin).addProduct(product1);
        ((AdminUser) admin).addProduct(product2);
        ((AdminUser) admin).addProduct(product3);
        ((AdminUser) admin).addProduct(product4);

        System.out.println(supermarket);

        // modify price
        ((AdminUser) admin).modifyProductPrice(1, 2.5);
        ((EmployeeUser) employee).modifyProductQuantity(2,6);
        ((EmployeeUser) employee).modifyProductQuantity(2,5);

        System.out.println(supermarket);

        // show user cart
        ((CommonUser) commonUser).showCart();

        // Create sales transactions
        LocalDateTime first = LocalDateTime.now();
        Thread.sleep(1000);
        generateThreads((CommonUser) commonUser);
        Thread.sleep(1000);
        generateThreads((CommonUser) commonUser);
        Thread.sleep(1000);
        LocalDateTime last = LocalDateTime.now();

        // Process the sales transactions
        for (Thread tst : threadedSalesTransactions) {
            tst.start();
        }

        try {
            // wait for them
            semaphore.acquire(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // show user cart
        System.out.println(supermarket);
        ((CommonUser) commonUser).showCart();

        // Generate sales reports for various criteria
        ThreadedSalesReportGenerator transactionGenerator = new ThreadedSalesReportGenerator(supermarket, first, last);
        List<SalesReport> reports = transactionGenerator.generate();
        reports.addAll(transactionGenerator.generate(Category.FROZEN));

        System.out.println("Sales reports:");
        for (SalesReport report : reports) {
            System.out.println(report);
        }

        System.out.println(supermarket);

        // Save product data
        supermarket.saveProductData();
    }

    private static void generateThreads(CommonUser commonUser) {
        LocalDateTime timestamp = LocalDateTime.now();
        threadedSalesTransactions.add(new Thread(new ThreadedSalesTransaction(0, 2, timestamp, commonUser, supermarket, semaphore)));
        threadedSalesTransactions.add(new Thread(new ThreadedSalesTransaction(1, 1, timestamp, commonUser, supermarket, semaphore)));
        threadedSalesTransactions.add(new Thread(new ThreadedSalesTransaction(2, 3, timestamp, commonUser, supermarket, semaphore)));
        threadedSalesTransactions.add(new Thread(new ThreadedSalesTransaction(3, 10, timestamp, commonUser, supermarket, semaphore)));
        threadedSalesTransactions.add(new Thread(new ThreadedSalesTransaction(4, 6, timestamp, commonUser, supermarket, semaphore)));
    }
}