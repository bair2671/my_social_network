package socialnetwork;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.CerereDePrietenieValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.PrietenieValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.CerereDePrietenieDb;
import socialnetwork.repository.database.MessageDb;
import socialnetwork.repository.database.PrietenieDb;
import socialnetwork.repository.database.UtilizatorDb;
import socialnetwork.service.Service;
import socialnetwork.ui.Ui;


public class Main {
    public static void main(String[] args) {
        //FISIERE
        //String fileName=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String userFileName="data/users.csv";
        String prietenieFileName="data/prietenii.csv";
        String messageFileName="data/messages.csv";
        String cereriDePrietenieFileName="data/cereriDePrietenie.csv";
        //Repository<Long,Utilizator> userRepository = new UtilizatorFile(userFileName, new UtilizatorValidator());
        //Repository<Tuple<Long,Long>, Prietenie> prietenieRepository = new PrietenieFile(prietenieFileName, new PrietenieValidator());
        //Repository<Long, Message> messageRepository = new MessageFile(messageFileName, new MessageValidator());
        //Repository<Long, CerereDePrietenie> cereriDePrietenieRepository = new CerereDePrietenieFile(cereriDePrietenieFileName, new CerereDePrietenieValidator());

        //BAZA DE DATE
        String url ="jdbc:postgresql://localhost:5432/socialnetwork";
        String username="postgres";
        String password="postgres";
        Repository<Long,Utilizator> userRepository = new UtilizatorDb(url,username,password,new UtilizatorValidator());
        Repository<Pereche<Long,Long>, Prietenie> prietenieRepository = new PrietenieDb(url,username,password,new PrietenieValidator());
        Repository<Long, CerereDePrietenie> cereriDePrietenieRepository = new CerereDePrietenieDb(url,username,password, new CerereDePrietenieValidator());
        Repository<Long, Message> messageRepository = new MessageDb(url,username,password, new MessageValidator());

        //userFileRepository.findAll().forEach(System.out::println);
        //prietenieFileRepository.findAll().forEach(System.out::println);
        Service service = new Service(userRepository,prietenieRepository,messageRepository,cereriDePrietenieRepository);
        Ui ui = new Ui(service);

        //ui.startUi();
        MainApp.main(args);

    }
}


