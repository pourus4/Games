package MineSweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Actions implements ActionListener, MouseListener {

	private MineSweeperLogic mine;
	Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = 50 * (ScreenSize.width / 100);
	private int screenHeight = 50 * (ScreenSize.height / 100);
	private int xScale;
	private int yScale;

	// These following four are not important. We simply tell the machine to pay no
	// mind when mouse is pressed, released, etc.
	// If these weren't here the computer would freak out and panic over what to do
	// because no instructions were given.

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}
	
	// Where the fun begins

	public Actions(MineSweeperLogic m) {

		mine = m;
		yScale = mine.getDisplayHeight() / mine.getHeight();
		xScale = mine.getDisplayWidth() / mine.getWidth();
	}

	public void sizeChange(){
		yScale = mine.getDisplayHeight() / mine.getHeight();
		xScale = mine.getDisplayWidth() / mine.getWidth();
	}

	// Any time an action is performed, redraw the board and keep it up to date.
	public void actionPerformed(ActionEvent e) {

		mine.refresh();
	}

	// Mouse clicky clicky
	public void mouseClicked(MouseEvent e) {
		boolean LeftClick = (e.getButton() == 1);
		boolean ShiftClick = (e.getButton() == 1 && e.isShiftDown());
		boolean AltClick = (e.getButton() == 1 && e.isAltDown());

		// Left click - reveals a tile
		if (LeftClick && !(ShiftClick||AltClick)) {
			int x = e.getX() / xScale;
			int y = e.getY() / yScale;

			mine.select(x, y);
		}
		// Shift Left Click Chord
		else if (ShiftClick) {
			int x = e.getX() / xScale;
			int y = e.getY() / yScale;

			mine.chord(x, y);
		}

		// Middle click
		else if (e.getButton() == 2) {

		}

		// Alt Click to place flag
		else if (AltClick) {
			int x = e.getX() / xScale;
			int y = e.getY() / yScale;

			mine.Mark(x, y);
		}

		mine.refresh(); // Gotta keep it fresh
	}

}