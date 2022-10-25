package socialnetwork.domain;

public class Activitate {
    private ActivitateTip tip;
    private String partener;
    private String data;

    public Activitate(ActivitateTip tip, String partener, String data) {
        this.tip = tip;
        this.partener = partener;
        this.data = data;
    }

    public ActivitateTip getTip() {
        return tip;
    }

    public String getPartener() {
        return partener;
    }

    public String getData() {
        return data;
    }
}
