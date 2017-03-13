package Game;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class TileR extends Button {
	static TileR[][] tile;
	// 0 = unclicked, 1 = leftClick, 2 = Flagged, 3 = question mark, 4 = mine, 5
	// = redMIne
	private int state = 0;
	private boolean mine;
	int Xaxis = 0;
	int Yaxis = 0;
	ImageView unclicked, num0, num1, num2, num3, num4, num5, num6, num7, num8, flag, questionMark;
	ImageView mineRevealed;
	ImageView mineDeath;

	public TileR(int x, int y) {
		state = 0;
		this.Xaxis = x;
		this.Yaxis = y;
		mine = false;
		double size = 50;
		setMinWidth(size);
		setMaxWidth(size);
		setMaxHeight(size);
		setMinHeight(size);

		// Setting images
		unclicked = new ImageView(new Image("res/cover.png"));
		unclicked.fitHeightProperty().bind(this.heightProperty());
		unclicked.fitWidthProperty().bind(this.widthProperty());

		mineRevealed = new ImageView(new Image("res/mine-grey.png"));
		mineRevealed.fitHeightProperty().bind(this.heightProperty());
		mineRevealed.fitWidthProperty().bind(this.widthProperty());

		mineDeath = new ImageView(new Image("res/mine-red.png"));
		mineDeath.fitHeightProperty().bind(this.heightProperty());
		mineDeath.fitWidthProperty().bind(this.widthProperty());

		flag = new ImageView(new Image("res/flag.png"));
		flag.fitHeightProperty().bind(this.heightProperty());
		flag.fitWidthProperty().bind(this.widthProperty());

		questionMark = new ImageView(new Image("res/question.png"));
		questionMark.fitHeightProperty().bind(this.heightProperty());
		questionMark.fitWidthProperty().bind(this.widthProperty());

		num0 = new ImageView(new Image("res/0.png"));
		num0.fitHeightProperty().bind(this.heightProperty());
		num0.fitWidthProperty().bind(this.widthProperty());

		num1 = new ImageView(new Image("res/1.png"));
		num1.fitHeightProperty().bind(this.heightProperty());
		num1.fitWidthProperty().bind(this.widthProperty());

		num2 = new ImageView(new Image("res/2.png"));
		num2.fitHeightProperty().bind(this.heightProperty());
		num2.fitWidthProperty().bind(this.widthProperty());

		num3 = new ImageView(new Image("res/3.png"));
		num3.fitHeightProperty().bind(this.heightProperty());
		num3.fitWidthProperty().bind(this.widthProperty());

		num4 = new ImageView(new Image("res/4.png"));
		num4.fitHeightProperty().bind(this.heightProperty());
		num4.fitWidthProperty().bind(this.widthProperty());

		num5 = new ImageView(new Image("res/5.png"));
		num5.fitHeightProperty().bind(this.heightProperty());
		num5.fitWidthProperty().bind(this.widthProperty());

		num6 = new ImageView(new Image("res/6.png"));
		num6.fitHeightProperty().bind(this.heightProperty());
		num6.fitWidthProperty().bind(this.widthProperty());

		num7 = new ImageView(new Image("res/7.png"));
		num7.fitHeightProperty().bind(this.heightProperty());
		num7.fitWidthProperty().bind(this.widthProperty());

		num8 = new ImageView(new Image("res/8.png"));
		num8.fitHeightProperty().bind(this.heightProperty());
		num8.fitWidthProperty().bind(this.widthProperty());

		setGraphic(unclicked);

		setOnMouseClicked(e -> {
			//tile = Play.getTile();
			MouseButton button = e.getButton();
			if (button == MouseButton.PRIMARY) {
				if (Play.firstClick) {
					Play.firstClick = false;
					Play.getFirstClick(this.Xaxis, this.Yaxis);
					Play.setMines(Play.difficulty);
					firstClear(this.Xaxis, this.Yaxis);
					// Play.initBoardBg();

				}
				if (!disableClick() && !this.isFlag()) {
					if (this.isMine() && !disableClick()) {
						this.setGraphic(mineDeath);
						this.setState(5);
						Play.minesRevealed++;
						gameOver();
					} else {
						checkZero(this.Xaxis, this.Yaxis);
						if (this.getState() != 10)
							this.setState(1);
						checkMines();
					}

				}
			} else if (button == MouseButton.SECONDARY) {
				if (!Play.firstClick) {
					if (!this.isFlag() && Play.mineCount() > 0)
						this.setFlag();
					else if (this.isFlag())
						this.removeFlag();
				}
			}

		});

	}

	private void firstClear(int x, int y) {
		// SO NEAT AND CLEAN GOOD JOB
		System.out.println(x + "cccccccccccccccccccccccccccccccc" + y);
		if (x < 0 || x > tile.length - 1 || y < 0 || y > tile[0].length - 1 || tile[x][y].getState() != 0)
			return;

		tile[x][y].checkMines();
		if (!tile[x][y].isMine() && tile[x][y].getState() == 10) {
			tile[x][y].setState(1);
			System.out.print("X: " + tile[x][y].Xaxis + " Y:" + tile[x][y].Yaxis);
			if (y > 0) {
				firstClear(x, y - 1);
				if (x > 0)
					firstClear(x - 1, y - 1);
				if (x > tile.length)
					firstClear(x + 1, y - 1);
			}
			if (x > 0) {
				firstClear(x - 1, y);
				if (y < tile[0].length) {
					firstClear(x - 1, y + 1);

					if (x < tile[0].length)
						firstClear(x + 1, y + 1);
				}
			}
			if (x < tile.length)
				firstClear(x + 1, y);

			if (y < tile[0].length)
				firstClear(x, y + 1);

			tile[x][y].checkMines();
			return;

		} else {
			return;
		}
	}

	/*
	 * private void firstClear(int x, int y) { // Try returning firstClear if(x
	 * < 0 || x > tile[0].length || y < 0 || y > tile[0].length) return;
	 * 
	 * 
	 * if (y > 0 && !tile[x][y - 1].isMine() && tile[x][y-1].getState() ==0){
	 * firstClear(x, y - 1); //checkMines(x, y - 1); } if (y > 0 && x > 0 &&
	 * !tile[x - 1][y - 1].isMine()&& tile[x-1][y-1].getState() ==0) {
	 * firstClear(x - 1, y - 1); //checkMines(x - 1, y - 1); } if (y > 0 && x <
	 * tile.length - 1 && !tile[x + 1][y - 1].isMine()&&
	 * tile[x+1][y-1].getState() ==0) { firstClear(x + 1, y - 1); //checkMines(x
	 * + 1, y - 1); } if (x > 0 && y < tile[0].length - 1 && !tile[x - 1][y +
	 * 1].isMine()&& tile[x-1][y+1].getState() ==0) { firstClear(x - 1, y + 1);
	 * //checkMines(x - 1, y + 1); } if (y < tile[0].length - 1 && x <
	 * tile.length - 1 && !tile[x + 1][y + 1].isMine()&&
	 * tile[x+1][y+1].getState() ==0) { firstClear(x + 1, y + 1); //checkMines(x
	 * + 1, y + 1); } if (x > 0 && !tile[x - 1][y].isMine()&&
	 * tile[x-1][y].getState() ==0) { firstClear(x - 1, y); //checkMines(x - 1,
	 * y); } if (x < tile.length - 1 && !tile[x + 1][y].isMine()&&
	 * tile[x+1][y].getState() ==0) { firstClear(x + 1, y); //checkMines(x + 1,
	 * y); } if (y < tile[0].length - 1 && !tile[x][y + 1].isMine()&&
	 * tile[x][y+1].getState() ==0) { firstClear(x, y + 1); //checkMines(x, y +
	 * 1); }
	 * 
	 * checkMines(x,y); return;
	 * 
	 * }
	 */
	public static void checkWin() {
		int win = 0;
		int numTiles = (tile.length * tile[0].length);
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[0].length; j++) {
				if (!tile[i][j].isMine() && tile[i][j].getState() != 0) {
					win++;
				}

			}
		}
		if (win == (numTiles - Play.numMines)) {
			win();
		}
	}

	public int checkMines() {
		int bombCount = 0;

		int y = this.Yaxis;
		int x = this.Xaxis;
		System.out.println("X: " + x + " Y: " + y);
		System.out.println(this.isMine() ? "True" : "False");

		if (y > 0 && tile[x][y - 1].isMine()) {
			bombCount++;
		}
		if (y > 0 && x > 0 && tile[x - 1][y - 1].isMine()) {
			bombCount++;
		}
		if (y > 0 && x < tile.length - 1 && tile[x + 1][y - 1].isMine()) {
			bombCount++;
		}
		if (x > 0 && y < tile[0].length - 1 && tile[x - 1][y + 1].isMine()) {
			bombCount++;
		}
		if (y < tile[0].length - 1 && x < tile.length - 1 && tile[x + 1][y + 1].isMine()) {
			bombCount++;
		}
		if (x > 0 && tile[x - 1][y].isMine()) {
			bombCount++;
		}
		if (x < tile.length - 1 && tile[x + 1][y].isMine()) {
			bombCount++;
		}
		if (y < tile[0].length - 1 && tile[x][y + 1].isMine()) {
			bombCount++;
		}

		if (!this.isMine()) {
			Play.tilesRevealed++;
			if (bombCount == 0) {
				this.setState(10);
				this.setGraphic(num0);
			} else if (bombCount == 1) {
				this.setState(1);
				this.setGraphic(num1);
			} else if (bombCount == 2) {
				this.setState(1);
				this.setGraphic(num2);
			} else if (bombCount == 3) {
				this.setState(1);
				this.setGraphic(num3);
			} else if (bombCount == 4) {
				this.setState(1);
				this.setGraphic(num4);
			} else if (bombCount == 5) {
				this.setState(1);
				this.setGraphic(num5);
			} else if (bombCount == 6) {
				this.setState(1);
				this.setGraphic(num6);
			} else if (bombCount == 7) {
				this.setState(1);
				this.setGraphic(num7);
			} else if (bombCount == 8) {
				this.setState(1);
				this.setGraphic(num8);
			}
		}
		return bombCount;
	}

	public void checkZero(int x, int y) {
		int bombCount = 0;

		if (y > 0 && tile[x][y - 1].isMine()) {
			bombCount++;
		}
		if (y > 0 && x > 0 && tile[x - 1][y - 1].isMine()) {
			bombCount++;
		}
		if (y > 0 && x < tile.length - 1 && tile[x + 1][y - 1].isMine()) {
			bombCount++;
		}
		if (x > 0 && y < tile[0].length - 1 && tile[x - 1][y + 1].isMine()) {
			bombCount++;
		}
		if (y < tile[0].length - 1 && x < tile.length - 1 && tile[x + 1][y + 1].isMine()) {
			bombCount++;
		}
		if (x > 0 && tile[x - 1][y].isMine()) {
			bombCount++;
		}
		if (x < tile.length - 1 && tile[x + 1][y].isMine()) {
			bombCount++;
		}
		if (y < tile[0].length - 1 && tile[x][y + 1].isMine()) {
			bombCount++;
		}

		if (!this.isMine()) {
			if (bombCount == 0) {
				System.out.println("ZERo");
				tile[x][y].setState(10);
				firstClear(x, y);
				tile[x][y].setGraphic(num0);

			}
		}

	}

	private static void win() {
		Play.setTopBar("face-win.png");

	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public boolean isMine() {
		return mine;
	}

	public void setMine() {
		mine = true;
		this.state = 4;

	}

	public boolean isFlag() {
		if (this.state == 2)
			return true;
		else
			return false;
	}

	public void setFlag() {
		this.state = 2;
		this.setGraphic(flag);
		Play.removeMine();
	}

	public void removeFlag() {
		this.state = 0;
		this.setGraphic(unclicked);
		Play.addMine();
		;
	}

	public void gameOver() {
		Play.setTopBar("face-dead.png");
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[0].length; j++) {
				if (tile[i][j].isMine() && tile[i][j].getState() != 5) {
					tile[i][j].setGraphic(new ImageView(new Image("res/mine-grey.png")));
					// Play.minesRevealed++;

				}
			}
		}

	}

	private static boolean disableClick() {
		if ((Play.minesRevealed == Play.numMines
				|| Play.tilesRevealed == (tile.length * tile[0].length) - Play.numMines))
			return true;
		else
			return false;
	}

}