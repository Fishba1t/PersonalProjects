package com.example.socialnetworksystem.service;

import com.example.socialnetworksystem.domain.Message;
import com.example.socialnetworksystem.domain.Prietenie;
import com.example.socialnetworksystem.domain.Utilizator;
import com.example.socialnetworksystem.repository.FriendshipDatabase;
import com.example.socialnetworksystem.repository.MessageDatabase;
import com.example.socialnetworksystem.repository.UserDatabase;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import static com.example.socialnetworksystem.domain.FriendRequest.REJECTED;


public class ServiceDB implements ServiceInterface {
    private UserDatabase repo_utilizator;
    private FriendshipDatabase repo_prieteni;
    private MessageDatabase repo_message;

    public ServiceDB(UserDatabase r1, FriendshipDatabase r2, MessageDatabase r3){
        this.repo_utilizator=r1;
        this.repo_prieteni=r2;
        this.repo_message=r3;
    }

    public void set_limit_offset(int l,int o){
        this.repo_utilizator.setLimit(l);
        this.repo_utilizator.setOffset(o);
        this.repo_prieteni.setLimit_friendships(l);
        this.repo_prieteni.setOffset_friendships(o);
        this.repo_prieteni.setLimit_requests(l);
        this.repo_prieteni.setOffset_requests(o);
        this.repo_message.setLimit(l);
        this.repo_message.setOffset(o);
    }

    public void set_offset_users(int o){
        this.repo_utilizator.setOffset(o);
    }

    public void set_offset_friendships(int o){
        this.repo_prieteni.setOffset_friendships(o);
    }

    public void set_offset_requests_user(int o){
        this.repo_prieteni.setOffset_requests(o);
    }

    public void set_offset_messages(int o){
        this.repo_message.setOffset(o);
    }

    public int get_number_of_users(){
        return this.repo_utilizator.get_count();
    }

    public int get_number_of_friendships(){
        return this.repo_prieteni.get_count();
    }
    public int get_number_of_messages(){
        return this.repo_message.get_count();
    }

    public int get_limit(){
        return this.repo_message.getLimit();
    }

    public int get_offset_users(){
        return this.repo_utilizator.getOffset();
    }

    public int get_offset_friendships(){
        return this.repo_prieteni.getOffset_friendships();
    }

    public int get_offset_friendships_user(){
        return this.repo_prieteni.getOffset_friendships();
    }

    public int get_offset_requests(){
        return this.repo_prieteni.getOffset_requests();
    }

    public int get_offset_messages(){
        return this.repo_message.getOffset();
    }

    public int get_number_of_request(UUID userID){
        return this.repo_prieteni.countFriendshipsWithRequestNotAccepted(userID);
    }

