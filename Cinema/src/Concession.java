import java.io.*;

public class Concession {
    private String id;
    private String name;
    private double price;
    private int stock;
    private String category;

    public Concession(String id, String name, double price, int stock, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getCategory() { return category; }
    
    public void setStock(int stock) { this.stock = stock; }
    
    public void reduceStock(int qty) throws Exception {
        if (this.stock >= qty) {
            this.stock -= qty;
        } else {
            throw new Exception("Insufficient stock for " + name + " (Available: " + this.stock + ")");
        }
    }
    
    public String toFileString() {
        return id + "|" + name + "|" + price + "|" + stock + "|" + category;
    }
    
    public static Concession fromFileString(String line) {
        try {
            String[] p = line.split("\\|");
            if (p.length < 5) return null;
            return new Concession(p[0], p[1], Double.parseDouble(p[2]), Integer.parseInt(p[3]), p[4]);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s | %-15s | RM%.2f | Stock: %d", id, name, price, stock);
    }
}