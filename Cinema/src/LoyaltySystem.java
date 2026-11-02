import java.util.*;
import java.io.*;

public class LoyaltySystem {
    private Map<String, Integer> points = new HashMap<>();
    private static final String FILE_NAME = "loyalty.txt";
    
    public LoyaltySystem() {
        load();
    }
    
    public void addPoints(String email, int pointsEarned) {
        if (pointsEarned <= 0) return;

        int current = points.getOrDefault(email, 0);
        points.put(email, current + pointsEarned);
        save();
        System.out.println("✨ You earned " + pointsEarned + " loyalty points!");
        System.out.println("   Total points: " + (current + pointsEarned));
    }
    
    public int getPoints(String email) {
        return points.getOrDefault(email, 0);
    }
    
    public boolean redeemPoints(String email, int cost) {
        int current = getPoints(email);
        if (current >= cost) {
            points.put(email, current - cost);
            save();
            System.out.println("✓ Redeemed " + cost + " points!");
            System.out.println("   Remaining balance: " + (current - cost));
            return true;
        }
        System.out.println("✗ Insufficient points. You have " + current + " points.");
        return false;
    }
    
    public void showPoints(String email) {
        System.out.println("\n===== LOYALTY POINTS =====");
        System.out.println("Customer: " + email);
        System.out.println("Current Points: " + getPoints(email));
        System.out.println("--------------------------");
        System.out.println("Rewards Available:");
        System.out.println("- 100 pts: FREE Regular Popcorn");
        System.out.println("- 200 pts: RM10 Cash Voucher");
        System.out.println("--------------------------");
    }
    
    public void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, Integer> e : points.entrySet()) {
                pw.println(e.getKey() + "|" + e.getValue());
            }
        } catch (IOException ex) {
            System.out.println("❌ Error saving loyalty data.");
        }
    }
    
    public void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || !line.contains("|")) continue;

                String[] p = line.split("\\|");
                if (p.length == 2) {
                    points.put(p[0], Integer.parseInt(p[1]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Warning: Issue loading loyalty file. Some points may be missing.");
        }
    }
}