package socialnetwork.repository.file;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Pereche;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class PrietenieFile extends AbstractFileRepository<Pereche<Long,Long>, Prietenie>{

    public PrietenieFile(String fileName, Validator<Prietenie> validator) {
        super(fileName, validator);
    }

    @Override
    public Prietenie extractEntity(List<String> attributes) {
        LocalDate date = LocalDate.of(Integer.parseInt(attributes.get(2)),Integer.parseInt(attributes.get(3)),Integer.parseInt(attributes.get(4)));
        LocalTime time = LocalTime.of(Integer.parseInt(attributes.get(5)),Integer.parseInt(attributes.get(6)));
        LocalDateTime dateTime = LocalDateTime.of(date,time);

        Prietenie prietenie = new Prietenie(dateTime);
        prietenie.setId(new Pereche<>(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1))));

        return prietenie;
    }

    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getId().getLeft()+";"+entity.getId().getRight()+";"
                +entity.getDate().getYear()+";"+entity.getDate().getMonthValue() +";"+entity.getDate().getDayOfMonth()+";"
                +entity.getDate().getHour()+";"+entity.getDate().getMinute();
    }

    public List<Utilizator> getUserFriends(Utilizator user){
        return user.getFriends();
    }
}
