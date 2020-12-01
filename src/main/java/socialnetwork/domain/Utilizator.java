package socialnetwork.domain;

import java.util.*;

public class Utilizator extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<Utilizator> friends;

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Utilizator> getFriends() {
        return friends;
    }

    public void addFriend(Utilizator friend){
        if(this.friends == null)
            this.friends = new ArrayList<>();
        if(!this.friends.contains(friend)) {
            this.friends.add(friend);
        }
    }
    public void deleteFriend(Utilizator friend){
        if(this.friends != null)
            this.friends.remove(friend);
    }

    @Override
    public String toString() {
        if(this.friends != null)
            return "Utilizator{" +
                    "firstName='" + firstName + "'" +
                    ", lastName='" + lastName + "'" +
                    ", friends=" + friends.stream().map(Utilizator::getLastName)
                                                  .reduce((x,y)->{return x+" "+y;}) + "}";
        else
            return "Utilizator{" +
                    "firstName='" + firstName + "'" +
                    ", lastName='" + lastName + "'" +
                    ", friends=" + "empty" + "}";
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