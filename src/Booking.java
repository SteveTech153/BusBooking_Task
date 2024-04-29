public class Booking {
    static int numberOfBookings = 0;
    private int id;
    private Bus bus;
    private User user;
    private String date;
    private int noOfConfirmedSeats;
    private int noOfWaitingListSeats;

    public Booking(User user, Bus bus, String date, int noOfConfirmedSeats, int noOfWaitingListSeats) {
        this.user = user;
        this.bus = bus;
        this.date = date;
        this.id = ++numberOfBookings;
        this.noOfConfirmedSeats = noOfConfirmedSeats;
        this.noOfWaitingListSeats = noOfWaitingListSeats;
    }
    public void showBooking() {
        System.out.println("Booking ID: " + id);
        System.out.println("User: " + user.getName());
        System.out.println("Source: " + bus.getSource());
        System.out.println("Destination: " + bus.getDestination());
        System.out.println("no Of Confirmed Seats: " + noOfConfirmedSeats);
        System.out.println("no Of Waiting List Seats: " + noOfWaitingListSeats);
        System.out.println("Bus: " + bus.getId());
        System.out.println("Date: " + date);
        System.out.println("");
    }
    public void viewDetails(){
        showBooking();
    }

    @Override
    public String toString() {
        return "Booking ID: " + id + "\n" +
                "User: " + user.getName() + "\n" +
                "Source: " + bus.getSource() + "\n" +
                "Destination: " + bus.getDestination() + "\n" +
                "Bus: " + bus.getId() + "\n" +
                "Date: " + date + "\n";
    }

    public int getId() {
        return id;
    }

    public Bus getBus() {
        return bus;
    }

    public User getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String newDate) {
        this.date = newDate;
    }
    public String getDestination() {
        return bus.getDestination();
    }
    public String getSource() {
        return bus.getSource();
    }
    public int getNoOfSeats(){
        return noOfConfirmedSeats + noOfWaitingListSeats;
    }
    public int getNoOfConfirmedSeats() {
        return noOfConfirmedSeats;
    }
    public void setNoOfConfirmedSeats(int i) {
        noOfConfirmedSeats = i;
    }
    public int getNoOfWaitingListSeats() {
        return noOfWaitingListSeats;
    }
    public void setNoOfWaitingListSeats(int i) {
        noOfWaitingListSeats = i;
    }

}
