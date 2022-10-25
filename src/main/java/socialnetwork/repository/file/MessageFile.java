package socialnetwork.repository.file;

import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MessageFile extends AbstractFileRepository<Long, Message>{

    public MessageFile(String fileName, Validator<Message> validator) {
        super(fileName, validator);
    }

    @Override
    public Message extractEntity(List<String> attributes) {
        LocalDate date = LocalDate.of(Integer.parseInt(attributes.get(2)),Integer.parseInt(attributes.get(3)),Integer.parseInt(attributes.get(4)));
        LocalTime time = LocalTime.of(Integer.parseInt(attributes.get(5)),Integer.parseInt(attributes.get(6)));
        LocalDateTime dateTime = LocalDateTime.of(date,time);

        Utilizator src = new Utilizator("fn","ln");
        src.setId(Long.parseLong(attributes.get(8)));

        List<Utilizator> dest = new ArrayList<Utilizator>();
        for(int i=0;i<Long.parseLong(attributes.get(9));i++) {
            Utilizator u = new Utilizator("fn","ln");
            u.setId(Long.parseLong(attributes.get(10+i)));
            dest.add(u);
        }
        Message msg = new Message(src,dest,attributes.get(7).toString(),dateTime);
        msg.setId(Long.parseLong(attributes.get(0)));

        if(attributes.get(1).toString().equals("null"))
            msg.setReply(null);
        else {
            Message reply = new Message(src, dest, "text", dateTime);
            reply.setId(Long.parseLong(attributes.get(1)));
            msg.setReply(reply);
        }

        return msg;
    }

    @Override
    protected String createEntityAsString(Message entity) {
        String reply = null;
        if(entity.getReply()!=null)
            reply = entity.getReply().getId().toString();
        String sir = entity.getId() + ";" + reply + ";"
                 + entity.getData().getYear() + ";" + entity.getData().getMonthValue() + ";" + entity.getData().getDayOfMonth() + ";"
                 + entity.getData().getHour() + ";" + entity.getData().getMinute() + ";"
                 + entity.getMessage() + ";" + entity.getFrom().getId() + ";" + entity.getTo().size();
        for(Utilizator u:entity.getTo())
             sir+=";"+u.getId();
        return sir;
    }
}

