package socialnetwork.domain;

public class CerereDePrietenieForTable {
    private Long id;
    private String expeditor;
    private String destinatar;
    private CerereDePrietenieStatus status;
    private String data;

    public CerereDePrietenieForTable(Long id,String expeditor, String destinatar, CerereDePrietenieStatus status, String data) {
        this.id = id;
        this.expeditor = expeditor;
        this.destinatar = destinatar;
        this.status = status;
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

    public CerereDePrietenieStatus getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }
}
