package socialnetwork.ui;

import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.NonExistentObjectException;
import socialnetwork.service.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Ui {
    Service service;
    Scanner in;
    String linieUtilizatorLogat;

    public Ui(Service service){
        this.service=service;
        in = new Scanner(System.in);
    }

    private void addUtilizator(){
        Long id;
        String firstName;
        String lastName;

        System.out.println("Introduceti Id-ul:");
        id = in.nextLong();
        String col = in.nextLine();
        System.out.println("Introduceti Prenumele:");
        firstName = in.nextLine();
        System.out.println("Introduceti Numele:");
        lastName = in.nextLine();

        Utilizator user = new Utilizator(firstName,lastName);
        user.setId(id);
        service.addUtilizator(user);
        System.out.println("Utilizator adaugat!");
    }

    private void removeUtilizator(){
        Long id;

        System.out.println("Introduceti Id-ul:");
        id = in.nextLong();

        Utilizator user = new Utilizator("","");
        user.setId(id);

        service.deleteUtilizator(user);
        System.out.println("Utilizator sters!");
    }

    private void updateUtilizator(){
        Long id;
        String firstName;
        String lastName;

        System.out.println("Introduceti Id-ul:");
        id = in.nextLong();
        String col = in.nextLine();
        System.out.println("Introduceti Prenumele nou:");
        firstName = in.nextLine();
        System.out.println("Introduceti Numele nou:");
        lastName = in.nextLine();

        Utilizator user = new Utilizator(firstName,lastName);
        user.setId(id);
        service.updateUtilizator(user);
        System.out.println("Utilizator actualizat!");
    }

    private void addPrietenie(){
        Long id1;
        Long id2;

        System.out.println("Introduceti Id-ul primului utilizator:");
        id1 = in.nextLong();
        System.out.println("Introduceti Id-ul celuilalt utilizator:");
        id2 = in.nextLong();

        service.addPrietenie(id1,id2);
        System.out.println("Prietenie adaugata!");
    }

    private void removePrietenie(){
        Long id1;
        Long id2;

        System.out.println("Introduceti Id-ul primului utilizator:");
        id1 = in.nextLong();
        System.out.println("Introduceti Id-ul celuilalt utilizator:");
        id2 = in.nextLong();

        service.deletePrietenie(id1,id2);
        System.out.println("Prietenie stearsa!");
    }

    private void afisareNrComunitati(){
        System.out.println("Numarul de comunitati este "+service.NrComunitati());
    }

    private void afisareCeaMaiSociabilaComunitate(){
        System.out.println("Cea mai sociabila comunitate este: ");
        List<Utilizator> l = service.CeaMaiSociabilaComunitate();
        for(Utilizator u : l)
            System.out.println(u);
    }

    private void afisarePrieteniiUtilizator(){
        Long id;

        System.out.println("Introduceti Id-ul:");
        id = in.nextLong();

        service.getUserFriends(id).stream()
            .map(x->x.getLastName()+"|"+x.getFirstName()+"|"+service.findOnePrietenie(x.getId(),id).getDate().toLocalDate())
            .forEach(System.out::println);
    }

    private void afisarePrieteniiUtilizatorLuna(){
        Long id;
        int luna;
        int an;

        System.out.println("Introduceti Id-ul:");
        id = in.nextLong();

        System.out.println("Introduceti luna:");
        luna = in.nextInt();
        if(luna<1 || luna>12) {
            System.out.println("Luna poate fi de la 1 la 12");
            return;
        }
        System.out.println("Introduceti anul:");
        an = in.nextInt();
        if(an> LocalDateTime.now().getYear()){
            System.out.println("Anul poate fi maxim anul curent!");
            return;
        }

        Predicate<Utilizator> pred = x->service.findOnePrietenie(x.getId(),id).getDate().getMonthValue()==luna && service.findOnePrietenie(x.getId(),id).getDate().getYear()==an;
        service.getUserFriends(id).stream()
                .filter(pred)
                .map(x->x.getLastName()+"|"+x.getFirstName()+"|"+service.findOnePrietenie(x.getId(),id).getDate().toLocalDate())
                .forEach(System.out::println);
    }

    private void trimitereMesaj(){
        Long messageId;
        Long idExepditor;
        int nrDestinatari;

        System.out.println("Introduceti Id-ul mesajului:");
        messageId = in.nextLong();

        System.out.println("Introduceti Id-ul expeditorului:");
        idExepditor= in.nextLong();

        System.out.println("Introduceti numarul de destinatari:");
        nrDestinatari = in.nextInt();
        List<Long> dest = new ArrayList<Long>();
        for(int i=0;i<nrDestinatari;i++) {
            System.out.println("Introduceti id-ul destinatarului "+(i+1)+":");
            Long id = in.nextLong();
            dest.add(id);
        }

        String col = in.nextLine();
        System.out.println("Introduceti textul mesajului:");
        String text = in.nextLine();

        service.addMessage(messageId,idExepditor,dest,text);
        System.out.println("Mesaj trimis!");
    }

    private void raspundereLaMesaj(){
        Long messageId;
        Long replyId;
        Long idExepditor;

        System.out.println("Introduceti id-ul mesajului la care vreti sa raspundeti:");
        messageId = in.nextLong();

        System.out.println("Introduceti id-ul utilizatorului care va raspunde:");
        idExepditor= in.nextLong();

        System.out.println("Introduceti id-ul mesajului raspuns:");
        replyId = in.nextLong();

        String col = in.nextLine();
        System.out.println("Introduceti textul mesajului:");
        String text = in.nextLine();

        service.replyToMesage(messageId,replyId,idExepditor,text);
        System.out.println("Raspuns trimis!");
    }

    private void stergereMesaj(){
        Long id;

        System.out.println("Introduceti Id-ul:");
        id = in.nextLong();

        service.deleteMessage(id);
        System.out.println("Mesaj sters!");
    }

    private void afisareConversatie(){
        Long id1;
        Long id2;

        System.out.println("Introduceti Id-ul primului utilizator:");
        id1 = in.nextLong();
        System.out.println("Introduceti Id-ul celuilalt utilizator:");
        id2 = in.nextLong();

        List<Message> conv = service.conversatie(id1,id2);

        conv.forEach(x->{
            System.out.println(x.getData()+" | "+x.getFrom().getFirstName()+" "+x.getFrom().getLastName()+": "+x.getMessage());
        });
    }

    private void logare(){
        Long id;
        System.out.println("Introduceti Id-ul de utilizator:");
        id = in.nextLong();
        if(service.findOneUtilizator(id).equals(null))
            throw new NonExistentObjectException(linieUtilizatorLogat+"Utilizator inexistent!");
        this.utilizatorStartUi(id);
        System.out.println(linieUtilizatorLogat+"V-ati delogat!");
    }

    private void afisareCereriDePrietenie(Long id){
        if(service.cereriDePrieteniePrimite(id).isEmpty())
            throw new NonExistentObjectException("Nu exista cereri de prietenie!");
        service.cereriDePrieteniePrimite(id).forEach(x->{
            System.out.println(
                    linieUtilizatorLogat + "Id: " + x.getId() + "; "
                    + "De la: " + service.findOneUtilizator(x.getIdExpeditor()).getFirstName()
                    + " " + service.findOneUtilizator(x.getIdExpeditor()).getLastName()
                    +"; Data: " + x.getData());
        });
    }

    private void trimitereCerereDePrietenie(Long id){
        Long idCerere;
        Long idDestinatar;
        System.out.println(linieUtilizatorLogat+"Introduceti Id-ul cererii:");
        idCerere = in.nextLong();
        System.out.println(linieUtilizatorLogat+"Introduceti Id-ul destinatarului:");
        idDestinatar = in.nextLong();
        service.addCerereDePrietenie(idCerere,id,idDestinatar);
        System.out.println(linieUtilizatorLogat+"Cerere trimisa!");
    }

    private void raspundeLaCerereDePrietenie(Long id){
        System.out.println(linieUtilizatorLogat+"Introduceti Id-ul cererii de prietenie:");
        Long idCerere = in.nextLong();
        System.out.println(linieUtilizatorLogat+"Introduceti raspunsul: true pentru acceptare, false pentru respingere");
        boolean raspuns = in.nextBoolean();
        service.raspundeLaCerereDePrietenie(id,idCerere,raspuns);
        if(raspuns)
            System.out.println(linieUtilizatorLogat+"Cerere acceptata!");
        else
            System.out.println(linieUtilizatorLogat+"Cerere respinsa!");
    }

    private void afisareMeniuUtilizator(){
        System.out.println(linieUtilizatorLogat+"Meniu:\n" +
                " 1 - afisare meniu\n" +
                " 2 - vizualizare cereri de prietenie in asteptare\n" +
                " 3 - trimitere cerere de prietenie\n" +
                " 4 - raspundere la cerere de prietenie\n" +
                " 0 - delogare\n");
    }

    private void utilizatorStartUi(Long id){
        Integer command;
        Utilizator user = service.findOneUtilizator(id);
        if(user==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        System.out.println("V-ati logat ca " + user.getFirstName() +" "+ user.getLastName()+"!");
        linieUtilizatorLogat = user.getFirstName()+" "+user.getLastName()+"~$: ";
        this.afisareMeniuUtilizator();
        while(true) {
            System.out.println(linieUtilizatorLogat +"Introduceti comanda:");
            command = in.nextInt();
            try {
                switch (command) {
                    case 1:
                        this.afisareMeniuUtilizator();
                        break;
                    case 2:
                        this.afisareCereriDePrietenie(id);
                        break;
                    case 3:
                        this.trimitereCerereDePrietenie(id);
                        break;
                    case 4:
                        this.raspundeLaCerereDePrietenie(id);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println(linieUtilizatorLogat +"Comanda invalida!");
                        break;
                }
            }
            catch (RuntimeException e){
                System.out.println(linieUtilizatorLogat+e.getMessage());
            }
        }
    }



    private void afisareMeniu(){
        System.out.println("Meniu:\n" +
                " 1 - afisare meniu\n" +
                " 2 - adaugare utilizator\n" +
                " 3 - stergere utilizator\n" +
                " 4 - adaugare prietenie\n" +
                " 5 - stergere prietenie\n" +
                " 6 - afisare numar comunitati\n" +
                " 7 - afisare cea mai sociabila comunitate\n" +
                " 8 - afisare relatii utilizator\n" +
                " 9 - afisare relatii utlizator intr-o luna data\n" +
                " 10 - trimitere mesaj\n" +
                " 11 - raspundere la mesaj\n" +
                " 12 - stergere mesaj\n" +
                " 13 - afisare conversatie intre doi utilizatori\n" +
                " 14 - logare\n" +
                " 0 - iesire\n");
    }

    public void startUi(){
        this.afisareMeniu();
        Integer command;
        while(true) {
            System.out.println("Introduceti comanda:");
            command = in.nextInt();
            try {
                switch (command) {
                    case 1:
                        this.afisareMeniu();
                        break;
                    case 2:
                        this.addUtilizator();
                        break;
                    case 3:
                        this.removeUtilizator();
                        break;
                    case 4:
                        this.addPrietenie();
                        break;
                    case 5:
                        this.removePrietenie();
                        break;
                    case 6:
                        this.afisareNrComunitati();
                        break;
                    case 7:
                        this.afisareCeaMaiSociabilaComunitate();
                        break;
                    case 8:
                        this.afisarePrieteniiUtilizator();
                        break;
                    case 9:
                        this.afisarePrieteniiUtilizatorLuna();
                        break;
                    case 10:
                        this.trimitereMesaj();
                        break;
                    case 11:
                        this.raspundereLaMesaj();
                        break;
                    case 12:
                        this.stergereMesaj();
                        break;
                    case 13:
                        this.afisareConversatie();
                        break;
                    case 14:
                        this.logare();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Comanda invalida!");
                        break;
                    }
                }
                catch (RuntimeException e){
                    System.out.println(e.getMessage());
                }
        }
    }

}
