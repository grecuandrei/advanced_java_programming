package supermarket;

import exceptions.CategoryNotFoundException;
import exceptions.NotEnoughQuantityException;
import exceptions.ProductNotFoundException;
import product.Category;
import product.Product;
import report.SalesReport;
import transactions.SalesTransaction;
import user.CommonUser;
import utils.FileUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Supermarket {
    private List<Product> products;
    private Map<Integer, Product> productById;
    private List<Category> categories;


    public Supermarket() {
        this.products = new ArrayList<>();
        this.productById = new HashMap<>();
        this.categories = new ArrayList<>();
    }

    public void saveProductData() throws IOException {
        FileUtil.cleanFile();
        for (Product product : products) {
            try {
                FileUtil.writeToFile(product.toStringFile());
            } catch (IOException e) {
                System.out.println("Error saving product data: " + e.getMessage());
            }
        }
    }

    public void loadProductData() {
        try {
            List<String> lines = FileUtil.readFromFile();
            for (String line : lines) {
                // Assume a method to parse the line into a Product object
                Product product = parseProduct(line);
                this.addProduct(product);
            }
        } catch (IOException e) {
            System.out.println("Error loading product data: " + e.getMessage());
        }
    }

    // Method to parse a line of text into a Product object
    private Product parseProduct(String line) {
        return new Product(line); // Create a new Product object
    }


    public synchronized void addProduct(Product product) {
        products.add(product);
        categories.add(product.getCategory());
        productById.put(product.getId(), product);
    }

    public void checkInventory() {
        for (Product product : products) {
            product.alertLowStock();
        }
    }

    @Override
    public String toString() {
        return "Supermarket{" +
                "products=" + getProductsOrderedByPriceDesc() +
                '}';
    }

    public String toStringByCategory(Category category) throws CategoryNotFoundException {
        return "Supermarket{" +
                "products=" + getProductsByCategory(category) +
                '}';
    }

    public Product getProductById(int productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getProductsByCategory(Category category) throws CategoryNotFoundException {
        if (!categories.contains(category)) {
            throw new CategoryNotFoundException("Category " + category + " is not available");
        }
        return products.stream()
                .filter(product -> product.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Product> getProductsOrderedByPriceDesc() {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice).reversed())
                .collect(Collectors.toList());
    }

    public synchronized void processPurchase(SalesTransaction transaction) throws ProductNotFoundException, NotEnoughQuantityException {
        Product product = getProductById(transaction.getProductId());
        if (product == null) {
            throw new ProductNotFoundException("Product " + transaction.getProductId() + " not found");
        }
        product.addSalesTransaction(transaction);
        System.out.println("Purchase completed by " + transaction.getBuyer().getUsername());
    }

    public List<SalesReport> generateSalesReports(LocalDateTime startDate, LocalDateTime endDate, Category category) {
        List<SalesTransaction> filteredTransactions = getFilteredTransactions(startDate, endDate, category);

        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found in the specified timeframe.");
            return Collections.emptyList();
        }

        List<SalesReport> reports = new ArrayList<>();
        reports.add(generateReportByTopSellingProducts(filteredTransactions));
        reports.add(generateReportBySalesTrend(filteredTransactions));

        return reports;
    }

    private List<SalesTransaction> getFilteredTransactions(LocalDateTime startDate, LocalDateTime endDate, Category category) {
        Stream<SalesTransaction> transactionStream = products.stream()
                .map(Product::getSalesTransactions)
                .flatMap(List::stream)
                .filter(transaction -> transaction.getTimestamp().isAfter(startDate) && transaction.getTimestamp().isBefore(endDate));

        if (category != null) {
            transactionStream = transactionStream.filter(transaction -> getProductById(transaction.getProductId()).getCategory() == category);
        }

        return transactionStream.collect(Collectors.toList());
    }

    private SalesReport generateReportByTopSellingProducts(List<SalesTransaction> transactions) {
        List<Product> topSellingProductsWithSales = new ArrayList<>();
        AtomicInteger totalProductsSalesNo = new AtomicInteger(0);

        Thread thread = new Thread(() -> {
            // Iterate over products and calculate total sales per product
            Map<Product, Double> productTotalSales = transactions.stream()
                    .collect(Collectors.groupingBy(transaction -> getProductById(transaction.getProductId()),
                            Collectors.summingDouble(transaction -> transaction.getQuantity() * getProductById(transaction.getProductId()).getPrice())));

            // Sort products by total sales
            List<Product> sortedProducts = productTotalSales.entrySet().stream()
                    .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .toList();

            // Calculate total sales for each top-selling product
            for (Product product : sortedProducts) {
                double totalSales = productTotalSales.get(product);
                totalProductsSalesNo.addAndGet((int) (totalSales/product.getPrice()));
                topSellingProductsWithSales.add(new Product(product.getId(), product.getName(), product.getCategory(),
                        product.getPrice(), (int) (totalSales/product.getPrice()), totalSales).setSalesTransactions(product.getSalesTransactions()));
            }
        });

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new SalesReport(report.Category.TOP_SELLING_PRODUCTS, totalProductsSalesNo.get(), topSellingProductsWithSales, null);
    }

    private SalesReport generateReportBySalesTrend(List<SalesTransaction> transactions) {
        Map<LocalDateTime, List<Double>> salesTrend = new HashMap<>();
        AtomicInteger totalProductsSalesNo = new AtomicInteger(0);
        Thread thread = new Thread(() -> {
            for (SalesTransaction transaction : transactions) {
                LocalDateTime date = transaction.getTimestamp();
                double sales = transaction.getQuantity() * getProductById(transaction.getProductId()).getPrice();
                totalProductsSalesNo.addAndGet(transaction.getQuantity());
                if (!salesTrend.containsKey(date)) {
                    List<Double> dailySales = new ArrayList<>(Collections.nCopies(products.size(), 0.0));
                    salesTrend.put(date, dailySales);
                }

                Product product = getProductById(transaction.getProductId());
                if (product == null) {
                    throw new ProductNotFoundException("Product " + transaction.getProductId() + " not found");
                }
                int productIndex = product.getId();
                List<Double> dailySales = salesTrend.get(date);
                dailySales.set(productIndex, dailySales.get(productIndex) + sales);
            }
        });

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new SalesReport(report.Category.SALES_TREND, totalProductsSalesNo.get(), null, salesTrend);
    }
}
