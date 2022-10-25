package socialnetwork.repository.database;

import socialnetwork.domain.Tuple;
import socialnetwork.domain.UtilizatorEveniment;
import socialnetwork.domain.validators.ExistentObjectException;
import socialnetwork.domain.validators.NonExistentObjectException;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UtilizatorEvenimentDb extends AbstractDbRepository<Tuple<Long,Long>, UtilizatorEveniment> {
    public UtilizatorEvenimentDb(String url, String username, String password, Validator<UtilizatorEveniment> validator) {
        super(url, username, password, validator);
        table="users_evenimente";
    }

    @Override
    public UtilizatorEveniment findOne(Tuple<Long,Long> id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" where id_user="+ id.getLeft()
                     + " and id_eveniment="+ id.getRight());
             ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()) {
                Long idUser = resultSet.getLong("id_user");
                Long idEvent = resultSet.getLong("id_eveniment");
                boolean notificari = resultSet.getBoolean("notificari");

                UtilizatorEveniment utilizatorEveniment = new UtilizatorEveniment(notificari);
                utilizatorEveniment.setId(new Tuple<>(idUser,idEvent));
                return utilizatorEveniment;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<UtilizatorEveniment> findAll() {
        Set<UtilizatorEveniment> utilizatoriEvenimente = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idUser = resultSet.getLong("id_user");
                Long idEvent = resultSet.getLong("id_eveniment");
                boolean notificari = resultSet.getBoolean("notificari");

                UtilizatorEveniment utilizatorEveniment = new UtilizatorEveniment(notificari);
                utilizatorEveniment.setId(new Tuple<>(idUser,idEvent));
                utilizatoriEvenimente.add(utilizatorEveniment);
            }

            return utilizatoriEvenimente;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return utilizatoriEvenimente;
    }

    @Override
    public UtilizatorEveniment save(UtilizatorEveniment entity) {
        validator.validate(entity);
        if(findOne(entity.getId())!=null)
            throw new ExistentObjectException("Utilizatorul participa deja la acest eveniment!!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                     table+" (\"id_user\" ,\"id_eveniment\", \"notificari\") "+
                     "VALUES ('"+entity.getId().getLeft()+"'::integer, '" +entity.getId().getRight()+"'::integer , "+
                     "'"+entity.getNotificari()+"'::bool)" +
                     "returning id_user;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UtilizatorEveniment delete(Tuple<Long,Long> id) {
        if(findOne(id)==null)
            throw new NonExistentObjectException("Utilizatorul dat nu participa la evenimentul dat!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM "+table+"\n" +
                     "    WHERE (id_user,id_eveniment) IN\n" +
                     "        (("+ id.getLeft()+","+ id.getRight()+"))"+ " returning id_user;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UtilizatorEveniment update(UtilizatorEveniment entity) {
        validator.validate(entity);
        if(findOne(entity.getId())==null)
            throw new NonExistentObjectException("Utilizatorul dat nu participa la evenimentul dat!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE public."+table+" SET\n" +
                     "\"notificari\" = '"+entity.getNotificari()+"'::bool " +
                     "    WHERE (id_user,id_eveniment) IN\n" +
                     "        (("+ entity.getId().getLeft()+","+ entity.getId().getRight()+"))"+ " returning id_user;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<UtilizatorEveniment> findPage(int offset, int limit) {
        Set<UtilizatorEveniment> utilizatoriEvenimente = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" order by id offset "+offset+" limit "+limit);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idUser = resultSet.getLong("id_user");
                Long idEvent = resultSet.getLong("id_eveniment");
                boolean notificari = resultSet.getBoolean("notificari");

                UtilizatorEveniment utilizatorEveniment = new UtilizatorEveniment(notificari);
                utilizatorEveniment.setId(new Tuple<>(idUser,idEvent));
                utilizatoriEvenimente.add(utilizatorEveniment);
            }

            return utilizatoriEvenimente;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return utilizatoriEvenimente;
    }

}
