import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements UserActions{
    static int numberOfUsers = 0;
    static HashMap<Integer, User> users = new HashMap<>();
    private String username;
    private int id;
    private int credit=0;
    private List<Booking> bookings = new ArrayList<>();
    public User(String name) {
        this.username = name;
        this.id = ++numberOfUsers;
        users.put(id, this);
    }
    public static User findUser(String name) {
        for(User user: users.values()) {
            if(user.getUserName().equals(name)) {
                return user;
            }
        }
        return new User(name);
    }
    public boolean viewBookings() {
        bookings.forEach(Booking::showBooking);
        if(bookings.size()==0){
            System.out.println("No bookings");
            return false;
        }
        System.out.println("");
        return true;
    }

    public String getUserName() {
        return username;
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
    public static User getUser(int id){
        return users.get(id);
    }
    public int getId() {
        return id;
    }

    @Override
    public void showCredentialsAndPrivileges() {
        System.out.println("Username: "+username);
        System.out.println("Id: "+id);
        System.out.println("Credit: "+credit);
        System.out.println("");
    }
}
