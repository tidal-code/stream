package com.tidal.stream.encryptor;

import org.jasypt.util.text.AES256TextEncryptor;

import java.util.logging.Logger;

import static com.google.common.base.Strings.isNullOrEmpty;

public class Decryptor {

    private Decryptor() {
    }

    static Logger log = Logger.getLogger(Decryptor.class.getName());

    public static String decrypt(String secret, String key) {
        String decryptedKey;

        if (isNullOrEmpty(secret)) {
            throw new DecryptorException("Secret value is null or empty. Please check your env variables to see " +
                    "if there is a key value pair with key " + key);
        }
        if (isNullOrEmpty(key)) {
            throw new DecryptorException("You haven't provided a secret key name. This key is used to assign " +
                    "a secret value that is stored in your env variable");
        }

        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(secret);

        try {
            decryptedKey = aes256TextEncryptor.decrypt(key.replace("ENC", ""));
            log.info("Your value has been retrieved and decrypted");
            return decryptedKey;
        } catch (Exception e) {
            throw new DecryptorException("Cannot decrypt your key, please supply correct secret and key values");
        }

    }
}
