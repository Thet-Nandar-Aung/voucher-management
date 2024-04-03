package sg.edu.nus.iss.springboot.voucher.management.utility;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class AESKeyGenerator {
/*
    public static void main(String[] args) {
        try {
            // Create a KeyGenerator instance for AES
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");

            // Initialize the KeyGenerator with the desired key size (in bits)
            // For example, 128, 192, or 256 bits
            keyGen.init(256); // Use a key size of 256 bits for stronger security

            // Generate the AES secret key
            SecretKey secretKey = keyGen.generateKey();

            // Print the encoded form of the secret key
            byte[] encodedKey = secretKey.getEncoded();
            System.out.println("Encoded Key (Hex): " + bytesToHex(encodedKey));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("AES algorithm not available: " + e.getMessage());
        }
    }
*/
    // Helper method to convert byte array to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
}
