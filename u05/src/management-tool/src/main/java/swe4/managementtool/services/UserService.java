package swe4.managementtool.services;

import swe4.managementtool.domain.User;
import swe4.managementtool.repositories.FakeUserRepository;
import swe4.managementtool.repositories.UserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Collection;

public class UserService {

    private static final int SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;

    private final UserRepository userRepository = new FakeUserRepository();
    private final SecureRandom random = new SecureRandom();
    private final SecretKeyFactory factory;

    public UserService() {
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public User insertUser(String username, String password) {
        final byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        try {
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return userRepository.insertUser(username, hash);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    public Collection<User> findAllUsers() {
        return userRepository.findAllUsers();
    }
}
