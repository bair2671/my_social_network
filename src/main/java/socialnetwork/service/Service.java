package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.ExistentObjectException;
import socialnetwork.domain.validators.NonExistentObjectException;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.EvenimentDb;
import socialnetwork.repository.database.UtilizatorDb;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Service {
    private Repository<Long, Utilizator> utilizatorRepo;
    private Repository<Pereche<Long,Long>, Prietenie> prientenieRepo;
    private Repository<Long, Message> messageRepo;
    private Repository<Long, CerereDePrietenie> cereriDePrietenieRepo;
    private EvenimentDb evenimentRepo;
    private Repository<Tuple<Long,Long>, UtilizatorEveniment> utilizatorEvenimentRepo;
    private List<List<Long>> conexe;

    public Service(Repository<Long, Utilizator> repoU, Repository<Pereche<Long,Long>,Prietenie> repoP, Repository<Long, Message> repoM, Repository<Long, CerereDePrietenie> repoCP) {
        this.utilizatorRepo = repoU;
        this.prientenieRepo = repoP;
        //this.conectarePrieteni();  //Se foloseste doar la fisiere
        this.messageRepo = repoM;
        //this.loadMessages();       //Se foloseste doar la fisiere
        this.cereriDePrietenieRepo = repoCP;
        this.conexe=new ArrayList<List<Long>>();
    }

    public Service(Repository<Long, Utilizator> repoU, Repository<Pereche<Long,Long>,Prietenie> repoP, Repository<Long, Message> repoM, Repository<Long, CerereDePrietenie> repoCP, EvenimentDb repoE, Repository<Tuple<Long,Long>, UtilizatorEveniment> repoUE) {
        this.utilizatorRepo = repoU;
        this.prientenieRepo = repoP;
        //this.conectarePrieteni();  //Se foloseste doar la fisiere
        this.messageRepo = repoM;
        //this.loadMessages();       //Se foloseste doar la fisiere
        this.cereriDePrietenieRepo = repoCP;
        this.evenimentRepo = repoE;
        this.utilizatorEvenimentRepo = repoUE;
        this.conexe=new ArrayList<List<Long>>();
    }

    /**
     * incarca corect mesajele in Service
     */
    private void loadMessages(){
        for(Message msg : this.messageRepo.findAll()){
            msg.setFrom(utilizatorRepo.findOne(msg.getFrom().getId()));
            for(Utilizator u : msg.getTo()) {
                u.setFirstName(utilizatorRepo.findOne(u.getId()).getFirstName());
                u.setLastName(utilizatorRepo.findOne(u.getId()).getLastName());
                u.setFriends(utilizatorRepo.findOne(u.getId()).getFriends());
            }
        }
        for(Message msg : this.messageRepo.findAll()){
            if(msg.getReply()!=null)
                msg.setReply(messageRepo.findOne(msg.getReply().getId()));
        }
    }

    /**
     * adauga prieteni in listele utilizatorilor conform prieteniilor
     */
    private void conectarePrieteni(){
        for(Prietenie p : this.prientenieRepo.findAll()){
            Utilizator u1 = this.utilizatorRepo.findOne(p.getId().getLeft());
            Utilizator u2 = this.utilizatorRepo.findOne(p.getId().getRight());
            u1.addFriend(u2);
            u2.addFriend(u1);
            this.utilizatorRepo.update(u1);
            this.utilizatorRepo.update(u2);
        }
    }

    /**
     * cauta un utilizator dupa id
     * @param id
     * @return un Utilizator
     */
    public Utilizator findOneUtilizator(Long id){
        return utilizatorRepo.findOne(id);
    }

    public Utilizator findUtilizatorByUsername(String username){
        return ((UtilizatorDb)utilizatorRepo).findOneByUsername(username);
    }

    public List<Utilizator> getUserFriends(Long id){
        List<Utilizator> friends=new ArrayList<Utilizator>();
        prientenieRepo.findAll().forEach(x->{
            if(x.getId().getLeft().equals(id))
                friends.add(utilizatorRepo.findOne(x.getId().getRight()));
            if(x.getId().getRight().equals(id))
                friends.add(utilizatorRepo.findOne(x.getId().getLeft()));
        });
        return friends;
    }

    public List<Utilizator> getPageUserFriends(Long id,int offset,int limit){
        List<Utilizator> friends=new ArrayList<Utilizator>();
        //TODO
        return friends;
    }

    /**
     *  adauga un Utilizator
     *  @param messageTask
     *  @throws ValidationException
     *              daca un messageTask nu e valid.
     * @throws IllegalArgumentException
     *              daca enttity este null.
     * @throws ExistentObjectException
     *              daca entitatea deja exista.
     */
    public Utilizator addUtilizator(Utilizator messageTask) {
        //if(findUtilizatorByUsername(messageTask.getUsername())!=null)
            //throw new ExistentObjectException("Exista deja un utilizator cu acest username!");
        Utilizator task = utilizatorRepo.save(messageTask);
        return task;
    }

    /**
     * returneaza lista cu toti utilizatorii
     * @return un  Iterable<Utilizator>
     */
    public Iterable<Utilizator> getAllUsers(){
        return utilizatorRepo.findAll();
    }

    public Iterable<Utilizator> getPageUsers(int offset,int limit){
        return utilizatorRepo.findPage(offset,limit);
    }

    ///TODO: add other methods

    /**
     *  sterge un Utilizator
     *  @param messageTask
     *  #return un Utilizator
     *  @throws ValidationException
     *              daca un messageTask nu e valid.
     * @throws IllegalArgumentException
     *             daca enttity este null.
     * @throws NonExistentObjectException
     *              daca entitatea nu exista.
     */
    public Utilizator deleteUtilizator(Utilizator messageTask) {
        Utilizator user = utilizatorRepo.findOne(messageTask.getId());
        utilizatorRepo.delete(messageTask.getId());
        for(Prietenie p : prientenieRepo.findAll())
            if(p.getId().getLeft().equals(messageTask.getId()) || p.getId().getRight().equals(messageTask.getId()))
                prientenieRepo.delete(p.getId());
        for(Message m : messageRepo.findAll())
            if(m.getFrom().getId().equals(messageTask.getId()) || m.getTo().contains(user))
                messageRepo.delete(m.getId());
        for(CerereDePrietenie cp : cereriDePrietenieRepo.findAll())
            if(cp.getIdDestinatar().equals(messageTask.getId()) || cp.getIdExpeditor().equals(messageTask.getId()))
                cereriDePrietenieRepo.delete(cp.getId());
        return messageTask;
    }

    /**
     *  actualizeaza un Utilizator
     *  @param messageTask
     *  @throws ValidationException
     *              daca un messageTask nu e valid.
     * @throws IllegalArgumentException
     *             daca enttity este null.
     * @throws NonExistentObjectException
     *              daca entitatea nu exista.
     */
    public Utilizator updateUtilizator(Utilizator messageTask) {
        Utilizator task = utilizatorRepo.update(messageTask);
        return task;
    }

    /**
     *  adauga o prietenie
     *  @param id1,id2
     * @throws NonExistentObjectException
     *              daca nu exista utilizatori cu id-urile date.
     * @throws ExistentObjectException
     *               daca prietenia deja exista.
     */
    public void addPrietenie(Long id1, Long id2){
        if(utilizatorRepo.findOne(id1)==null || utilizatorRepo.findOne(id2)==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        Prietenie p = new Prietenie(LocalDateTime.now());
        p.setId(new Pereche<Long,Long>(id1,id2));
        prientenieRepo.save(p);
        Utilizator u1 = utilizatorRepo.findOne(id1);
        Utilizator u2 = utilizatorRepo.findOne(id2);
        u1.addFriend(u2);
        u2.addFriend(u1);
        utilizatorRepo.update(u1);
        utilizatorRepo.update(u2);
    }

    /**
     *  sterge o prietenie
     *  @param id1
     *  @param id2
     * @throws NonExistentObjectException
     *              daca nu exista utilizatori cu id-urile date sau daca prietenia nu exista
     */
    public void deletePrietenie(Long id1, Long id2){
        if(utilizatorRepo.findOne(id1)==null || utilizatorRepo.findOne(id2)==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        Prietenie p = new Prietenie(LocalDateTime.now());
        p.setId(new Pereche<Long,Long>(id1,id2));
        prientenieRepo.delete(p.getId());
        Utilizator u1 = utilizatorRepo.findOne(id1);
        Utilizator u2 = utilizatorRepo.findOne(id2);
        u1.unFriend(u2);
        u2.unFriend(u1);
        utilizatorRepo.update(u1);
        utilizatorRepo.update(u2);
    }

    /**
     * returneaza lista cu toate prieteniile
     * @return un  Iterable<Prietenie>
     */
    public Iterable<Prietenie> getAllPrietenii(){
        return prientenieRepo.findAll();
    }

    public Iterable<Prietenie> getPagePrietenii(int offset,int limit){
        return prientenieRepo.findPage(offset,limit);
    }

    /**
     * cauta o prietenie dupa id-urile celor doi utilizatori
     * @param id1
     * @param id2
     * @return o Prietenie
     */
    public Prietenie findOnePrietenie(Long id1,Long id2){
        return prientenieRepo.findOne(new Pereche<Long,Long>(id1,id2));
    }

    /**
     * cauta un Message dupa id
     * @param id
     * @return un Message
     */
    public Message findOneMessage(Long id){
        Message m = messageRepo.findOne(id);
        if(m==null)
            throw new NonExistentObjectException("Mesaj inexistent!");
        return m;
    }

    /**
     * adauga un Message
     * @param messageId
     * @param idExpeditor
     * @param destId
     * @param text
     * @throws ValidationException
     *              daca message creat cu parametrii dati nu e valid
     * @throws IllegalArgumentException
     *              daca enttity este null.
     * @throws ExistentObjectException
     *              daca entitatea deja exista.
     */
    public Message addMessage(Long messageId,Long idExpeditor,List<Long> destId,String text) {
        Utilizator expeditor = utilizatorRepo.findOne(idExpeditor);
        if(expeditor==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        List<Utilizator> dest = new ArrayList<Utilizator>();
        destId.forEach(x->{
            Utilizator y = utilizatorRepo.findOne(x);
            if(y==null)
                throw new NonExistentObjectException("Utilizator inexistent!");
            dest.add(y);
        });
        Message msg = new Message(utilizatorRepo.findOne(idExpeditor),dest,text,LocalDateTime.now());
        msg.setId(messageId);
        Message task = messageRepo.save(msg);
        return task;
    }

    public void replyToMesage(Long messageId,Long replyId,Long idExpeditor,String text){
        Message msg = messageRepo.findOne(messageId);
        if(msg==null)
            throw new NonExistentObjectException("Mesaj inexistent!");
        Utilizator expeditor = utilizatorRepo.findOne(idExpeditor);
        if(expeditor==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        if(!msg.getTo().contains(expeditor))
            throw new NonExistentObjectException("Utilizatorul dat nu se afla printre destinatarii mesajului la care doriti sa raspundeti!");
        Message reply = new Message(expeditor,Arrays.asList(msg.getFrom()),text,LocalDateTime.now());
        reply.setReply(msg);
        reply.setId(replyId);
        messageRepo.save(reply);
    }

    /**
     * sterge un Message
     * @param id
     * @return un Message
     * @throws NonExistentObjectException
     *              daca entitatea nu exista.
     */
    public Message deleteMessage(Long id) {
        Message task = messageRepo.delete(id);
        messageRepo.findAll().forEach(x->{
            if(x.getReply()!=null)
                if(x.getReply().getId()==id) {
                    x.setReply(null);
                    //messageRepo.update(x);
                }
        });
        messageRepo.findAll().forEach(x->{messageRepo.update(x);});
        return task;
    }

    public Iterable<Message> getPageMessages(int offset,int limit){
        return messageRepo.findPage(offset,limit);
    }

    public List<MessageForTable> mesajePrimiteForTable(Long id){
        Utilizator user =utilizatorRepo.findOne(id);
        if(user==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        List<MessageForTable> messages= new ArrayList<MessageForTable>();
        for(Message message : messageRepo.findAll()) {
            if (message.getTo().contains(user))
                messages.add(new MessageForTable(message.getId(),
                        message.getFrom().getFirstName() + " " + message.getFrom().getLastName(),
                        user.getFirstName() + " " + user.getLastName(),
                        message.getMessage(), message.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
        }
        messages.sort(new Comparator<MessageForTable>() {
            @Override
            public int compare(MessageForTable o1, MessageForTable o2) {
                return (int)ChronoUnit.SECONDS.between(DateConverter.fromStringRomanianToLocalDateTime(o1.getData()),
                        DateConverter.fromStringRomanianToLocalDateTime(o2.getData()));
            }
        });
        return messages;
    }

    public List<MessageForTable> mesajePrimiteDeLaPrietenPerioadaForTable(Long id,Long deLa,LocalDateTime start,LocalDateTime end){
        if(ChronoUnit.SECONDS.between(start,end)<0)
            throw new RuntimeException("Data de sfarsit nu poate sa fie dupa data de inceput!");
        Utilizator user =utilizatorRepo.findOne(id);
        if(user==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        List<MessageForTable> messages= new ArrayList<MessageForTable>();
        for(Message message : messageRepo.findAll()) {
            if (message.getTo().contains(user) && message.getFrom().getId().equals(deLa) && ChronoUnit.SECONDS.between(message.getData(),start)<0 && ChronoUnit.SECONDS.between(message.getData(),end)>0)
                messages.add(new MessageForTable(message.getId(),
                        message.getFrom().getFirstName() + " " + message.getFrom().getLastName(),
                        user.getFirstName() + " " + user.getLastName(),
                        message.getMessage(), message.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
        }
        messages.sort(new Comparator<MessageForTable>() {
            @Override
            public int compare(MessageForTable o1, MessageForTable o2) {
                return DateConverter.fromStringRomanianToLocalDateTime(o1.getData()).compareTo(DateConverter.fromStringRomanianToLocalDateTime(o2.getData()));
            }
        });
        return messages;
    }

    public List<MessageForTable> mesajeTrimiseForTable(Long id){
        Utilizator user =utilizatorRepo.findOne(id);
        if(user==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        List<MessageForTable> messages= new ArrayList<MessageForTable>();
        for(Message message : messageRepo.findAll())
            if(message.getFrom().getId()==id)
                for(Utilizator dest:message.getTo()) {
                    messages.add(new MessageForTable(message.getId(),
                            message.getFrom().getFirstName() + " " + message.getFrom().getLastName(),
                            dest.getFirstName() + " " + dest.getLastName(),
                            message.getMessage(), message.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
                }
        messages.sort(new Comparator<MessageForTable>() {
            @Override
            public int compare(MessageForTable o1, MessageForTable o2) {
                return (int)ChronoUnit.SECONDS.between(DateConverter.fromStringRomanianToLocalDateTime(o1.getData()),
                        DateConverter.fromStringRomanianToLocalDateTime(o2.getData()));
            }
        });
        return messages;
    }



    public List<Message> conversatie(Long id1,Long id2){
        Utilizator u1 = utilizatorRepo.findOne(id1);
        Utilizator u2 = utilizatorRepo.findOne(id2);
        if(u1.equals(null) || u2.equals(null))
            throw new NonExistentObjectException("Utilizator inexistent!");
        List<Message> conv = new ArrayList<Message>();
        for(Message msg : messageRepo.findAll())
            if((msg.getFrom().equals(u1) && msg.getTo().contains(u2)) || (msg.getFrom().equals(u2) && msg.getTo().contains(u1)))
                conv.add(msg);
        conv.sort(new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getData().compareTo(o2.getData());
            }
        });
        return conv;
    }

    /**
     *  adauga o prietenie si construieste componenetele conexe ale retelei
     *  @return numarul de comunitati
     */
    public int NrComunitati(){
        List<Long> parcurse=new ArrayList<Long>();
        HashMap<Long, Boolean> colorare = new HashMap<Long,Boolean>();
        utilizatorRepo.findAll().forEach(u->{
            colorare.put(u.getId(),false);
        });
        int contor = 0;
        while(colorare.containsValue(false)){
            contor++;
            Long key = -1L;
            for(Map.Entry<Long, Boolean> u: colorare.entrySet())
                if(u.getValue().equals(false)){
                    key=u.getKey();
                    u.setValue(true);
                    break;
                }
            this.bfs(colorare,key);

            List<Long> membri = new ArrayList<Long>();
            for(Map.Entry<Long, Boolean> u: colorare.entrySet()){
                if(u.getValue().equals(true) && !parcurse.contains(u.getKey())){
                    membri.add(u.getKey());
                    parcurse.add(u.getKey());
                };
            }
            conexe.add(membri);
        }
        return contor;
    }

    /**
     *  parcurge prietenii utilizatorului cu id key, fiind folosita la numararea componentelor conexe
     *  @param colorare
     *  @param key
     */
    private void bfs(HashMap<Long,Boolean> colorare,Long key){
        Queue<Long> queue = new LinkedList<Long>();
        queue.add(key);
        while(!queue.isEmpty()) {
            Long u = queue.remove();
            Utilizator user = utilizatorRepo.findOne(u);
            for (Utilizator v : getUserFriends(user.getId()))
                if(colorare.get(v.getId()).equals(false)){
                    colorare.put(v.getId(),true);
                    queue.add(v.getId());
                }
        }
    }

    /**
     * adauga o prietenie
     * @param colorare
     * @param key
     * @param maxD
     * @return un Tuple cu lungimea celui mai lung drum de la utilizatorul cu id key si cu id-ul utilizatorului pana la care e drumul
     */
    private Pereche<Integer,Long> bfs_CelMaiLungDrum(HashMap<Long,Boolean> colorare, Long key, Integer maxD) {
        HashMap<Long,Integer> dist = new HashMap<Long,Integer>();
        HashMap<Long,Long> parinti = new HashMap<Long,Long>();
        Long dest=null;
        Integer maxDist=maxD;
        for (Map.Entry<Long, Boolean> u : colorare.entrySet()) {
            if(!u.getKey().equals(key)) {
                u.setValue(false);
                dist.put(u.getKey(),null);
                parinti.put(u.getKey(),null);
            }
        }
        colorare.put(key,true);
        dist.put(key,0);
        parinti.put(key,null);
        Queue<Long> queue = new LinkedList<Long>();
        queue.add(key);
        while (!queue.isEmpty()) {
            Long u = queue.remove();
            Utilizator user = utilizatorRepo.findOne(u);
            for (Utilizator v : getUserFriends(user.getId()))
                if (colorare.get(v.getId()).equals(false)) {
                    //colorare.put(v.getId(), true);
                    dist.put(v.getId(),dist.get(u)+1);
                    if(dist.get(v.getId())>maxDist){
                        maxDist=dist.get(v.getId());
                        dest = v.getId();
                    }
                    parinti.put(v.getId(),u);
                    queue.add(v.getId());
                }
            colorare.put(u,true);
        }
        return new Pereche<Integer,Long>(maxDist,dest);
    }

    /**
     * @return o lista cu Utilizatorii din cea mai sociabila comunitate
     */
    public ArrayList<Utilizator> CeaMaiSociabilaComunitate(){
        ArrayList<Utilizator> com = new ArrayList<Utilizator>();
        Integer maxDist = 0;
        Long source=null;
        Long dest=null;
        HashMap<Long, Boolean> colorare = new HashMap<Long,Boolean>();
        utilizatorRepo.findAll().forEach(u->{
            colorare.put(u.getId(),false);
        });
        for(Utilizator u : utilizatorRepo.findAll()) {
            Pereche<Integer, Long> t = bfs_CelMaiLungDrum(colorare, u.getId(), maxDist);
            if(t.getLeft()>maxDist){
                maxDist=t.getLeft();
                source=u.getId();
                dest=t.getRight();
            }
        }
        List<Long> list = new ArrayList<Long>();
        this.NrComunitati();
        for(List<Long> l:conexe)
            if(l.contains(source) && l.contains(dest)) {
                list.addAll(l);
                break;
            }
        for(long id:list)
            com.add(utilizatorRepo.findOne(id));
        return com;
    }

    public CerereDePrietenie findOneCerereDePrietenie(Long id){
        return cereriDePrietenieRepo.findOne(id);
    }

    public List<CerereDePrietenie> cereriDePrieteniePrimite(Long id){
        if(utilizatorRepo.findOne(id)==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        List<CerereDePrietenie> cereri = new ArrayList<CerereDePrietenie>();
        for(CerereDePrietenie cerere : cereriDePrietenieRepo.findAll())
            if(cerere.getIdDestinatar().equals(id))
                cereri.add(cerere);
        return cereri;
    }

    public List<CerereDePrietenie> cereriDePrietenieTrimise(Long id){
        if(utilizatorRepo.findOne(id)==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        List<CerereDePrietenie> cereri = new ArrayList<CerereDePrietenie>();
        for(CerereDePrietenie cerere : cereriDePrietenieRepo.findAll())
            if(cerere.getIdExpeditor().equals(id))
                cereri.add(cerere);
        return cereri;
    }

    public void addCerereDePrietenie(Long idCerere,Long idExpeditor,Long idDestinatar){
        if(utilizatorRepo.findOne(idExpeditor)==null || utilizatorRepo.findOne(idExpeditor)==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        cereriDePrietenieRepo.findAll().forEach(x->{
            if(x.getIdExpeditor().equals(idExpeditor) && x.getIdDestinatar().equals(idDestinatar) && x.getStatus()==CerereDePrietenieStatus.PENDING)
                throw new ExistentObjectException("Deja exista o cerere in asteptare catre acest utilizator!");
            if(x.getIdExpeditor().equals(idDestinatar) && x.getIdDestinatar().equals(idExpeditor) && x.getStatus()==CerereDePrietenieStatus.PENDING)
                throw new ExistentObjectException("Deja exista o cerere in asteptare primita de la acest utilizator!");
        });
        prientenieRepo.findAll().forEach(x->{
            if(x.getId().equals(new Pereche<Long, Long>(idExpeditor, idDestinatar)))
                throw new ExistentObjectException("Deja sunteti prieteni!");
        });
        CerereDePrietenie cerere = new CerereDePrietenie(idExpeditor,idDestinatar,CerereDePrietenieStatus.PENDING,LocalDateTime.now());
        cerere.setId(idCerere);
        cereriDePrietenieRepo.save(cerere);
    }

    public void raspundeLaCerereDePrietenie(Long idUtilizator,Long idCerere,boolean raspuns){
        if(utilizatorRepo.findOne(idUtilizator)==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        CerereDePrietenie cerere = cereriDePrietenieRepo.findOne(idCerere);
        if(!cereriDePrieteniePrimite(idUtilizator).contains(cerere))
            throw new NonExistentObjectException("Cerere inexistenta!");
        if(cerere.getStatus()==CerereDePrietenieStatus.PENDING) {
            if (raspuns) {
                cerere.setStatus(CerereDePrietenieStatus.APPROVED);
                addPrietenie(cerere.getIdExpeditor(), cerere.getIdDestinatar());
            } else
                cerere.setStatus(CerereDePrietenieStatus.REJECTED);
        }
        cereriDePrietenieRepo.update(cerere);
    }

    public void deleteCerereDePrietenie(Long id) {
        CerereDePrietenie task = cereriDePrietenieRepo.delete(id);
    }

    public List<CerereDePrietenieForTable> cereriDePrietenieForTable(Long id){
        List<CerereDePrietenieForTable> cereri = new ArrayList<CerereDePrietenieForTable>();
        cereriDePrietenieTrimise(id).forEach(x->{
            String expeditor = utilizatorRepo.findOne(x.getIdExpeditor()).getFirstName()+" "+utilizatorRepo.findOne(x.getIdExpeditor()).getLastName();
            String destinatar = utilizatorRepo.findOne(x.getIdDestinatar()).getFirstName()+" "+utilizatorRepo.findOne(x.getIdDestinatar()).getLastName();
            CerereDePrietenieForTable cerere = new CerereDePrietenieForTable(x.getId(),expeditor,destinatar,x.getStatus(),x.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            cereri.add(cerere);
        });
        cereriDePrieteniePrimite(id).forEach(x->{
            String expeditor = utilizatorRepo.findOne(x.getIdExpeditor()).getFirstName()+" "+utilizatorRepo.findOne(x.getIdExpeditor()).getLastName();
            String destinatar = utilizatorRepo.findOne(x.getIdDestinatar()).getFirstName()+" "+utilizatorRepo.findOne(x.getIdDestinatar()).getLastName();
            CerereDePrietenieForTable cerere = new CerereDePrietenieForTable(x.getId(),expeditor,destinatar,x.getStatus(),x.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            cereri.add(cerere);
        });
        cereri.sort(new Comparator<CerereDePrietenieForTable>() {
            @Override
            public int compare(CerereDePrietenieForTable o1, CerereDePrietenieForTable o2) {
                return (int)ChronoUnit.SECONDS.between(DateConverter.fromStringRomanianToLocalDateTime(o1.getData()), DateConverter.fromStringRomanianToLocalDateTime(o2.getData()));
            }
        });
        return cereri;
    }

    public List<Activitate> activitatiPerioada(Long id,LocalDateTime start,LocalDateTime end){
        if(ChronoUnit.SECONDS.between(start,end)<0)
            throw new RuntimeException("Data de sfarsit nu poate sa fie dupa data de inceput!");
        List<Activitate> activitati = new ArrayList<Activitate>();
        prientenieRepo.findAll().forEach(x->{
            if(ChronoUnit.SECONDS.between(x.getDate(),start)<=0 && ChronoUnit.SECONDS.between(x.getDate(),end)>=0) {
                if(x.getId().getLeft()==id) {
                    Utilizator user = utilizatorRepo.findOne(x.getId().getRight());
                    activitati.add(new Activitate(ActivitateTip.Prietenie, user.getFirstName()+" "+user.getLastName(), x.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
                }
                else{
                    Utilizator user = utilizatorRepo.findOne(x.getId().getLeft());
                    activitati.add(new Activitate(ActivitateTip.Prietenie, user.getFirstName()+" "+user.getLastName(), x.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
                }
            }
        });
        mesajePrimiteForTable(id).forEach(x->{
            if(ChronoUnit.SECONDS.between(DateConverter.fromStringRomanianToLocalDateTime(x.getData()),start)<0 && ChronoUnit.SECONDS.between(DateConverter.fromStringRomanianToLocalDateTime(x.getData()),end)>0) {
                activitati.add(new Activitate(ActivitateTip.Mesaj, x.getExpeditor(), x.getData()));
            }
        });
        activitati.sort(new Comparator<Activitate>() {
            @Override
            public int compare(Activitate o1, Activitate o2) {
                return (int)ChronoUnit.SECONDS.between(DateConverter.fromStringRomanianToLocalDateTime(o2.getData()), DateConverter.fromStringRomanianToLocalDateTime(o1.getData()));
            }
        });
        return activitati;
    }

    public List<Eveniment> evenimenteUtilizator(Long idUser){
        return evenimentRepo.evenimenteUtilizator(idUser);
    }

    public List<Utilizator> participantiEveniment(Long idEvent){
        return evenimentRepo.participantiEveniment(idEvent);
    }

    public void adaugaEveniment(String nume,LocalDateTime data){
        Eveniment eveniment = new Eveniment(nume,data);
        eveniment.setId(0L);
        evenimentRepo.save(eveniment);
    }

    public void deleteEveniment(Long idEvent){
        evenimentRepo.delete(idEvent);
    }

    public void inscriereLaEveniment(Long idUser,Long idEvent){
        if(utilizatorRepo.findOne(idUser)==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        if(evenimentRepo.findOne(idEvent)==null)
            throw new NonExistentObjectException("Eveniment inexistent!");
        UtilizatorEveniment utilizatorEveniment = new UtilizatorEveniment(true);
        utilizatorEveniment.setId(new Tuple<>(idUser,idEvent));
        utilizatorEvenimentRepo.save(utilizatorEveniment);
    }

    public void dezabonareDeLaEveniment(Long idUser,Long idEvent){
        if(utilizatorRepo.findOne(idUser)==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        if(evenimentRepo.findOne(idEvent)==null)
            throw new NonExistentObjectException("Eveniment inexistent!");
        UtilizatorEveniment utilizatorEveniment = utilizatorEvenimentRepo.findOne(new Tuple<>(idUser,idEvent));
        utilizatorEvenimentRepo.delete(utilizatorEveniment.getId());
    }

    public void setareNotificari(Long idUser,Long idEvent,boolean notificari){
        if(utilizatorRepo.findOne(idUser)==null)
            throw new NonExistentObjectException("Utilizator inexistent!");
        if(evenimentRepo.findOne(idEvent)==null)
            throw new NonExistentObjectException("Eveniment inexistent!");
        UtilizatorEveniment utilizatorEveniment = utilizatorEvenimentRepo.findOne(new Tuple<>(idUser,idEvent));
        utilizatorEveniment.setNotificari(notificari);
        utilizatorEvenimentRepo.update(utilizatorEveniment);
    }

    public Iterable<Eveniment> getAllEvents(){
        return evenimentRepo.findAll();
    }

    public Iterable<Eveniment> getPageEvents(int offset,int limit){
        return evenimentRepo.findPage(offset,limit);
    }

    public UtilizatorEveniment findOneUtilizatorEveniment(Long idUser,Long idEvent){
        return utilizatorEvenimentRepo.findOne(new Tuple<Long,Long>(idUser,idEvent));
    }

    public void stergeEvenimenteleExpirate(){
        evenimentRepo.findAll().forEach(e->{
            if(e.getData().compareTo(LocalDateTime.now())<0) {
                utilizatorEvenimentRepo.findAll().forEach(x->{
                    if(x.getId().getRight()==e.getId())
                        utilizatorEvenimentRepo.delete(x.getId());
                });
                evenimentRepo.delete(e.getId());
            }
        });
    }

}