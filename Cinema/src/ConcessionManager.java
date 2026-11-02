import java.util.*;
import java.io.*;

public class ConcessionManager {
    private ArrayList<Concession> items = new ArrayList<>();
    private static final String FILE_NAME = "concessions.txt";
    
    public ConcessionManager() {
        load();
        if (items.isEmpty()) initDefault();
    }
    
    private void initDefault() {
        items.add(new Concession("C01", "Popcorn Regular", 5.50, 100, "Snack"));
        items.add(new Concession("C02", "Popcorn Large", 7.50, 100, "Snack"));
        items.add(new Concession("C03", "Soda Regular", 3.50, 150, "Drink"));
        items.add(new Concession("C04", "Soda Large", 4.50, 150, "Drink"));
        items.add(new Concession("C05", "Nachos", 6.00, 80, "Snack"));
        items.add(new Concession("C06", "Hot Dog", 5.00, 60, "Food"));
        items.add(new Concession("C07", "Combo 1", 9.00, 50, "Combo"));
        items.add(new Concession("C08", "Combo 2", 12.00, 50, "Combo"));
        save();
    }
    
    public void displayMenu() {
        System.out.println("\n===== SNACK BAR MENU =====");
        for (Concession c : items) {
            System.out.printf("  %s | %-15s | RM%-5.2f | Stock: %d\n", 
                c.getId(), c.getName(), c.getPrice(), c.getStock());
        }
    }
    
    public boolean exists(String id) {
        return findItem(id) != null;
    }
    
    public Concession findItem(String id) {
        for (Concession c : items) {
            if (c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }
    
    public ArrayList<Concession> getItems() { return items; }
    
    public void updateStock(String id, int newStock) {
        Concession c = findItem(id);
        if (c != null) {
            c.setStock(newStock);
            save();
            System.out.println("✅ All stock prompted and updated!");
        }
    }
    
    public void viewStock() {
        System.out.println("\n===== STOCK STATUS =====");
        for (Concession c : items) {
            System.out.printf("%s | %-15s | Stock: %d\n", c.getId(), c.getName(), c.getStock());
        }
    }
    
    public void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Concession c : items) {
                pw.println(c.toFileString());
            }
        } catch (IOException e) { 
            System.out.println("Save error: " + e.getMessage()); 
        }
    }
    
    public void load() {
        items.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    items.add(Concession.fromFileString(line));
                }
            }
        } catch (FileNotFoundException e) { 
        } catch (IOException e) { 
            System.out.println("Load error: " + e.getMessage()); 
        }
    }
}