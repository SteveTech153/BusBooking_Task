import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Bus {
    static int defaultSeats = 20;
    static int defaultWaitingList = 5;
    static int noOfBuses = 0;
    private int id=0;
    private HashMap<DateSchedulePair, Integer> bookedSeats = new HashMap<>();
    private HashMap<DateSchedulePair, Integer> numberOfWaitingList = new HashMap<>();
    private int totalSeats = 20;
    private int totalNumberOfWaitingList = 5;
    static HashMap<Integer, List<Schedule_SourceDestinationTimeDaysPair>> allSchedules = new HashMap<>();
    private List<Schedule_SourceDestinationTimeDaysPair> schedules = new ArrayList<>();
    static HashMap<Integer, Bus> busIdMap = new HashMap<>();
    static HashMap<BusDatePair<String,String,String>, HashMap<String, Bus>> srcDstDateBusMap = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    public Bus(int noOfSeats, int noOfWaitingList) {
        this.totalSeats = noOfSeats;
        this.totalNumberOfWaitingList = noOfWaitingList;
        this.id = ++noOfBuses;
        busIdMap.put(id, this);
    }

    public int getAvailableSeats(DateSchedulePair dateSchedulePair) {
        return totalSeats - bookedSeats.getOrDefault(dateSchedulePair, 0);

    }
    public int getTotalAvailableSeats(DateSchedulePair dateSchedulePair) {
        return totalSeats - bookedSeats.getOrDefault(dateSchedulePair, 0) + totalNumberOfWaitingList - numberOfWaitingList.getOrDefault(dateSchedulePair, 0);
    }
    public void setTotalSeats(int i) {
        totalSeats = i;
    }
    public void setBookedSeats(DateSchedulePair dateSchedulePair, int i) {
        bookedSeats.put(dateSchedulePair, i);
    }
    public boolean isWaitingListAvailable(DateSchedulePair dateSchedulePair) {
        return totalNumberOfWaitingList - numberOfWaitingList.getOrDefault(dateSchedulePair, 0) > 0;
    }
    public void showSchedule(){
        for(int i=0; i<schedules.size(); i++){
            System.out.println("Source: "+schedules.get(i).getSource()+" Destination: "+schedules.get(i).getDestination()+" Time: "+schedules.get(i).getStartTime()+"-"+schedules.get(i).getEndTime()+" Days: "+schedules.get(i).getDays());
        }
    }
    public Schedule_SourceDestinationTimeDaysPair getSchedule(String startTime, String date){
        LocalDate localDate = LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String day = localDate.getDayOfWeek().toString();
        for(Schedule_SourceDestinationTimeDaysPair schedule: schedules){
            if(schedule.getStartTime().equals(startTime) && schedule.getDays().contains(day.toLowerCase())){
                return schedule;
            }
        }
        return null;
    }
    public void addSchedule(String source, String destination, String startTime, String endTime, List<String> days){
        for(Schedule_SourceDestinationTimeDaysPair schedule: schedules){
            for(Object day: schedule.getDays()){
                if(days.contains((String)day)){
                    if((schedule.getStartTime().compareTo(startTime)<=0 && schedule.getEndTime().compareTo(startTime)>=0) || (schedule.getStartTime().compareTo(endTime)<=0 && schedule.getEndTime().compareTo(endTime)>=0)){
                        System.out.println("Time slot collides with another schedule. Enter different time slot again: ");
                        return;
                    }
                }
            }
        }
        schedules.add(new Schedule_SourceDestinationTimeDaysPair(source, destination, startTime, endTime, days));
        if(!allSchedules.containsKey(id)){
            allSchedules.put(id, schedules);
        }
    }
    public int getId() {
        return this.id;
    }
    public static Bus getBus(int id) {
        return busIdMap.get(id);
    }
    public void setTotalNumberOfWaitingList(int i) {
        totalNumberOfWaitingList = i;
    }
    public int getTotalNumberOfWaitingList() {
        return totalNumberOfWaitingList;
    }
    public void setNumberOfWaitingList(DateSchedulePair dateSchedulePair, int i) {
        numberOfWaitingList.put(dateSchedulePair, i);
    }

    public int getNumberOfWaitingList(DateSchedulePair dateSchedulePair) {
        return numberOfWaitingList.getOrDefault(dateSchedulePair, 0);
    }
    public int getAvailableSeatsForWaitingList(DateSchedulePair dateSchedulePair) {
        return totalNumberOfWaitingList - numberOfWaitingList.getOrDefault(dateSchedulePair, 0);
    }
    public static void showAllBuses(){
        System.out.println("Available buses: ");
        for(int i=0; i<Bus.busIdMap.size(); i++){
            System.out.print((i+1) + "-> ");
            Bus.busIdMap.get(i+1).showSchedule();
        }
    }

    public void showAvailableNoOfSeats(String source, String destination, String date, String time){
        Bus bus = srcDstDateBusMap.get(new BusDatePair<>(source, destination, date)).get(time);
        if(bus != null) {
            System.out.println("Available seats : "+bus.getAvailableSeats(new DateSchedulePair(date, time))+". Available waiting list: "+bus.getAvailableSeatsForWaitingList(new DateSchedulePair(date, time))+"\n");
        } else {
            System.out.println("Available seats : "+Bus.defaultSeats+". Available waiting list: "+Bus.defaultWaitingList+"\n");
        }
        System.out.println("");
    }

    public int getBookedSeats(DateSchedulePair dateSchedulePair) {
        return bookedSeats.getOrDefault(dateSchedulePair, 0);
    }
}
