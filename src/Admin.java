import java.util.List;

public class Admin extends User{

    public Admin(String name) {
        super(name);
    }

    public void addCity(String city){
        Booker.availableCities.add(city);
    }

    public void removeCity(String city){
        Booker.availableCities.remove(city);
    }

    public void setCities(List<String> cities){
        Booker.availableCities = cities;
    }

    public void createBus(String source, String destination, int noOfSeats, int noOfWaitingList, List<String> dates){
        for(int i=0; i<dates.size(); i++){
            Bus bus = new Bus(source, destination, noOfSeats, noOfWaitingList);
            Booker.busIdMap.put(bus.getId(), bus);
            Booker.srcDstDateBusMap.put(new BusDatePair<>(source, destination, dates.get(i)), bus);
        }
    }

    public void modifyBus(int busId, String source, String destination, int noOfSeats, int noOfWaitingList){
        Booker.busIdMap.get(busId).setSource(source);
        Booker.busIdMap.get(busId).setDestination(destination);
        Booker.busIdMap.get(busId).setAvailableSeats(noOfSeats);
        Booker.busIdMap.get(busId).setTotalNumberOfWaitingList(noOfWaitingList);
    }

    public void deleteBus(int busId){
        Booker.srcDstDateBusMap.remove(new BusDatePair<>(Booker.busIdMap.get(busId).getSource(), Booker.busIdMap.get(busId).getDestination(), ""));
        Booker.busIdMap.remove(busId);
    }

    public void addDefaultSeatsForSourceDestination(String source, String destination, int noOfSeats, int noOfWaitingList){
        Booker.defaultSeatsForSourceDestination.put(source+","+destination, noOfSeats);
        Booker.defaultWLForSourceDestination.put(source+","+destination, noOfWaitingList);
    }

    public void setDefaultSeats(int defaultSeats, int defaultWaitingList){
        Bus.defaultSeats = defaultSeats;
        Bus.defaultWaitingList = defaultWaitingList;
    }


}
