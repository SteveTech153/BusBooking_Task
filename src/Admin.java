import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Admin extends User implements UserActions{
    static Scanner sc = new Scanner(System.in);
    public Admin(String name) {
        super(name);
    }
    public static Admin findAdmin(String name) {
        for(User user: User.users.values()) {
            if(user.getUserName().equals(name)) {
                return (Admin) user;
            }
        }
        return new Admin(name);
    }

    public void addCity(String city){
        Booking.availableCities.add(city);
    }

    public void removeCity(String city){
        Booking.availableCities.remove(city);
    }

    public void setCities(List<String> cities){
        Booking.availableCities = cities;
    }
    public void showAllUsers(){
        for(User user: User.users.values()){
            System.out.println("User: "+user.getUserName()+" Id: "+user.getId());
        }
        if(User.users.size()==0){
            System.out.println("No users created yet");
        }
    }

    public void assignSourceDestinationTimeToBus(){
        System.out.println("Available buses: ");
        for(int i=0; i<Bus.busIdMap.size(); i++){
            System.out.print((i+1) + "-> BusId: "+Bus.busIdMap.get(i+1).getId());
            Bus.busIdMap.get(i+1).showSchedule();
        }
        if(Bus.busIdMap.size()==0){
            System.out.println("No buses available.");
            return;
        }
        System.out.println("");
        System.out.println("Enter choice: ");
        int busId = sc.nextInt();
        while(busId<=0 || busId>Bus.busIdMap.size()){
            System.out.println("Invalid choice. Enter again: ");
            busId = sc.nextInt();
        }
        System.out.println("Enter source city: ");
        System.out.println("Available cities: ");
        for(int i=0; i<Booking.availableCities.size(); i++){
            System.out.println((i+1) + "-> " + Booking.availableCities.get(i));
        }
        int sourceIndex = sc.nextInt();
        while(sourceIndex<=0 || sourceIndex>Booking.availableCities.size()){
            System.out.println("Invalid source city. Enter again: ");
            sourceIndex = sc.nextInt();
        }
        String source = Booking.availableCities.get(sourceIndex-1);

        System.out.println("Enter destination city: ");

        int destinationIndex = sc.nextInt();
        while(destinationIndex<=0 || destinationIndex>Booking.availableCities.size() || destinationIndex==sourceIndex){
            System.out.println("Invalid destination city. Enter again: ");
            destinationIndex = sc.nextInt();
        }
        String destination = Booking.availableCities.get(destinationIndex-1);

        System.out.println("Enter starting time(HH:MM): ");
        String startTime = sc.next();
        while(!startTime.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")){
            System.out.println("Invalid time slot. Enter again: ");
            startTime = sc.next();
        }
        System.out.println("Enter ending time(HH:MM): ");
        String endTime = sc.next();
        while(!endTime.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")){
            System.out.println("Invalid time slot. Enter again: ");
            endTime = sc.next();
        }

        System.out.println("Enter number of days the bus is available for this route in a week: ");
        int cnt = sc.nextInt();
        while(cnt<=0){
            System.out.println("Number of days should be greater than 0. Enter again: ");
            cnt = sc.nextInt();
        }
        List<String> days = new ArrayList<>();
        if(cnt==7){
            days.add("monday");
            days.add("tuesday");
            days.add("wednesday");
            days.add("thursday");
            days.add("friday");
            days.add("saturday");
            days.add("sunday");
            Bus.busIdMap.get(busId).addSchedule(source, destination, startTime, endTime, days);
            return;
        }
        for (int i = 0; i < cnt; i++) {
            System.out.println("Enter day " + (i + 1) + ": ");
            String day = sc.next();
            while (!day.matches("Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday")) {
                System.out.println("Invalid day.(Type Monday or Tuesday or Wednesday or Thursday or Friday or Saturday or Sunday) Enter again: ");
                day = sc.next();
                day = day.toLowerCase();
            }
            days.add(day);
        }
        Bus.busIdMap.get(busId).addSchedule(source, destination, startTime, endTime, days);

    }
    public void createBus(){
        System.out.println("Enter number of seats: ");
        int noOfSeats = sc.nextInt();
        while(noOfSeats<=0){
            System.out.println("Number of seats should be greater than 0. Enter again: ");
            noOfSeats = sc.nextInt();
        }

        System.out.println("Enter number of waiting list: ");
        int noOfWaitingList = sc.nextInt();
        while(noOfWaitingList<0 || noOfWaitingList>noOfSeats){
            System.out.println("Number of waiting list should be greater than or equal to 0 and should be less than or equal to total number of seats. Enter again: ");
            noOfWaitingList = sc.nextInt();
        }
        Bus bus = new Bus(noOfSeats, noOfWaitingList);
        Bus.busIdMap.put(bus.getId(), bus);
    }

    public void modifyBus(int busId, int noOfSeats, int noOfWaitingList){
        Bus bus = Bus.busIdMap.get(busId);
        if(bus==null){
            System.out.println("Bus not found");
            return;
        }
        bus.setTotalSeats(noOfSeats);
        bus.setTotalNumberOfWaitingList(noOfWaitingList);
    }

    public void deleteBus(int busId){
        if(Bus.busIdMap.containsKey(busId)){
            Bus.busIdMap.remove(busId);
        }
        else{
            System.out.println("Bus not found");
        }
    }
    public void setCreditForCancellationOnSameDay (int i) {
        Booking.creditForCancellationOnSameDay = i;
    }
    public void setCreditForCancellationBefore (int i) {
        Booking.creditForCancellationBefore = i;
    }
    @Override
    public void showCredentialsAndPrivileges() {
        System.out.println("Admin: "+ this.getUserName());
        showPrivileges();
    }
    public void showPrivileges(){
        System.out.println("1. Can add city");
        System.out.println("2. Can remove city");
        System.out.println("3. Can create bus");
        System.out.println("4. Can assign source destination time to bus");
        System.out.println("5. Can delete bus");
        System.out.println("6. Can view buses");
        System.out.println("7. Can modify bus");
        System.out.println("8. Can set the credits for cancellation");
        System.out.println("9. Can See All users");
        System.out.println("");
    }
}
