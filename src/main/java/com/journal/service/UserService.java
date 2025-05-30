package com.journal.service;

import java.util.List;
import java.util.Optional;

import com.journal.dto.Mail;
import com.journal.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journal.entity.User;
import com.journal.repository.UserRepository;

@Service
@Slf4j
@Transactional
public class UserService {


    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Value("${mail.subject}")
    private String subject;

    @Value("${mail.content}")
    private String content;

    @Autowired
    private MailSenderImpl mailSenderImpl;

    public User saveEntry(User user) {
        return userRepository.save(user);
    }

    public User saveUserEntry(User user) throws Exception {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.getUserRoles().add("USER");
        try {
            User savedUser = userRepository.save(user);
            Mail mail = new Mail(user.getEmail(), subject, content);
            mailSenderImpl.sendMail(mail); //It will send mail on the user email
            return savedUser;
        } catch (Exception e) {
            log.error("Error saving user entry: {}", e.getMessage());
            throw new Exception("Error saving user entry: " + e.getMessage());
        }

    }

    public List<User> getAllEntries() throws UserException {

        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            log.info("No user entries found");
            throw new UserException("No user entries found");
        }
        return userList;
    }

    public User getEntry(ObjectId id) throws UserException {
        try {
            Optional<User> entry = userRepository.findById(id);
            return entry.orElseThrow(() -> new UserException("User entry not found for id: " + id));
        } catch (UserException e) {
            log.error("User entry not found", e.getMessage());
            throw e;
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String deleteEntry(ObjectId id) {
        userRepository.deleteById(id);
        return "User Entry deleted successfully";
    }

    public String deleteByUsername(String username) throws UserException {
        log.info("delete user request received for {}", username);
        User user = getUserByUsername(username);
        if (user == null) {
            throw new UserException("User entry not found for username: " + username);
        }
        userRepository.deleteByUsername(username);
        return "User Entry deleted successfully" + " for username: " + username;
    }

    public User createAdmin(User user) throws Exception {
        User existingUser = getUserByUsername(user.getUsername());
        User updatedUser;
        try {
            if (existingUser != null) {
                if (!existingUser.getUserRoles().contains("ADMIN")) {
                    existingUser.getUserRoles().add("ADMIN");
                    updatedUser = saveEntry(existingUser);
                    return updatedUser;
                }
                throw new UserException("User is already an ADMIN");
            }
        } catch (UserException e) {
            log.error("User is already an ADMIN: {}", e);
            throw e;
        }

        user.getUserRoles().add("ADMIN");
        updatedUser = saveUserEntry(user);
        return updatedUser;
    }

}
