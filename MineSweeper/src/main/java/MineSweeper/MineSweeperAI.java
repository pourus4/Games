package MineSweeper;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * 
 * @author Connor Version 2 - Starts using tank solving algorithm.
 */

public class MineSweeperAI {
	private MineSweeperLogic mine;
	private Tiles[][] mapTile;
	private int mapWidth, mapHeight, bombs, games, wins, losses;
	private long runTime = System.currentTimeMillis();
	private static int BForceLimit = 8;
	private static boolean endGame;
	private boolean[][] targeted, flagged, revealed;
	private boolean earlyGame = true;
	private ArrayList<Target> targets = new ArrayList<Target>();
	private ArrayList<boolean[]> solutions;
	private boolean debug = true;

	MineSweeperAI(int w, int h, int b, boolean a, int g) {
		mine = new MineSweeperLogic(w, h, b, a);
		mapTile = mine.getTiles();
		mapWidth = mine.getWidth();
		mapHeight = mine.getHeight();
		bombs = b;
		targeted = new boolean[mapWidth][mapHeight];
		games = g;
		wins = 0;
		losses = 0;

		nextTarget();
	}

	public void nextTarget() {
		int played = 0;
		try {
			do {
				do {
					switch (targets.size()) {
						case 0:
							pickTargets(earlyGame);
						default:
							clickTarget();
							Thread.sleep(50);
					}
				} while (!mine.isFinished());
				played++;
				gameOver(mine.botGameOver());
			} while (played < games);
			done();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		}
	}

	private void clickTarget() {
		do {
			Target temp = targets.get(0);
			targets.remove(0);
			int x = temp.getX();
			int y = temp.getY();
			if (temp.isMine()) {
				mine.Mark(x, y);
				System.out.println("Target Coordinates: " + temp.getX() + ", " + temp.getY() + " flagged");
			} else {
				mine.select(x, y);
				if (mapTile[x][y].BombsNearMe() == 0) {
					SafeArea(x, y);
				}
				System.out.println("Target Coordinates: " + temp.getX() + ", " + temp.getY() + " safe");
			}
		} while (!targets.isEmpty());
		return;
	}

	private void pickTargets(boolean firstMove) {
		// targeted = findTargetedTiles();
		if (firstMove) {
			randomTarget(firstMove);
		} else {
			basicAlgorithm(true);
			System.out.println("Finished checking for guarenteed mines.  Targets Size is: " + targets.size());
			basicAlgorithm(false);
			System.out.println("Finished checking for guarenteed safe tiles.  Targets Size is: " + targets.size());
			if (targets.size() == 0) {
				System.out.println("Tank Solver");
				TankSolver();
				if (targets.size() == 0) {
					System.out.println("Guessing.");
					randomTarget(earlyGame);
				}
			}

		}
		return;
	}

	private void basicAlgorithm(boolean mines) {
		if (mines) {
			for (int x = 0; x < mapWidth; x++) {
				for (int y = 0; y < mapHeight; y++) {
					if (!mapTile[x][y].isCovered() && mapTile[x][y].BombsNearMe() != 0) {
						nearbyFlags(mapTile[x][y]);
						int coveredNear = CoveredNearMe(mapTile[x][y]) - mapTile[x][y].getNearbyFlags();
						if (coveredNear == mapTile[x][y].BombsNearMe() - mapTile[x][y].getNearbyFlags()) {
							addCoveredNearby(mapTile[x][y], true);
							continue;
						}
						System.out.print("");
					}
				}
			}
		} else {
			for (int x = 0; x < mapWidth; x++) {
				for (int y = 0; y < mapHeight; y++) {
					nearbyFlags(mapTile[x][y]);
					if (!mapTile[x][y].isCovered() && mapTile[x][y].BombsNearMe() != 0) {
						if (mapTile[x][y].BombsNearMe() == mapTile[x][y].getNearbyFlags()) {
							addCoveredNearby(mapTile[x][y], false);
							continue;
						} else if (mapTile[x][y].BombsNearMe() <= mapTile[x][y].getNearbyFlags()) {
							System.out.println("fek");
						}
						System.out.print("");
					}

				}
			}
		}
		return;
	}

