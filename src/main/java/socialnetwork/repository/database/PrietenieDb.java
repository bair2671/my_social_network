package socialnetwork.repository.database;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.ExistentObjectException;
import socialnetwork.domain.validators.NonExistentObjectException;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PrietenieDb extends AbstractDbRepository<Pereche<Long,Long>, Prietenie> {
    public PrietenieDb(String url, String username, String password, Validator<Prietenie> validator) {
        super(url, username, password, validator);
        table="prietenii";
    }

    @Override
    public Prietenie findOne(Pereche<Long, Long> longLongPereche) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" where id1="+ longLongPereche.getLeft()
                     + " and id2="+ longLongPereche.getRight()+ " or id1="+ longLongPereche.getRight()+ " and id2="+ longLongPereche.getLeft());
             ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String data = resultSet.getString("data");

                Prietenie prietenie = new Prietenie(new DateConverter().fromStringEnglishToLocalDateTime(data));
                prietenie.setId(new Pereche<Long,Long>(id1,id2));
                return prietenie;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> prietenii = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String data = resultSet.getString("data");

                Prietenie prietenie = new Prietenie(new DateConverter().fromStringEnglishToLocalDateTime(data));
                prietenie.setId(new Pereche<Long,Long>(id1,id2));
                prietenii.add(prietenie);
            }

            return prietenii;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenii;
    }

    @Override
    public Prietenie save(Prietenie entity) {
        validator.validate(entity);
        if(findOne(entity.getId())!=null)
            throw new ExistentObjectException("Exista deja o prietenie intre acesti utilizatori!!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                     table+" (\"id1\" ,\"id2\", \"data\") "+
                     "VALUES ('"+entity.getId().getLeft()+"'::integer, '" +entity.getId().getRight()+"'::integer, "+
                     "'"+entity.getDate()+"'::timestamp without time zone)" +
                     "returning id1;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Prietenie delete(Pereche<Long, Long> longLongPereche) {
        if(findOne(longLongPereche)==null)
            throw new NonExistentObjectException("Nu exista prietenie intre acesti utilizatori!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM "+table+"\n" +
                     "    WHERE (id1,id2) IN\n" +
                     "        (("+ longLongPereche.getRight()+","+ longLongPereche.getLeft()+"))"+
                     "      or (id1,id2) IN  (("+ longLongPereche.getLeft()+","+ longLongPereche.getRight()+")) returning id1;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Prietenie update(Prietenie entity) {
        return null;
    }

    @Override
    public Iterable<Prietenie> findPage(int offset, int limit) {
        Set<Prietenie> prietenii = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" order by id1 offset "+offset+" limit "+limit);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String data = resultSet.getString("data");

                Prietenie prietenie = new Prietenie(new DateConverter().fromStringEnglishToLocalDateTime(data));
                prietenie.setId(new Pereche<Long,Long>(id1,id2));
                prietenii.add(prietenie);
            }

            return prietenii;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenii;
    }
}
