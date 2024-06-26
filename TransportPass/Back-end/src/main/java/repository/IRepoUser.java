package repository;

import domain.User;

public interface IRepoUser extends IRepository<Integer, User>{
    public User findOneByUsernameAndPassword(String username, String password);
}
