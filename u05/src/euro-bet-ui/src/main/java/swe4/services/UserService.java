package swe4.services;

import swe4.domain.User;
import swe4.repositories.RepositoryFactory;
import swe4.repositories.UserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.Optional;

public class UserService {

    private static final int SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;
    public static final String SEPARATOR = ":";
    public static final int RADIX = 16;

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
        User user = new User(firstname, lastname, username, createPasswordHash(password));
        userRepository.insertUser(user);
    }

    public void updateUser(final User user) {
        userRepository.updateUser(user);
    }

    public void updateUserPassword(final User user, final String password) {
        user.setPassword(createPasswordHash(password));
        updateUser(user);
    }

    public void deleteUser(User user) {
        userRepository.deleteUser(user);
    }

    public Collection<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Optional<User> findUserByUsername(final String username) {
        return userRepository.findUserByUsername(username);
    }

    public boolean userExists(final String username, final String password) {
        return userRepository.findUserByUsername(username)
                .stream()
                .anyMatch(user -> validatePassword(password, user.getPassword()));
    }

    private static String createPasswordHash(String password) {
        char[] chars = password.toCharArray();
        try {
            byte[] salt = getSalt();
            PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERATION_COUNT, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return ITERATION_COUNT + SEPARATOR + toHex(salt) + SEPARATOR + toHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[SALT_LENGTH];
        sr.nextBytes(salt);
        return salt;
    }

    private static boolean validatePassword(String input, String storedPassword) {
        String[] parts = storedPassword.split(SEPARATOR);
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(input.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for (int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }

    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(RADIX);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), RADIX);
        }
        return bytes;
    }

}
