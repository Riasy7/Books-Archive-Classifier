package package1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

public class ReadBinaryAndDeserialize {
	/**
	 * This method will read from the binary files created in part 2 and display the records in an interactive menu.
	 */
	public static void do_part3() {
		// hard coded binary files
		String[] binaryFiles = { "Cartoons_Comics.csv.ser", "Hobbies_Collectibles.csv.ser", "Movies_TV_Books.csv.ser",
				"Music_Radio_Books.csv.ser", "Nostalgia_Eclectic_Books.csv.ser", "Old_Time_Radio_Books.csv.ser",
				"Sports_Sports_Memorabilia.csv.ser", "Trains_Planes_Automobiles.csv.ser" };
		// object input stream to read from binary files
		ObjectInputStream ois = null;
		// array of books to store the books from the binary files
		Book[][] books = new Book[binaryFiles.length][];

		// loop to read from each binary file
		for (int i = 0; i < binaryFiles.length; i++) {

			try {
				// create a new object input stream and read the array of books from the binary file
				ois = new ObjectInputStream(new FileInputStream(binaryFiles[i]));
				books[i] = (Book[]) ois.readObject();

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		// interactive menu
		Scanner kb = new Scanner(System.in);
		int selectedFile = 0;
		int currentIndex = 0;

		// loop to display the menu 
		while (true) {
		    System.out.println(
		        "-----------------------------\n" + 
		        "         Main Menu\n" + 
		        "-----------------------------"
		    );
		    System.out.println(
		        "v View the selected file: " + binaryFiles[selectedFile] + " (" + books[selectedFile].length + " records)\n" + 
		        "s Select a file to view\n" + 
		        "x Exit\n" + 
		        "-----------------------------\n" + 
		        "\nEnter Your Choice: "
		    );
		    String input = kb.next();

		    if (input.equals("v")) {
		        while (true) {
		            System.out.println("\nEnter a number (0 to exit): ");
		            int n = kb.nextInt();

		            if (n == 0) {
		                break;
		            }

		            int start, end;
					// check if the number is positive or negative
		            if (n > 0) {
		                start = currentIndex;
		                end = currentIndex + n; 
						// check if the end is greater than the length of the array
		                if (end > books[selectedFile].length) {
		                    end = books[selectedFile].length;
		                    System.out.println("EOF has been reached");
		                    break;
		                }
		            } else { // if the number is negative
		                end = currentIndex + 1;
		                start = currentIndex + n + 1;
						// check if the start is less than 0
		                if (start < 0) {
		                    start = 0;
		                    System.out.println("BOF has been reached");
		                    break;
		                }
		            }
					// display the records depending on the start and end
		            for (int i = start; i < end; i++) {
		                System.out.println(books[selectedFile][i]);
		            }

		            currentIndex = (n > 0) ? end - 1 : start;
		        }
		    } else if (input.equals("s")) {
		        System.out.println(
		            "-----------------------------\n" + 
		            "       File Sub-Menu\n" + 
		            "-----------------------------"
		        );
		        for (int i = 0; i < binaryFiles.length; i++) {
		            System.out.println((i+1) + "  " + binaryFiles[i] + " (" + books[i].length + " records)");
		        }
		        System.out.println(
		            "9  Exit\n" + 
		            "-----------------------------\n" + 
		            "\nEnter Your Choice: "
		        );
		        input = kb.next();
		        if (input.equals("9")) {
		            System.exit(0);
		        } else {
		            selectedFile = Integer.parseInt(input) - 1; 
		            currentIndex = 0; 
		        }
		    } else if (input.equalsIgnoreCase("x")) {
		        System.exit(0);
		    }
		}

	}


	
}
