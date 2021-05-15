package swe4.managementtool.services;

import swe4.managementtool.domain.User;
import swe4.managementtool.repositories.RepositoryFactory;
import swe4.managementtool.repositories.UserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Collection;
import java.util.Objects;

public class UserService {

    private static final int SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;

    private final UserRepository userRepository = RepositoryFactory.userRepositoryInstance();
    private final SecureRandom random = new SecureRandom();
    private final SecretKeyFactory factory;

    public UserService() {
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public void insertUser(String firstname, String lastname, String username, String password) {
        Objects.requireNonNull(firstname);
        Objects.requireNonNull(lastname);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        User user = new User(firstname, lastname, username, createHash(password));
        userRepository.insertUser(user);
    }

    public void updateUser(final User user) {
        userRepository.updateUser(user);
    }

    public void updateUserPassword(final User user, String password) {
        user.setHash(createHash(password));
        updateUser(user);
    }

    public Collection<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    private byte[] createHash(String password) {
        final byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        try {
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }
}
