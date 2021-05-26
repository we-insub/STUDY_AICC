/*
 * AICC (AI Caption Center) version 1.0
 *
 *  Copyright ⓒ 2020 sorizava corp. All rights reserved.
 *
 *  This is a proprietary software of sorizava corp, and you may not use this file except in
 *  compliance with license agreement with sorizava corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of sorizava corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *
 * @package kr.co.aicc.infra.util
 * @file StringCryptoConverter.java
 * @author developer
 * @since 2020. 06. 08
 * @version 1.0
 * @title AICC
 * @description
 *
 *
 * << 개정이력 (Modification Information) >>
 *
 *   수정일		수정자	수정내용
 * -------------------------------------------------------------------------------
 * 2020. 08. 26	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.infra.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class StringCryptoConverter {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final byte[] KEY = "ZGCk4KtxSmipJrTy".getBytes(); // 16, 24, 32 Byte

    public static String convertToDatabaseColumn(String attribute) {
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(Base64.getEncoder().encode(cipher.doFinal(attribute.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertToEntityAttribute(String dbData) {
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
//        String str = "안녕하세요. abcdebf 123$afadfafda";
        String str = "12345678901234---";

        StringCryptoConverter stringCryptoConverter = new StringCryptoConverter();
        String s = stringCryptoConverter.convertToDatabaseColumn(str);
        System.out.println("s = " + s);

        String s1 = stringCryptoConverter.convertToEntityAttribute(s);
        System.out.println("s1 = " + s1);
    }
}