	private void addCoveredNearby(Tiles tile, boolean b) {
		int checked = 0;
		int xMod = -1;
		int x = tile.getX();
		int y = tile.getY();
		do {
			do {
				int yMod = -1;
				do {
					try {
						if (mapTile[x + xMod][y + yMod].isCovered() && !targeted[x + xMod][y + yMod]) {
							targets.add(new Target(x + xMod, y + yMod, b));
							targeted[x + xMod][y + yMod] = true;
						}
						checked++;
					} catch (ArrayIndexOutOfBoundsException e) {
						checked++;
					}
					yMod++;
				} while (yMod <= 1);
				xMod++;
			} while (xMod <= 1);
		} while (checked < 8);
	}

	private void findTargetedTiles() {
		targeted = new boolean[mapWidth][mapHeight];
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if ((!mapTile[x][y].isCovered() || mapTile[x][y].isFlagged()) && !targeted[x][y]) {
					targeted[x][y] = true;
				} else if (mapTile[x][y].isCovered() && !mapTile[x][y].isFlagged()) {
					targeted[x][y] = false;
				}
			}
		}
	}

	private void randomTarget(boolean firstMove) {
		if (firstMove) {
			int i = 0;
			Random rand = new Random();

			int xMax = mapWidth - 1;
			int yMax = mapHeight - 1;
			assignTargets: do {
				boolean inputAccepted = false;
				int num = rand.nextInt(3);
				switch (num) {
					case 0:
						num = rand.nextInt(4);
						switch (num) {
							case 0:
								if (!targeted[1][1]) {
									targets.add(new Target(1, 1, false));
									targeted[1][1] = true;
									inputAccepted = true;
								}
								break;
							case 1:
								if (!targeted[xMax - 1][1]) {
									targets.add(new Target(xMax - 1, 1, false));
									targeted[xMax - 1][1] = true;
									inputAccepted = true;
								}
								break;
							case 2:
								if (!targeted[1][yMax - 1]) {
									targets.add(new Target(1, yMax - 1, false));
									targeted[1][yMax - 1] = true;
									inputAccepted = true;
								}
								break;
							case 3:
								if (!targeted[xMax - 1][yMax - 1]) {
									targets.add(new Target(xMax - 1, yMax - 1, false));
									targeted[xMax - 1][yMax - 1] = true;
									inputAccepted = true;
								}
						}
						break;
					case 1:
						num = rand.nextInt(4);
						switch (num) {
							case 0:
								if (!targeted[xMax / 2][1]) {
									targets.add(new Target(xMax / 2, 1, false));
									targeted[xMax / 2][1] = true;
									inputAccepted = true;
								}
								break;
							case 1:
								if (!targeted[xMax / 2][yMax]) {
									targets.add(new Target(xMax / 2, yMax, false));
									targeted[xMax / 2][yMax] = true;
									inputAccepted = true;
								}
								break;
							case 2:
								if (!targeted[1][yMax / 2]) {
									targets.add(new Target(1, yMax / 2, false));
									targeted[1][yMax / 2] = true;
									inputAccepted = true;
								}
								break;
							case 3:
								if (!targeted[xMax][yMax / 2]) {
									targets.add(new Target(xMax, yMax / 2, false));
									targeted[xMax][yMax / 2] = true;
									inputAccepted = true;
								}
								break;
						}
						break;
					case 2:
						if (!targeted[xMax / 2][yMax / 2]) {
							targets.add(new Target(xMax / 2, yMax / 2, false));
							targeted[xMax / 2][yMax / 2] = true;
							inputAccepted = true;
						}

				}

				if (inputAccepted) {
					i++;
				}
			} while (i < 3);
			earlyGame = false;
		} else {
			System.out.println("Random Target");
			ArrayList<Tiles> possibleTarget = new ArrayList<Tiles>();
			for (int x = 0; x < mapWidth; x++) {
				for (int y = 0; y < mapHeight; y++) {
					if (!targeted[x][y]) {
						possibleTarget.add(mapTile[x][y]);
					}
				}
			}
			try {
				Random n = new Random();
				int index = n.nextInt(possibleTarget.size());
				int x = possibleTarget.get(index).getX();
				int y = possibleTarget.get(index).getY();
				if (!targeted[x][y]) {
					targets.add(new Target(x, y, false));
					targeted[x][y] = true;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Caught Exception");
				findTargetedTiles();
				randomTarget(earlyGame);
			}
		}
		return;
	}

	/*
	 * contains the decision making logic for the TankSolver(which tile(s) it will
	 * pick)
	 */
	private void TankSolver() {
		ArrayList<Tiles> emptyTiles = new ArrayList<Tiles>();
		ArrayList<Tiles> borderTiles = new ArrayList<Tiles>();

		endGame = true;
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (!mapTile[x][y].isFlagged() && mapTile[x][y].isCovered()) {
					emptyTiles.add(mapTile[x][y]);
				}
			}
		}

		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (borderTile(x, y) && !mapTile[x][y].isFlagged()) {
					borderTiles.add(mapTile[x][y]);
				}
			}
		}

		int squaresToCheck = emptyTiles.size() - borderTiles.size();

		if (squaresToCheck > BForceLimit) {
			endGame = false;
		} else {
			borderTiles = emptyTiles;
		}

		ArrayList<ArrayList<Tiles>> sections;

		if (!endGame) {
			sections = splitSections(borderTiles);
		} else {
			sections = new ArrayList<ArrayList<Tiles>>();
			sections.add(borderTiles);
		}
		int totalCases = 1;
		double bestProbability = 0;
		int bestProbI = -1;
		int bestSectionProbability = -1;
		boolean usable = false;
		boolean success = false;
		for (int s = 0; s < sections.size(); s++) {
			flagged = new boolean[mapWidth][mapHeight];
			flagged = revealedOrFlagged(false);
			revealed = new boolean[mapWidth][mapHeight];
			revealed = revealedOrFlagged(true);

			solutions = new ArrayList<boolean[]>();

			ArrayList<Tiles> VisibleBorderTiles = VisibleBorderTiles(sections.get(s));

			int m = 0;
			for (Tiles tile : VisibleBorderTiles) {// this is so we can see how many bombs max could be in the section.
				m += (tile.BombsNearMe() - getNearbyFlags(tile));
			}

			if (VisibleBorderTiles.size() == 1) {
				continue;
			} else {
				usable = true;
			}
			TankRecurse(sections.get(s), VisibleBorderTiles, solutions, 0, m);
			if (solutions.size() == 0) {
				System.out.println("Something went wrong, retrying.");
				return;
			}
			System.out.println("Tank Recurser Finished");

			for (int i = 0; i < sections.get(s).size(); i++) {
				boolean allSafe = true;
				boolean allMines = true;

				for (boolean[] sol : solutions) {
					if (sol[i]) {
						allSafe = false;
					} else {
						allMines = false;
					}
				}
				int x = sections.get(s).get(i).getX();
				int y = sections.get(s).get(i).getY();

				if (allMines) {
					System.out.println("Tank Solver Placing Flag at " + x + ", " + y + ".");
					targets.add(new Target(x, y, true));
					targeted[x][y] = true;
				} else if (allSafe) {
					System.out.println("Tank Solver Revealing Tile at " + x + ", " + y + ".");
					success = true;
					targets.add(new Target(x, y, false));
					targeted[x][y] = true;
				}
			}

			totalCases *= solutions.size();

			if (success) {
				continue;
			} else {
				int maxEmpty = Integer.MIN_VALUE;
				int iEmpty = -1;
				for (int i = 0; i < sections.get(s).size(); i++) {
					int nEmpty = 0;
					for (boolean[] sol : solutions) {
						if (!sol[i]) {
							nEmpty++;
						}
					}
					if (nEmpty > maxEmpty) {
						maxEmpty = nEmpty;
						iEmpty = i;
					}
				}
				double probability = (double) maxEmpty / (double) solutions.size();

				if (probability > bestProbability) {
					bestProbability = probability;
					bestProbI = iEmpty;
					bestSectionProbability = s;
				}
			}

		}
		if (usable) {
			if (BForceLimit == 8 && squaresToCheck > 8 && squaresToCheck <= 13) {
				System.out.println("Extending Brute Force Horrizons");
				BForceLimit = 13;
				TankSolver();
				BForceLimit = 8;
				return;
			}
			runTime = System.currentTimeMillis() - runTime;
			int flagsPlaced = bombs - mine.getFlagsRemaining();
			if (success) {
				System.out.printf("TANK Solver successfully invoked at step %d (%dms, %d cases)%s\n", flagsPlaced,
						runTime, totalCases, (!endGame ? "" : "*"));
				return;
			}
			System.out.printf("TANK Solver guessing with probability %1.2f at step %d (%dms, %d cases)%s\n",
					bestProbability, flagsPlaced, runTime, totalCases, (!endGame ? "" : "*"));
			int x = sections.get(bestSectionProbability).get(bestProbI).getX();
			int y = sections.get(bestSectionProbability).get(bestProbI).getY();
			targets.add(new Target(x, y, false));
		} else{
			return;
		}
	}

	private ArrayList<ArrayList<Tiles>> splitSections(ArrayList<Tiles> border) {
		ArrayList<ArrayList<Tiles>> result = new ArrayList<ArrayList<Tiles>>();
		for (int i = 0; i < border.size() - 1; i++) {
			Tiles temp = border.get(i);
			boolean inSection = false;
			for (int k = 0; k < result.size(); k++) {
				if (result.get(k).contains(temp)) {
					inSection = true;
					break;
				}
			}
			if (!inSection) {
				ArrayList<Tiles> newSection = new ArrayList<Tiles>();
				int x = temp.getX();
				int y = temp.getY();
				newSection = createBorderSection(newSection, border, x, y);
				result.add(newSection);
			}
		}
		return result;
	}

	private ArrayList<Tiles> createBorderSection(ArrayList<Tiles> sec, ArrayList<Tiles> borderTiles, int x, int y) {
		int checked = 0;
		int xMod = -1;
		do {
			do {
				int yMod = -1;
				do {
					try {
						Tiles temp = mapTile[x + xMod][y + yMod];
						if (borderTiles.contains(temp) && !sec.contains(temp)) {
							sec.add(temp);
							createBorderSection(sec, borderTiles, x + xMod, y + yMod);
						}
						checked++;
					} catch (ArrayIndexOutOfBoundsException e) {
						checked++;
					}
					yMod++;
				} while (yMod <= 1);
				xMod++;
			} while (xMod <= 1);
		} while (checked < 8);
		return sec;
	}

	private ArrayList<Tiles> VisibleBorderTiles(ArrayList<Tiles> sec) {
		ArrayList<Tiles> vBT = new ArrayList<Tiles>();
		for (Tiles tile : sec) {
			int x = tile.getX();
			int y = tile.getY();
			int checked = 0;
			int xMod = -1;
			do {
				do {
					int yMod = -1;
					do {
						try {
							if (!mapTile[x + xMod][y + yMod].isCovered()
									&& !vBT.contains(mapTile[x + xMod][y + yMod])) {
								vBT.add(mapTile[x + xMod][y + yMod]);
							}
							checked++;
						} catch (ArrayIndexOutOfBoundsException e) {
							checked++;
						}
						yMod++;
					} while (yMod <= 1);
					xMod++;
				} while (xMod <= 1);
			} while (checked < 8);
		}
		return vBT;
	}

	private ArrayList<boolean[]> TankRecurse(ArrayList<Tiles> sec, ArrayList<Tiles> vBT, ArrayList<boolean[]> sol,
			int k, int m) {
		int secBombs = 0;
		int flagCount = 0;
		if(sol.size() == Integer.MAX_VALUE-2){
			System.out.println("To fuckin much");
			System.out.print("");
		}
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (flagged[x][y]) {
					flagCount++;
				}
			}
		}
		if (flagCount > bombs) {
			return null;
		}
		if (k == sec.size()) {

			if (endGame && flagCount < bombs) {
				return null;
			} else {
				boolean[] solution = new boolean[sec.size()];
				for (int i = 0; i < sec.size(); i++) {
					int x = sec.get(i).getX();
					int y = sec.get(i).getY();
					solution[i] = flagged[x][y];
					if (flagged[x][y]) {
						secBombs++;
					}
				}
				if (secBombs > m) {
					return null;
				} else {
					sol.add(solution);
					return sol;
				}

			}

		} else {
			for (Tiles tile : vBT) {
				int x = tile.getX();
				int y = tile.getY();

				int bNMe = freeNearMe(flagged, x, y);
				int freeNearMe = freeNearMe(revealed, x, y);

				int surround = 0;

				if ((x == 0 && y == 0) || (x == mapWidth - 1 && y == mapHeight - 1)) {
					surround = 3;
				} else if ((x == 0 && y == mapHeight - 1) || (x == mapWidth - 1 && y == 0)) {
					surround = 3;
				} else if (x == 0 || y == 0 || x == mapWidth - 1 || y == mapHeight - 1) {
					surround = 5;
				} else {
					surround = 8;
				}

				if (bNMe > tile.BombsNearMe()) {
					return null;
				} else if (surround - freeNearMe < tile.BombsNearMe()) {
					return null;
				}
			}
			for (Tiles tile : sec) {
				int x = tile.getX();
				int y = tile.getY();
				if (flagged[x][y]) {
					secBombs++;
				}
			}
			if (secBombs > m) {
				return null;
			} else {
				Tiles temp = sec.get(k);
				int x = temp.getX();
				int y = temp.getY();

				flagged[x][y] = true;
				TankRecurse(sec, vBT, sol, k + 1, m);
				flagged[x][y] = false;

				revealed[x][y] = true;
				TankRecurse(sec, vBT, sol, k + 1, m);
				revealed[x][y] = false;
			}
		}
		return null;
	}

	private int CoveredNearMe(Tiles tile) {
		int covered = 0;
		int checked = 0;
		int xMod = -1;
		int x = tile.getX();
		int y = tile.getY();
		do {
			do {
				int yMod = -1;
				do {
					try {
						if (mapTile[x + xMod][y + yMod].isCovered()) {
							covered++;
						}
						checked++;
					} catch (ArrayIndexOutOfBoundsException e) {
						checked++;
					}
					yMod++;
				} while (yMod <= 1);
				xMod++;
			} while (xMod <= 1);
		} while (checked < 8);
		return covered;
	}

	private int freeNearMe(boolean[][] visible, int x, int y) {
		int result = 0;
		int checked = 0;
		int xMod = -1;
		do {
			do {
				int yMod = -1;
				do {
					try {
						if (visible[x + xMod][y + yMod] && (xMod != 0 && yMod != 0)) {
							result++;
						}
						checked++;
					} catch (ArrayIndexOutOfBoundsException e) {
						checked++;
					}
					yMod++;
				} while (yMod <= 1);
				xMod++;
			} while (xMod <= 1);
		} while (checked < 8);

		return result;
	}

	private int getNearbyFlags(Tiles tile) {
		int x = tile.getX();
		int y = tile.getY();
		nearbyFlags(tile);
		return mapTile[x][y].getNearbyFlags();
	}

	private void nearbyFlags(Tiles tile) {// sets the amount of flags that are in range of a tile.
		int flags = 0;
		int checked = 0;
		int xMod = -1;
		int x = tile.getX();
		int y = tile.getY();
		do {
			do {
				int yMod = -1;
				do {
					try {
						if (mapTile[x + xMod][y + yMod].isFlagged()) {
							flags++;
						}
						checked++;
					} catch (Exception e) {
						checked++;
					}
					yMod++;
				} while (yMod <= 1);
				xMod++;
			} while (xMod <= 1);
		} while (checked < 8);

		mapTile[x][y].setNearbyFlags(flags);
	}

	private boolean borderTile(int x, int y) {
		if (mapTile[x][y].isCovered()) {
			int checked = 0;
			int xMod = -1;

			do {
				do {
					int yMod = -1;
					do {
						try {
							if (!mapTile[x + xMod][y + yMod].isCovered()) {
								return true;
							}
							checked++;
						} catch (ArrayIndexOutOfBoundsException e) {
							checked++;
						}
						yMod++;
					} while (yMod <= 1);
					xMod++;
				} while (xMod <= 1);
			} while (checked < 8);
		}
		return false;
	}

	private void gameOver(boolean wl) {
		if (wl) {
			wins++;
			System.out.println("noice");
		} else {
			System.out.println("fuck");
			losses++;
		}
		earlyGame = true;
		mine = new MineSweeperLogic(mapWidth, mapHeight, bombs, false);
		mapTile = mine.getTiles();
		targeted = new boolean[mapWidth][mapHeight];
		return;
	}

	private void done() {
		int winPercent = wins * 100;
		winPercent = winPercent / games;
		int lossPercent = losses * 100;
		lossPercent = lossPercent / games;
		JOptionPane.showMessageDialog(null,
				"The AI won " + winPercent + "% and lost " + lossPercent + "% out of " + games + " games");

	}

	private void SafeArea(int x, int y) {
		boolean[][] safeTiles = new boolean[mapWidth][mapHeight];
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				safeTiles[i][j] = false;
			}
		}

		safeTiles[x][y] = true;
		SafeAreaArray(x, y, safeTiles);

		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (safeTiles[i][j]) {
					targeted[i][j] = true;
					mine.select(i, j);
				}
			}
		}
	}

	private boolean[][] SafeAreaArray(int x, int y, boolean[][] tested) {
		int checked = 0;
		int xMod = -1;

		do {
			do {
				int yMod = -1;
				do {
					try {
						if (mapTile[x + xMod][y + yMod].BombsNearMe() == 0 && !tested[x + xMod][y + yMod]) {
							tested[x + xMod][y + yMod] = true;
							SafeAreaArray(x + xMod, y + yMod, tested);
						} else {
							targeted[x + xMod][y + yMod] = true;
							mine.select(x + xMod, y + yMod);
						}
						checked++;
					} catch (ArrayIndexOutOfBoundsException e) {
						checked++;
					}
					yMod++;
				} while (yMod <= 1);
				xMod++;
			} while (xMod <= 1);
		} while (checked < 8);
		return tested;
	}

	private boolean[][] revealedOrFlagged(boolean rF) {
		boolean[][] tested = new boolean[mapWidth][mapHeight];
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (rF && !mapTile[x][y].isCovered()) {
					tested[x][y] = true;
				} else if (!rF && mapTile[x][y].isFlagged()) {
					tested[x][y] = true;
				} else {
					tested[x][y] = false;
				}
			}
		}
		return tested;
	}

	private void debug() {
		Object[] options = { "YES.", "NO." };
		int x = 0;
		int y = 0;
		int choice = JOptionPane.showOptionDialog(null, "Do You Want To Manually Select the Tile to test?", null,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (choice == JOptionPane.YES_OPTION) {
			boolean inputAccepted = false;

			do {
				String input = JOptionPane
						.showInputDialog("Enter the X-Coordinate of the tile you want to check.  Between " + 0 + " and "
								+ (mapWidth - 1) + " inclusive.");
				try {
					int value = Integer.parseInt(input);
					if (value < 0 || value >= mapWidth) {
						JOptionPane.showMessageDialog(null, "enter a value inside of the range.",
								"ERROR: INVALID INPUT!", JOptionPane.ERROR_MESSAGE);
					} else {
						x = value;
						inputAccepted = true;
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Enter Numbers Only.", "ERROR: INVALID INPUT!",
							JOptionPane.ERROR_MESSAGE);
				}
			} while (!inputAccepted);
			inputAccepted = false;
			do {
				String input = JOptionPane
						.showInputDialog("Enter the Y-Coordinate of the tile you want to check.  Between " + 0 + " and "
								+ (mapHeight - 1) + " inclusive.");
				try {
					int value = Integer.parseInt(input);
					if (value < 0 || value >= mapHeight) {
						JOptionPane.showMessageDialog(null, "enter a value inside of the range.",
								"ERROR: INVALID INPUT!", JOptionPane.ERROR_MESSAGE);
					} else {
						y = value;
						inputAccepted = true;
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Enter Numbers Only.", "ERROR: INVALID INPUT!",
							JOptionPane.ERROR_MESSAGE);
				}
			} while (!inputAccepted);
		} else {
			Random rand = new Random();
			x = rand.nextInt(mapWidth);
			y = rand.nextInt(mapHeight);
		}

		int surround = 0;
		int checked = 0;
		int xMod = -1;
		do {
			do {
				int yMod = -1;
				do {
					try {
						if (mapTile[x + xMod][y + yMod].isCovered() || !mapTile[x + xMod][y + yMod].isCovered()
								|| !mapTile[x + xMod][y + yMod].isFlagged() && !(xMod == 0 && yMod == 0)) {
							surround++;
						}
						checked++;
					} catch (ArrayIndexOutOfBoundsException e) {
						checked++;
					}
					yMod++;
				} while (yMod <= 1);
				xMod++;
			} while (xMod <= 1);
		} while (checked < 8);
		surround--;
		String message = "There are " + surround + " Tiles surrounding the Tile at (" + x + ", " + y
				+ ").  Would you like to check another Tile?";
		choice = JOptionPane.showOptionDialog(null, message, null, JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		if (choice == JOptionPane.YES_OPTION) {
			debug();
		} else {
			System.exit(0);
		}
	}

}
