package com.aniruddhc.acemusic.player.InAppBilling;

import org.junit.Test;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Public key and private key generated using http://phpseclib.sourceforge.net/rsa/examples.html.
 *
 * Signature calculated using https://8gwifi.org/rsasignverifyfunctions.jsp.
 */
public class SecurityTest {

    private final String pubKeyString = "" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqGKukO1De7zhZj6+H0qtjTkVxwTCpvKe4eCZ0" +
            "FPqri0cb2JZfXJ/DgYSF6vUpwmJG8wVQZKjeGcjDOL5UlsuusFncCzWBQ7RKNUSesmQRMSGkVb1/" +
            "3j+skZ6UtW+5u09lHNsj6tQ51s1SPrCBkedbNf0Tp0GbMJDyR4e9T04ZZwIDAQAB";

    private final String privKeyString = "" +
            "MIICXAIBAAKBgQCqGKukO1De7zhZj6+H0qtjTkVxwTCpvKe4eCZ0FPqri0cb2JZfXJ/DgYSF6vUp\n" +
            "wmJG8wVQZKjeGcjDOL5UlsuusFncCzWBQ7RKNUSesmQRMSGkVb1/3j+skZ6UtW+5u09lHNsj6tQ5\n" +
            "1s1SPrCBkedbNf0Tp0GbMJDyR4e9T04ZZwIDAQABAoGAFijko56+qGyN8M0RVyaRAXz++xTqHBLh\n" +
            "3tx4VgMtrQ+WEgCjhoTwo23KMBAuJGSYnRmoBZM3lMfTKevIkAidPExvYCdm5dYq3XToLkkLv5L2\n" +
            "pIIVOFMDG+KESnAFV7l2c+cnzRMW0+b6f8mR1CJzZuxVLL6Q02fvLi55/mbSYxECQQDeAw6fiIQX\n" +
            "GukBI4eMZZt4nscy2o12KyYner3VpoeE+Np2q+Z3pvAMd/aNzQ/W9WaI+NRfcxUJrmfPwIGm63il\n" +
            "AkEAxCL5HQb2bQr4ByorcMWm/hEP2MZzROV73yF41hPsRC9m66KrheO9HPTJuo3/9s5p+sqGxOlF\n" +
            "L0NDt4SkosjgGwJAFklyR1uZ/wPJjj611cdBcztlPdqoxssQGnh85BzCj/u3WqBpE2vjvyyvyI5k\n" +
            "X6zk7S0ljKtt2jny2+00VsBerQJBAJGC1Mg5Oydo5NwD6BiROrPxGo2bpTbu/fhrT8ebHkTz2epl\n" +
            "U9VQQSQzY1oZMVX8i1m5WUTLPz2yLJIBQVdXqhMCQBGoiuSoSjafUhV7i1cEGpb88h5NBYZzWXGZ\n" +
            "37sJ5QsW+sJyoNde3xH8vdXhzU7eT82D6X/scw9RZz+/6rCJ4p0=" +
            "";

    private final BigInteger modulus = new BigInteger("119445732379544598056145200053932732" +
            "8778638467996523849895883037375273287439705598832111464872863171681422024" +
            "4695550890293603512470939722117866449572142802998472686837535916820328344" +
            "2617134197706515425366188396513684446494070223079865755643116690165578452" +
            "542158755074958452695530623055205290232290667934914919");

    private final BigInteger publicExponent = new BigInteger("65537");

    @Test
    public void whenGeneratePublicKey_readsKeyCorrectly() {
        // act
        RSAPublicKey publicKey = ((RSAPublicKey) Security.generatePublicKey(pubKeyString));

        // assert
        assertEquals("RSA", publicKey.getAlgorithm());

        assertEquals(publicExponent, publicKey.getPublicExponent());
        assertEquals(modulus, publicKey.getModulus());

        byte[] encoded = Base64.getDecoder().decode(pubKeyString.getBytes());
        assertArrayEquals(encoded, publicKey.getEncoded());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenGeneratePublicKey_incorrectlyFormattedKey_throwsException() {
        Security.generatePublicKey("incorrectly encoded public key");
    }

    @Test
    public void whenVerifyWithCorrectSignedDataAndSignature_returnsTrue() {
        String signedData = "this is my signed message. it's me!";
        String signature = "WFju3ImwMhpcfhrHCiwruXk/li+k96sPujIvzoR1V94bcuT1RDeATEZYXGPNXp" +
                "lBx2boDK3/D8e2uNrv4qTATkQBEmLJUdlilKXdeAPCcC9XlmAghW6QwWnKx9g6i2s0KAysufq" +
                "bJHmrfoVxZtfA4GvLcZaYW8jorSTnBTtOkXs=";

        PublicKey publicKey = Security.generatePublicKey(pubKeyString);
        assertTrue(Security.verify(publicKey, signedData, signature));
    }

    @Test
    public void whenVerifyWithWrongSignedDataAndSignature_returnsFalse() {
        String signedData = "this signed data differs from signature data";
        String signature = "WFju3ImwMhpcfhrHCiwruXk/li+k96sPujIvzoR1V94bcuT1RDeATEZYXGPNXp" +
                "lBx2boDK3/D8e2uNrv4qTATkQBEmLJUdlilKXdeAPCcC9XlmAghW6QwWnKx9g6i2s0KAysufq" +
                "bJHmrfoVxZtfA4GvLcZaYW8jorSTnBTtOkXs=";

        PublicKey publicKey = Security.generatePublicKey(pubKeyString);
        assertFalse(Security.verify(publicKey, signedData, signature));
    }
}