package Game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameLogic {
    private JFrame display;
    private PuzzleGrid grid;
    private int boardWidth, boardLength, boardSquares;
    private int[][] gameBoard, solution;
    private Pair<Integer, Integer> emptyLocation;
    private boolean finished;
    Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int displayWidth = 50 * (ScreenSize.width / 100);
    private int displayHeight = 50 * (ScreenSize.height / 100);

    public GameLogic(int width, int length) {
        boardWidth = width;
        boardLength = length;
        boardSquares = width * length;
        finished = false;
        gameBoard = setUpBoard(width, length, false);
        emptyLocation = findEmptyTile(gameBoard);
        solution = setUpBoard(width, length, true);
        printGrid();
        grid = new PuzzleGrid(this);
        display = new JFrame("Number Puzzle Grid");

        display.add(grid, BorderLayout.CENTER);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setResizable(false);
        display.pack();
        display.setVisible(true);
    }

    private int[][] setUpBoard(int width, int length, boolean sol) {
        int[][] result = new int[width][length];
        if (sol) {
            int i = 1;
            for (int y = 0; y < length; y++) {
                for (int x = 0; x < width; x++) {
                    result[x][y] = i;
                    i++;
                }
            }
            result[width - 1][length - 1] = 0;
        } else {
            LinkedList<Integer> values = new LinkedList<Integer>();
            Random rand = new Random();
            ArrayList<Pair<Integer, Integer>> usedCoordinates = new ArrayList<Pair<Integer, Integer>>();
            for (int i = 0; i < boardSquares; i++) {
                values.add(i);
            }
            do {
                int x = rand.nextInt(width);
                int y = rand.nextInt(length);
                Pair<Integer, Integer> temp = new Pair<Integer, Integer>(x, y);
                if (!usedCoordinates.contains(temp)) {
                    result[x][y] = values.pop();
                    usedCoordinates.add(temp);
                }
            } while (!values.isEmpty());
        }

        return result;
    }

    public void pickTile(int x, int y) {
        System.out.println("(" + x + ", " + y + ")" + gameBoard[x][y]);
        int x2 = emptyLocation.getFirst();
        int y2 = emptyLocation.getSecond();
        if (gameBoard[x][y] == 0) {
            return;
        } else {
            if (x != x2 && y != y2) {
                return;
            } else {
                ArrayList<Tile> moving = new ArrayList<Tile>();
                ArrayList<Pair<Integer, Integer>> movingCoordinates = new ArrayList<Pair<Integer, Integer>>();
                if (x == x2) {
                    if (y > y2) {
                        for (int i = y; i >= y2; i--) {
                            Tile temp = new Tile(x, i, gameBoard[x][i]);
                            Pair<Integer, Integer> coords = new Pair<Integer, Integer>(x, i);
                            moving.add(temp);
                            movingCoordinates.add(coords);
                        }
                    } else {
                        for (int i = y; i <= y2; i++) {
                            Tile temp = new Tile(x, i, gameBoard[x][i]);
                            Pair<Integer, Integer> coords = new Pair<Integer, Integer>(x, i);
                            moving.add(temp);
                            movingCoordinates.add(coords);
                        }
                    }

                } else if (y == y2) {
                    if (x > x2) {
                        for (int i = x; i >= x2; i--) {
                            Tile temp = new Tile(i, y, gameBoard[i][y]);
                            Pair<Integer, Integer> coords = new Pair<Integer, Integer>(i, y);
                            moving.add(temp);
                            movingCoordinates.add(coords);
                        }
                    } else {
                        for (int i = x; i <= x2; i++) {
                            Tile temp = new Tile(i, y, gameBoard[i][y]);
                            Pair<Integer, Integer> coords = new Pair<Integer, Integer>(i, y);
                            moving.add(temp);
                            movingCoordinates.add(coords);
                        }
                    }
                } else {
                    throw new RuntimeException("fuck");
                }
                Tile emptyLocation = moving.get(moving.size() - 1);
                moving.remove(moving.size() - 1);
                moving.add(0, emptyLocation);
                for (int i = 0; i < movingCoordinates.size(); i++) {
                    int a = movingCoordinates.get(i).getFirst();
                    int b = movingCoordinates.get(i).getSecond();
                    moving.get(i).setX(a);
                    moving.get(i).setY(b);
                }
                for (Tile tile : moving) {
                    int i = tile.getX();
                    int j = tile.getY();
                    gameBoard[i][j] = tile.getNum();
                }
                if (solved()) {
                    win();
                }
                refresh();
                return;
            }
        }
    }

    public void refresh() {
        emptyLocation = findEmptyTile(gameBoard);
        printGrid();
        display.repaint();
    }

    private Pair<Integer, Integer> findEmptyTile(int[][] board) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == 0) {
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        throw new RuntimeException("no empty tiles. fuk");
    }

    private boolean solved() {
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardLength; y++) {
                if (gameBoard[x][y] != solution[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void win() {
        JOptionPane.showMessageDialog(null, "you did it. yay.");
        System.exit(0);
    }

    private void printGrid() {
        System.out.println("\n Solution");
        System.out.println("(1,0): " + solution[1][0]);
        for (int y = 0; y < boardLength; y++) {
            for (int x = 0; x < boardWidth; x++) {
                System.out.print(solution[x][y] + "\t");
            }
            System.out.println("");
        }

    }

    public int[][] getBoard() {
        return gameBoard;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardLength() {
        return boardLength;
    }

}

class Tile {
    private int x, y, num;

    public Tile(int a, int b, int n) {
        x = a;
        y = b;
        num = n;
    }

    public int getX() {
        return x;
    }

    public void setX(int a) {
        x = a;
    }

    public int getY() {
        return y;
    }

    public void setY(int b) {
        y = b;
    }

    public int getNum() {
        return num;
    }
}

class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair<Object, Object> otherPair = (Pair) other;
            return ((this.first == otherPair.first
                    || (this.first != null && otherPair.first != null && this.first.equals(otherPair.first)))
                    && (this.second == otherPair.second || (this.second != null && otherPair.second != null
                            && this.second.equals(otherPair.second))));
        }

        return false;
    }

    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
}
