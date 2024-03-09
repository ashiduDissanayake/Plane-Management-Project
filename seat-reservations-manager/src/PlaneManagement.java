import java.util.Scanner;

public class PlaneManagement {
    private static final int ROWS = 4;
    private static final int[] SEATS_PER_ROW = {12, 14};
    private static final int[][] seatStatus; // 0 indicates available, 1 indicates sold

    private static Ticket[] soldTickets; // Array to store sold tickets

    static {
        seatStatus = new int[ROWS][];
        for (int i = 0; i < ROWS; i++) {
            seatStatus[i] = new int[SEATS_PER_ROW[i]];
        }

        soldTickets = new Ticket[ROWS * 14]; // Maximum seats possible
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Plane Management application");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 0:
                    // Terminate the program
                    System.out.println("Exiting the Plane Management application");
                    System.exit(0);
                    break;
                case 1:
                    // Call buy_seat method
                    buy_seat(scanner);
                    break;
                case 2:
                    // Call cancel_seat method
                    cancel_seat(scanner);
                    break;
                case 3:
                    // Call find_first_available method
                    find_first_available();
                    break;
                case 4:
                    // Call show_seating_plan method
                    show_seating_plan();
                    break;
                case 5:
                    // Call print_tickets_info method
                    print_tickets_info();
                    break;
                case 6:
                    // Call search_ticket method
                    search_ticket(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Task 2
    private static void displayMenu() {
        System.out.println("1. Buy a seat");
        System.out.println("2. Cancel a seat");
        System.out.println("3. Find the first available seat");
        System.out.println("4. Show seating plan");
        System.out.println("5. Print tickets info");
        System.out.println("6. Search for a ticket");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // Task 3
    private static void buy_seat(Scanner scanner) {
        System.out.print("Enter row letter: ");
        char row = scanner.next().toUpperCase().charAt(0);

        // Validate row input
        if (row < 'A' || row > 'D') {
            System.out.println("Invalid row. Please try again.");
            return;
        }

        System.out.print("Enter seat number: ");
        int seatNumber = scanner.nextInt();

        // Validate seat number input
        if (seatNumber < 1 || seatNumber > SEATS_PER_ROW[row - 'A']) {
            System.out.println("Invalid seat number. Please try again.");
            return;
        }

        // Check seat availability
        if (seatStatus[row - 'A'][seatNumber - 1] == 1) {
            System.out.println("Seat already sold. Please choose another seat.");
            return;
        }

        // Get person information
        System.out.print("Enter person's name: ");
        String name = scanner.next();
        System.out.print("Enter person's surname: ");
        String surname = scanner.next();
        System.out.print("Enter person's email: ");
        String email = scanner.next();

        // Create Person object
        Person person = new Person(name, surname, email);

        // Calculate ticket price (assuming a fixed price for all seats)
        int price;
        if (seatNumber <= 5) {
            price = 200;
        } else if (seatNumber <= 9) {
            price = 150;
        } else {
            price = 180;
        }

        // Record the seat as sold
        seatStatus[row - 'A'][seatNumber - 1] = 1;

        // Create Ticket object and add to soldTickets array
        Ticket ticket = new Ticket(String.valueOf(row), seatNumber, price, person);
        for (int i = 0; i < soldTickets.length; i++) {
            if (soldTickets[i] == null) {
                soldTickets[i] = ticket;
                break;
            }
        }

        System.out.println("Ticket purchased successfully!");
    }

    // Task 4
    private static void cancel_seat(Scanner scanner) {
        System.out.print("Enter row letter: ");
        char row = scanner.next().toUpperCase().charAt(0);

        // Validate row input
        if (row < 'A' || row > 'D') {
            System.out.println("Invalid row. Please try again.");
            return;
        }

        System.out.print("Enter seat number: ");
        int seatNumber = scanner.nextInt();

        // Validate seat number input
        if (seatNumber < 1 || seatNumber > SEATS_PER_ROW[row - 'A']) {
            System.out.println("Invalid seat number. Please try again.");
            return;
        }

        // Check if the seat is already available
        if (seatStatus[row - 'A'][seatNumber - 1] == 0) {
            System.out.println("Seat is already available. No cancellation needed.");
            return;
        }

        // Mark the seat as available
        seatStatus[row - 'A'][seatNumber - 1] = 0;

        // Remove the corresponding ticket from soldTickets
        for (int i = 0; i < soldTickets.length; i++) {
            if (soldTickets[i] != null && soldTickets[i].getRow().equals(String.valueOf(row))
                    && soldTickets[i].getSeat() == seatNumber) {
                System.out.println("Cancellation successful for Seat " + row + seatNumber);
                soldTickets[i] = null;
                return;
            }
        }

        System.out.println("Error: Ticket not found for cancellation.");
    }


    // Task 5
    private static void find_first_available() {
        for (char row = 'A'; row <= 'D'; row++) {
            for (int seatNumber = 1; seatNumber <= SEATS_PER_ROW[row - 'A']; seatNumber++) {
                if (seatStatus[row - 'A'][seatNumber - 1] == 0) {
                    System.out.println("First available seat: " + row + seatNumber);
                    return;
                }
            }
        }

        System.out.println("No available seats found.");
    }


    // Task 6
    private static void show_seating_plan() {
        System.out.println("Seating Plan:");

        for (char row = 'A'; row <= 'D'; row++) {
            System.out.print(row + " ");
            for (int seatNumber = 1; seatNumber <= SEATS_PER_ROW[row - 'A']; seatNumber++) {
                if (seatStatus[row - 'A'][seatNumber - 1] == 0) {
                    System.out.print("O ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }


    // Task 10
    private static void print_tickets_info() {
        System.out.println("Tickets Information:");

        int totalAmount = 0;

        for (Ticket ticket : soldTickets) {
            if (ticket != null) {
                ticket.printTicketInfo();
                System.out.println("------------------------");
                totalAmount += ticket.getPrice();
            }
        }

        System.out.println("Total Amount for Sold Tickets: £" + totalAmount);
    }

    // Task 11:
    private static void search_ticket(Scanner scanner) {
        System.out.print("Enter row letter: ");
        char row = scanner.next().toUpperCase().charAt(0);

        // Validate row input
        if (row < 'A' || row > 'D') {
            System.out.println("Invalid row. Please try again.");
            return;
        }

        System.out.print("Enter seat number: ");
        int seatNumber = scanner.nextInt();

        // Validate seat number input
        if (seatNumber < 1 || seatNumber > SEATS_PER_ROW[row - 'A']) {
            System.out.println("Invalid seat number. Please try again.");
            return;
        }

        for (Ticket ticket : soldTickets) {
            if (ticket != null && ticket.getRow().equals(String.valueOf(row)) && ticket.getSeat() == seatNumber) {
                System.out.println("Ticket Information:");
                ticket.printTicketInfo();
                return;
            }
        }

        System.out.println("No information found for Seat " + row + seatNumber);
    }

}
