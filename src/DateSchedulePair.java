import java.util.Objects;

public class DateSchedulePair <Da, Sc>{
    private Da date;
    private Sc schedule;
    public DateSchedulePair(Da date, Sc schedule){
        this.date = date;
        this.schedule = schedule;
    }
    public String getDate(){
        return (String)date;
    }
    public Schedule_SourceDestinationTimeDaysPair getSchedule(){
        return (Schedule_SourceDestinationTimeDaysPair) schedule;
    }
    @Override
    public int hashCode() {
        return Objects.hash(date, schedule);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateSchedulePair<?, ?> that = (DateSchedulePair<?, ?>) o;
        return Objects.equals(date, that.date) && Objects.equals(schedule, that.schedule);
    }
}
