package socialnetwork.repository.database;

import socialnetwork.domain.Crypter;
import socialnetwork.domain.DateConverter;
import socialnetwork.domain.Eveniment;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ExistentObjectException;
import socialnetwork.domain.validators.NonExistentObjectException;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EvenimentDb extends AbstractDbRepository<Long, Eveniment> {
    public EvenimentDb(String url, String username, String password, Validator<Eveniment> validator) {
        super(url, username, password, validator);
        table="evenimente";
    }

    @Override
    public Eveniment findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" where id="+id);
             ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()) {
                Long idEvent = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String data = resultSet.getString("data");

                Eveniment eveniment = new Eveniment(nume,new DateConverter().fromStringEnglishToLocalDateTime(data));
                eveniment.setId(idEvent);
                return eveniment;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Eveniment> findAll() {
        Set<Eveniment> evenimente = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idEvent = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String data = resultSet.getString("data");

                Eveniment eveniment = new Eveniment(nume,new DateConverter().fromStringEnglishToLocalDateTime(data));
                eveniment.setId(idEvent);
                evenimente.add(eveniment);
            }

            return evenimente;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return evenimente;
    }

    @Override
    public Eveniment save(Eveniment entity) {
        validator.validate(entity);
        if(findOne(entity.getId())!=null)
            throw new ExistentObjectException("Exista deja acest eveniment!!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                     table+" (\"nume\", \"data\") "+
                     "VALUES ('"+entity.getNume()+"'::character varying , "+
                     "'"+entity.getData()+"'::timestamp without time zone)" +
                     "returning id;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Eveniment delete(Long id) {
        if(findOne(id)==null)
            throw new NonExistentObjectException("Nu exista eveniment cu acest id!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM "+table+"\n" +
                     "    WHERE id IN\n" +
                     "        ("+id+") returning id;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Eveniment update(Eveniment entity) {
        return null;
    }

    @Override
    public Iterable<Eveniment> findPage(int offset, int limit) {
        Set<Eveniment> evenimente = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" order by id offset "+offset+" limit "+limit);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idEvent = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String data = resultSet.getString("data");

                Eveniment eveniment = new Eveniment(nume,new DateConverter().fromStringEnglishToLocalDateTime(data));
                eveniment.setId(idEvent);
                evenimente.add(eveniment);
            }

            return evenimente;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return evenimente;
    }

    public List<Eveniment> evenimenteUtilizator(Long idUser){
        if(findUtilizator(idUser)==null)
            throw new NonExistentObjectException("Nu exista utilizator cu acest id!");
        List<Eveniment> evenimente = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users_evenimente where id_user="+idUser);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idEvent = resultSet.getLong("id_eveniment");

                Eveniment eveniment = findOne(idEvent);
                evenimente.add(eveniment);
            }
            return  evenimente;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return evenimente;
    }

    public List<Utilizator> participantiEveniment(Long idEvent){
        if(findOne(idEvent)==null)
            throw new NonExistentObjectException("Nu exista eveniment cu acest id!");
        List<Utilizator> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users_evenimente where id_eveniment="+idEvent);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idUser = resultSet.getLong("id");

                Utilizator user = findUtilizator(idUser);
                users.add(user);
            }
            return users;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private Utilizator findUtilizator(Long id){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users where id="+id);
             ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()) {
                Long idUser = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                Utilizator utilizator = new Utilizator(firstName, lastName, username,new Crypter().decrypt(password));
                utilizator.setId(idUser);
                return utilizator;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
