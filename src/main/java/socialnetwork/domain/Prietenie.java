package socialnetwork.domain;

import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long,Long>> {
    LocalDateTime date;

    public Prietenie() {
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Prietenie{" + this.getId().getLeft()+ "-" + this.getId().getRight() +
                " at date=" + this.date +
                '}';
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }
    /**
     * sets a date for a friendship. for file
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }


}
