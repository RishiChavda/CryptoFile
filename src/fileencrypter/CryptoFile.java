package fileencrypter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
    
    public void cryptofile(File plainFile, String key, String method, String direction)
    		throws IncorrectCryptoException, 
    			NoSuchPaddingException,
    			NoSuchAlgorithmException, 
    			InvalidKeyException, 
    			IOException{
    	
        Cipher cipher = Cipher.getInstance(method+"/CTR/PKCS7Padding", new BouncyCastleProvider());
		cipher.init((direction.equals("Encrypt") ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE), 
				new SecretKeySpec(key.getBytes("UTF-8"), method));
		
		FileInputStream fileInput= new FileInputStream(plainFile);
		FileOutputStream fileOutput = new FileOutputStream((direction.equals("Encrypt")?"CIPHER":"PLAIN")
				+ plainFile.getName());
		CipherInputStream cis = new CipherInputStream(fileInput, cipher);
		CipherOutputStream cos = new CipherOutputStream(fileOutput, cipher);
		
		System.out.println(plainFile.getName() + " encrypt with " + key + " " + method + " " + direction);
		
		InputStreamReader isr = new InputStreamReader(fileInput);
		BufferedReader reader = new BufferedReader(isr);
		String line;
		while((line=reader.readLine()) != null) {
			cos.write(line.getBytes("UTF-8"));
		}
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
			
			Label chooseFileLbl = new Label("Choose a file: ");
			chooseFileLbl.setFont(formFont);
			pane.add(chooseFileLbl, 2, 3);
			
			Button chooseFileBtn = new Button("Select file...");
			chooseFileBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					plainFile = fileWindow.showOpenDialog(window);
				}
			});
			chooseFileBtn.setFont(formFont);
			pane.add(chooseFileBtn, 3, 3);
			
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
						cryptofile(
							plainFile,
							keyInputTxt.getText(),
							methodInputCB.getValue(),
							directionInputCB.getValue()
						);
					}
					catch(IncorrectCryptoException | InvalidKeyException | NoSuchPaddingException | 
							NoSuchAlgorithmException | IOException ice) {
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