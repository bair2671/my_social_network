package socialnetwork.domain;

import java.time.LocalDateTime;


public class Prietenie extends Entity<Pereche<Long,Long>> {
    LocalDateTime date;

    public Prietenie(LocalDateTime date) {
        this.date = date;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
            "Id Utilizator1 ='" + this.getId().getLeft() + '\'' +
            ", Id Utilizator2 ='" + this.getId().getRight() + '\'' +
            ", data =" + this.date.toString() +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prietenie)) return false;
        Prietenie that = (Prietenie) o;
        return  this.getId().equals(that.getId());
    }
}
