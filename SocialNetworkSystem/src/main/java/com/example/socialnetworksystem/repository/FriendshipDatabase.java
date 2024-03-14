package com.example.socialnetworksystem.repository;

import com.example.socialnetworksystem.domain.FriendRequest;
import com.example.socialnetworksystem.domain.Prietenie;
import com.example.socialnetworksystem.domain.Utilizator;
import com.example.socialnetworksystem.validators.PrietenieValidator;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FriendshipDatabase extends AbstractDatabaseRepository<UUID, Prietenie> {
    private int limit_friendships;
    private int offset_friendships;
    private int limit_requests;
    private int offset_requests;

    public void setLimit_friendships(int limit_friendships) {
        this.limit_friendships = limit_friendships;
    }

    public int getOffset_friendships() {
        return offset_friendships;
    }

    public void setOffset_friendships(int offset_friendships) {
        this.offset_friendships = offset_friendships;
    }

    public void setLimit_requests(int limit_requests) {
        this.limit_requests = limit_requests;
    }

    public int getOffset_requests() {
        return offset_requests;
    }

    public void setOffset_requests(int offset_requests) {
        this.offset_requests = offset_requests;
    }


    public FriendshipDatabase(String url, String username, String password, PrietenieValidator validator){
        super(url, username, password, "friendships",validator);
        loadData();
    }


    @Override
    public void loadData() {
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

    public int get_countfriendships(UUID userID){
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + tableName + " WHERE request = 'ACCEPTED' AND (id_user1 = ? OR id_user2 = ?)")) {
            statement.setObject(1, userID.toString()); // Set userID for id_user1
            statement.setObject(2, userID.toString()); // Set userID for id_user2

            ResultSet resultSet = statement.executeQuery();
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
    public Iterable<Prietenie> findAll_DB() {
        Set<Prietenie> friendships = new HashSet<>();
        final int limit_value = this.limit_friendships;
        final int limit_offset = this.offset_friendships;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + " LIMIT " + limit_value + " OFFSET " + limit_offset);
            ResultSet resultSet = statement.executeQuery();)
        {
            while(resultSet.next()){
                UUID id_ = UUID.fromString(resultSet.getString("id"));
                UUID id_user1 = UUID.fromString(resultSet.getString("id_user1"));
                UUID id_user2 = UUID.fromString(resultSet.getString("id_user2"));
                LocalDateTime from = resultSet.getTimestamp("friendsfrom").toLocalDateTime();


                Utilizator u1 = retrieveUser(id_user1);
                Utilizator u2 = retrieveUser(id_user2);

                if(u1!=null && u2!=null){
                    Prietenie friendship = new Prietenie(u1, u2,from);
                    friendship.set_request(FriendRequest.valueOf(resultSet.getString("request")));
                    friendship.setId(id_);
                    friendships.add(friendship);
                }
            }
            return friendships;
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return friendships;
    }

    public Iterable<Prietenie> findAll_DBNoLimitOffset() {
        Set<Prietenie> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName);
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                UUID id_ = UUID.fromString(resultSet.getString("id"));
                UUID id_user1 = UUID.fromString(resultSet.getString("id_user1"));
                UUID id_user2 = UUID.fromString(resultSet.getString("id_user2"));
                LocalDateTime from = resultSet.getTimestamp("friendsfrom").toLocalDateTime();

                Utilizator u1 = retrieveUser(id_user1);
                Utilizator u2 = retrieveUser(id_user2);

                if (u1 != null && u2 != null) {
                    Prietenie friendship = new Prietenie(u1, u2, from);
                    friendship.set_request(FriendRequest.valueOf(resultSet.getString("request")));
                    friendship.setId(id_);
                    friendships.add(friendship);
                }
            }
            return friendships;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return friendships;
    }

    public Iterable<Prietenie> findAll_FriendshipsVerificare(UUID userID) {
        Set<Prietenie> friendships = new HashSet<>();
        final int limit_value = this.limit_friendships;
        final int limit_offset = this.offset_friendships;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM " + tableName + " WHERE request = 'ACCEPTED' AND (id_user1 = ? OR id_user2 = ?) LIMIT ? OFFSET ?")
        ) {
            statement.setObject(1, userID.toString()); // Set userID for id_user1
            statement.setObject(2, userID.toString()); // Set userID for id_user2
            statement.setInt(3, limit_value); // Set the limit parameter
            statement.setInt(4, limit_offset); // Set the offset parameter

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID id_ = UUID.fromString(resultSet.getString("id"));
                UUID id_user1 = UUID.fromString(resultSet.getString("id_user1"));
                UUID id_user2 = UUID.fromString(resultSet.getString("id_user2"));
                LocalDateTime from = resultSet.getTimestamp("friendsfrom").toLocalDateTime();

                Utilizator u1 = retrieveUser(id_user1);
                Utilizator u2 = retrieveUser(id_user2);

                if (u1 != null && u2 != null) {
                    Prietenie friendship = new Prietenie(u1, u2, from);
                    friendship.set_request(FriendRequest.valueOf(resultSet.getString("request")));
                    friendship.setId(id_);
                    friendships.add(friendship);
                }
            }
            return friendships;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return friendships;
    }

    public Iterable<Prietenie> findAll_Request(UUID userID) {
        Set<Prietenie> friendships = new HashSet<>();
        //System.out.println("In repo:"+userID);
        final int limit_value = this.limit_requests;
        final int limit_offset = this.offset_requests;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE request != 'ACCEPTED' AND (id_user1 = ? OR id_user2 = ?) LIMIT ? OFFSET ?");
        ) {
            statement.setObject(1, userID.toString());
            statement.setObject(2, userID.toString());
            statement.setInt(3, limit_value); // Set the limit parameter
            statement.setInt(4, limit_offset); // Set the offset parameter

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID id_ = UUID.fromString(resultSet.getString("id"));
                UUID id_user1 = UUID.fromString(resultSet.getString("id_user1"));
                UUID id_user2 = UUID.fromString(resultSet.getString("id_user2"));
                LocalDateTime from = resultSet.getTimestamp("friendsfrom").toLocalDateTime();

                Utilizator u1 = retrieveUser(id_user1);
                Utilizator u2 = retrieveUser(id_user2);

                if (u1 != null && u2 != null) {
                    Prietenie friendship = new Prietenie(u1, u2, from);
                    friendship.set_request(FriendRequest.valueOf(resultSet.getString("request")));
                    friendship.setId(id_);
                    friendships.add(friendship);
                }
            }
            return friendships;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return friendships;
    }

    public int countFriendshipsWithRequestNotAccepted(UUID userID) {
        int count = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + tableName + " WHERE request != 'ACCEPTED' AND (id_user1 = ? OR id_user2 = ?)");
        ) {
            statement.setObject(1, userID.toString()); // Set the user ID parameter for the first '?'
            statement.setObject(2, userID.toString()); // Set the user ID parameter for the second '?'

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return count;
    }

    @Override
    public Optional<Prietenie> delete(UUID uuid) {
        System.out.println(uuid);
        Optional<Prietenie> f = super.delete(uuid);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = (?);");) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            return f;
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return f;
    }

    @Override
    public Optional<Prietenie> save (Prietenie entity){
        Optional<Prietenie> f = super.save(entity);

        if (f != null){
            try(Connection connection = DriverManager.getConnection(url, username, password)){
                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?);");
                statement.setString(1, entity.getId().toString());
                statement.setString(2, entity.getUser1().getId().toString());
                statement.setString(3, entity.getUser2().getId().toString());
                statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
                statement.setString(5,entity.getRequest());
                statement.executeUpdate();
            }
            catch(SQLException e){
                System.out.println(e);
            }
        }
        return f;
    }

    public void modify(Prietenie entity){
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = connection.prepareStatement("UPDATE friendships  SET request = (?) WHERE id = (?);");
            statement.setString(1, entity.getRequest());
            statement.setString(2,entity.getId().toString());
            statement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }



    private Utilizator retrieveUser(UUID userId) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?;");
        ) {
            statement.setString(1, userId.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                Utilizator user = new Utilizator(firstName, lastName, email);
                user.setId(id);

                return user;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

}

