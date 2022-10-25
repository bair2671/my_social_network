package socialnetwork.domain;

import java.time.LocalDateTime;

public class Eveniment extends  Entity<Long>{
    String nume;
    LocalDateTime data;

    public Eveniment(String nume, LocalDateTime data) {
        this.nume = nume;
        this.data = data;
    }

    public String getNume() {
        return nume;
    }

    public LocalDateTime getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Eveniment{" +
                "nume='" + nume + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

}
