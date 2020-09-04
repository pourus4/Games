package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Actions implements ActionListener, MouseListener {
    private GameLogic game;
    private int xScale;
    private int yScale;

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void sizeChange() {
        xScale = game.getDisplayWidth() / game.getBoardWidth();
        yScale = game.getDisplayHeight() / game.getBoardLength();
    }

    public Actions(GameLogic g) {
        game = g;
        xScale = game.getDisplayWidth() / game.getBoardWidth();
        yScale = game.getDisplayHeight() / game.getBoardLength();
    }

    public void actionPerformed(ActionEvent e) {
        game.refresh();
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1) {
            int x = e.getX() / xScale;
            int y = e.getY() / yScale;
            game.pickTile(x, y);
        }
    }

}