public class Bus {
    static int defaultSeats = 20;
    static int defaultWaitingList = 5;
    private int id=0;
    private String source;
    private String destination;
    private int availableSeats = 20;
    private int totalSeats = 20;
    private int totalNumberOfWaitingList = 5;
    private int numberOfWaitingList = 0;
    public Bus(String source, String destination) {
        this.source = source;
        this.destination = destination;
        this.id = ++id;
    }
    public Bus(String source, String destination, int noOfSeats, int noOfWaitingList) {
        this.source = source;
        this.destination = destination;
        this.availableSeats = noOfSeats;
        this.totalSeats = noOfSeats;
        this.totalNumberOfWaitingList = noOfWaitingList;
        this.id = ++id;
    }
    public void showDetails(){
        System.out.println("Bus ID: " + id);
        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);
        System.out.println("Available Seats: " + availableSeats);
        System.out.println("Total Seats: " + totalSeats);
    }
    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int i) {
        availableSeats = i;
    }
    public boolean isWaitingListAvailable() {
        return numberOfWaitingList < totalNumberOfWaitingList;
    }

    public int getId() {
        return this.id;
    }

    public String getSource() {
        return this.source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getDestination() {
        return this.destination;
    }
    public void setTotalNumberOfWaitingList(int i) {
        totalNumberOfWaitingList = i;
    }
    public int getTotalNumberOfWaitingList() {
        return totalNumberOfWaitingList;
    }
    public void setNumberOfWaitingList(int i) {
        numberOfWaitingList = i;
    }

    public int getNumberOfWaitingList() {
        return numberOfWaitingList;
    }
    public int getAvailableSeatsForWaitingList() {
        return totalNumberOfWaitingList - numberOfWaitingList;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bus that = (Bus) o;
        return source.equals(that.source) && destination.equals(that.destination);
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
