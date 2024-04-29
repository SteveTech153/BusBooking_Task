import java.util.Objects;

public class BusDatePair <S, De, D>{
    private S source;
    private De destination;
    private D date;
    public BusDatePair(S source, De destination, D date) {
        this.source = source;
        this.destination = destination;
        this.date = date;
    }
    public S getSource() {
        return source;
    }
    public De getDestination() {
        return destination;
    }
    public D getDate() {
        return date;
    }
    @Override
    public int hashCode() {
        return Objects.hash(source, destination, date);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusDatePair<?, ?, ?> that = (BusDatePair<?, ?, ?>) o;
        return Objects.equals(source, that.source) && Objects.equals(destination, that.destination) && Objects.equals(date, that.date);
    }
}
