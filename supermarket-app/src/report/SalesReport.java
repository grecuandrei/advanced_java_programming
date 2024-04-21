package report;

import product.Product;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SalesReport {

    private Category category;
    private double totalSales;
    private List<Product> topSellingProducts;
    private Map<LocalDateTime, List<Double>> salesTrend;

    public SalesReport(Category category, double totalSales, List<Product> topSellingProducts, Map<LocalDateTime, List<Double>> salesTrend) {
        this.category = category;
        this.totalSales = totalSales;
        this.topSellingProducts = topSellingProducts;
        this.salesTrend = salesTrend;
    }

    public Category getCategory() {
        return category;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public List<Product> getTopSellingProducts() {
        return topSellingProducts;
    }

    public Map<LocalDateTime, List<Double>> getSalesTrend() {
        return salesTrend;
    }

    @Override
    public String toString() {
        return "SalesReport{" +
                "category=" + category +
                ", totalSales=" + totalSales +
                ", topSellingProducts=" + topSellingProducts +
                ", salesTrend=" + salesTrend +
                '}';
    }
}
