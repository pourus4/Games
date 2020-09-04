package MineSweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

/**
 * 
 * @author Connor This version will size the JPannel to take up a quarter of the
 *         screen and will have the tiles scale based on how many rows and
 *         columns there are.
 */
public class Grid extends JPanel {
	private static final long serialVersionUID = 1L;

	private MineSweeperLogic mine;
	private Actions controls;
	private Tiles[][] cells;
	private int xScale, yScale;
	Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = 50 * (ScreenSize.width / 100);
	private int screenHeight = 50 * (ScreenSize.height / 100);
	private Color[] colors = { Color.BLUE, Color.CYAN.darker(), Color.GREEN.darker(), Color.YELLOW.darker(),
			Color.ORANGE.darker(), Color.PINK.darker(), Color.MAGENTA };

	public Grid(MineSweeperLogic m) { // Creating a new game so we can draw a board for it
		mine = m;
		controls = new Actions(mine);
		cells = mine.getTiles();
		yScale = screenHeight / mine.getHeight();
		xScale = screenWidth / mine.getWidth();

		addMouseListener(controls);

		setPreferredSize(new Dimension(screenWidth, screenHeight));
	}

	public void sizeChange(Dimension size) {
		setPreferredSize(size);
		yScale = size.height / mine.getHeight();
		xScale = size.width / mine.getWidth();
		screenHeight = size.height;
		screenWidth = size.width;
		controls.sizeChange();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		cells = mine.getTiles();

		if (mine.isFinished()) {
			gameEndProcedures(g);
		} 
		else if (mine.isPaused()) {
			// g.setColor(Color.LIGHT_GRAY);
			// g.fillRect(0, 0, screenWidth, screenHeight);
		} else {
			for (int i = 0; i < mine.getWidth(); i++) {

				for (int j = 0; j < mine.getHeight(); j++) {

					Tiles current = cells[i][j];

					g.setColor(Color.BLACK);
					g.drawRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);

					if (current.isFlagged()) {

						g.setColor(Color.ORANGE); // Flagging a mine.
						g.fillRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
						g.setColor(Color.BLACK);

					}

					else if (current.isMarked()) {
						g.setColor(Color.GREEN.darker());
						g.fillRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
						g.setColor(Color.BLACK);
					}

					// Unflagged cells
					else if (current.isCovered()) { // Covered cells
						g.setColor(Color.DARK_GRAY);
						g.fillRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
						g.setColor(Color.BLACK);
					} else { // Empty cells or numbered cells
						g.setColor(Color.WHITE);
						g.fillRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
						g.setColor(Color.BLACK);
					}

					if (!current.isCovered()) {
						drawNumber(i, j, current.BombsNearMe(), g);
					}
					g.setColor(Color.BLACK);
					g.drawRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
				}
			}
		}
	}

	public void gameEndProcedures(Graphics g) {
		for (int i = 0; i < mine.getWidth(); i++) {
			for (int j = 0; j < mine.getHeight(); j++) {

				Tiles current = cells[i][j];

				if (current.isBomb() && !current.isFlagged()) { // unmarked bombs shown
					g.setColor(Color.RED);
					g.fillRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
					g.setColor(Color.BLACK);
					g.drawLine(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
					g.drawLine(i * xScale, j * yScale + yScale, i * xScale + xScale, j * yScale);
				} else if (current.isBomb() && current.isFlagged()) { // Shows Correctly Flagged mines when game is
					// over

					g.setColor(Color.GREEN);
					g.fillRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
					g.setColor(Color.BLACK);

					g.drawLine(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
					g.drawLine(i * xScale, j * yScale + yScale, i * xScale + xScale, j * yScale);
				} else if (current.isFlagged() && !current.isBomb()) { // Shows cells that the player incorrectly
					// identified as mines.
					g.setColor(Color.YELLOW);
					g.fillRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
					g.setColor(Color.BLACK);
				} else if (!current.isFlagged() && !current.isBomb()) {
					drawNumber(i, j, current.BombsNearMe(), g);
				}
				g.setColor(Color.BLACK);
				g.drawRect(i * xScale, j * yScale, i * xScale + xScale, j * yScale + yScale);
			}
		}
	}

	public void drawNumber(int i, int j, int count, Graphics g) {
		// The following part is very self explanatory - drawing the numbers.
		// Not very interesting work.
		// Not a fun time.
		// Rating: 0/10. Would not recommend.
		if (count > 0)
			g.setColor(colors[count - 1]);
		if (count == 1) {
			g.drawLine(i * xScale + 13, j * yScale + 5, i * xScale + 13, j * yScale + 9); // 3
			g.drawLine(i * xScale + 13, j * yScale + 11, i * xScale + 13, j * yScale + 15); // 6
		} else if (count == 2) {
			g.drawLine(i * xScale + 8, j * yScale + 4, i * xScale + 12, j * yScale + 4); // 2
			g.drawLine(i * xScale + 13, j * yScale + 5, i * xScale + 13, j * yScale + 9); // 3
			g.drawLine(i * xScale + 8, j * yScale + 10, i * xScale + 12, j * yScale + 10); // 4
			g.drawLine(i * xScale + 7, j * yScale + 11, i * xScale + 7, j * yScale + 15); // 5
			g.drawLine(i * xScale + 8, j * yScale + 16, i * xScale + 12, j * yScale + 16); // 7
		} else if (count == 3) {
			g.drawLine(i * xScale + 8, j * yScale + 4, i * xScale + 12, j * yScale + 4); // 2
			g.drawLine(i * xScale + 13, j * yScale + 5, i * xScale + 13, j * yScale + 9); // 3
			g.drawLine(i * xScale + 8, j * yScale + 10, i * xScale + 12, j * yScale + 10); // 4
			g.drawLine(i * xScale + 13, j * yScale + 11, i * xScale + 13, j * yScale + 15); // 6
			g.drawLine(i * xScale + 8, j * yScale + 16, i * xScale + 12, j * yScale + 16); // 7
		} else if (count == 4) {
			g.drawLine(i * xScale + 7, j * yScale + 5, i * xScale + 7, j * yScale + 9); // 1
			g.drawLine(i * xScale + 13, j * yScale + 5, i * xScale + 13, j * yScale + 9); // 3
			g.drawLine(i * xScale + 8, j * yScale + 10, i * xScale + 12, j * yScale + 10); // 4
			g.drawLine(i * xScale + 13, j * yScale + 11, i * xScale + 13, j * yScale + 15); // 6
		} else if (count == 5) {
			g.drawLine(i * xScale + 7, j * yScale + 5, i * xScale + 7, j * yScale + 9); // 1
			g.drawLine(i * xScale + 8, j * yScale + 4, i * xScale + 12, j * yScale + 4); // 2
			g.drawLine(i * xScale + 8, j * yScale + 10, i * xScale + 12, j * yScale + 10); // 4
			g.drawLine(i * xScale + 13, j * yScale + 11, i * xScale + 13, j * yScale + 15); // 6
			g.drawLine(i * xScale + 8, j * yScale + 16, i * xScale + 12, j * yScale + 16); // 7
		} else if (count == 6) {
			g.drawLine(i * xScale + 7, j * yScale + 5, i * xScale + 7, j * yScale + 9); // 1
			g.drawLine(i * xScale + 8, j * yScale + 4, i * xScale + 12, j * yScale + 4); // 2
			g.drawLine(i * xScale + 8, j * yScale + 10, i * xScale + 12, j * yScale + 10); // 4
			g.drawLine(i * xScale + 7, j * yScale + 11, i * xScale + 7, j * yScale + 15); // 5
			g.drawLine(i * xScale + 13, j * yScale + 11, i * xScale + 13, j * yScale + 15); // 6
			g.drawLine(i * xScale + 8, j * yScale + 16, i * xScale + 12, j * yScale + 16); // 7
		} else if (count == 7) {
			g.drawLine(i * xScale + 8, j * yScale + 4, i * xScale + 12, j * yScale + 4); // 2
			g.drawLine(i * xScale + 13, j * yScale + 5, i * xScale + 13, j * yScale + 9); // 3
			g.drawLine(i * xScale + 13, j * yScale + 11, i * xScale + 13, j * yScale + 15); // 6
		} else if (count == 8) {
			g.drawLine(i * xScale + 7, j * yScale + 5, i * xScale + 7, j * yScale + 9); // 1
			g.drawLine(i * xScale + 8, j * yScale + 4, i * xScale + 12, j * yScale + 4); // 2
			g.drawLine(i * xScale + 13, j * yScale + 5, i * xScale + 13, j * yScale + 9); // 3
			g.drawLine(i * xScale + 8, j * yScale + 10, i * xScale + 12, j * yScale + 10); // 4
			g.drawLine(i * xScale + 7, j * yScale + 11, i * xScale + 7, j * yScale + 15); // 5
			g.drawLine(i * xScale + 13, j * yScale + 11, i * xScale + 13, j * yScale + 15); // 6
			g.drawLine(i * xScale + 8, j * yScale + 16, i * xScale + 12, j * yScale + 16); // 7
		}
		g.setColor(Color.BLACK);
	}

}
