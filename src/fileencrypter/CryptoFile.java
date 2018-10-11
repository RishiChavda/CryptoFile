package fileencrypter;

import java.io.File;

import javax.crypto.Cipher;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CryptoFile extends Application {
	File plainFile;
	
    static SecretKeySpec key = new SecretKeySpec("skeyskeyskeyskey".getBytes(), "AES");
    static IvParameterSpec iv = new IvParameterSpec("skeyskeyskeyskey".getBytes());
	
    private static String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS7Padding", new BouncyCastleProvider());

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return new String(cipher.doFinal(plaintext.getBytes()));
    }

    private static String decrypt(String ctext) throws Exception {
    	Cipher cipher = Cipher.getInstance("AES/CFB/PKCS7PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(ctext.getBytes()));
    }
    
    public static void main(String[] args) {
    	launch(args);
    	try {
    		String key = "skeyskey";
			String message = "thisismymessagethisismssagethisismymessage";
			
			System.out.println(encrypt("hello"));
			System.out.println("AES " + Cipher.getMaxAllowedKeyLength("AES"));
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
	}
    
    public File encryptFile(File plainFile) {
    	
    	
    	return null;
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
					if(plainFile!=null) {
						System.out.println(plainFile.getName());
						encryptFile(plainFile);
					}
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
			pane.add(keyInputTxt, 3, 4);
			
			Label methodInputLbl = new Label("Choose method: ");
			methodInputLbl.setFont(formFont);
			pane.add(methodInputLbl, 2, 5);
			
			ComboBox<String> methodInputCB = new ComboBox();
			methodInputCB.getItems().addAll("AES", "Serpent", "Blowfish", "RC5");
			methodInputCB.setStyle("-fx-font: 20.0px 'Times New Roman';");
			pane.add(methodInputCB, 3, 5);
			
			Label keySizeLbl = new Label("Choose key size: ");
			keySizeLbl.setFont(formFont);
			pane.add(keySizeLbl, 2, 6);
			
			ToggleGroup keySizeGroup = new ToggleGroup();
			RadioButton rb128 = new RadioButton("128-bit");
			rb128.setFont(formFont);
			rb128.setToggleGroup(keySizeGroup);
			rb128.setSelected(true);
			RadioButton rb256 = new RadioButton("256-bit");
			rb256.setFont(formFont);
			rb256.setToggleGroup(keySizeGroup);
			pane.add(rb128, 3, 6);
			pane.add(rb256, 3, 7);
			
			HBox btnBox = new HBox(10);
			Button encryptBtn = new Button("Encrypt");
			encryptBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					
				}
			});
			encryptBtn.setFont(formFont);
			Button decryptBtn = new Button("Decrypt");
			encryptBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					
				}
			});
			decryptBtn.setFont(formFont);
			btnBox.getChildren().addAll(encryptBtn, decryptBtn);
			pane.add(btnBox, 3, 9);
			
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