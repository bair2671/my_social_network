package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<Utilizator> friends;
    private String username;
    private String password;

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<Utilizator>();
        if(firstName.length()<3)
            this.password = "abc";
        else
            this.password = firstName.toLowerCase();
    }

    public Utilizator(String firstName, String lastName,String username,String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<Utilizator>();
        this.username = username;
        this.password = password;
    }

    /**
     *
     * @return prenumele utilizatorului
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * modifica prenumele utilizatorului
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return numele utilizatorului
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * modifica numele utilizatorului
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return lista de preiteni a utilizatorului
     */
    public List<Utilizator> getFriends() {
        return friends;
    }

    /**
     * modifica lista de prieteni a  utilizatorului
     * @param friends
     */
    public void setFriends(List<Utilizator> friends){
        this.friends = friends;
    }

    /**
     * Adauga un Utilizator in lista de prieteni a utilizatorului
     * @param fr
     */
    public void addFriend(Utilizator fr) {
        this.friends.add(fr); }

    /**
     * Sterge un Utilizator din lista de prieteni a utilizatorului
     * @param fr
     */
    public void unFriend(Utilizator fr) { this.friends.remove(fr); }

    /**
     *
     * @return parola utilizatorului
     */
    public String getPassword() {
        return password;
    }

    /**
     * modifica parola utilizatorului
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}