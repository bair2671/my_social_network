package socialnetwork.repository.database;

import socialnetwork.domain.CerereDePrietenie;
import socialnetwork.domain.CerereDePrietenieStatus;
import socialnetwork.domain.DateConverter;
import socialnetwork.domain.validators.NonExistentObjectException;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CerereDePrietenieDb extends AbstractDbRepository<Long, CerereDePrietenie> {
    public CerereDePrietenieDb(String url, String username, String password, Validator<CerereDePrietenie> validator) {
        super(url, username, password, validator);
        table="cereri_de_prietenie";
    }

    @Override
    public CerereDePrietenie findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" where id="+id);
             ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()) {
                Long idExpeditor = resultSet.getLong("idExpeditor");
                Long idDestinatar = resultSet.getLong("idDestinatar");
                String status = resultSet.getString("status");
                String data = resultSet.getString("data");

                CerereDePrietenie cerere = new CerereDePrietenie(idExpeditor,idDestinatar, CerereDePrietenieStatus.valueOf(status),new DateConverter().fromStringEnglishToLocalDateTime(data));
                cerere.setId(id);
                return cerere;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<CerereDePrietenie> findAll() {
        Set<CerereDePrietenie> cereri = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idExpeditor = resultSet.getLong("idExpeditor");
                Long idDestinatar = resultSet.getLong("idDestinatar");
                String status = resultSet.getString("status");
                String data = resultSet.getString("data");

                CerereDePrietenie cerere = new CerereDePrietenie(idExpeditor,idDestinatar,CerereDePrietenieStatus.valueOf(status),new DateConverter().fromStringEnglishToLocalDateTime(data));
                cerere.setId(id);
                cereri.add(cerere);
            }

            return cereri;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return cereri;
    }

    @Override
    public CerereDePrietenie save(CerereDePrietenie entity) {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                     table+" (\"idExpeditor\" ,\"idDestinatar\", \"status\", \"data\") "+
                     "VALUES ('"+entity.getIdExpeditor()+"'::integer, '" +entity.getIdDestinatar()+"'::integer, "+
                     "'"+entity.getStatus().toString()+"'::character varying, '"+entity.getData()+"'::timestamp without time zone)" +
                     "returning id;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CerereDePrietenie delete(Long id) {
        if(findOne(id)==null)
            throw new NonExistentObjectException("Nu exista cerere de prietenie cu acest id!");
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
    public CerereDePrietenie update(CerereDePrietenie entity) {
        validator.validate(entity);
        if(findOne(entity.getId())==null)
            throw new NonExistentObjectException("Nu exista cerere de prietenie cu acest id!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE public."+table+" SET\n" +
                     "\"status\" = '"+entity.getStatus().toString()+"'::character varying " +
                     "WHERE\n id = "+entity.getId()+"" +
                     "returning id;");
             ResultSet resultSet = statement.executeQuery()) {}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<CerereDePrietenie> findPage(int offset, int limit) {
        Set<CerereDePrietenie> cereri = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from "+table+" order by id offset "+offset+" limit "+limit);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idExpeditor = resultSet.getLong("idExpeditor");
                Long idDestinatar = resultSet.getLong("idDestinatar");
                String status = resultSet.getString("status");
                String data = resultSet.getString("data");

                CerereDePrietenie cerere = new CerereDePrietenie(idExpeditor,idDestinatar,CerereDePrietenieStatus.valueOf(status),new DateConverter().fromStringEnglishToLocalDateTime(data));
                cerere.setId(id);
                cereri.add(cerere);
            }

            return cereri;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return cereri;
    }
}
