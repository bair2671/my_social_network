package socialnetwork.repository.database;

import socialnetwork.domain.Crypter;
import socialnetwork.domain.DateConverter;
import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ExistentObjectException;
import socialnetwork.domain.validators.NonExistentObjectException;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageDb extends AbstractDbRepository<Long, Message> {
    public MessageDb(String url, String username, String password, Validator<Message> validator) {
        super(url, username, password, validator);
        table="messages";
    }

    @Override
    public Message findOne(Long msgId) {
        boolean gol = true;
        List<Utilizator> destinatari = new ArrayList<Utilizator>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" where msg_id="+msgId);
             ResultSet resultSet = statement.executeQuery()) {

             Long id=null,from=null,reply=null,to;
             String text=null,data=null;

             while (resultSet.next()) {
                id = resultSet.getLong("msg_id");
                from = resultSet.getLong("from");
                to = resultSet.getLong("to");
                reply = resultSet.getLong("reply");
                text = resultSet.getString("text");
                data = resultSet.getString("data");

                destinatari.add(findUtilizator(to));
                gol=false;
             }

             if(!gol) {
                 Message message = new Message(findUtilizator(from), destinatari, text, new DateConverter().fromStringEnglishToLocalDateTime(data));
                 message.setId(msgId);
                 if (reply != null && reply != 0)
                     message.setReply(findOne(reply));
                 return message;
             }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT distinct msg_id from "+table);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("msg_id");
                Message msg = findOne(id);
                messages.add(msg);
            }

            return messages;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        validator.validate(entity);
        Long msgId=null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT MAX(msg_id) FROM "+table+";");
             ResultSet resultSet = statement.executeQuery())
        {
            resultSet.next();
            msgId = resultSet.getLong("max")+1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if(findOne(msgId)!=null)
            throw new ExistentObjectException("Exista deja un mesaj cu acest id!");

        for(int i=0;i<entity.getTo().size();i++) {
            if(entity.getReply()!=null) {
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                             table + " (\"reply\", \"from\", \"to\", \"text\", \"data\", \"msg_id\") " +
                             "VALUES ('" + entity.getReply().getId() + "'::integer, '" + entity.getFrom().getId() + "'::integer, " +
                             "'" + entity.getTo().get(i).getId() + "'::integer, '" + entity.getMessage() + "'::character varying," +
                             "'" + entity.getData() + "'::timestamp without time zone, '" + msgId + "'::integer)" +
                             "returning id;");
                     ResultSet resultSet = statement.executeQuery()) {
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                             table + " (\"from\", \"to\", \"text\", \"data\", \"msg_id\") " +
                             "VALUES ('" + entity.getFrom().getId() + "'::integer, " +
                             "'" + entity.getTo().get(i).getId() + "'::integer, '" + entity.getMessage() + "'::character varying," +
                             "'" + entity.getData() + "'::timestamp without time zone, '" + msgId + "'::integer)" +
                             "returning id;");
                     ResultSet resultSet = statement.executeQuery()) {
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    @Override
    public Message delete(Long id) {
        if(findOne(id)==null)
            throw new NonExistentObjectException("Nu exista mesaj cu acest id!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM "+table+"\n" +
                     "    WHERE msg_id IN\n" +
                     "        ("+id+") returning id;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message update(Message entity) {
        validator.validate(entity);
        if(findOne(entity.getId())==null)
            throw new NonExistentObjectException("Nu exista mesaj cu acest id!");
        if(entity.getReply()!=null) {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("UPDATE public." + table + " SET\n" +
                         "\"reply\" = '" + entity.getReply().getId() + "'::integer " +
                         "    WHERE msg_id IN" +
                         "        ("+entity.getId()+") returning id;");
                 ResultSet resultSet = statement.executeQuery()) {
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("UPDATE public." + table + " SET\n" +
                         "\"reply\" = null " +
                         "    WHERE msg_id IN\n" +
                         "        ("+entity.getId()+") returning id;");
                 ResultSet resultSet = statement.executeQuery()) {
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    public Iterable<Message> findPage(int offset, int limit) {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT distinct msg_id from "+table+" order by id offset "+offset+" limit "+limit);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("msg_id");
                Message msg = findOne(id);
                messages.add(msg);
            }

            return messages;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private Utilizator findUtilizator(Long id){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users where id="+id);
             ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()) {
                Long idUser = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String password = resultSet.getString("password");
                String username = resultSet.getString("username");

                Utilizator utilizator = new Utilizator(firstName, lastName, username, new Crypter().decrypt(password));
                utilizator.setId(idUser);
                return utilizator;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
