/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.utility;

import com.sun.javafx.scene.layout.region.Margins.Converter;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import static org.apache.tomcat.jni.Shm.buffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 *
 * @author Aleks
 */
@Component
public class EncryptionUtil {

    
    private Environment env;

    private SecretKeySpec skeySpec;

    @Autowired
    public EncryptionUtil(Environment env) {
        this.env = env;
        String key = this.env.getProperty("aes.key");
        this.skeySpec = new SecretKeySpec(key.getBytes(), "AES");
    }

  
    public byte[] encrypt(String input)
            throws GeneralSecurityException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(input.getBytes());
    }

    public String decrypt(byte[] input) throws GeneralSecurityException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return new String(cipher.doFinal(input));
    }

}
