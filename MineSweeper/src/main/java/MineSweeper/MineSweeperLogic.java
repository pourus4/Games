package MineSweeper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import TimeDisplay.Stopwatch;

/**
 * 
 * @author Connor
 */
public class MineSweeperLogic {
	private Grid grid;
	public JButton reset, pause;
	private JPanel time;
	private JFrame display, pauseScreen;
	private boolean finished, human, firstMove, wL, paused;
	private int[][] nearbyBombs;
	// Keeps track of bombs that are within a 1 tile radius of a specific spot on
	// the map
	private int mapHeight, mapWidth, bombCount, CoveredSquares, firstX, firstY, xBound;
	private FlagCounter FlagsRemaining;
	private Stopwatch gameTime = new Stopwatch("", false);
	private Tiles[][] mapTile;
	Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	// private int displayWidth = (50 * ScreenSize.width) / 100;
	private int displayWidth = 50 * (ScreenSize.width / 100);
	// private int displayHeight = (50 * ScreenSize.height) / 100;
	private int displayHeight = 50 * (ScreenSize.height / 100);

	MineSweeperLogic(int Width, int Height, int Count, boolean h) {
		mapHeight = Height;
		mapWidth = Width;
		bombCount = Count;
		human = h;
		xBound = 2 * (displayWidth / mapWidth);

		firstMove = true;
		paused = false;

		mapTile = new Tiles[mapWidth][mapHeight];
		FlagsRemaining = new FlagCounter(bombCount, mapWidth, mapHeight);

		bombAssignment();
		closeBombs();

		display = new JFrame("MineSweeper");
		pauseScreen = new JFrame("MineSweeper: paused");

		grid = new Grid(this);

		pause = new JButton("Pause");

		reset = new JButton("Reset");

		display.add(grid, BorderLayout.CENTER);
		display.add(FlagsRemaining, BorderLayout.EAST);
		display.add(pause, BorderLayout.SOUTH);

		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.setResizable(true);
		display.pack();
		display.setVisible(true);
		gameTime.Start();

		// bestWinTime();

		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
			}
		});
		display.addComponentListener(new ComponentListener() {

			public void componentResized(ComponentEvent e) {
				displayWidth = display.getWidth() - xBound;
				displayHeight = display.getHeight() - (2 * pause.getHeight());// 26 is the height of the Pause Button.
				grid.sizeChange(new Dimension(displayWidth, displayHeight));
				refresh();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void QuitGame() {// asks the player if they want to quit the game
		Object[] options = { "YES", "NO" };
		JOptionPane query = new JOptionPane("Do you want to keep playing?", JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION, null, options, options[0]);
		JDialog dialog = query.createDialog(null, "Pick One.");
		dialog.pack();
		dialog.setVisible(true);
		Object choice = query.getValue();
		if (options[1].equals(choice)) {
			display.dispatchEvent(new WindowEvent(display, WindowEvent.WINDOW_CLOSING));
		} else if (options[0].equals(choice)) {
			ChangeDifficulty();
		}
	}

	private void ChangeDifficulty() {
		Object[] options = { "YES", "NO" };
		JOptionPane query = new JOptionPane("Do you want to change the difficulty?", JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION, null, options, options[1]);
		JDialog dialog = query.createDialog(null, "Pick One.");
		dialog.pack();
		dialog.setVisible(true);
		Object choice = query.getValue();
		if (choice.equals(options[0])) {
			new NewGame(human, 0);
			display.dispose();
		}
	}

	private void pause() {
		if (paused) {// game is paused, do stuff to resume game.
			pauseScreen.setVisible(false);
			display.add(pause, BorderLayout.SOUTH);
			display.setVisible(true);
			refresh();
			gameTime.pause();
			paused = false;
		} else {// game is not paused, do stuff to pause game.
			paused = true;
			gameTime.pause();
			display.setVisible(false);
			JPanel s = new JPanel(new GridLayout());

			JLabel l = new JLabel("you've been playing for " + gameTime.runTime(), JLabel.CENTER);
			l.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			l.setFont(l.getFont().deriveFont(20f));
			s.add(l);
			pauseScreen.add(s, BorderLayout.CENTER);
			pauseScreen.add(pause, BorderLayout.WEST);
			pauseScreen.add(reset, BorderLayout.EAST);
			pauseScreen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			pauseScreen.setResizable(false);
			pauseScreen.pack();
			pauseScreen.setVisible(true);
		}
	}

	private void bombAssignment() {
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				Tiles tile = new Tiles(x, y);
				mapTile[x][y] = tile;
			}
		}
		int temp = bombCount;
		do {
			Random randomNumbers = new Random();
			int x = randomNumbers.nextInt(mapWidth);
			int y = randomNumbers.nextInt(mapHeight);
			int delta = randomNumbers.nextInt();
			if (delta % 2 == 0 && !mapTile[x][y].isBomb() && (closeBombs(x, y) < 7)) {
				if (firstMove && x == firstX && y == firstY) {
					continue;
				}
				temp -= 1;
				mapTile[x][y].SetBomb();
			}
		} while (temp > 0);
		closeBombs();
		return;
	}

	public void chord(int x, int y) {// reveals all the tiles surrounding the selected tile
		int checked = 0;
		int xMod = -1;
		do {
			do {
				int yMod = -1;
				do {
					try {
						if (mapTile[x + xMod][y + yMod].isBomb() && !mapTile[x + xMod][y + yMod].isFlagged()) {
							lose();
							checked = 9;
						} else {
							select(x + xMod, y + yMod);
							checked++;
						}

					} catch (Exception e) {
						checked++;
					}
					yMod++;
				} while (yMod <= 1);
				xMod++;
			} while (xMod <= 1);

		} while (checked < 8);
		resetMarks();
		refresh();
		if (won())
			win();

	}

	// Creates a 2D Array showing the number of bombs each map tile has within a 1
	// unit radius.
	private void closeBombs() {
		nearbyBombs = new int[mapWidth][mapHeight];

		for (int row = 0; row < nearbyBombs.length; row++) {
			for (int col = 0; col < nearbyBombs[row].length; col++) {
				int rowMod = -1;
				int checked = 0;
				int temp = 0;
				if (!mapTile[row][col].isBomb()) {
					// outermost do-while ensures that all 8 surrounding tiles are checked for bombs
					do {

						do {
							int colMod = -1;
							do {
								try {
									if (mapTile[row + rowMod][col + colMod].isBomb())
										temp++;
									checked++;
								} catch (Exception e) {
									checked++;
								}
								colMod++;
							} while (colMod <= 1);
							rowMod++;
						} while (rowMod <= 1);

					} while (checked < 8);

					nearbyBombs[row][col] = temp;
					mapTile[row][col].setBombsNearMe(temp);

				}
			}
		}
	}

	private int closeBombs(int x, int y) {// this is only to limit the number of bombs surrounding a tile to 6
		closeBombs();
		return nearbyBombs[x][y];
	}

	public int getHeight() {
		return mapHeight;
	}

	public int getBombCount() {
		return bombCount;
	}

	public int getRemainingFlags() {
		return FlagsRemaining.getRemainingFlags();
	}

	public Tiles[][] getTiles() {
		return mapTile;
	}

	public int getWidth() {
		return mapWidth;
	}

	public int getFlagsRemaining() {
		return FlagsRemaining.getRemainingFlags();
	}

	public boolean isFinished() {
		return finished;
	}

	private void lose() {
		finished = true;
		if (human) {
			for (int x = 0; x < mapWidth; x++) {
				for (int y = 0; y < mapHeight; y++) {
					if (!mapTile[x][y].isCovered())
						mapTile[x][y].unFlag();
					mapTile[x][y].Reveal();
				}
			}
			refresh();
			JOptionPane.showMessageDialog(null, "YOU LOSE!");
			reset();
		} else {
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setGameOver(false);
		}
	}

	public void Mark(int x, int y) {
		if (mapTile[x][y].isFlagged()) {
			mapTile[x][y].unFlag();
			FlagsRemaining.FlagRemoved();
			mapTile[x][y].setMarker();
		} else if (mapTile[x][y].isMarked()) {
			mapTile[x][y].removeMarker();
		} else {
			mapTile[x][y].Flag();
			FlagsRemaining.FlagPlaced();
		}
		resetMarks();
		refresh();
		if (won())
			win();
	}

	public void refresh() {
		display.repaint();
		FlagsRemaining.repaint();
	}

	public void reset() { // resets the board after the game has finished

		if (human) {
			QuitGame();
		}
		finished = false;
		firstMove = true;
		bombAssignment();
		closeBombs();
		FlagsRemaining.newGame();

	}

	private void resetMarks() {
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (!(mapTile[x][y].isCovered()) && mapTile[x][y].isFlagged()) {
					mapTile[x][y].unFlag();
					FlagsRemaining.FlagRemoved();
				} else if (!mapTile[x][y].isCovered() && mapTile[x][y].isMarked()) {
					mapTile[x][y].removeMarker();
				}
			}
		}
	}

	public void safeZone(int x, int y) {
		boolean[][] safeTiles = new boolean[mapWidth][mapHeight];
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				safeTiles[i][j] = false;
			}
		}
		safeTiles[x][y] = true;
		safeZoneArray(x, y, safeTiles);

		// revealing map tiles that have been marked by safeZoneArray
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (safeTiles[x][y]) {
					mapTile[x][y].Reveal();
				}

			}
		}
		resetMarks();
		refresh();
		if (won()) {
			win();
		}

	}

	public boolean[][] safeZoneArray(int x, int y, boolean[][] tested) {// Tests to find connected tiles that have no
		// bombs nearby and returns them to safeZone to
		// be revealed
		int checked = 0;
		int xMod = -1;
		do {

			do {

				int yMod = -1;

				do {

					try {
						if (mapTile[x + xMod][y + yMod].BombsNearMe() == 0 && !tested[x + xMod][y + yMod]) {
							tested[x + xMod][y + yMod] = true;
							safeZoneArray(x + xMod, y + yMod, tested);
							checked++;
						} else {
							mapTile[x + xMod][y + yMod].Reveal();
							checked++;
						}

					} catch (Exception e) {
						checked++;
					}

					yMod++;
				} while (yMod <= 1);

				xMod++;

			} while (xMod <= 1);

		} while (checked < 8);
		resetMarks();
		refresh();
		return tested;
	}

	public void select(int x, int y) {
		if (mapTile[x][y].isFlagged() || mapTile[x][y].isMarked()) {
			return;
		} else if (mapTile[x][y].isBomb() && !firstMove) {
			lose();
		} else if (mapTile[x][y].isBomb() && firstMove) {
			firstX = x;
			firstY = y;
			bombAssignment();
			firstMove = false;
			mapTile[x][y].Reveal();
			if (mapTile[x][y].BombsNearMe() == 0 && human) {
				safeZone(x, y);
			} else if (won()) {
				win();
			}
			resetMarks();
			refresh();
			return;
		} else {
			firstMove = false;
			mapTile[x][y].Reveal();
			if (mapTile[x][y].BombsNearMe() == 0 && human) {
				safeZone(x, y);
			} else if (won()) {
				win();
			}
			resetMarks();
			refresh();
			return;
		}
	}

	public void ShowMap() {// Shows all details of the map for testing purposes
		System.out.println(
				"Map Width:\t" + mapWidth + "\t" + "Map Height:\t" + mapHeight + "\t" + "Bomb Count:\t" + bombCount);
		System.out.println("Bombs at (0,0):\t" + mapTile[0][0].BombsNearMe());
		System.out.println("");
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (mapTile[x][y].isBomb())
					System.out.print("#");
				else {
					System.out.print(mapTile[x][y].BombsNearMe());
					mapTile[x][y].Reveal();
				}
				System.out.print("\t");
			}
			System.out.println("\n");
		}
		refresh();

	}

	private void win() {
		finished = true;
		if (human) {
			for (int x = 0; x < mapWidth; x++) {
				for (int y = 0; y < mapHeight; y++) {
					mapTile[x][y].Reveal();

					if (!mapTile[x][y].isBomb())
						mapTile[x][y].unFlag();
				}
			}
			refresh();
			JOptionPane.showMessageDialog(null, "YOU WIN!");
			reset();
		} else {
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setGameOver(true);
		}
	}

	private boolean won() {
		CoveredSquares = mapWidth * mapHeight;
		int correctBombs = 0;
		// the first set of for loops determines how many squares are still covered
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (!mapTile[x][y].isCovered())
					CoveredSquares--;
			}
		}

		// Checks if there are no hidden tiles on the map and all of the flags have been
		// used
		if (FlagsRemaining.getRemainingFlags() == 0) {
			for (int x = 0; x < mapWidth; x++) {
				for (int y = 0; y < mapHeight; y++) {
					if (mapTile[x][y].isFlagged() && !mapTile[x][y].isBomb())
						return false;
					else if (mapTile[x][y].isFlagged() && mapTile[x][y].isBomb())
						correctBombs++;
				}
			}

		}
		if (CoveredSquares == correctBombs)
			return true;
		else
			return false;
	}

	public boolean isHuman() {
		return human;
	}

	public boolean isPaused() {
		return paused;
	}

	private void setGameOver(boolean wl) {
		wL = wl;
		display.dispose();
	}

	public boolean botGameOver() {// called by the AI when the game ends to see if the game was won or lost.
		return wL;
	}

	public int getDisplayWidth() {
		return displayWidth;
	}

	public int getDisplayHeight() {
		return displayHeight;
	}

}
