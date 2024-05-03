import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public void dummy() {
//        Admin admin = new Admin("admin");
//        admin.setCities(new ArrayList<String>(Arrays.asList("Chennai", "Madurai", "Trichy", "Coimbatore", "Salem", "Tirunelveli", "Tiruppur", "Erode", "Vellore", "Tiruvannamalai")));
//
//        Booker booker = new Booker();
//        User user1 = new User("user1");
//        int choice = 1;
//
//        booker.showAvailableNoOfSeats("Chennai", "Madurai", "2024-04-29");
//
//        booker.book(user1, "Chennai", "Madurai", "2024-04-29", 19, false);
//        user1.viewBookings();
//
//        User user2 = new User("user2");
//        booker.book(user2, "Chennai", "Madurai", "2024-04-29", 1, false);
//        user2.viewBookings();
//
//        User user3 = new User("user3");
//        booker.book(user3, "Chennai", "Madurai", "2024-04-29", 5, false);
//        user3.viewBookings();
//
//        booker.cancelBooking(user2,2, false);
//
//        user3.getBooking(3).viewDetails();
//
//        booker.requestChangeDate(user1, 1, "2024-04-30");
////        booker.showAvailableNoOfSeats("Chennai", "Madurai", "2024-04-30");
//
//        user3.getBooking(3).viewDetails();
//
//        booker.showAvailableNoOfSeats("Chennai", "Madurai", "2024-04-29");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bus Booking System\n");
        String choice = "1";
        while (!choice .equals("0")) {
            System.out.println("1 for Admin");
            System.out.println("2 for User");
            System.out.println("0 to exit");
            choice = sc.next();
            switch (choice) {
                case "0":
                    break;
                case "1":
                    System.out.println("Enter name: ");
                    String name = sc.next();
                    Admin admin = Admin.findAdmin(name);
                    if(admin==null){
                        break;
                    }
                    String adminChoice = "1";
                    while (!adminChoice.equals("0")) {
                        System.out.println("1 to add city");
                        System.out.println("2 to remove city");
                        System.out.println("3 to create bus");
                        System.out.println("4 to assign source destination time to bus");
                        System.out.println("5 to delete bus");
                        System.out.println("6 to view buses");
                        System.out.println("7 to modify bus");
                        System.out.println("8 to set the credits for cancellation");
                        System.out.println("9 to show All users");
                        System.out.println("10 to show all bookings");
                        System.out.println("11 to show credentials and privileges");
                        System.out.println("0 to exit");
                        adminChoice = sc.next();
                        switch (adminChoice) {
                            case "0":
                                break;
                            case "1":
                                System.out.println("Enter city: ");
                                String city = sc.next();
                                admin.addCity(city.toLowerCase());
                                break;
                            case "2":
                                if(Booking.availableCities.size()==0){
                                    System.out.println("No cities available");
                                    break;
                                }
                                System.out.println("Available cities: ");
                                for(int i=0; i<Booking.availableCities.size(); i++){
                                    System.out.println(Booking.availableCities.get(i));
                                }
                                System.out.println("Enter city: ");
                                String city1 = sc.next();
                                admin.removeCity(city1.toLowerCase());
                                break;
                            case "3":
                                admin.createBus();
                                break;
                            case "4":
                                admin.assignSourceDestinationTimeToBus();
                                break;
                            case "5":
                                if(Bus.noOfBuses==0)
                                {
                                    System.out.println("No buses to delete");
                                    break;
                                }
                                Bus.showAllBuses();
                                System.out.println("enter bus id to delete: ");
                                int busId = sc.nextInt();
                                admin.deleteBus(busId);
                                break;
                            case "6":
                                Bus.showAllBuses();
                                break;
                            case "7":
                                if(Bus.noOfBuses==0){
                                    System.out.println("No buses to modify");
                                    break;
                                }
                                Bus.showAllBuses();
                                System.out.println("Enter bus id to modify: ");
                                int busId1 = sc.nextInt();
                                System.out.println("Enter new number of seats: ");
                                int seats = sc.nextInt();
                                while(seats<=0){
                                    System.out.println("Invalid number of seats. Enter again: ");
                                    seats = sc.nextInt();
                                }
                                System.out.println("Enter new number of waiting list: ");
                                int waitingList = sc.nextInt();
                                while(waitingList<0 || waitingList>seats){
                                    System.out.println("Invalid number of waiting list or waiting list is higher than number of seats. Enter again: ");
                                    waitingList = sc.nextInt();
                                }
                                admin.modifyBus(busId1, seats, waitingList);
                                break;
                            case "8":
                                System.out.println("Enter credit to set for cancellation on the day of journey: ");
                                int credit = sc.nextInt();
                                admin.setCreditForCancellationOnSameDay(credit);
                                System.out.println("Enter credit to set for cancellation before the day of journey: ");
                                int credit1 = sc.nextInt();
                                admin.setCreditForCancellationBefore(credit1);
                                break;
                            case "9":
                                admin.showAllUsers();
                                break;
                            case "10":
                                admin.showAllBookings();
                                break;
                            case "11":
                                admin.showCredentialsAndPrivileges();
                                break;
                            default:
                                System.out.println("Invalid choice");
                                break;
                        }
                    }
                    break;
                case "2":
                    System.out.println("Enter username: ");
                    String name1 = sc.next();
                    User user = User.findUser(name1);
                    String userChoice = "1";
                    while (!userChoice.equals("0")) {
                        System.out.println("1 to show Available cities");
                        System.out.println("2 to book");
                        System.out.println("3 to cancel booking");
                        System.out.println("4 to request change date");
                        System.out.println("5 to view booking details and status");
                        System.out.println("6 view all bookings details and statuses");
                        System.out.println("7 to show credentials");
                        System.out.println("0 to exit");
                        userChoice = sc.next();
                        switch (userChoice) {
                            case "0":
                                break;
                            case "1":
                                Booking.showAvailableCities();
                                break;
                            case "2":
                                Booking.book(user.getId(), false);
                                break;
                            case "3":
                                Booking.cancelBooking(user.getId(), false);
                                break;
                            case "4":
                                Booking.requestChangeDate(user.getId());
                                break;
                            case "5":
                                System.out.println("Enter booking id: ");
                                int bookingId = sc.nextInt();
                                Booking booking = user.getBooking(bookingId);
                                if(booking!=null)
                                    user.getBooking(bookingId).viewDetails();
                                else
                                    System.out.println("No booking exist");
                                break;
                            case "6":
                                user.viewBookings();
                                break;
                            case "7":
                                user.showCredentialsAndPrivileges();
                                break;
                            default:
                                System.out.println("Invalid choice");
                                break;
                        }
                    }
                    break;
            }
        }
    }
}