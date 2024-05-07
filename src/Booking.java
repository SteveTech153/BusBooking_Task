import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Booking {
    static int numberOfBookings = 0;
    private int id;
    private int busId;
    private int userId;
    private String date;
    private String source;
    private String destination;
    private String startTime;
    private String endTime;
    private String day;
    private int noOfConfirmedSeats;
    private int noOfWaitingListSeats;
    static List<String> availableCities = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static HashMap<Integer, Booking> bookingMap = new HashMap<>();
    static List<Booking> waitingList = new ArrayList<>();
    //static HashMap<BusDatePair<String,String,String>, Bus> bookedMap = new HashMap<>();
    static int creditForCancellationOnSameDay = 50;
    static int creditForCancellationBefore = 80;
    static Scanner sc = new Scanner(System.in);

    public Booking(int userId, int busId, String date, int noOfConfirmedSeats, int noOfWaitingListSeats, String source, String destination, String startTime, String endTime){
        this.userId = userId;
        this.busId = busId;
        this.date = date;
        this.source = source;
        this.destination = destination;
        this.id = ++numberOfBookings;
        this.noOfConfirmedSeats = noOfConfirmedSeats;
        this.noOfWaitingListSeats = noOfWaitingListSeats;
        this.startTime = startTime;
        this.endTime = endTime;
        bookingMap.put(id, this);
    }
    public void showBooking() {
        System.out.println("Booking ID: " + id);
        System.out.println("User: " + User.getUser(userId).getUserName());
        System.out.println("no Of Confirmed Seats: " + noOfConfirmedSeats);
        System.out.println("no Of Waiting List Seats: " + noOfWaitingListSeats);
        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);
        System.out.println("Bus: " + busId);
        System.out.println("Date: " + date);
        System.out.println("");
    }
    public void viewDetails(){
        showBooking();
    }

    @Override
    public String toString() {
        return "Booking ID: " + id + "\n" +
                "User: " + User.getUser(userId).getUserName() + "\n" +
                "Bus: " + busId + "\n" +
                "Date: " + date + "\n";
    }

    public int getId() {
        return id;
    }
    public String getSource() {
        return source;
    }
    public String getDestination() {
        return destination;
    }

    public Bus getBus() {
        return Bus.getBus(busId);
    }

    public User getUser() {
        return User.getUser(userId);
    }

    public String getDate() {
        return date;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public int getUserId() {
        return userId;
    }

    public void setDate(String newDate) {
        this.date = newDate;
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


    public static boolean book(int userId, boolean changeDate) {
        System.out.println("Available cities : ");
        for(int i=0;i<availableCities.size();i++) {
            System.out.println((i+1)+" -> "+availableCities.get(i));
        }
        if(availableCities.size()==0){
            System.out.println("No cities available.\n");
            return false;
        }
        System.out.println("Enter the source city number : ");
        int sourceIndex = sc.nextInt();
        while(sourceIndex<0 || sourceIndex>availableCities.size()){
            System.out.println("Invalid source city. Enter again: ");
            sourceIndex = sc.nextInt();
        }
        String source = availableCities.get(sourceIndex-1);
        System.out.println("Enter the destination city number : ");
        int destinationIndex = sc.nextInt();
        while(destinationIndex<0 || destinationIndex>availableCities.size() || destinationIndex==sourceIndex){
            System.out.println("Invalid destination city. Enter again: ");
            destinationIndex = sc.nextInt();
        }
        String destination = availableCities.get(destinationIndex-1);

        System.out.println("Enter the date(yyyy-mm-dd) : ");
        String date = sc.next();
        while(true){
            try {
                LocalDate parsedDate = LocalDate.parse(date);
                if(LocalDate.now().compareTo(parsedDate)>0){
                    System.out.println("Invalid date. Enter again: ");
                    date = sc.next();
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Enter again: ");
                date = sc.next();
            }
        }

        String day = LocalDate.parse(date).getDayOfWeek().toString();
        HashMap<Integer, List<Schedule_SourceDestinationTimeDaysPair>> allSchedulesBusIdMap = Bus.allSchedulesBusIdMap;
        HashMap<Integer, Schedule_SourceDestinationTimeDaysPair> matchedSchedules = new HashMap<>();
        for(Map.Entry<Integer, List<Schedule_SourceDestinationTimeDaysPair>> entry: allSchedulesBusIdMap.entrySet()){
            for(Schedule_SourceDestinationTimeDaysPair schedule: entry.getValue()){
                if(schedule.getSource().equals(source) && schedule.getDestination().equals(destination) && schedule.getDays().contains(day.toLowerCase()) && (date.compareTo(LocalDate.now().toString())>0 || (date.compareTo(LocalDate.now().toString())==0 && schedule.getStartTime().compareTo(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))>0))){
                    matchedSchedules.put(entry.getKey(), schedule);
                    System.out.println("BusId: "+entry.getKey()+" Source: "+source+" Destination: "+destination+" Time: "+schedule.getStartTime()+"-"+schedule.getEndTime()+" Days: "+schedule.getDays()+ "seats available: "+Bus.getBus(entry.getKey()).getAvailableSeats(new DateSchedulePair<>(date, schedule)));
                }
            }
        }

        if(matchedSchedules.isEmpty()){
            System.out.println("No buses available for this route on this day\n");
            return false;
        }
        System.out.println("Enter the busId you want to book : ");
        int busId = sc.nextInt();
        while(!matchedSchedules.containsKey(busId)){
            System.out.println("Invalid busId. Enter again: ");
            busId = sc.nextInt();
        }
        System.out.println("pick a starting time(HH:MM): ");
        String startTime = sc.next();
        while(!startTime.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]") || matchedSchedules.get(busId).getStartTime().compareTo(startTime)!=0){
            System.out.println("Invalid time slot. Enter again: ");
            startTime = sc.next();
        }

        System.out.println("Enter number of seats you want to book : ");
        int noOfSeats = sc.nextInt();
        while(noOfSeats<=0){
            System.out.println("Invalid number of seats. Enter again: ");
            noOfSeats = sc.nextInt();
        }
        return book(userId, busId, source, destination, date, noOfSeats, startTime, matchedSchedules.get(busId), changeDate);
    }
    public static boolean book( int userId, int busId, String source, String destination, String date, int noOfSeats, String startTime, Schedule_SourceDestinationTimeDaysPair schedule, boolean changeDate) {
        Bus bus = Bus.getBus(busId);
        DateSchedulePair dateSchedulePair = new DateSchedulePair<>(date, schedule);
        if(bus.getTotalAvailableSeats(dateSchedulePair) < noOfSeats ) {
            System.out.println("Requested number of seats cannot be booked. Available seats : "+bus.getAvailableSeats(dateSchedulePair)+". Available waiting list: "+ bus.getAvailableSeatsForWaitingList(dateSchedulePair)+"\n");
            return false;
        }else if(bus.getAvailableSeats(dateSchedulePair) >= noOfSeats ) {
            Booking booking = new Booking(userId, busId, date, noOfSeats, 0, source, destination, startTime, schedule.getEndTime());
            bookings.add(booking);
            bus.setBookedSeats(dateSchedulePair, bus.getBookedSeats(dateSchedulePair)+noOfSeats);
           // bookedMap.put(new BusDatePair<String,String,String>(source, destination, date), bus);
            User user = User.getUser(userId);
            user.addBooking(booking);
            if(user.getCredit()>=noOfSeats*100) {
                user.reduceCredit(noOfSeats * 100);
                if(!changeDate){
                    System.out.println("You have to pay 0%");
                }
            }else{
                if(!changeDate)
                    System.out.println("You have to pay "+((100 - user.getCredit()/(noOfSeats*100)))+"%");
                user.setCredit(0);
            }
            if(!changeDate) {
                System.out.println("Booking successful");
                System.out.println("All seats are confirmed\n");
                System.out.println("Your Booking Id is : " + booking.getId() + "\n");
            }
            return true;
        }else{
            int noOfSeatsCopy = noOfSeats, remainingSeats = noOfSeats-bus.getAvailableSeats(dateSchedulePair);
            Booking booking = new Booking(userId, busId, date, bus.getAvailableSeats(dateSchedulePair), remainingSeats, source, destination, startTime, schedule.getEndTime());
            bookings.add(booking);
            waitingList.add(booking);
            bus.setBookedSeats(dateSchedulePair, bus.getBookedSeats(dateSchedulePair)+bus.getAvailableSeats(dateSchedulePair));
            bus.setNumberOfWaitingList(dateSchedulePair,bus.getNumberOfWaitingList(dateSchedulePair)+remainingSeats);
            //bookedMap.put(new BusDatePair<String,String,String>(source, destination, date), bus);
            User user = User.getUser(userId);
            user.addBooking(booking);
            if(user.getCredit()>=noOfSeats*100) {
                user.reduceCredit(noOfSeats * 100);
                if(!changeDate)
                    System.out.println("You have to pay 0%");
            }else{
                if(!changeDate)
                    System.out.println("You have to pay "+((100 - user.getCredit()/(noOfSeats*100)))+"%");
                user.setCredit(0);
            }
            if(!changeDate) {
                System.out.println("Booking successful for " + (noOfSeatsCopy - remainingSeats) + " seats and you've been added to the waiting list for " + remainingSeats + " seats");
                System.out.println("Your booking Id is: " + booking.getId() + "\n");
            }
            return true;
        }
    }


    public static void showAvailableCities() {
        availableCities.forEach(System.out::println);
        System.out.println("");
        if(availableCities.size()==0){
            System.out.println("No cities available\n");
        }
    }

    public static void cancelBooking(Integer userId, boolean changeDate) {
        System.out .println("Your bookings: ");
        User user = User.getUser(userId);
        if(!user.viewBookings())
            return;
        System.out.println("Enter booking id to cancel: ");
        int bookingId = sc.nextInt();
        Booking booking = user.getBooking(bookingId);
        while(booking==null){
            System.out.println("Invalid booking id. Enter again: ");
            bookingId = sc.nextInt();
            booking = user.getBooking(bookingId);
        }
        if(booking.date.compareTo(LocalDate.now().toString())<0){
            System.out.println("Cannot cancel booking");
            return;
        }

        int numberOfSeatsToCancel = 1;
        if(booking.getNoOfSeats() > 1){
            System.out.println("Enter number of seats to cancel: ");
            numberOfSeatsToCancel = sc.nextInt();
            while(numberOfSeatsToCancel<=0 || numberOfSeatsToCancel>booking.getNoOfSeats()){
                System.out.println("Invalid number of seats. Enter again: ");
                numberOfSeatsToCancel = sc.nextInt();
            }
        }
        cancelBooking(userId, bookingId, numberOfSeatsToCancel, changeDate);

    }
    public static void cancelBooking(Integer userId, int bookingId, int numberOfSeatsToCancel, boolean changeDate) {
        User user = User.getUser(userId);
        Booking booking = user.getBooking(bookingId);
        if(numberOfSeatsToCancel <= booking.getNoOfWaitingListSeats()){
            Bus bus = Bus.getBus(booking.busId);
            Schedule_SourceDestinationTimeDaysPair schedule = bus.getSchedule(booking.startTime, booking.date);
            DateSchedulePair dateSchedulePair = new DateSchedulePair<>(booking.date, schedule);
            bus.setNumberOfWaitingList(dateSchedulePair, bus.getNumberOfWaitingList(dateSchedulePair)-numberOfSeatsToCancel);
            booking.setNoOfWaitingListSeats(booking.getNoOfWaitingListSeats()-numberOfSeatsToCancel);
            if(booking.getNoOfWaitingListSeats()==0){
                waitingList.remove(booking);
            }
            if(booking.getNoOfWaitingListSeats()==0 && booking.getNoOfConfirmedSeats()==0){
                bookings.remove(booking);
                user.removeBooking(booking);
            }
            if(!changeDate)
                System.out.println("Booking cancelled successfully");
            if(booking.date.compareTo(LocalDate.now().toString())==0){
                user.addCredit(creditForCancellationOnSameDay*numberOfSeatsToCancel);
            }else{
                user.addCredit(creditForCancellationBefore*numberOfSeatsToCancel);
            }
            System.out.println("");
            return;
        }else{
            int remainingSeats = numberOfSeatsToCancel - booking.getNoOfWaitingListSeats();
            if(booking.getNoOfWaitingListSeats()>0){
                booking.setNoOfWaitingListSeats(0);
                waitingList.remove(booking);
            }
            booking.setNoOfConfirmedSeats(booking.getNoOfConfirmedSeats()-remainingSeats);
            if(booking.getNoOfConfirmedSeats()==0){
                bookings.remove(booking);
                user.removeBooking(booking);
            }
            Bus bus = Bus.getBus(booking.busId);
            Schedule_SourceDestinationTimeDaysPair schedule = bus.getSchedule(booking.startTime, booking.date);
            DateSchedulePair dateSchedulePair = new DateSchedulePair<>(booking.date, schedule);
            bus.setBookedSeats(dateSchedulePair, bus.getBookedSeats(dateSchedulePair)-remainingSeats);
            bus.setNumberOfWaitingList(dateSchedulePair, bus.getNumberOfWaitingList(dateSchedulePair)-remainingSeats);
            if(!changeDate)
                System.out.println("Booking cancelled successfully");
            if(booking.date.compareTo(LocalDate.now().toString())==0){
                user.addCredit(creditForCancellationOnSameDay*numberOfSeatsToCancel);
                if(!changeDate)
                    System.out.println("Credit added: "+creditForCancellationOnSameDay*numberOfSeatsToCancel);
            }else{
                user.addCredit(creditForCancellationBefore*numberOfSeatsToCancel);
                if(!changeDate)
                    System.out.println("Credit added: "+creditForCancellationBefore*numberOfSeatsToCancel);
            }
            System.out.println("");
            if(waitingList.size()>0){
                List<Booking> bookingsToRemove = new ArrayList<>();
                Iterator<Booking> iterator = waitingList.iterator();
                while (iterator.hasNext()) {
                    Booking waitingBooking = iterator.next();
                    Bus waitingbBus = Bus.getBus(waitingBooking.busId);
                    if(Bus.getBus(waitingBooking.busId).getSchedule(waitingBooking.startTime, waitingBooking.date).equals(schedule)){
                        if(bus.getAvailableSeats(dateSchedulePair)>0){
                            int seatsToConfirm = Math.min(waitingBooking.getNoOfWaitingListSeats(), bus.getAvailableSeats(dateSchedulePair));
                            waitingBooking.setNoOfWaitingListSeats(waitingBooking.getNoOfWaitingListSeats()-seatsToConfirm);
                            waitingBooking.setNoOfConfirmedSeats(waitingBooking.getNoOfConfirmedSeats()+seatsToConfirm);
                            bus.setBookedSeats(dateSchedulePair, bus.getBookedSeats(dateSchedulePair)+seatsToConfirm);
                            bus.setNumberOfWaitingList(dateSchedulePair, bus.getNumberOfWaitingList(dateSchedulePair)-seatsToConfirm);
                            if(waitingBooking.getNoOfWaitingListSeats()==0){
                                bookingsToRemove.add(waitingBooking);
                            }
                            if(waitingBooking.getNoOfWaitingListSeats()==0 && waitingBooking.getNoOfConfirmedSeats()==0){
                                bookingsToRemove.add(waitingBooking);
                            }
                            if(bus.getAvailableSeats(dateSchedulePair)==0){
                                break;
                            }
                        }
                    }
                }
                waitingList.removeAll(bookingsToRemove);
            }
        }
    }

    public static void requestChangeDate(Integer userId){
        System.out.println("Your bookings: ");
        User user = User.getUser(userId);
        if(!user.viewBookings())
            return;
        System.out.println("Enter booking id to change date: ");
        int bookingId = sc.nextInt();
        Booking booking = user.getBooking(bookingId);
        while(booking==null){
            System.out.println("Invalid booking id. Enter again: ");
            bookingId = sc.nextInt();
            booking = user.getBooking(bookingId);
        }
        System.out.println("Enter new date(yyyy-mm-dd): ");
        String newDate = sc.next();
        while(true){
            try {
                LocalDate parsedDate = LocalDate.parse(newDate);
                if(LocalDate.now().compareTo(parsedDate)>0 || newDate.compareTo(booking.getDate())<=0){
                    System.out.println("Invalid date. Enter again: ");
                    newDate = sc.next();
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Enter again: ");
                newDate = sc.next();
            }
        }
        if(book(userId, booking.getBus().getId(), booking.getSource(), booking.getDestination(), newDate, booking.getNoOfSeats(), booking.getStartTime(), booking.getBus().getSchedule(booking.getStartTime(), newDate), true)){
            cancelBooking(userId, bookingId, booking.getNoOfSeats(), true);
            System.out.println("Date changed successfully");
        }else{
            System.out.println("Date change failed");
        }
    }


}
