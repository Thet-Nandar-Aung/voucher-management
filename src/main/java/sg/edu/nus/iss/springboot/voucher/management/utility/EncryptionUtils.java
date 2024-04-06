package sg.edu.nus.iss.springboot.voucher.management.utility;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtils {

	@Value("${aes.secret.key}")
	private String aesSecretKey;
	
	private static final String AES_ALGORITHM = "AES";
	
	public String decrypt(String encryptedCode) throws Exception {
		byte[] bytekey = hexStringToByteArray(aesSecretKey.trim());
		SecretKeySpec sks = new SecretKeySpec(bytekey, AES_ALGORITHM);
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, sks);
		byte[] decrypted = cipher.doFinal(hexStringToByteArray(encryptedCode));
		String OriginalPassword = new String(decrypted);
		return OriginalPassword;
	}

	public String encrypt(String code) throws Exception {
		byte[] bytekey = hexStringToByteArray(aesSecretKey.trim());
		SecretKeySpec sks = new SecretKeySpec(bytekey,AES_ALGORITHM);
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
		byte[] encrypted = cipher.doFinal(code.getBytes());
		String encryptedpwd = byteArrayToHexString(encrypted);
		return encryptedpwd;
	}
	
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	private static byte[] hexStringToByteArray(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}
	/*
	public static void main(String[] args) throws Exception {
		EncryptionUtils util = new EncryptionUtils();
		String verificationCode = "4E5FCA157F8CEC4E6A351A349C08AC05896D21C97F102BBE318A70314B651E46BB23B575199E2A55720380070701C43D";
        String decodedVerificationCode = "7f03a9a9-d7a5-4742-bc85-68d52b2bee45";
        try {
			String decodedVerificationCode1 = util.encrypt(decodedVerificationCode);
			System.out.println(decodedVerificationCode1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

}
