package socialnetwork.repository.database;

import socialnetwork.domain.Crypter;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ExistentObjectException;
import socialnetwork.domain.validators.NonExistentObjectException;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UtilizatorDb extends AbstractDbRepository<Long, Utilizator>{
    public UtilizatorDb(String url, String username, String password, Validator<Utilizator> validator) {
        super(url, username, password, validator);
        table = "users";
    }

    @Override
    public Utilizator findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" where id="+id);
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

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String userName = resultSet.getString("username");
                String password = new Crypter().decrypt(resultSet.getString("password"));

                Utilizator utilizator = new Utilizator(firstName, lastName, userName,password);
                utilizator.setId(id);
                users.add(utilizator);
            }

            return users;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Utilizator save(Utilizator entity) {
        validator.validate(entity);
        //if(findOne(entity.getId())!=null)
            //throw new ExistentObjectException("Exista deja un utilizator cu acest id!");
        if(findOneByUsername(entity.getUsername())!=null)
            throw new ExistentObjectException("Exista deja un utilizator cu acest username!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                     table+" (\"firstName\", \"lastName\",\"username\", \"password\") "+
                     "VALUES ('" +entity.getFirstName()+"'::character varying, "+
                     "'"+entity.getLastName()+"'::character varying, " + "'"+ entity.getUsername()+"'::character varying, '"+
                     new Crypter().encrypt(entity.getPassword())+"'::character varying)" +
                     "returning id;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utilizator delete(Long id) {
        if(findOne(id)==null)
            throw new NonExistentObjectException("Nu exista utilizator cu acest id!");
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
    public Utilizator update(Utilizator entity) {
        validator.validate(entity);
        if(findOne(entity.getId())==null)
            throw new NonExistentObjectException("Nu exista utilizator cu acest id!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE public."+table+" SET\n" +
                     "\"firstName\" = '"+entity.getFirstName()+"'::character varying, " +
                     "\"lastName\" = '"+entity.getLastName()+"'::character  varying, "+
                     "\"username\" = '"+entity.getUsername()+"'::character  varying, "+
                     "\"password\" = '"+new Crypter().encrypt(entity.getPassword()) +"'::character  varying "+
                     "WHERE\n id = "+entity.getId()+"" +
                     "returning id;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Utilizator> findPage(int offset, int limit) {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" order by id offset "+offset+" limit "+limit);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String userName = resultSet.getString("username");
                String password = new Crypter().decrypt(resultSet.getString("password"));

                Utilizator utilizator = new Utilizator(firstName, lastName,userName,password);
                utilizator.setId(id);
                users.add(utilizator);
            }

            return users;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return users;

    }

    public Utilizator findOneByUsername(String usernameU) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" where username= '"+usernameU+"'");
             ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()) {
                Long idUser = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String password = resultSet.getString("password");
                String userNameU = resultSet.getString("username");

                Utilizator utilizator = new Utilizator(firstName, lastName, userNameU, new Crypter().decrypt(password));
                utilizator.setId(idUser);
                return utilizator;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
