package socialnetwork.domain;

import java.time.LocalDateTime;

public class CerereDePrietenie extends Entity<Long>{
    Long idExpeditor;
    Long idDestinatar;
    CerereDePrietenieStatus status;
    LocalDateTime data;

    public CerereDePrietenie(Long idExpeditor,Long idDestinatar,CerereDePrietenieStatus status,LocalDateTime data){
        this.idExpeditor=idExpeditor;
        this.idDestinatar=idDestinatar;
        this.status = status;
        this.data = data;
    }

    public Long getIdDestinatar() {
        return idDestinatar;
    }

    public Long getIdExpeditor() {
        return idExpeditor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public CerereDePrietenieStatus getStatus() {
        return status;
    }

    public void setStatus(CerereDePrietenieStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CerereDePrietenie)) return false;
        CerereDePrietenie that = (CerereDePrietenie) o;
        return  this.getId().equals(that.getId());
    }

    @Override
    public String toString() {
        return "CerereDePrietenie{" +
                "Id Expeditor=" + this.getIdExpeditor() +
                ", Id Destinatar=" + this.getIdDestinatar() +
                ", Data= " + this.getData() +
                '}';
    }
}
