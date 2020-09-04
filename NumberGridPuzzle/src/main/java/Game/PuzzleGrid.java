package Game;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JPanel;

public class PuzzleGrid extends JPanel {
    private static final long serialVersionUID = 1L;
    private GameLogic game;
    private Actions controls;
    private int[][] board;
    Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = 50 * (ScreenSize.width / 100);
    private int screenHeight = 50 * (ScreenSize.height / 100);
    private int xScale, yScale;

    public PuzzleGrid(GameLogic g) {
        game = g;
        controls = new Actions(game);
        board = game.getBoard();
        xScale = screenWidth / game.getBoardWidth();
        yScale = screenHeight / game.getBoardLength();

        addMouseListener(controls);

        setPreferredSize(new Dimension(screenWidth, screenHeight));
    }

    public void sizeChange(Dimension size) {
        setPreferredSize(size);
        xScale = size.width / game.getBoardWidth();
        yScale = size.height / game.getBoardLength();
        controls.sizeChange();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board = game.getBoard();

        for (int x = 0; x < game.getBoardWidth(); x++) {
            for (int y = 0; y < game.getBoardLength(); y++) {
                g.setColor(Color.BLACK);
                g.drawRect(x * xScale, y * yScale, x * xScale + xScale, y * yScale + yScale);

                // g.setColor(Color.WHITE);
                // g.fillRect(x * xScale, y * yScale, x * xScale + xScale, y * yScale + yScale);

                if(board[x][y] == 0){
                    continue;
                }
                int[] num = numSeperate(board[x][y]);

                int shift = 1;
                for(int i = num.length - 1; i > -1; i--) {
                    int n = num[i];
                    drawNumbers(n, shift, x, y, g);
                    shift += 9;
                }
            }
        }
    }

