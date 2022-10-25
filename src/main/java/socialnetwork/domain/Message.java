package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long>{
    private Utilizator from;
    private List<Utilizator> to;
    private String message;
    private LocalDateTime data;
    private Message reply;

    public Message(Utilizator src, List<Utilizator> dest, String txt,LocalDateTime data){
        this.from = src;
        this.to = dest;
        this.message = txt;
        this.data = data;
        this.reply = null;
    }

    public Message getReply() {
        return reply;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getMessage(){
        return message;
    }

    public Utilizator getFrom() {
        return from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public void setReply(Message reply){
        this.reply = reply;
    }

    public void setFrom(Utilizator from){
        this.from=from;
    }

    public void setTo(List<Utilizator> to){
        this.to=to;
    }

    public String toString(){
        return "Message{ Id: " + this.getId() + "; Data: " + this.getData().toString() +
                "; De la: \'" + this.from.getFirstName() + " " + this.from.getLastName() +
                "\'; Pentru: " + this.getTo().size() + " persoane" +
                "; Text: \'" + message + "\' }";
    }
}
