package socialnetwork.repository.file;

import socialnetwork.domain.CerereDePrietenie;
import socialnetwork.domain.CerereDePrietenieStatus;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class CerereDePrietenieFile extends AbstractFileRepository<Long, CerereDePrietenie>{

    public CerereDePrietenieFile(String fileName, Validator<CerereDePrietenie> validator) {
        super(fileName, validator);
    }

    @Override
    public CerereDePrietenie extractEntity(List<String> attributes) {
        LocalDate date = LocalDate.of(Integer.parseInt(attributes.get(4)),Integer.parseInt(attributes.get(5)),Integer.parseInt(attributes.get(6)));
        LocalTime time = LocalTime.of(Integer.parseInt(attributes.get(7)),Integer.parseInt(attributes.get(8)));
        LocalDateTime dateTime = LocalDateTime.of(date,time);

        CerereDePrietenie cerere = new CerereDePrietenie(Long.parseLong(attributes.get(1)),Long.parseLong(attributes.get(2)), CerereDePrietenieStatus.valueOf(attributes.get(3)),dateTime);
        cerere.setId(Long.parseLong(attributes.get(0)));

        return cerere;
    }

    @Override
    protected String createEntityAsString(CerereDePrietenie entity) {
        return entity.getId()+";"+entity.getIdExpeditor()+";"+entity.getIdDestinatar()+";"+entity.getStatus().toString()+";"+
                + entity.getData().getYear() + ";" + entity.getData().getMonthValue() + ";" + entity.getData().getDayOfMonth() + ";"
                + entity.getData().getHour() + ";" + entity.getData().getMinute();
    }
}

