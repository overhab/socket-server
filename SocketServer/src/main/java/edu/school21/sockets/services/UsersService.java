package edu.school21.sockets.services;

import edu.school21.sockets.models.User;

public interface UsersService {
    boolean signUp(String username, String password);
    boolean signIn(String username, String password);
    void saveMessage(String username, String message);
}
