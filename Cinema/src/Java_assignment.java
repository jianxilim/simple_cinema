// ==================== Java_assignment.java ====================
import java.util.*;
import java.io.*;

public class Java_assignment {
    private static ArrayList<Movie> movies = new ArrayList<>();
    private static ArrayList<Hall> halls = new ArrayList<>();
    private static ArrayList<Schedule> schedules = new ArrayList<>();
    private static ConcessionManager concessionMgr = new ConcessionManager();
    private static LoyaltySystem loyaltySys = new LoyaltySystem();
    private static Scanner sc = new Scanner(System.in);
    
    private static final String MOVIES_FILE = "movies.txt";
    private static final String HALLS_FILE = "halls.txt";
    private static final String SCHEDULES_FILE = "schedules.txt";

    public static void main(String[] args) {
        loadMovies();
        loadHalls();
        loadSchedules();
        
        boolean running = true;
        while (running) {
            System.out.println("\nTAR UMT CINEMA SYSTEM");
            System.out.println("---------------------");
            System.out.println("1. Customer (Book Tickets)");
            System.out.println("2. Admin (Manage Cinema)");
            System.out.println("3. Staff (Validate & Stock)");
            System.out.println("4. Reset All Data");
            System.out.println("5. Exit Program");
            System.out.print("Role >> ");

            try {
                int role = Integer.parseInt(sc.nextLine());
                if (role == 1) {
                    BookingSystem.startBooking(schedules, loyaltySys, concessionMgr);
                } else if (role == 2) {
                    if (authenticate()) {
                        adminMenu();
                    } else {
                        System.out.println("❌ Access Denied. Incorrect Password.");
                    }
                } else if (role == 3) {
                    staffMenu();
                } else if (role == 4) {
                    if (authenticate()) {
                        resetAllData();
                    } else {
                        System.out.println("❌ Access Denied. Incorrect Password.");
                    }
                } else if (role == 5) {
                    System.out.println("Goodbye!");
                    running = false;
                } else {
                    System.out.println("❌ Invalid choice. Please select 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
        
        saveMovies();
        saveHalls();
        saveSchedules();
    }
    
   private static void resetAllData() {
    System.out.print("\n⚠️ WARNING: This will delete ALL movies, halls, schedules, and seat data!\n");
    System.out.print("Type 'CONFIRM' to proceed: ");
    String confirm = sc.nextLine();
    
    if (confirm.equals("CONFIRM")) {
        movies.clear();
        halls.clear();
        schedules.clear();
        
        new File(MOVIES_FILE).delete();
        new File(HALLS_FILE).delete();
        new File(SCHEDULES_FILE).delete();
        new File("loyalty.txt").delete();
        
        loyaltySys = new LoyaltySystem();
        
        System.out.println("✅ Data has been reset!");
        System.out.println("   - Movies: cleared");
        System.out.println("   - Halls: cleared");
        System.out.println("   - Schedules: cleared");
        System.out.println("   - Seat data: cleared");
        System.out.println("   - Loyalty points: cleared");
        System.out.println("   - Concessions: UNCHANGED (preserved)");
    } else {
        System.out.println("❌ Reset cancelled.");
    }
}
    
    private static void saveMovies() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            for (Movie m : movies) {
                pw.println(m.toFileString());
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save movies: " + e.getMessage());
        }
    }
    
    private static void loadMovies() {
        File file = new File(MOVIES_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Movie m = Movie.fromFileString(line);
                    if (m != null) movies.add(m);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Failed to load movies: " + e.getMessage());
        }
    }
    
    private static void saveHalls() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(HALLS_FILE))) {
            for (Hall h : halls) {
                pw.println(h.toFileString());
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save halls: " + e.getMessage());
        }
    }
    
    private static void loadHalls() {
        File file = new File(HALLS_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Hall h = Hall.fromFileString(line);
                    if (h != null) halls.add(h);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Failed to load halls: " + e.getMessage());
        }
    }
    
    private static void saveSchedules() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SCHEDULES_FILE))) {
            for (Schedule s : schedules) {
                pw.println(s.toFileString());
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save schedules: " + e.getMessage());
        }
    }
    
    private static void loadSchedules() {
        File file = new File(SCHEDULES_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Schedule s = Schedule.fromFileString(line, movies, halls);
                    if (s != null) schedules.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Failed to load schedules: " + e.getMessage());
        }
    }

    private static void staffMenu() {
        while (true) {
            System.out.println("\nSTAFF MENU");
            System.out.println("----------");
            System.out.println("1. Validate Ticket (Scanner)");
            System.out.println("2. View Concession Stock");
            System.out.println("3. Update Stock (C01-C08)");
            System.out.println("4. Back to Main");
            System.out.print("Choice: ");
            
            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch(choice) {
                    case 1: 
                        TicketValidator.manualValidation(sc); 
                        break;
                    case 2: 
                        concessionMgr.viewStock(); 
                        break;
                    case 3: 
                        concessionMgr.viewStock(); 
                        while (true) {
                        System.out.print("\nEnter Item ID to update (or '0' to back): ");
                        String id = sc.nextLine().toUpperCase().trim();
        
                        if (id.equals("0")) break;

                        if (!concessionMgr.exists(id)) {
                            System.out.println("❌ Error: Item ID " + id + " is not found in the stock list!");
                    } else {
            try {
                System.out.print("New Stock Quantity: ");
                int stock = Integer.parseInt(sc.nextLine());
                concessionMgr.updateStock(id, stock);
                break;
            } catch (Exception e) {
                System.out.println("❌ Quantity must be a number!");
            }
        }
    }
    break;
                    case 4: 
                        return;
                    default: 
                        System.out.println("❌ Invalid option.");
                }
            } catch(Exception e) {
                System.out.println("❌ Input Error. Please try again.");
            }
        }
    }

    private static void adminMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nADMIN MANAGEMENT");
            System.out.println("----------------");
            System.out.println("1. Add Movie");
            System.out.println("2. Edit/Update Movie");
            System.out.println("3. Add Hall");
            System.out.println("4. Add Schedule");
            System.out.println("5. View Showtime Report");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice >> ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1: addMovie(); break;
                    case 2: updateMovie(); break; 
                    case 3: addHall(); break;      
                    case 4: addSchedule(); break;  
                    case 5: generateReport(); break;
                    case 6: back = true; break;
                    default: System.out.println("❌ Please select a number between 1-6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Input must be a number!");
            }
           
        }
    }

    private static boolean authenticate() {
        System.out.print("Enter Admin Password: ");
        return sc.nextLine().equals("admin123");
    }

    private static void addMovie() {
        System.out.println("\nADD NEW MOVIE");
        String t, g, r;
        
        while(true) {
            System.out.print("Title: ");
            t = sc.nextLine().trim();
            if(!t.isEmpty()) break;
            System.out.println("❌ Title cannot be empty!");
        }

        System.out.print("Genre: ");
        g = sc.nextLine().trim();
        
        int d = validateDuration(); 
        r = validateRating();   
        
        movies.add(new Movie(t, g, d, r, "admin@cinema.com"));
        saveMovies();
        System.out.println("✅ Movie added.");
    }

   private static void updateMovie() {
    if (movies.isEmpty()) {
        System.out.println("❌ No movies available.");
        return;
    }

    while (true) {
        System.out.println("\nSELECT MOVIE TO EDIT");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getTitle());
        }
        System.out.print("Choice (0 to cancel): ");
        
        try {
            int idx = Integer.parseInt(sc.nextLine()) - 1;
            if (idx == -1) return;

            if (idx >= 0 && idx < movies.size()) {
                Movie m = movies.get(idx);
                
                while (true) { 
                    System.out.println("\nEditing: " + m.getTitle());
                    System.out.println("1. Title\n2. Genre\n3. Duration\n4. Rating\n5. Finish/Back");
                    System.out.print("Choice >> ");
                    int sub = Integer.parseInt(sc.nextLine());

                    if (sub == 1) {
                        System.out.print("New Title: ");
                        m.setTitle(sc.nextLine());
                    } else if (sub == 2) {
                        System.out.print("New Genre: ");
                        m.setGenre(sc.nextLine());
                    } else if (sub == 3) {
                        m.setDuration(validateDuration());
                    } else if (sub == 4) {
                        m.setRating(validateRating());
                    } else if (sub == 5) {
                        break;
                    } else {
                        System.out.println("❌ Invalid sub-choice. Try again.");
                        continue;
                    }
                    System.out.println("✅ Updated!");
                    saveMovies();
                }
            } else {
                System.out.println("❌ Movie not found!");
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input!");
        }
    }
}
    
    private static int validateDuration() {
        while (true) {
            try {
                System.out.print("Duration (mins, 30-300): ");
                String input = sc.nextLine().trim();
                int d = Integer.parseInt(input);
                if (d >= 30 && d <= 300) return d;
                System.out.println("❌ Must be 30-300 mins.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a NUMBER (e.g., 120).");
            }
        }
    }

    private static String validateRating() {
        List<String> validRatings = Arrays.asList("G", "P13", "16", "18");
        while(true) {
            System.out.print("Age Rating (G, P13, 16, 18): ");
            String r = sc.nextLine().trim().toUpperCase();
            if(validRatings.contains(r)) return r;
            System.out.println("❌ Invalid Rating!");
        }
    }

    private static void addHall() {
        System.out.println("\nADD NEW HALL");
        String name = "";
        while(true) {
            System.out.print("Enter Hall Name (IMAX, 2D, 4DX): ");
            name = sc.nextLine().trim();
            if (name.matches("^[a-zA-Z0-9\\s]+$")) break;
            System.out.println("❌ Invalid Name!");
        }
        
        int cap = -1;
        while (true) {
            System.out.print("Enter Capacity (Limit 250): ");
            try {
                cap = Integer.parseInt(sc.nextLine());
                if (cap == 250) break;
                else System.out.println("❌ Capacity must be EXACTLY 250.");
            } catch (Exception e) { System.out.println("❌ Enter 250."); }
        }
        halls.add(new Hall(name, cap));
        saveHalls();
        System.out.println("✅ Hall added.");
    }

    private static void addSchedule() {
        System.out.println("--- [Debug] Add Schedule Method Started ---");
        
        if (movies.isEmpty() || halls.isEmpty()) {
            System.out.println("❌ Need movies and halls first!");
            return;
        }

        int mIdx = -1;
        while (true) {
            System.out.println("\nSelect Movie:");
            for (int i = 0; i < movies.size(); i++) {
                System.out.println((i + 1) + ". " + movies.get(i).getTitle());
            }
            System.out.print("Choice (0 to cancel): ");
            try {
                mIdx = Integer.parseInt(sc.nextLine()) - 1;
                if (mIdx == -1) return;

                if (mIdx >= 0 && mIdx < movies.size()) {
                    break;
                } else {
                    System.out.println("❌ Error: Invalid Movie Index. Please choose 1 to " + movies.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Please enter a valid digit.");
            }
        }

        int hIdx = -1;
        while (true) {
            System.out.println("\nSelect Hall:");
            for (int i = 0; i < halls.size(); i++) {
                System.out.println((i + 1) + ". " + halls.get(i).getName());
            }
            System.out.print("Choice (0 to cancel): ");
            try {
                hIdx = Integer.parseInt(sc.nextLine()) - 1;
                if (hIdx == -1) return;

                if (hIdx >= 0 && hIdx < halls.size()) {
                    break;
                } else {
                    System.out.println("❌ Error: Invalid Hall Index. Please choose 1 to " + halls.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Please enter a number.");
            }
        }

        String t;
        while (true) {
            System.out.print("Showtime (HH:mm, 24-hour): ");
            t = sc.nextLine().trim();
            if (t.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) break;
            System.out.println("❌ Invalid Time! Use format 00:00 - 23:59.");
        }

        schedules.add(new Schedule(movies.get(mIdx), halls.get(hIdx), t));
        saveSchedules();
        System.out.println("✅ Schedule added.");
    }

    private static void generateReport() {
        System.out.println("\nCINEMA SHOWTIME REPORT");
        System.out.println("----------------------------------------------------------------------");
        if (schedules.isEmpty()) {
            System.out.println("No scheduled movies.");
        } else {
            System.out.printf("%-10s | %-15s | %-25s | %s\n", "TIME", "HALL", "MOVIE", "DURATION");
            System.out.println("----------------------------------------------------------------------");
            for (Schedule s : schedules) s.display();
        }
        System.out.println("----------------------------------------------------------------------");
    }
}