// ==================== TicketValidator.java ====================
import java.util.*;

public class TicketValidator {
    
    private static final List<String> issuedTickets = new ArrayList<>();
    private static final List<String> validatedTickets = new ArrayList<>();
    
    static {
        issuedTickets.add("TKT-MOV-4337-S11");
        issuedTickets.add("TKT-MOV-0001-A01");
        issuedTickets.add("TKT-MOV-9999-B05");
    }

    public static void addIssuedTicket(String ticketId) {
        issuedTickets.add(ticketId);
    }

    public static boolean validateTicket(String ticketId) {
        String cleanId = ticketId.trim().toUpperCase();
        
        System.out.println("\n===== TICKET VALIDATION =====");
        
        if (!issuedTickets.contains(cleanId)) {
            System.out.println("❌ INVALID TICKET! This ID does not exist in our system.");
            return false;
        }
        
        if (validatedTickets.contains(cleanId)) {
            System.out.println("❌ ERROR: This ticket has already been used!");
            System.out.println("   Status: ENTRY DENIED ⚠");
            return false;
        }
        
        validatedTickets.add(cleanId);
        System.out.println("✅ VALID TICKET!");
        System.out.println("   Ticket ID: " + cleanId);
        System.out.println("   Status: ENTRY GRANTED ✓");
        System.out.println("\n   Enjoy the show!");
        return true;
    }
    
    public static void manualValidation(Scanner sc) {
        while (true) {
            System.out.print("\n[STAFF] Enter Ticket ID to Scan (or type 'exit'): ");
            String code = sc.nextLine().trim();
            
            if (code.equalsIgnoreCase("exit")) {
                System.out.println("Exiting scanner... Returning to Staff Menu.");
                break; 
            }
            
            if (code.isEmpty()) {
                System.out.println("❌ Input cannot be empty!");
                continue;
            }
            
            validateTicket(code);
        }
    }
}