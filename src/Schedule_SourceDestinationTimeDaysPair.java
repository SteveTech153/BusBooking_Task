import java.util.ArrayList;

public class Schedule_SourceDestinationTimeDaysPair<S, De, ST, ET, Da>{
    private S source;
    private De destination;
    private ST startTime;
    private ET endTime;
    private Da days;
    public Schedule_SourceDestinationTimeDaysPair(S source, De destination, ST time, ET endTime, Da days){
        this.source = source;
        this.destination = destination;
        this.startTime = time;
        this.endTime = endTime;
        this.days = days;
    }
    public String getSource(){
        return (String)source;
    }
    public String getDestination(){
        return (String)destination;
    }
    public String getStartTime(){
        return (String)startTime;
    }
    public String getEndTime(){
        return (String)endTime;
    }
    public ArrayList<String> getDays(){
        return (ArrayList<String>) days;
    }
}
