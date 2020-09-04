package MineSweeper;

/**
 * This class just stores information on a cell
 */
import java.math.BigInteger;
import java.util.ArrayList;

public class Tiles {

	private boolean isBomb, isFlagged, isCovered, isMarked, linked, possibleBomb, unknown;
	private int SurroundingBombs, nearbyFlags, coveredNearMe, x, y;
	private BigInteger ProbableBomb;
	private ArrayList<Tiles> hiddensNear;
	private ArrayList<Tiles> linkedWith = new ArrayList<Tiles>();

	Tiles(int a, int b) {
		x = a;
		y = b;
		isBomb = false;
		isFlagged = false;
		isCovered = true;
		unknown = false;
		linked = false;
		possibleBomb = false;
		SurroundingBombs = 0;

	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void SetBomb() {
		isBomb = true;
	}

	public boolean isBomb() {
		return isBomb;
	}

	public void setMarker(){
		isMarked = true;
	}

	public void removeMarker(){
		isMarked = false;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void Flag() {
		isFlagged = true;
	}

	public void unFlag() {
		isFlagged = false;
	}

	public boolean isFlagged() {
		return isFlagged;
	}
	
	public void setNearbyFlags(int n) {
		nearbyFlags = n;
	}
	
	public int getNearbyFlags() {
		return nearbyFlags;
	}

	public void Reveal() {
		isCovered = false;
	}

	public boolean isCovered() {
		return isCovered;
	}
	
	public void setCoveredNearMe(int n, ArrayList<Tiles> temp) {
		coveredNearMe = n;
		hiddensNear = temp;
	}
	
	public ArrayList<Tiles> hiddensNear(){
		return hiddensNear;
	}
	
	public int getCoveredNearMe() {
		return coveredNearMe;
	}

	public void setBombsNearMe(int i) {
		SurroundingBombs = i;
	}

	public int BombsNearMe() {
		return SurroundingBombs;
	}
	
	public void setBombProbability(BigInteger n) {
		ProbableBomb = n;
	}
	
	public BigInteger getBombProbability() {
		return ProbableBomb;
	}
	
	public void setUnknown(boolean u) {
		unknown = u;
	}
	
	public boolean isUnknown() {
		return unknown;
	}
	
	public void link(boolean linkMe) {
		linked = linkMe;
	}
	
	public boolean isLinked() {
		return linked;
	}
	
	public void linkedWith(ArrayList<Tiles> temp) {
		linkedWith = (ArrayList)temp.clone();
	}
	
	public ArrayList<Tiles> linkedWith(){
		return linkedWith;
	}
	
	

}
