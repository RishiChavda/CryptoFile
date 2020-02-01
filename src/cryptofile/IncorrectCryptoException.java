package fileencrypter;

public class IncorrectCryptoException extends Exception{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void printStackTrace() {
		System.err.println("Key size incorrect - must match algorithm's allowed key sizes");
		super.printStackTrace();
	}

}