    public boolean userExist(Utilizator user){
        return repo_utilizator.userExists(user);
    }
    public boolean adaugare_utilizator(Utilizator u) {
        try{
            if(repo_utilizator.save(u).isPresent())
                return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public boolean adaugare_mesaj(Message u) {
        try{
            if(repo_message.save(u).isPresent())
                return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public void modifica_mesaj(Message entity,Message m){
        repo_message.modifica(entity,m);
    }

    public void modifica_utilizator(Utilizator u,String n,String l, String e){
        repo_utilizator.modifica(u,n,l,e);
    }

    public void sterge_utilizator(String email) {
        if(email==null){
            throw new IllegalArgumentException("EMAIL-UL NU POATE SA FIE NULL");
        }
        Utilizator u = getUtilizatorEmail(email);
        if(u==null){
            throw new IllegalArgumentException("NU EXISTA UTILIZATOR CU ACEST EMAIL");
        }
        else repo_utilizator.delete(u.getId());
    }

    public void creazaPrietenie(String email1, String email2) {
        Utilizator u1, u2;
        u1 = getUtilizatorEmail(email1);
        u2 = getUtilizatorEmail(email2);

        // Check if the friendship already exists
        Iterable<Prietenie> prietenii = repo_prieteni.findAll_DBNoLimitOffset();
        for (Prietenie existingFriendship : prietenii) {
            Utilizator existingU1 = existingFriendship.getUser1();
            Utilizator existingU2 = existingFriendship.getUser2();

            // Check if the friendship matches either way
            if ((existingU1.getEmail().equals(email1) && existingU2.getEmail().equals(email2)) ||
                    (existingU1.getEmail().equals(email2) && existingU2.getEmail().equals(email1))) {
                throw new IllegalArgumentException("PRIETENIE EXISTENTA INTRE UTILIZATORI");
            }

        }
        // Friendship doesn't exist, create a new one
        Prietenie prietenie = new Prietenie(u1, u2);
        repo_prieteni.save(prietenie);
    }

    public void modificaPrietenie(Prietenie p){
        repo_prieteni.modify(p);
    }

    public void stergePrietenie(String email1, String email2) {
        Utilizator u1,u2;
        u1=getUtilizatorEmail(email1);
        u2=getUtilizatorEmail(email2);
        if(email1==null || email2==null){
            throw new IllegalArgumentException("EMAIL-UL NU POATE SA FIE NULL");
        }
        if(u1 == null || u2 == null){
            throw new IllegalArgumentException("NU EXISTA UTILIZATOR CU ACEST EMAIL");
        }
        else{
            Iterable<Prietenie> lista = repo_prieteni.findAll();
            for(Prietenie p : lista){
                if (Objects.equals(p.getUser1().getEmail(), u1.getEmail()) && Objects.equals(p.getUser2().getEmail(), u2.getEmail())) {
                    System.out.println(p.getUser1().getEmail());
                    System.out.println(p.getUser2().getEmail());
                    repo_prieteni.delete(p.getId());
                    break;
                }
                if (Objects.equals(p.getUser2().getEmail(), u1.getEmail()) && Objects.equals(p.getUser1().getEmail(), u2.getEmail())) {
                    System.out.println(p.getUser1().getEmail());
                    System.out.println(p.getUser2().getEmail());
                    repo_prieteni.delete(p.getId());
                    break;
                }
            }
        }
    }

    public Iterable<Utilizator> getAllUtilizatori() {
        return repo_utilizator.findAll();
    }

    public Iterable<Utilizator> getUtilizatoriLimitOffset(){ return repo_utilizator.findAll_Limit_Offset();}

    public Iterable<Message> getAllMessages() {
        return repo_message.findAll_DB();
    }

    public Iterable<Prietenie> findAll_Request(UUID userID) {
        return repo_prieteni.findAll_Request(userID);
    }

    public Iterable<Prietenie> findAll_DBNoLimitOffset(){
        return repo_prieteni.findAll_DBNoLimitOffset();
    }
    public int get_countFriendships(UUID userID){
        return this.repo_prieteni.get_countfriendships(userID);
    }

    public Iterable<Prietenie>getAll_FriendshipsVerificare(UUID userID){
        return repo_prieteni.findAll_FriendshipsVerificare(userID);
    }

    public Iterable<Prietenie>getAll_FriendshipsLimitOffset(){
        return repo_prieteni.findAll_DB();
    }

    public void declineFriendRequest(String email1, String email2) {
        Utilizator u1,u2;
        u1=getUtilizatorEmail(email1);
        u2=getUtilizatorEmail(email2);
        if(u1 == null || u2 == null){
            throw new IllegalArgumentException("NU EXISTA UTILIZATOR CU ACEST EMAIL");
        }
        else{
            Iterable<Prietenie> lista = repo_prieteni.findAll();
            lista.forEach(p->{
                if(p.getUser1()==u1 && p.getUser2()==u2){
                    p.set_request(REJECTED);
                }
            });
        }
    }

    public Utilizator getUtilizatorEmail(String email) {
        Iterable<Utilizator> lista = repo_utilizator.findAll();
        AtomicReference<Utilizator> found = new AtomicReference<>();
        lista.forEach(u->{
            if(u.getEmail().equals(email)){
                found.set(u);
            }
        });
        return found.get();
    }

}
