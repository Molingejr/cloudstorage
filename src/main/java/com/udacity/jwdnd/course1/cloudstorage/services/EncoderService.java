package com.udacity.jwdnd.course1.cloudstorage.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncoderService {
    private Logger logger = LoggerFactory.getLogger(EncryptionService.class);

    public String encodeValue() {
        byte[] key = new byte[16];
        try {
            SecureRandom random = new SecureRandom();
            random.nextBytes(key);
        }catch(Exception e){
                logger.error(e.getMessage());
        }
        return Base64.getEncoder().encodeToString(key);
    }
}
