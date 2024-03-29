package com.example.socialnetworksystem.repository;

import com.example.socialnetworksystem.domain.Utilizator;
import com.example.socialnetworksystem.validators.Validator;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserDatabase extends AbstractDatabaseRepository<UUID, Utilizator> {

    private int limit =0;
    private int offset =0;

    public int getOffset() {
        return offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public UserDatabase(String url, String username, String password, Validator<Utilizator> validator){
        super(url, username, password, "users",validator);
        loadData();
    }

    @Override
    public void loadData(){
        findAll_DB().forEach(super::save);
    }

    public int get_count(){
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + tableName + ";");
            ResultSet resultSet = statement.executeQuery();) {
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return 0;
    }

    @Override
    public Iterable<Utilizator> findAll(){
        return findAll_DB();
    }

    @Override
    protected Iterable<Utilizator> findAll_DB(){
        Set<Utilizator> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName +";");
            ResultSet resultSet = statement.executeQuery();) {
            while(resultSet.next()){
                UUID id = UUID.fromString(resultSet.getString("id"));
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                Utilizator user = new Utilizator(firstName, lastName, email);
                user.setId(id);
                user.setPassword(password);
                users.add(user);
            }
            return users;
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return users;
    }

    public Iterable<Utilizator> findAll_Limit_Offset(){
        Set<Utilizator> users = new HashSet<>();
        final int limit_value = this.limit;
        final int limit_offset = this.offset;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + " LIMIT " + limit_value + " OFFSET " + limit_offset);
            ResultSet resultSet = statement.executeQuery();) {
            while(resultSet.next()){
                UUID id = UUID.fromString(resultSet.getString("id"));
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                Utilizator user = new Utilizator(firstName, lastName, email);
                user.setId(id);
                user.setPassword(password);
                users.add(user);
            }
            return users;
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return users;
    }


    public boolean userExists(Utilizator user){
        Iterable<Utilizator> allUsers = findAll_DB();
        List<Utilizator> usersList = StreamSupport.stream(allUsers.spliterator(), false)
                .collect(Collectors.toList());
        for (Utilizator existingUser : usersList) {
            if (existingUser.getEmail().equals(user.getEmail()) || existingUser.getPassword().equals(user.getPassword())) {
                return true;  // Match found
            }
        }
        return false;  // Replace this with the actual return statement based on your logic
    }
    @Override
    public Optional<Utilizator> save(Utilizator entity){
        Optional<Utilizator> u = super.save(entity);

        if (u != null){
            try(Connection connection = DriverManager.getConnection(url, username, password)){
                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?);");
                statement.setString(1, entity.getId().toString());
                statement.setString(2, entity.getFirstName());
                statement.setString(3, entity.getLastName());
                statement.setString(4, entity.getEmail());
                statement.setString(5, entity.getPassword());
                statement.executeUpdate();
            }
            catch(SQLException e){
                System.out.println(e);
            }
        }
        return u;
    }

    public void modifica(Utilizator entity,String n, String l, String e){
        if (entity != null){
            try(Connection connection = DriverManager.getConnection(url, username, password)){
                PreparedStatement statement = connection.prepareStatement("UPDATE " + tableName + " SET firstname = (?), lastname = (?), email = (?) WHERE ID = (?);");
                statement.setString(1, n);
                statement.setString(2, l);
                statement.setString(3, e);
                statement.setString(4, entity.getId().toString());
                statement.executeUpdate();
            }
            catch(SQLException el){
                System.out.println(e);
            }
        }
    }

    @Override
    public Optional<Utilizator> delete(UUID uuid) {
        Optional<Utilizator> u = super.delete(uuid);
        if(u.isEmpty()){
            try(Connection connection = DriverManager.getConnection(url, username, password)){
                PreparedStatement statement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE ID = (?);");
                String id = uuid.toString();
                statement.setString(1,id);
                statement.executeUpdate();
                PreparedStatement statement2 = connection.prepareStatement("DELETE FROM friendships" + " WHERE id_user1 = (?);");
                statement2.setString(1,id);
                statement2.executeUpdate();
                PreparedStatement statement3 = connection.prepareStatement("DELETE FROM friendships" + " WHERE id_user2 = (?);");
                statement3.setString(1,id);
                statement3.executeUpdate();

            }
            catch(SQLException e){
                System.out.println(e);
            }
        }
        return u;
    }
}