    private void drawNumbers(int n, int shift, int x, int y, Graphics g) {
        g.setColor(Color.BLUE);
        x *= xScale;
        if (n == 0) {
			// Horizontal Lines
			g.drawLine(x + shift + 13, y * yScale + 5, x + shift + 13, y * yScale + 9); // 3
			g.drawLine(x + shift + 13, y * yScale + 11, x + shift + 13, y * yScale + 15); // 6
			// Vertical lines
			g.drawLine(x + shift + 13, y * yScale + 15, x + shift + 18, y * yScale + 15);// Top line of Zero
			g.drawLine(x + shift + 13, y * yScale + 5, x + shift + 18, y * yScale + 5);
			// Horizontal Lines
			g.drawLine(x + shift + 18, y * yScale + 5, x + shift + 18, y * yScale + 9); // 3
			g.drawLine(x + shift + 18, y * yScale + 11, x + shift + 18, y * yScale + 15); // 6
		} else if (n == 1) {
			g.drawLine(x + shift + 13, y * yScale + 5, x + shift + 13, y * yScale + 9); // 3
			g.drawLine(x + shift + 13, y * yScale + 11, x + shift + 13, y * yScale + 15); // 6
		} else if (n == 2) {
			g.drawLine(x + shift + 8, y * yScale + 4, x + shift + 12, y * yScale + 4); // 2
			g.drawLine(x + shift + 13, y * yScale + 5, x + shift + 13, y * yScale + 9); // 3
			g.drawLine(x + shift + 8, y * yScale + 10, x + shift + 12, y * yScale + 10); // 4
			g.drawLine(x + shift + 7, y * yScale + 11, x + shift + 7, y * yScale + 15); // 5
			g.drawLine(x + shift + 8, y * yScale + 16, x + shift + 12, y * yScale + 16); // 7
		} else if (n == 3) {
			g.drawLine(x + shift + 8, y * yScale + 4, x + shift + 12, y * yScale + 4); // 2
			g.drawLine(x + shift + 13, y * yScale + 5, x + shift + 13, y * yScale + 9); // 3
			g.drawLine(x + shift + 8, y * yScale + 10, x + shift + 12, y * yScale + 10); // 4
			g.drawLine(x + shift + 13, y * yScale + 11, x + shift + 13, y * yScale + 15); // 6
			g.drawLine(x + shift + 8, y * yScale + 16, x + shift + 12, y * yScale + 16); // 7
		} else if (n == 4) {
			g.drawLine(x + shift + 7, y * yScale + 5, x + shift + 7, y * yScale + 9); // 1
			g.drawLine(x + shift + 13, y * yScale + 5, x + shift + 13, y * yScale + 9); // 3
			g.drawLine(x + shift + 8, y * yScale + 10, x + shift + 12, y * yScale + 10); // 4
			g.drawLine(x + shift + 13, y * yScale + 11, x + shift + 13, y * yScale + 15); // 6
		} else if (n == 5) {
			g.drawLine(x + shift + 7, y * yScale + 5, x + shift + 7, y * yScale + 9); // 1
			g.drawLine(x + shift + 8, y * yScale + 4, x + shift + 12, y * yScale + 4); // 2
			g.drawLine(x + shift + 8, y * yScale + 10, x + shift + 12, y * yScale + 10); // 4
			g.drawLine(x + shift + 13, y * yScale + 11, x + shift + 13, y * yScale + 15); // 6
			g.drawLine(x + shift + 8, y * yScale + 16, x + shift + 12, y * yScale + 16); // 7
		} else if (n == 6) {
			g.drawLine(x + shift + 7, y * yScale + 5, x + shift + 7, y * yScale + 9); // 1
			g.drawLine(x + shift + 8, y * yScale + 4, x + shift + 12, y * yScale + 4); // 2
			g.drawLine(x + shift + 8, y * yScale + 10, x + shift + 12, y * yScale + 10); // 4
			g.drawLine(x + shift + 7, y * yScale + 11, x + shift + 7, y * yScale + 15); // 5
			g.drawLine(x + shift + 13, y * yScale + 11, x + shift + 13, y * yScale + 15); // 6
			g.drawLine(x + shift + 8, y * yScale + 16, x + shift + 12, y * yScale + 16); // 7
		} else if (n == 7) {
			g.drawLine(x + shift + 8, y * yScale + 4, x + shift + 12, y * yScale + 4); // 2
			g.drawLine(x + shift + 13, y * yScale + 5, x + shift + 13, y * yScale + 9); // 3
			g.drawLine(x + shift + 13, y * yScale + 11, x + shift + 13, y * yScale + 15); // 6
		} else if (n == 8) {
			g.drawLine(x + shift + 7, y * yScale + 5, x + shift + 7, y * yScale + 9); // 1
			g.drawLine(x + shift + 8, y * yScale + 4, x + shift + 12, y * yScale + 4); // 2
			g.drawLine(x + shift + 13, y * yScale + 5, x + shift + 13, y * yScale + 9); // 3
			g.drawLine(x + shift + 8, y * yScale + 10, x + shift + 12, y * yScale + 10); // 4
			g.drawLine(x + shift + 7, y * yScale + 11, x + shift + 7, y * yScale + 15); // 5
			g.drawLine(x + shift + 13, y * yScale + 11, x + shift + 13, y * yScale + 15); // 6
			g.drawLine(x + shift + 8, y * yScale + 16, x + shift + 12, y * yScale + 16); // 7
		}
		if (n == 9) {
			g.drawLine(x + shift + 7, y * yScale + 5, x + shift + 7, y * yScale + 9); // 1
			g.drawLine(x + shift + 8, y * yScale + 4, x + shift + 12, y * yScale + 4); // 2
			g.drawLine(x + shift + 13, y * yScale + 5, x + shift + 13, y * yScale + 9); // 3
			g.drawLine(x + shift + 8, y * yScale + 10, x + shift + 12, y * yScale + 10); // 4
			g.drawLine(x + shift + 13, y * yScale + 11, x + shift + 13, y * yScale + 16); // 8
        }
        g.setColor(Color.BLACK);
    }

    private int[] numSeperate(int num){
        int counter = 0;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		do {
			int n = num % 10;
			if (num == 0)
				temp.add(counter, 0);
			else
				temp.add(counter, n);
			counter++;
			num = num / 10;
		} while (num > 0);
		int[] result = new int[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			result[i] = temp.get(i);
        }
        return result;
    }

}