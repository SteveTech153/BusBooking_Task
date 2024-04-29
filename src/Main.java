import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Admin admin = new Admin("admin");
        admin.setCities(new ArrayList<String>(Arrays.asList("Chennai", "Madurai", "Trichy", "Coimbatore", "Salem", "Tirunelveli", "Tiruppur", "Erode", "Vellore", "Tiruvannamalai")));

        Booker booker = new Booker();
        User user1 = new User("user1");
        int choice = 1;

        booker.showAvailableNoOfSeats("Chennai", "Madurai", "2024-04-29");

        booker.book(user1, "Chennai", "Madurai", "2024-04-29", 19, false);
        user1.viewBookings();

        User user2 = new User("user2");
        booker.book(user2, "Chennai", "Madurai", "2024-04-29", 1, false);
        user2.viewBookings();

        User user3 = new User("user3");
        booker.book(user3, "Chennai", "Madurai", "2024-04-29", 5, false);
        user3.viewBookings();

        booker.cancelBooking(user2,2, false);

        user3.getBooking(3).viewDetails();

        booker.requestChangeDate(user1, 1, "2024-04-30");
//        booker.showAvailableNoOfSeats("Chennai", "Madurai", "2024-04-30");

        user3.getBooking(3).viewDetails();

        booker.showAvailableNoOfSeats("Chennai", "Madurai", "2024-04-29");
    }
}