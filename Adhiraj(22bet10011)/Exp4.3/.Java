interface Booking {
    // Abstract method to book a seat
    boolean bookSeat(int seatNumber, String customerType);
    
    // Default method to print a message about available seats
    default void printAvailableSeats() {
        System.out.println("Displaying available seats...");
    }
}

// Concrete class implementing the Booking interface
class TicketBooking implements Booking {
    private boolean[] seats;

    // Constructor to initialize the seats
    public TicketBooking(int numSeats) {
        seats = new boolean[numSeats]; // All seats initially available (false)
    }

    // Implementation of the bookSeat method from the interface
    @Override
    public synchronized boolean bookSeat(int seatNumber, String customerType) {
        if (seats[seatNumber]) {
            System.out.println(customerType + " - Seat " + seatNumber + " is already booked.");
            return false; // Seat already booked
        } else {
            seats[seatNumber] = true;
            System.out.println(customerType + " - Seat " + seatNumber + " successfully booked.");
            return true;
        }
    }

    // We can still override the default printAvailableSeats() if we want custom behavior
    @Override
    public void printAvailableSeats() {
        System.out.print("Available Seats: ");
        for (int i = 0; i < seats.length; i++) {
            if (!seats[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
}

// Regular Booking Thread class
class RegularBookingThread extends Thread {
    private Booking bookingSystem;
    private int seatNumber;

    public RegularBookingThread(Booking bookingSystem, int seatNumber) {
        this.bookingSystem = bookingSystem;
        this.seatNumber = seatNumber;
    }

    @Override
    public void run() {
        bookingSystem.bookSeat(seatNumber, "Regular Customer");
    }
}

// VIP Booking Thread class
class VIPBookingThread extends Thread {
    private Booking bookingSystem;
    private int seatNumber;

    public VIPBookingThread(Booking bookingSystem, int seatNumber) {
        this.bookingSystem = bookingSystem;
        this.seatNumber = seatNumber;
    }

    @Override
    public void run() {
        bookingSystem.bookSeat(seatNumber, "VIP Customer");
    }
}

// Main Class to run the booking system
public class Main {
    public static void main(String[] args) {
        // Create a TicketBooking instance using the Booking interface
        Booking ticketBooking = new TicketBooking(5);

        // Create and start threads for booking tickets
        Thread t1 = new RegularBookingThread(ticketBooking, 1);
        Thread t2 = new VIPBookingThread(ticketBooking, 2);
        Thread t3 = new RegularBookingThread(ticketBooking, 3);
        Thread t4 = new VIPBookingThread(ticketBooking, 1); // Trying to book same seat
        Thread t5 = new RegularBookingThread(ticketBooking, 2); // Trying to book already booked seat
        Thread t6 = new RegularBookingThread(ticketBooking, 4);

        // Set VIP threads with higher priority (10)
        t2.setPriority(Thread.MAX_PRIORITY);
        t4.setPriority(Thread.MAX_PRIORITY);

        // Start the threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print the available seats after all bookings
        ticketBooking.printAvailableSeats();
    }
}
