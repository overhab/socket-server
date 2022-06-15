package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessageRepository;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component("usersService")
public class UsersServiceImpl implements UsersService {

    @Autowired
    @Qualifier("usersRepository")
    private UsersRepository usersRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean signUp(String username, String password) {
        if (usersRepository.findByUsername(username).isPresent()) {
            System.out.println("User with nickname " + username + " already exists");
            return false;
        }

        String hashPassword = passwordEncoder.encode(password);
        User user = new User(username, hashPassword);

        usersRepository.save(user);
        return true;
    }

    @Override
    public boolean signIn(String username, String password) {
        Optional<User> user = usersRepository.findByUsername(username);
        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveMessage(String username, String text) {
        Message message = new Message(usersRepository.findByUsername(username).get(), text, Timestamp.valueOf(LocalDateTime.now()));
        messageRepository.save(message);
    }
}
