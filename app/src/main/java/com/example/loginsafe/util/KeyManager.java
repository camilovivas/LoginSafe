package com.example.loginsafe.util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class KeyManager {

    public static final int SALT_BYTES = 24;
    public static final int HASH_BYTES = 256;
    public static final int PBKDF2_ITERATIONS = 1000;

    public boolean validate(String origin, String pass) throws InvalidKeySpecException, NoSuchAlgorithmException {
        char[] split = origin.toCharArray();
        String salt = "";
        String hash = "";
        //sacar salt
        for (int i = 4; i<52; i++){
            salt += split[i];
        }
        //sacar hash
        for (int i =52; i<split.length;i++){
            hash += split[i];
        }

        byte[] saltOrigin = fromHex(salt);
        byte[] hashOrigin = fromHex(hash);
        byte[] testHash = pbkdf2(pass.toCharArray(),saltOrigin,PBKDF2_ITERATIONS,HASH_BYTES);

        return slowEquals(hashOrigin,testHash);
    }

    public String generatePassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte [] salt = salting();
        byte [] hash = pbkdf2(password.toCharArray(),salt,PBKDF2_ITERATIONS,HASH_BYTES);
        return PBKDF2_ITERATIONS + toHex(salt) + toHex(hash);
    }

    public byte[] salting() throws NoSuchAlgorithmException {
        // Generate a random salt
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        return salt;
    }

    private boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    public byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return skf.generateSecret(spec).getEncoded();
    }

    public String toHex(byte[] array){
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    private byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

}
