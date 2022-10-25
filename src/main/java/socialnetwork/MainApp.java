package socialnetwork;
import javafx.application.Application;
import javafx.stage.Stage;
import socialnetwork.controller.PrimaryStageController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.*;
import socialnetwork.service.Service;

public class MainApp extends Application{

    @Override
    public void start(Stage stage) {
        String prietenieFileName="data/prietenii.csv";
        String messageFileName="data/messages.csv";
        String cereriDePrietenieFileName="data/cereriDePrietenie.csv";

        String url ="jdbc:postgresql://localhost:5432/socialnetwork";
        String username="postgres";
        String password="postgres";
        Repository<Long, Utilizator> userRepository = new UtilizatorDb(url,username,password,new UtilizatorValidator());
        Repository<Pereche<Long,Long>, Prietenie> prietenieRepository = new PrietenieDb(url,username,password,new PrietenieValidator());
        Repository<Long, CerereDePrietenie> cereriDePrietenieRepository = new CerereDePrietenieDb(url,username,password, new CerereDePrietenieValidator());
        Repository<Long, Message> messageRepository = new MessageDb(url,username,password, new MessageValidator());
        EvenimentDb evenimentRepository = new EvenimentDb(url,username,password,new EvenimentValidator());
        Repository<Tuple<Long,Long>, UtilizatorEveniment> utilizatorEvenimentRepository = new UtilizatorEvenimentDb(url,username,password,new UtilizatorEvenimentValidator());

        Service service = new Service(userRepository,prietenieRepository,messageRepository,cereriDePrietenieRepository,evenimentRepository,utilizatorEvenimentRepository);

        PrimaryStageController controller = new PrimaryStageController(stage,service);
        stage.setTitle("SocialNetwork");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
