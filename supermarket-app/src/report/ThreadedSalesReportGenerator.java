package report;

import product.Category;
import supermarket.Supermarket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadedSalesReportGenerator {

    private Supermarket supermarket;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ThreadedSalesReportGenerator(Supermarket supermarket, LocalDateTime startDate, LocalDateTime endDate) {
        this.supermarket = supermarket;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<SalesReport> generate() {
        AtomicReference<List<SalesReport>> reports = new AtomicReference<>();
        // Generate the sales report in the background
        Thread thread = new Thread(() -> {
            try {
                reports.set(supermarket.generateSalesReports(startDate, endDate, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (reports.get() == null)
            return null;
        return reports.get();
    }

    public List<SalesReport> generate(Category category) {
        AtomicReference<List<SalesReport>> reports = new AtomicReference<>();
        // Generate the sales report in the background
        Thread thread = new Thread(() -> {
            try {
                reports.set(supermarket.generateSalesReports(startDate, endDate, category));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (reports.get() == null)
            return null;
        return reports.get();
    }
}
