package ir.annotation.waiter.procedure;

import io.netty.util.CharsetUtil;
import org.junit.Test;

import javax.crypto.KeyAgreement;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class KeyExchangerTest {

    @Test
    public void testProcedure() throws Exception {
        // Alice side, generates key pair.
        var aliceKeyPairGenerator = KeyPairGenerator.getInstance(KeyExchanger.KeyExchangeRequest.Algorithm.DiffieHellman.name());
        aliceKeyPairGenerator.initialize(1024, new SecureRandom());
        var aliceKeyPair = aliceKeyPairGenerator.generateKeyPair();

        // Bob Side, receives alice's public key.
        var keyExchanger = new KeyExchanger(new PublicPrivateKeyPairGenerator());
        var bobKeyExchange = keyExchanger.apply(Executors.newSingleThreadExecutor(), new KeyExchanger.KeyExchangeRequest(
                KeyExchanger.KeyExchangeRequest.Algorithm.DiffieHellman,
                KeyExchanger.KeyExchangeRequest.KeySize._1024,
                aliceKeyPair.getPublic()
        )).get().get();


        // Alice Side, receives bob's public key.
        var keyAgreement = KeyAgreement.getInstance(KeyExchanger.KeyExchangeRequest.Algorithm.DiffieHellman.name());
        keyAgreement.init(aliceKeyPair.getPrivate(), new SecureRandom());
        keyAgreement.doPhase(bobKeyExchange.getPublicKey(), true);
        var sharedSecretKey = keyAgreement.generateSecret();

        assertEquals(
                new String(Base64.getEncoder().encode(bobKeyExchange.getSecretKey()), CharsetUtil.UTF_8),
                new String(Base64.getEncoder().encode(sharedSecretKey), CharsetUtil.UTF_8)
        );
    }
}
