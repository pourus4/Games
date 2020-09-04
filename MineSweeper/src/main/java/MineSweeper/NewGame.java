package MineSweeper;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class NewGame {
	private boolean human;
	private int bombs, width, height, games;

	NewGame() {
		Object[] options = { "I'll Play", "I want the AI to play" };
		int query = JOptionPane.showOptionDialog(null, "Pick One", "Chose Game Mode", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (query == 0) {
			Human();
		} else {
			AI();
		}
	}

	NewGame(boolean isHuman, int zeroIfDifficultyChange) {
		if (isHuman) {
			if (zeroIfDifficultyChange == 0) {
				Human();
			} else {
				new MineSweeperLogic(width, height, bombs, true);
			}
		} else {
			if (zeroIfDifficultyChange == 0) {
				AI();
			} else {
				new MineSweeperLogic(width, height, bombs, false);
			}
		}
	}

	private void AI(){
		Object[] options = { "Easy(9x9 Grid 15 Bombs)", "Medium(16x16 Grid 50 Bombs)", "Hard(30x16 Grid 105 Bombs)",
				"Custom", "Cancel" };
		JOptionPane query = new JOptionPane("What Difficulty Do You Want the AI to Play On?", JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_CANCEL_OPTION, null, options, options[0]);
		JDialog dialog = query.createDialog(null, "Pick One");
		dialog.pack();
		dialog.setVisible(true);
		Object choice = query.getValue();
		if (choice.equals(options[0])) {
			width = height = 9;
			bombs = 15;
		} else if (choice.equals(options[1])) {
			width = height = 16;
			bombs = 50;
		} else if (choice.equals(options[2])) {
			width = 30;
			height = 16;
			bombs = 105;
		} else if (choice.equals(options[3])) {
			width = CustomDifficulty("What Width Do You Want?", 5, 150);
			height = CustomDifficulty("What Height Do You Want?", 5, 150);
			int bombMax = (width * height);
			bombs = CustomDifficulty("How Many Bombs Do You Want?", 1, bombMax);
		} else {
			JOptionPane.showMessageDialog(null, "closing game.");
			System.exit(0);
		}
		games = CustomDifficulty("How Many Games Should the AI Play?", 10, 1000);

		new MineSweeperAI(width, height, bombs, false, games);

	}

	private void Human() {
		Object[] options = { "Easy(9x9 Grid 10 Bombs)", "Medium(16x16 Grid 40 Bombs)", "Hard(30x16 Grid 99 Bombs)",
				"Custom", "Cancel" };
		JOptionPane query = new JOptionPane("What Difficulty Do You Want to Play On?", JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_CANCEL_OPTION, null, options, options[0]);
		JDialog dialog = query.createDialog(null, "Pick One");
		dialog.pack();
		dialog.setVisible(true);
		Object choice = query.getValue();
		if (choice.equals(options[0])) {
			width = height = 9;
			bombs = 10;
			new MineSweeperLogic(9, 9, 10, true);
		} else if (choice.equals(options[1])) {
			width = height = 16;
			bombs = 40;
			new MineSweeperLogic(16, 16, 40, true);
		} else if (choice.equals(options[2])) {
			width = 30;
			height = 16;
			bombs = 99;
			new MineSweeperLogic(30, 16, 99, true);
		} else if (choice.equals(options[3])) {
			width = CustomDifficulty("What Width Do You Want?", 5, 150);
			height = CustomDifficulty("What Height Do You Want?", 5, 150);
			int bombMax = (width * height);
			bombs = CustomDifficulty("How Many Bombs Do You Want?", 1, bombMax);
			new MineSweeperLogic(width, height, bombs, true);
		} else {
			JOptionPane.showMessageDialog(null, "closing game.");
			System.exit(0);
		}

	}

	private int CustomDifficulty(String str, int a, int b) {
		int max, min;
		if (a > b) {
			min = b;
			max = a;
		} else {
			max = b;
			min = a;
		}

		boolean inputAccepted = false;
		while (!inputAccepted) {
			String input = JOptionPane
					.showInputDialog(str + " Min: " + String.valueOf(min) + " Max: " + String.valueOf(max));

			try {
				int value = Integer.parseInt(input);
				if (value >= min && value <= max) {// User Input inside of acceptable range
					inputAccepted = true;
					return value;
				} else
					inputAccepted = false;

			} catch (NumberFormatException e) {
				inputAccepted = false;
				JOptionPane.showMessageDialog(null, "Please input a number only");
			}

		}

		return 0;

	}

}
