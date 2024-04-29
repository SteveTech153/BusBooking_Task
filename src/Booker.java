import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Booker {
    static List<String> availableCities = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static List<Booking> waitingList = new ArrayList<>();
    static HashMap<BusDatePair<String,String,String>, Bus> srcDstDateBusMap = new HashMap<>();
    static HashMap<Integer, Bus> busIdMap = new HashMap<>();
    static HashMap<BusDatePair<String,String,String>, Bus> bookedMap = new HashMap<>();
    static HashMap<String, Integer> defaultSeatsForSourceDestination = new HashMap<>();
    static HashMap<String, Integer> defaultWLForSourceDestination = new HashMap<>();
    private int creditForCancellationOnSameDay = 50;
    private int creditForCancellationBefore = 80;

    public boolean book(User user, String source, String destination, String date, int noOfSeats) {
        return book(user, source, destination, date, noOfSeats, false);
    }

    public boolean book(User user, String source, String destination, String date, int noOfSeats, boolean changeDate) {
        if(availableCities.contains(source) && availableCities.contains(destination)) {
            Bus bus = srcDstDateBusMap.get(new BusDatePair<String,String,String>(source, destination, date));
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if(String.valueOf(currentDate).compareTo(date) >0) {
                System.out.println("Invalid date\n");
                return false;
            }else{
                if(bus==null){
                    bus = new Bus(source, destination, defaultSeatsForSourceDestination.getOrDefault(source+","+destination, Bus.defaultSeats), defaultWLForSourceDestination.getOrDefault(source+","+destination, Bus.defaultWaitingList));
                    srcDstDateBusMap.put(new BusDatePair<String,String,String>(source, destination, date), bus);
                    busIdMap.put(bus.getId(), bus);
                }
                if(bus.getAvailableSeats() >= noOfSeats ) {
                    Booking booking = new Booking(user, bus, date, noOfSeats, 0);
                    bookings.add(booking);
                    user.addBooking(booking);
                    bus.setAvailableSeats(bus.getAvailableSeats() - noOfSeats);
                    bookedMap.put(new BusDatePair<String,String,String>(source, destination, date), bus);
                    if(user.getCredit()!=0 && user.getCredit() < 100*noOfSeats){
                        if(!changeDate) System.out.println("Your credit is used and you've been provided with a discount of "+(user.getCredit()/100*noOfSeats)*100 +"% on the booking.");
                        user.setCredit(0);
                    }else if(user.getCredit()!=0){
                        if(!changeDate) System.out.println("You've been provided with a discount of 100% on the booking by using your credit points.");
                        user.reduceCredit(100*noOfSeats);
                    }
                    if(!changeDate) System.out.println("Booking successful");
                    System.out.println("Your Booking Id is : "+booking.getId()+"\n");
                    return true;
                }else if(bus.getAvailableSeatsForWaitingList()>=(noOfSeats-bus.getAvailableSeats())){
                    int noOfSeatsCopy = noOfSeats, remainingSeats = noOfSeats-bus.getAvailableSeats();
                    Booking booking = new Booking(user, bus, date, bus.getAvailableSeats(), remainingSeats);
                    if(bus.getAvailableSeats()>0){
                        bus.setAvailableSeats(0);
                        bookedMap.put(new BusDatePair<String,String,String>(source, destination, date), bus);
                    }
                    bookings.add(booking);
                    waitingList.add(booking);
                    user.addBooking(booking);
                    bus.setNumberOfWaitingList(bus.getNumberOfWaitingList() + remainingSeats);
                    bookedMap.put(new BusDatePair<String,String,String>(source, destination, date), bus);
                    if(user.getCredit()!=0 && user.getCredit() < 100*noOfSeats){
                        if(!changeDate) System.out.println("Your credit is used and you've been provided with a discount of "+(user.getCredit()/100*noOfSeats)*100 +"% on the booking.");
                        user.setCredit(0);
                    }else if(user.getCredit()!=0){
                        if(!changeDate) System.out.println("You've been provided with a discount of 100% on the booking by using your credit points.");
                        user.reduceCredit(100*noOfSeats);
                    }
                    System.out.println("Booking successful for "+(noOfSeatsCopy-remainingSeats)+" seats and you've been added to the waiting list for "+remainingSeats+" seats");
                    System.out.println("Your booking Id is: "+booking.getId()+"\n");
                    return true;
                }
                else {
                    System.out.println("Requested number of seats cannot be booked. Available seats : "+bus.getAvailableSeats()+". Available waiting list: "+bus.getAvailableSeatsForWaitingList()+"\n");
                    return false;
                }

            }
        } else {
            System.out.println("Invalid source or destination\n");
            return false;
        }
}

    public void showAvailableCities() {
        availableCities.forEach(System.out::println);
        System.out.println("");
    }
    public void cancelBooking(User user, int bookingId) {
        cancelBooking(user, bookingId, false);
    }

    public void cancelBooking(User user, int bookingId, boolean changeDate) {
        Booking booking = user.getBooking(bookingId);
        if(booking != null && booking.getDate().compareTo(LocalDate.now().toString())<=0) {
            Bus bus = booking.getBus();
            bus.setAvailableSeats(bus.getAvailableSeats() + booking.getNoOfSeats());
            bookings.remove(booking);
            user.removeBooking(booking);
            bookedMap.remove(new BusDatePair<String,String,String>(bus.getSource(), bus.getDestination(), booking.getDate()));
            if(booking.getDate().compareTo(LocalDate.now().toString())==0) {
                user.addCredit(creditForCancellationOnSameDay * booking.getNoOfSeats());
                if(!changeDate) System.out.println("Credit of "+creditForCancellationOnSameDay*booking.getNoOfSeats()+" added to your account");
            }else if(booking.getDate().compareTo(LocalDate.now().toString())>0) {
                user.addCredit(creditForCancellationBefore * booking.getNoOfSeats());
                if(!changeDate) System.out.println("Credit of "+creditForCancellationBefore*booking.getNoOfSeats()+" added to your account");
            }
            int waitingListReduced = 0, ind = 0;
            List<Booking> waitingListToBeRemoved = new ArrayList<>();
            while(waitingListReduced<booking.getNoOfConfirmedSeats() && !waitingList.isEmpty() && ind<waitingList.size()){
                Booking waitingBooking = waitingList.get(ind);
                if(waitingBooking.getBus().equals(bus) && waitingBooking.getDate().equals(booking.getDate())) {
                    if (waitingBooking.getNoOfWaitingListSeats() <= booking.getNoOfConfirmedSeats()){
                        waitingListToBeRemoved.add(waitingBooking);
                        bus.setNumberOfWaitingList(bus.getNumberOfWaitingList() - waitingBooking.getNoOfWaitingListSeats());
                        bus.setAvailableSeats(bus.getAvailableSeats() - waitingBooking.getNoOfWaitingListSeats());
                        waitingBooking.setNoOfConfirmedSeats(waitingBooking.getNoOfConfirmedSeats()+waitingBooking.getNoOfWaitingListSeats());
                        waitingBooking.setNoOfWaitingListSeats(0);
                        waitingListReduced+=waitingBooking.getNoOfWaitingListSeats();
                    }else{
                        waitingBooking.setNoOfConfirmedSeats(waitingBooking.getNoOfConfirmedSeats()+booking.getNoOfConfirmedSeats());
                        waitingBooking.setNoOfWaitingListSeats(waitingBooking.getNoOfWaitingListSeats()-booking.getNoOfConfirmedSeats());
                        bus.setNumberOfWaitingList(bus.getNumberOfWaitingList() - booking.getNoOfConfirmedSeats());
                        bus.setAvailableSeats(bus.getAvailableSeats() - booking.getNoOfConfirmedSeats());
                        waitingListReduced+=booking.getNoOfConfirmedSeats();
                    }
                }
                ind++;
            }
            waitingList.removeAll(waitingListToBeRemoved);
            if(!changeDate) System.out.println("Booking cancelled successfully");
        } else {
            System.out.println("Invalid booking ID or booking cannot be cancelled");
        }
        System.out.println("");
    }

    public void requestChangeDate(User user, int bookingId, String newDate){
        Booking booking = user.getBooking(bookingId);
        if(newDate.compareTo(booking.getDate())>0 && newDate.compareTo(LocalDate.now().toString())>0){
            int addedCredit = 0;
            if(newDate.compareTo(LocalDate.now().toString())==0){
                addedCredit = creditForCancellationOnSameDay * booking.getNoOfSeats();
                user.addCredit(addedCredit);
            }else{
                addedCredit = creditForCancellationBefore * booking.getNoOfSeats();
                user.addCredit(addedCredit);
            }
            boolean booked = book(user, booking.getSource(), booking.getDestination(), newDate, booking.getNoOfSeats(), true);
            if(booked){
                cancelBooking(user, bookingId, true);
                System.out.println("Booking date changed successfully");
            }else{
                System.out.println("Cannot be changed to this date. Seats not available.");
                user.reduceCredit(addedCredit);
            }
        }else{
            System.out.println("Cannot be changed to this date");
        }
        System.out.println("");
    }

    public void showAvailableNoOfSeats(String source, String destination, String date){
        Bus bus = srcDstDateBusMap.get(new BusDatePair<String,String,String>(source, destination, date));
        if(bus != null) {
            System.out.println("Available seats : "+bus.getAvailableSeats()+". Available waiting list: "+bus.getAvailableSeatsForWaitingList()+"\n");
        } else {
            System.out.println("Available seats : "+Bus.defaultSeats+". Available waiting list: "+Bus.defaultWaitingList+"\n");
        }
        System.out.println("");
    }



}
