import java.util.ArrayList;
import java.util.List;

public class User {
    static int numberOfUsers = 0;
    private String name;
    private int id;
    private int credit=0;
    private List<Booking> bookings = new ArrayList<>();
    public User(String name) {
        this.name = name;
        this.id = ++numberOfUsers;
    }
    public void viewBookings() {
        bookings.forEach(Booking::showBooking);
        System.out.println("");
    }

    public String getName() {
        return name;
    }
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public Booking getBooking(int bookingId) {
        for(Booking booking: bookings) {
            if(booking.getId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }
    public void addCredit(int credit){
        this.credit+=credit;
    }
    public int getCredit(){
        return credit;
    }
    public void reduceCredit(int credit){
        this.credit-=credit;
    }
    public void setCredit(int credit){
        this.credit=credit;
    }
}
