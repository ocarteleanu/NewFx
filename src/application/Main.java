package application;
	

import java.util.ArrayList;
import java.util.Collections;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import textanalyzer.*;


/**
 * Main program features a GUI that allows users to get displayed the words
 * of "The Raven" poem by Edgar Poe, 20 per page. The words can be hidden and also 
 * the user can navigate from one 20-word block to another. The program uses classes 
 * defined in the textanalyzer package.
 * @author octavian
 * @version 1.0
 */
public class Main extends Application {
	/**
	 * The elements of the GUI are defined as variables such as buttons.
	 * Also the other variables define the lists that store and sort the words in the poem
	 */
	private static Text poemTitle, poemWords;	
	private Button button, buttonTwo, buttonNext;
	private static ArrayList <String> allWords;
	//define a 2-dimension String array to help displaying blocks of 20 words
	private static String[][] blocksOfWords= new String[25][20];
	private static String theText = "";
	private static int nextDisplay = 0;
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Text Analyzer in JavaFx");			
			button = new Button();
			buttonTwo = new Button();
			buttonNext = new Button();
			button.setText("Display the words");
			button.setLayoutX(120);
			button.setLayoutY(180);
			button.setDisable(false);
			buttonTwo.setText("Hide the words");
			buttonTwo.setLayoutX(250);
			buttonTwo.setLayoutY(580);
			buttonTwo.setDisable(false);
			buttonNext.setText("Next words");
			buttonNext.setLayoutX(120);
			buttonNext.setLayoutY(180);
			buttonNext.setDisable(false);
			allWords = TextAnalyzer.trimPunctuation(TextAnalyzer.cleanArrayNoTags(TextAnalyzer.fileManager()));
			ArrayList<String> listOfUniqueWords = new ArrayList<>();
			ArrayList<PairOfWordsAndOccurrences> listOfPairs = new ArrayList<>();
			for(String eachWord : allWords){
				if(!listOfUniqueWords.contains(eachWord)){
					listOfUniqueWords.add(eachWord);
				}
			}
			int[] occurrenceCounter = new int[allWords.size() - 1];
			for(int j = 0; j < allWords.size() - 1; j++){
				occurrenceCounter[j] = 0;
			}
			for(String uniqueWord : listOfUniqueWords){
				for(int i = 0; i < allWords.size() - 1; i++){
					if(uniqueWord.compareTo(allWords.get(i)) == 0){
						occurrenceCounter[listOfUniqueWords.indexOf(uniqueWord)] ++;
					}
				}
				PairOfWordsAndOccurrences newPair = new PairOfWordsAndOccurrences(uniqueWord, 
						occurrenceCounter[listOfUniqueWords.indexOf(uniqueWord)]);				
				listOfPairs.add(newPair);			
			}
			//sort the pairs array list in descending order of the word frequency
			Collections.sort(listOfPairs, new SortingTool());
			//define and initiate the Text elements of the GUI
			poemTitle = new Text();
			poemWords = new Text();
			poemTitle.setText("Edgar Poe - The Raven");
			poemTitle.setLayoutX(60);
			poemTitle.setLayoutY(50);
			poemTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
			poemTitle.setFill(Color.STEELBLUE);
			poemWords.setLayoutX(60);
			//poemWords.setLayoutY(50);
			poemWords.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
			poemWords.setFill(Color.BLACK);
			for(int c = 0; c < 9; c++){
				listOfPairs.add(new PairOfWordsAndOccurrences(" ", 0));
			}
			int i = 0;			
			while(i < listOfPairs.size()){
				for(int a = 0; a < 25; a++){
					for(int b = 0; b < 20; b++){
						blocksOfWords[a][b] = listOfPairs.get(i).word + "     " + listOfPairs.get(i).occurrence + " times";
						i++;
					}
				}
			}
			/**
			 * the setOnAction method controls the interaction between the user and the GUI
			 */
			button.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					theText = "\n";
					theText += "These are the words, by frequency, in the poem: \n";
					for (int y = 0; y < 20; y++){
						theText += blocksOfWords[0][y] + "\n";
					}
					nextDisplay++;
					poemWords.setText(theText);
					button.setDisable(true);
					buttonTwo.setDisable(false);
					buttonNext.setDisable(false);
				}
					
			});
			/**
			 * the setOnAction method controls the interaction between the user and the GUI
			 */
			buttonNext.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					theText = " ";
					theText = "\n";
					theText += "These are the words, by frequency, in the poem: \n";
					for (int y = 0; y < 20; y++){
						if(blocksOfWords[nextDisplay][y].compareTo("      0 times") != 0)
						theText += blocksOfWords[nextDisplay][y] + "\n";
					}
					poemWords.setText(theText);
					if(nextDisplay >= 24){
						nextDisplay = 0;
					}
					else {
						nextDisplay++;
					}
				}
					
			});
			/**
			 * the setOnAction method controls the interaction between the user and the GUI
			 */
			buttonTwo.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					theText = " ";
					poemWords.setText(theText);
					button.setDisable(false);
					buttonTwo.setDisable(true);
					buttonNext.setDisable(true);
					nextDisplay = 0;
				}
					
			});
			VBox mainBox = new VBox();
			HBox middleBox = new HBox();
			Pane titlePane = new Pane();	
			Pane listPane = new Pane();
			HBox bottomBox = new HBox();
			mainBox.setPrefSize(600, 600);
			mainBox.setStyle("-fx-background-color: #" + "ffe6cc;");
			middleBox.setPrefSize(600, 35);
			middleBox.setStyle("-fx-background-color: #" + "99ff33;");
			middleBox.setPadding(new Insets(0, 0, 0, 180));
			titlePane.setPrefSize(600, 100);
			titlePane.setStyle("-fx-background-color: #" + "ffaa80;");
			listPane.setPrefSize(600, 430);
			listPane.setStyle("-fx-background-color: #" + "e6e600;");
			bottomBox.setStyle("-fx-background-color: #" + "99ff33;");
			bottomBox.setPadding(new Insets(0, 0, 0, 240));
			bottomBox.setPrefSize(600, 35);
			listPane.getChildren().add(poemWords);
			middleBox.getChildren().addAll(button, buttonNext);
			bottomBox.getChildren().add(buttonTwo);
			titlePane.getChildren().add(poemTitle);
			mainBox.getChildren().addAll(titlePane, middleBox, listPane, bottomBox);
			Scene scene = new Scene(mainBox,600,600);			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param args String array type used in the main method
	 * It calls the launch method that will trigger displaying the GUI
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
