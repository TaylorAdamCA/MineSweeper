package Game;

import java.util.Random;

import javax.swing.GroupLayout.Alignment;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Play extends Application {
	static Tile[][] tile;
	static int gameTime;
	static GridPane timeTopBar = new GridPane();
	static GridPane mines = new GridPane();
	static Random rand = new Random();
	static BorderPane bg = new BorderPane();
	static BorderPane bp = new BorderPane();
	static GridPane gamePane = new GridPane();
	static BorderPane topBar = new BorderPane();
	static MenuBar menuBar = new MenuBar();
//	static Menu file = new Menu("File");
//	static Menu edit = new Menu("Edit");
	static Menu newgame = new Menu("NewGame");
	static int numMine = 0;
	static int numMineFlags;
	static int difficulty = -1;
	static int numMines;
	static boolean firstClick = true;
	static int firstClickY = 0;
	static int firstClickX = 0;
	static int minesRevealed = 0;
	static int tilesRevealed = 0;
	static Stage theStage;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage theStage) {
		Play.theStage = theStage;
		TopTimer toptimer = new TopTimer();

		ImageView dead, win, score, time;
		MenuItem easy = new MenuItem("easy");
		newgame.getItems().add(easy);
		MenuItem medium = new MenuItem("medium");
		newgame.getItems().add(medium);
		MenuItem hard = new MenuItem("hard");
		newgame.getItems().add(hard);
		if (difficulty < 0 || difficulty > 2) {
			difficulty = 0;
			newGame();

		}

		menuBar.getMenus().addAll(newgame);

		easy.setOnAction(e -> {
			difficulty = 0;
			newGame();
			theStage.sizeToScene();
			
		});

		medium.setOnAction(e -> {
			difficulty = 1;
			newGame();
			theStage.sizeToScene();
			
		});

		hard.setOnAction(e -> {
			difficulty = 2;
			newGame();
			theStage.sizeToScene();
		});
		
		if(theStage.getScene() != null)
			theStage.setScene(null);
		
		
		theStage.setTitle("Minesweeper");
		theStage.setScene(new Scene(bg));
		theStage.show();

	}

	private static void setTiles() {
		int size = difficulty(difficulty);
		if (size == 32) {
			tile = new Tile[size / 2][size];
		} else {
			tile = new Tile[size][size];

		}

	}

	private static void newGame() {
		
		firstClick = true;
		firstClickX = 0;
		firstClickY = 0;
		minesRevealed = 0;
		setTiles();
		initTiles();
		initBoard();
		clear();

	
		
	}

	public static int difficulty(int x) {
		if (x == 0)
			return 8;
		else if (x == 1)
			return 16;
		else if (x == 2)
			return 32;
		else
			return 0;
	}

	public static void clear() {
		minesRevealed = 0;
		tilesRevealed = 0;
		firstClick = true;

	}

	private static void initTiles() {
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[0].length; j++) {
				tile[i][j] = new Tile(i, j);
				gamePane.add(tile[i][j], j, i);
			}
		}

	}

	public static void getFirstClick(int x, int y) {
		firstClickY = y;
		firstClickX = x;

	}

	public static int setMines(int diff) {
		int y = firstClickY;
		int x = firstClickX;
		int[][] no = new int[tile.length][tile[0].length];
		no[x][y] = 1;

		if (y > 0) {
			no[x][y - 1] = 1;
		}
		if (y > 0 && x > 0) {
			no[x - 1][y - 1] = 1;
		}
		if (y > 0 && x < tile.length - 1) {
			no[x + 1][y - 1] = 1;
		}
		if (x > 0 && y < tile[0].length - 1) {
			no[x - 1][y + 1] = 1;
		}
		if (y < tile[0].length - 1 && x < tile.length - 1) {
			no[x + 1][y + 1] = 1;
		}
		if (x > 0) {
			no[x - 1][y] = 1;
		}
		if (x < tile.length - 1) {
			no[x + 1][y] = 1;
		}
		if (y < tile[0].length - 1) {
			no[x][y + 1] = 1;
		}

		if (diff == 0)
			numMines = 10;
		else if (diff == 1)
			numMines = 40;
		else if (diff == 2)
			numMines = 99;

		numMineFlags = numMines;
		for (int i = 0; i < numMines; i++) {
			int rand1 = 0;
			if (difficulty == 2)
				rand1 = rand.nextInt(difficulty(difficulty / 2));
			else
				rand1 = rand.nextInt(difficulty(difficulty));

			int rand2 = rand.nextInt(difficulty(difficulty));

			if (no[rand1][rand2] == 1) {

				i--;

				continue;
			} else if (tile[rand1][rand2].isMine()) {
				i--;

			}
			tile[rand1][rand2].setMine();

		}
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[0].length; j++) {
				System.out.print(tile[i][j].getState());
			}
			System.out.println();
			;
		}
		return numMines;

	}

	private static void initBoard() {
		gameTime = 0;

		if (tile[0].length == 32)
			numMine = 99;
		else if (tile[0].length == 16)
			numMine = 40;
		else
			numMine = 10;

		
		mines.add(new ImageView(new Image("res/Digits/" + (0) + ".png")), 0, 0);
		mines.add(new ImageView(new Image("res/Digits/" + (0) + ".png")), 1, 0);
		
		mines.add(new ImageView(new Image("res/Digits/" + 0 + ".png")), 2, 0);
		

	
		topBar.setLeft(mines);
		setTopBar("face-smile.png");
		bp.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 2;" +

				"-fx-border-radius: 5;" + "-fx-border-color: darkgrey white white darkgrey;" + "-fx-background-color: white;"
				+ "-fx-effect: innershadow(three-pass-box, grey, 10, 0.7, 0, 0);");
		topBar.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 2;" +

				"-fx-border-radius: 5;" + "-fx-border-color: grey white white grey;" + "-fx-background-color: lightgrey;"
				);
		menuBar.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 2;" +

				"-fx-border-radius: 5;" + "-fx-border-color: grey white white grey;" + "-fx-background-color: lightgrey;"
				);
		
				
				
			
		
		
		bg.setStyle("-fx-border-style: solid inside;" + "-fx-border-width:4;" +

				"-fx-border-radius: 5;" + "-fx-border-color: darkgrey white white darkgrey;" + "-fx-background-color:	lightgrey;"
				+ "-fx-effect: innershadow(three-pass-box, darkgrey, 0, 0.7, 0, 0);");
		bp.setCenter(gamePane);
		bp.setTop(topBar);
		bg.setCenter(bp);
		bg.setTop(menuBar);

		bg.setPadding(new Insets(10, 10, 10, 10));
		bg.requestFocus();
		// start(theStage);

	}

	public static Tile[][] getTile() {
		return tile;
	}

	public static void setTopBar(String s) {

		ImageView topBarIMG = new ImageView(new Image("/res/" + s));
		topBarIMG.setOnMouseClicked(e -> {
			newGame();
		});
		topBar.setCenter(topBarIMG);

	}

	static void addMine() {
		numMineFlags++;
	}

	static int mineCount() {
		return numMine;
	}

	static void removeMine() {
		numMineFlags--;
	}

	// static void setTimerBar() {
	// gameTime++;
	// timeTopBar.setText(String.format("%1$03d", gameTime));
	// timeTopBar.setTextAlignment(TextAlignment.CENTER);
	// timeTopBar.autosize();
	//
	// topBar.setLeft(timeTopBar);
	// mines.setText(String.format("%1$03d",numMineFlags));
	// mines.autosize();
	// topBar.setRight(mines);
	// }
	//
	static void setTimerBarImg() {
		gameTime++;
		if (gameTime % 100 == 0) {
			timeTopBar.add(new ImageView(new Image("res/Digits/" + (gameTime / 100) % 100 + ".png")), 0, 0);
		} else if (gameTime < 100) {
			timeTopBar.add(new ImageView(new Image("res/Digits/" + 0 + ".png")), 0, 0);
		}
		if (gameTime % 10 == 0) {
			timeTopBar.add(new ImageView(new Image("res/Digits/" + (gameTime / 10) % 10 + ".png")), 1, 0);
		} else if (gameTime < 10) {
			timeTopBar.add(new ImageView(new Image("res/Digits/" + 0 + ".png")), 1, 0);
		}
		timeTopBar.add(new ImageView(new Image("res/Digits/" + (gameTime % 10) + ".png")), 3, 0);
		topBar.setRight(timeTopBar);

		mines.add(new ImageView(new Image("res/Digits/" + (0) + ".png")), 0, 0);
		if (numMineFlags >= 10) {
			mines.add(new ImageView(new Image("res/Digits/" + (numMineFlags / 10) % 10 + ".png")), 1, 0);
		} else {
			mines.add(new ImageView(new Image("res/Digits/" + 0 + ".png")), 1, 0);
		}

		mines.add(new ImageView(new Image("res/Digits/" + (numMineFlags % 10) + ".png")), 2, 0);
		topBar.setLeft(mines);
	}

}

class TopTimer {
	private Timeline animation;
	private int gameTime = 0;

	public TopTimer() {
		animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> Play.setTimerBarImg()));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
	}

}
