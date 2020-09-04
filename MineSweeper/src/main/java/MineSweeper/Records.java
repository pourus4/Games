package MineSweeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Records {
	private char difficulty;
	
	public Records(int width, int height, int bombs) {
		if(width == 9 && height == 9 && bombs == 10) {
			difficulty = 'E'; //easy mode
		} else if(width == 16 && height == 16 && bombs == 40) {
			difficulty = 'M'; //medium mode
		} else if(width == 30 && height == 16 && bombs == 99) {
			difficulty = 'H'; //hard mode
		} else {
			throw new IllegalArgumentException("Records Are Not Available For Custom Games");
		}
	}
	
	private void bestWinTime() {
		ClassLoader classLoader = getClass().getClassLoader();
		File records = new File(classLoader.getResource("timeRecords.txt").getFile());
		System.out.println(records.getAbsolutePath());
		try {
			Scanner sc = new Scanner(new FileInputStream(records));
			String line;
			do{
				line = sc.nextLine();
				if(line.charAt(0) == difficulty){
					break;
				}
			}while(sc.hasNextLine());
			
// 			FileWriter fw = new FileWriter(records);
//			fw.write(gameTime.runTime());
// 			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
