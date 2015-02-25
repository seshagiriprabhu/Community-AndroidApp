package com.project.communityorganizer.Services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by
 * @author seshagiri on 23/2/15.
 */
public class passwordHash {

    /**
     * A function to calculate the SHA-512 hash of a given message
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public String findHash(String message) throws NoSuchAlgorithmException {
        MessageDigest md;
        try {
            md= MessageDigest.getInstance("SHA-512");

            md.update(message.getBytes());
            byte[] mb = md.digest();
            String out = "";
            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
                String s = Integer.toHexString(temp);
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                out += s;
            }
            return out;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return null;
    }
}
