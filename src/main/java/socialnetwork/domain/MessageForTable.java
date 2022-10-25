package socialnetwork.domain;

public class MessageForTable {
    private Long id;
    private String expeditor;
    private String destinatar;
    private String text;
    private String data;

    public MessageForTable(Long id,String expeditor, String destinatar,String text,String data) {
        this.id = id;
        this.expeditor = expeditor;
        this.destinatar = destinatar;
        this.text = text;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public String getExpeditor() {
        return expeditor;
    }

    public String getDestinatar() {
        return destinatar;
    }

    public String getText() {
        return text;
    }

    public String getData() {
        return data;
    }
}
