package fileencrypter;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CryptoFile extends Application {
	File plainFile;
	
	public static void main(String[] args) {
    	launch(args);
	}
    
    public void encryptFile(File inputFile, String key, String method)
    		throws IncorrectCryptoException, 
    			NoSuchPaddingException,
    			NoSuchAlgorithmException, 
    			InvalidKeyException, 
    			IOException, 
    			InvalidAlgorithmParameterException{
    	
    	// check method is correct
    	System.out.println(plainFile.getName() + " ENCRYPT with '" + key + "' " + method);
		String newFileName = plainFile.getAbsolutePath() + ".encrypted";
		
    	byte[] iv = new byte[16];
        Cipher fCipher = Cipher.getInstance(method+"/CTR/NoPadding", new BouncyCastleProvider());
		fCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), method), 
				new IvParameterSpec(iv));
		
		// change below depending on ENCRYPT_MODE/DECRYPT_MODE
		FileInputStream fis = new FileInputStream(inputFile);
		CipherInputStream cis = new CipherInputStream(fis, fCipher);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] b = new byte[1024];
		int bytesRead = 0;
		while((bytesRead = cis.read(b)) >= 0) {
			baos.write(b, 0, bytesRead);
		}
		FileOutputStream fos = new FileOutputStream(new File(newFileName));
		fos.write(baos.toByteArray());
		fos.close();
	}
    
    public void decryptFile(File inputFile, String key, String method)
    		throws IncorrectCryptoException, 
    			NoSuchPaddingException,
    			NoSuchAlgorithmException, 
    			InvalidKeyException, 
    			IOException, 
    			InvalidAlgorithmParameterException{
    	
    	// check method is correct
    	System.out.println(plainFile.getName() + " DECRYPT with '" + key + "' " + method);
		String newFileName = plainFile.getAbsolutePath() + ".decrypted";
		
    	byte[] iv = new byte[16];
        Cipher fCipher = Cipher.getInstance(method+"/CTR/NoPadding", new BouncyCastleProvider());
		fCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), method), 
				new IvParameterSpec(iv));
		
		// change below depending on ENCRYPT_MODE/DECRYPT_MODE
		FileInputStream fis = new FileInputStream(inputFile);
		CipherInputStream cis = new CipherInputStream(fis, fCipher);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] b = new byte[1024];
		int bytesRead = 0;
		while((bytesRead = cis.read(b)) >= 0) {
			baos.write(b, 0, bytesRead);
		}
		FileOutputStream fos = new FileOutputStream(new File(newFileName));
		fos.write(baos.toByteArray());
		fos.close();
	}



	@Override
	public void start(Stage window) throws Exception {
		try {
			// File stuff
			FileChooser fileWindow = new FileChooser();
			Font formFont = new Font("Times New Roman", 20.0);
			
			// GUI stuff
			GridPane pane = new GridPane();
			pane.setVgap(10);
			pane.setHgap(10);
			pane.setPadding(new Insets(10,10,10,10));
			
			Label windowTitle = new Label("CryptoFile");
			windowTitle.setFont(new Font("Times New Roman", 30.0));
			window.setTitle("CryptoFile");
			pane.add(windowTitle, 3, 0);
			
			Label chooseInputFileLbl = new Label("Choose a file: ");
			chooseInputFileLbl.setFont(formFont);
			pane.add(chooseInputFileLbl, 2, 3);
			
			Button chooseFileBtn = new Button("Select input file...");
			chooseFileBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					plainFile = fileWindow.showOpenDialog(window);
				}
			});
			chooseFileBtn.setFont(formFont);
			pane.add(chooseFileBtn, 3, 3);
			
			Label chooseOutputFileLbl = new Label("Select output file...");
			
			Label keyInputLbl = new Label("Enter key: ");
			keyInputLbl.setFont(formFont);
			pane.add(keyInputLbl, 2, 4);
			
			TextField keyInputTxt = new TextField();
			keyInputTxt.setPromptText("e.g. asecretkey");
			keyInputTxt.setFont(formFont);
			keyInputTxt.setText("akeyakeyakeyakey"); // REMOVE THIS
			pane.add(keyInputTxt, 3, 4);
			
			Label methodInputLbl = new Label("Choose method: ");
			methodInputLbl.setFont(formFont);
			pane.add(methodInputLbl, 2, 5);
			
			ComboBox<String> methodInputCB = new ComboBox();
			methodInputCB.getItems().addAll("AES", "Serpent", "Blowfish", "RC5");
			methodInputCB.setStyle("-fx-font: 20.0px 'Times New Roman';");
			pane.add(methodInputCB, 3, 5);
			
			Label directionInputLbl = new Label("Direction: ");
			directionInputLbl.setFont(formFont);
			pane.add(directionInputLbl, 2, 6);
			
			ComboBox<String> directionInputCB = new ComboBox();
			directionInputCB.getItems().addAll("Encrypt", "Decrypt");
			directionInputCB.setStyle("-fx-font: 20.0px 'Times New Roman';");
			pane.add(directionInputCB, 3, 6);
			
			Button runBtn = new Button("Run");
			runBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						if(directionInputCB.getValue().equals("Encrypt")) {
							encryptFile(
								plainFile,
								keyInputTxt.getText(),
								methodInputCB.getValue()
								);
						}
						else if(directionInputCB.getValue().equals("Decrypt")) {
							decryptFile(
								plainFile,
								keyInputTxt.getText(),
								methodInputCB.getValue()
							);
						}
					}
					catch(IncorrectCryptoException | InvalidKeyException | NoSuchPaddingException | 
							NoSuchAlgorithmException | IOException | InvalidAlgorithmParameterException ice) {
						ice.printStackTrace();
					}
				}
			});
			runBtn.setFont(formFont);
			
			pane.add(runBtn, 3, 12);
			
			Scene screen = new Scene(pane , 500, 500);
			window.setScene(screen);
			window.setResizable(false);
			window.show();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}