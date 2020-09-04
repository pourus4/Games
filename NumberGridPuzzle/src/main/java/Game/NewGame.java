package Game;

import javax.swing.*;

public class NewGame {
    private int boardWidth, boardLength;
    public static void main(String[] args) {
        new NewGame(3, 10);
    }

    public NewGame(int min, int max) {
        difficulty: do {
            Object[] options = { "4x4", "5x5", "6x6", "7x7", "Custom", "Change Game" };
            int choice = JOptionPane.showOptionDialog(null, "What Difficulty Would You Like To Play?", "Pick One",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[4]);
            if (choice == 0) {
                boardWidth = boardLength = 4;
                break difficulty;
            } else if (choice == 1) {
                boardWidth = boardLength = 5;
                break difficulty;
            } else if (choice == 2) {
                boardWidth = boardLength = 6;
                break difficulty;
            } else if (choice == 3) {
                boardWidth = boardLength = 7;
                break difficulty;
            } else if (choice == 4) {
                boardWidth = customDifficulty(min, max);
                boardLength = customDifficulty(min, max);
                break difficulty;
            } else if (choice == 5) {
                System.exit(0);
            }
        } while (true);
        new GameLogic(boardWidth, boardLength);
    }

    private int customDifficulty(int min, int max) {
        String str = "Enter a number between " + min + " and " + max + " inclusive.";
       do{
            String input = JOptionPane.showInputDialog(null, str);
            try{
                int num = Integer.parseInt(input);
                if(num >= min && num <= max){
                    return num;
                } else {
                    str = "Input Must Be Between " + min + " and " + max + ".";
                    if(num < min){
                        str += " " + num + " is too low.";
                    } else if(num > max){
                        str += " " + num + " is too high.";
                    }
                }
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Please Enter a Number.", "ERROR: INVALID INPUT!", JOptionPane.ERROR_MESSAGE);
            }
        }while(true);
    }

}