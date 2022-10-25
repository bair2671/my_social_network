package socialnetwork.domain;

public class EvenimentForTable {
    Long id;
    String nume;
    String data;

    public EvenimentForTable(Long id,String nume, String data) {
        this.id = id;
        this.nume = nume;
        this.data = data;
    }

    public Long getId() { return id; }

    public String getNume() {
        return nume;
    }

    public String getData() {
        return data;
    }

}
