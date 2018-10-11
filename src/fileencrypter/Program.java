package fileencrypter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Program {
    static SecretKeySpec key = new SecretKeySpec("skeyskeyskeyskey".getBytes(), "AES");
    static IvParameterSpec iv = new IvParameterSpec("skeyskeyskeyskey".getBytes());
	
    private static String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", new BouncyCastleProvider());

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return new String(cipher.doFinal(plaintext.getBytes()));
    }

    private static String decrypt(String ctext) throws Exception {
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(ctext.getBytes()));
    }
    
    public static void main(String[] args) {
    	try {
    		String key = "skeyskey";
			String message = "thisismymessagethisismymessage";
			
			System.out.println(encrypt("hello"));
			System.out.println("AES " + Cipher.getMaxAllowedKeyLength("AES"));
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
	}
	

}