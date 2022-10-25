package socialnetwork.domain;

public class UtilizatorEveniment extends Entity<Tuple<Long,Long>>{
    private boolean notificari;

    public UtilizatorEveniment(boolean notificari) {
        this.notificari = notificari;
    }

    public boolean getNotificari() {
        return notificari;
    }

    public void setNotificari(boolean notificari) {
        this.notificari = notificari;
    }

}
