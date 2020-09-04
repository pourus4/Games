package MineSweeper;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;

public class FlagCounter extends Canvas {

	private static final long serialVersionUID = 1L;
	Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = 50 * (ScreenSize.width / 100);
	private int screenHeight = 50 * (ScreenSize.height / 100);
	private int RemainingFlags, TotalMines, xScale;
	// private String Flags;
	private int[] Flags;
	Canvas canvas = new Canvas();

	FlagCounter(int bombCount, int Width, int Height) {
		TotalMines = bombCount;
		RemainingFlags = bombCount;
		xScale = screenWidth / Width;
		JFrame FlagCounterDisplay = new JFrame("FlagCounter");
		canvas.setSize(xScale, screenHeight);
		FlagCounterDisplay.add(canvas);
		FlagCounterDisplay.pack();
		FlagCounterDisplay.setVisible(false);

		setPreferredSize(new Dimension(2 * xScale, screenHeight));
		System.out.println("2 * xScale = " + xScale*2);
	}

	public void newGame() {
		RemainingFlags = TotalMines;
		repaint();
	}

	private void bombsToFind(int Count) {// Stores the number of bombs remaining in the Flags int arraylist from largest
											// place to smallest. INT VERSION
		int counter = 0;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		do {
			int n = Count % 10;
			if (Count == 0)
				temp.add(counter, 0);
			else
				temp.add(counter, n);
			counter++;
			Count = Count / 10;
		} while (Count > 0);
		Flags = new int[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			Flags[i] = temp.get(i);
		}
	}

	public int getRemainingFlags() {
		repaint();
		return RemainingFlags;
	}

	public void FlagPlaced() {
		RemainingFlags--;
		repaint();
	}

	public void FlagRemoved() {
		RemainingFlags++;
		repaint();
	}

	public void paint(Graphics g) {

		bombsToFind(RemainingFlags);
		int shift = 1;
		for (int i = Flags.length - 1; i > -1; i--) {
			int n = Flags[i];
			drawNumbers(n, shift, i);
			shift += 9;
		}

	}

	private void drawNumbers(int current, int shift, int i) {

		Graphics g = getGraphics();
		g.setColor(Color.BLACK);

		if (current == 0) {
			// Horizontal Lines
			g.drawLine(i + shift + 13, i + 5, i + shift + 13, i + 9); // 3
			g.drawLine(i + shift + 13, i + 11, i + shift + 13, i + 15); // 6
			// Vertical lines
			g.drawLine(i + shift + 13, i + 15, i + shift + 18, i + 15);// Top line of Zero
			g.drawLine(i + shift + 13, i + 5, i + shift + 18, i + 5);
			// Horizontal Lines
			g.drawLine(i + shift + 18, i + 5, i + shift + 18, i + 9); // 3
			g.drawLine(i + shift + 18, i + 11, i + shift + 18, i + 15); // 6
		} else if (current == 1) {
			g.drawLine(i + shift + 13, i + 5, i + shift + 13, i + 9); // 3
			g.drawLine(i + shift + 13, i + 11, i + shift + 13, i + 15); // 6
		} else if (current == 2) {
			g.drawLine(i + shift + 8, i + 4, i + shift + 12, i + 4); // 2
			g.drawLine(i + shift + 13, i + 5, i + shift + 13, i + 9); // 3
			g.drawLine(i + shift + 8, i + 10, i + shift + 12, i + 10); // 4
			g.drawLine(i + shift + 7, i + 11, i + shift + 7, i + 15); // 5
			g.drawLine(i + shift + 8, i + 16, i + shift + 12, i + 16); // 7
		} else if (current == 3) {
			g.drawLine(i + shift + 8, i + 4, i + shift + 12, i + 4); // 2
			g.drawLine(i + shift + 13, i + 5, i + shift + 13, i + 9); // 3
			g.drawLine(i + shift + 8, i + 10, i + shift + 12, i + 10); // 4
			g.drawLine(i + shift + 13, i + 11, i + shift + 13, i + 15); // 6
			g.drawLine(i + shift + 8, i + 16, i + shift + 12, i + 16); // 7
		} else if (current == 4) {
			g.drawLine(i + shift + 7, i + 5, i + shift + 7, i + 9); // 1
			g.drawLine(i + shift + 13, i + 5, i + shift + 13, i + 9); // 3
			g.drawLine(i + shift + 8, i + 10, i + shift + 12, i + 10); // 4
			g.drawLine(i + shift + 13, i + 11, i + shift + 13, i + 15); // 6
		} else if (current == 5) {
			g.drawLine(i + shift + 7, i + 5, i + shift + 7, i + 9); // 1
			g.drawLine(i + shift + 8, i + 4, i + shift + 12, i + 4); // 2
			g.drawLine(i + shift + 8, i + 10, i + shift + 12, i + 10); // 4
			g.drawLine(i + shift + 13, i + 11, i + shift + 13, i + 15); // 6
			g.drawLine(i + shift + 8, i + 16, i + shift + 12, i + 16); // 7
		} else if (current == 6) {
			g.drawLine(i + shift + 7, i + 5, i + shift + 7, i + 9); // 1
			g.drawLine(i + shift + 8, i + 4, i + shift + 12, i + 4); // 2
			g.drawLine(i + shift + 8, i + 10, i + shift + 12, i + 10); // 4
			g.drawLine(i + shift + 7, i + 11, i + shift + 7, i + 15); // 5
			g.drawLine(i + shift + 13, i + 11, i + shift + 13, i + 15); // 6
			g.drawLine(i + shift + 8, i + 16, i + shift + 12, i + 16); // 7
		} else if (current == 7) {
			g.drawLine(i + shift + 8, i + 4, i + shift + 12, i + 4); // 2
			g.drawLine(i + shift + 13, i + 5, i + shift + 13, i + 9); // 3
			g.drawLine(i + shift + 13, i + 11, i + shift + 13, i + 15); // 6
		} else if (current == 8) {
			g.drawLine(i + shift + 7, i + 5, i + shift + 7, i + 9); // 1
			g.drawLine(i + shift + 8, i + 4, i + shift + 12, i + 4); // 2
			g.drawLine(i + shift + 13, i + 5, i + shift + 13, i + 9); // 3
			g.drawLine(i + shift + 8, i + 10, i + shift + 12, i + 10); // 4
			g.drawLine(i + shift + 7, i + 11, i + shift + 7, i + 15); // 5
			g.drawLine(i + shift + 13, i + 11, i + shift + 13, i + 15); // 6
			g.drawLine(i + shift + 8, i + 16, i + shift + 12, i + 16); // 7
		}
		if (current == 9) {
			g.drawLine(i + shift + 7, i + 5, i + shift + 7, i + 9); // 1
			g.drawLine(i + shift + 8, i + 4, i + shift + 12, i + 4); // 2
			g.drawLine(i + shift + 13, i + 5, i + shift + 13, i + 9); // 3
			g.drawLine(i + shift + 8, i + 10, i + shift + 12, i + 10); // 4
			g.drawLine(i + shift + 13, i + 11, i + shift + 13, i + 16); // 8
		}

	}

}