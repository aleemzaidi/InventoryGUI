package azaidi6.inventorygui.helper;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Generate a hashed password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 rounds of salt
    }

    // Verify password during login
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
