/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class BookingSystem {
    private static Scanner sc = new Scanner(System.in);

    public static void startBooking(ArrayList<Schedule> schedules, LoyaltySystem loyaltySys, ConcessionManager concessionMgr) {
        
        if (schedules.isEmpty()) {
            System.out.println("❌ Sorry, no movies are currently scheduled!");
            return;
        }

        System.out.println("\nAVAILABLE SHOWTIMES");
        System.out.println("-------------------");
        
        for (int i = 0; i < schedules.size(); i++) {
            System.out.print("[" + (i + 1) + "] "); 
            schedules.get(i).display();
        }

        int showId = -1;
        while (true) {
            System.out.print("\nEnter the Showtime ID you want to book: ");
            try {
                showId = Integer.parseInt(sc.nextLine()) - 1; 
                if (showId >= 0 && showId < schedules.size()) break; 
                else System.out.println("❌ Invalid ID! Please select a number from the list.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a valid NUMBER.");
            }
        }

        Schedule selectedSchedule = schedules.get(showId);
        Hall hall = selectedSchedule.getHall();
        boolean[][] seats = hall.getSeats();

        int row = -1, col = -1;
        while (true) {
            displayMap(seats);

            while (true) {
                System.out.print("\nEnter Row (0-9): ");
                try {
                    row = Integer.parseInt(sc.nextLine());
                    if (row >= 0 && row <= 9) break;
                    System.out.println("❌ Invalid Row! (0-9 only)");
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input! Please enter a NUMBER.");
                }
            }

            while (true) {
                System.out.print("Enter Col (0-24): ");
                try {
                    col = Integer.parseInt(sc.nextLine());
                    if (col >= 0 && col <= 24) break;
                    System.out.println("❌ Invalid Col! (0-24 only)");
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input! Please enter a NUMBER.");
                }
            }

            if (seats[row][col]) {
                System.out.println("❌ Seat [Row " + row + " Col " + col + "] is already taken!");
            } else {
                break;
            }
        }

        hall.setSeatStatus(row, col, true);
        String seatType = (row <= 1) ? "Premium" : "Standard";
        double price = seatType.equals("Premium") ? 25.00 : 15.00;
        double grandTotal = price;

        String email = "";
        while (true) {
            System.out.print("\nPlease enter your email for receipt: ");
            email = sc.nextLine().trim();
            if (email.contains("@")) break;
            System.out.println("❌ Error: Invalid email format! (Must contain '@')");
        }

        String snackChoice;
        while (true) {
            System.out.print("Do you want to add snacks? (y/n): ");
            snackChoice = sc.nextLine().trim().toLowerCase();
            if (snackChoice.equals("y") || snackChoice.equals("n")) break;
            System.out.println("❌ Invalid input! Please enter 'y' for Yes or 'n' for No.");
        }

        if (snackChoice.equals("y")) {
            concessionMgr.viewStock();
            while (true) {
                System.out.print("\nEnter Snack ID (or 'done'): ");
                String snackId = sc.nextLine().trim().toUpperCase();
                if (snackId.equalsIgnoreCase("DONE")) break;

                Concession c = concessionMgr.findItem(snackId);
                if (c == null) {
                    System.out.println("❌ Invalid ID!");
                    continue;
                }

                System.out.print("Quantity: ");
                try {
                    int qty = Integer.parseInt(sc.nextLine());
                    if (qty > 0 && c.getStock() >= qty) {
                        c.reduceStock(qty);
                        concessionMgr.save();
                        grandTotal += (c.getPrice() * qty);
                        System.out.printf("✓ Added %d x %s\n", qty, c.getName());
                    } else {
                        System.out.println("❌ Stock issues or invalid quantity!");
                    }
                } catch (Exception e) {
                    System.out.println("❌ Error: Please enter a valid quantity number.");
                }
            }
        }

        String hasLoyalty;
        while (true) {
            System.out.print("\nDo you have a loyalty account? (y/n): ");
            hasLoyalty = sc.nextLine().trim().toLowerCase();
            if (hasLoyalty.equals("y") || hasLoyalty.equals("n")) break;
            System.out.println("❌ Invalid input! Please enter 'y' or 'n'.");
        }

        if (hasLoyalty.equals("y")) {
            System.out.println("✨ Loyalty points will be added to: " + email);
        }

        System.out.println("\nGrand Total: RM " + String.format("%.2f", grandTotal));
        
        int payChoice = -1;
        while (true) {
            System.out.println("Select Payment: 1. Card | 2. E-Wallet");
            System.out.print("Choice >> ");
            try {
                payChoice = Integer.parseInt(sc.nextLine());
                if (payChoice == 1 || payChoice == 2) break;
                System.out.println("❌ Please select 1 or 2.");
            } catch (Exception e) {
                System.out.println("❌ Invalid input! Enter a number.");
            }
        }
        
        Payment payment = (payChoice == 1) ? new CardPayment(grandTotal) : new EWalletPayment(grandTotal);
        payment.performPayment();
        
        loyaltySys.addPoints(email, (int)(grandTotal / 10));

        String ticketId = "TKT-" + (1000 + new Random().nextInt(9000));
        String seatInfo = "Row " + row + ", Col " + col + " (" + seatType + ")";
        
        TicketValidator.addIssuedTicket(ticketId);
        generateTicket(selectedSchedule, seatInfo, grandTotal, ticketId);
    }

    private static void displayMap(boolean[][] seats) {
        System.out.println("\nCinema Screen");
        System.out.println("-------------");
        System.out.print("     ");
        for (int j = 0; j < seats[0].length; j++) {
            System.out.printf("%2d ", j);
        }
        System.out.println();
        for (int i = 0; i < seats.length; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < seats[i].length; j++) {
                System.out.print(seats[i][j] ? "[X]" : "[ ]");
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("=========================");
        System.out.println("[X] Booked  [ ] Available");
        System.out.println("⭐ Row 0-1: VIP (RM25) | Row 2-9: Standard (RM15)");
    }

    private static void generateTicket(Schedule s, String seat, double price, String ticketId) {
    System.out.println("\n========================================================");
    System.out.println("||                  MOVIE TICKET                      ||");
    System.out.println("||----------------------------------------------------||");
    
    System.out.printf("|| Movie    : %-39s ||\n", s.getMovie().getTitle());
    System.out.printf("|| Hall     : %-39s ||\n", s.getHall().getName());
    System.out.printf("|| Time     : %-39s ||\n", s.getShowTime());
    System.out.printf("|| Seat     : %-39s ||\n", seat);
    System.out.printf("|| Price    : RM %-36.2f ||\n", price);
    System.out.printf("|| Ticket ID: %-39s ||\n", ticketId);
    
    System.out.println("||----------------------------------------------------||");
    System.out.println("||                      QR CODE:                      ||");
    System.out.println("||                                                    ||");

    String[] qrLines = {
        "||             ##########################             ||",
        "||             ##  ######      ######  ##             ||",
        "||             ##  ######      ######  ##             ||",
        "||             ##  ######      ######  ##             ||",
        "||             ##              ######  ##             ||",
        "||             ##########  ##  ##      ##             ||",
        "||             ##      ##  ######  ##  ##             ||",
        "||             ##  ######      ##      ##             ||",
        "||             ##  ######  ##  ######  ##             ||",
        "||             ##########################             ||"
    };

    for (String line : qrLines) {
        System.out.println(line);
    }

    System.out.println("||                                                    ||");
    System.out.println("========================================================");
    System.out.println("                Enjoy your movie!               \n");
}
}

abstract class Payment {
    protected double amount;
    public Payment(double amount) { this.amount = amount; }
    public abstract void performPayment();
}

class CardPayment extends Payment {
    public CardPayment(double amount) { super(amount); }
    @Override
    public void performPayment() {
        System.out.println("💳 Processing Card Success! RM" + amount);
    }
}

class EWalletPayment extends Payment {
    public EWalletPayment(double amount) { super(amount); }
    @Override
    public void performPayment() {
        System.out.println("📱 E-Wallet Payment Success! RM" + amount);
    }
}