package MineSweeper;

public class Target {
	private int x, y;
	private boolean isMine;
	public Target(int targetX, int targetY, boolean isBomb) {
		x = targetX;
		y = targetY;
		isMine = isBomb;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean isMine() {//true if bomb
		return isMine;
	}

}
