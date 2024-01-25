// Java Program
//
// -----------------------------------------------------
// Books Archive Classifier
// Written by Ahmad Saadawi
// Github: https://github.com/Riasy7
// repo: https://github.com/Riasy7/Books-Archive-Classifier
// -----------------------------------------------------
/**
 * This program will sort through a list of files and check for syntax and semantic errors. 
 * It will then write the valid records to a file and the errors to another file. It will then create a binary file and read from it.
 * It will then display the records in the binary file in an interactive menu.
 * @author Ahmad Saadawi
 */
package package1;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;
import java.io.ObjectOutputStream;

/**
 * This is the main Driver Class that will run the program and call the methods
 */
public class Driver {
	// counters for each genre and error files
	private static int ccbCounter = 1;
	private static int hcbCounter = 1;
	private static int mtvCounter = 1;
	private static int mrbCounter = 1;
	private static int nebCounter = 1;
	private static int otrCounter = 1;
	private static int ssmCounter = 1;
	private static int tpaCounter = 1;
	private static int syntaxErrorCounter = 1;
	private static int semanticErrorCounter = 1;

	/**
	 * This method will first read from a file that contains the names of the files.
	 * It will then check for syntax errors and write the valid records to their respective genre based files and the errors to another file.
	 * @throws FileNotFoundException
	 * @throws TooManyFieldsException
	 * @throws TooFewFieldsException
	 * @throws MissingFieldException
	 * @throws UnknownGenreException
	 */
	public static void do_part1() throws FileNotFoundException, TooManyFieldsException, TooFewFieldsException,
			MissingFieldException, UnknownGenreException {
		// scanner to read from file containing file names
		Scanner scanner = new Scanner(new File("/Users/ahmadsaadawi/eclipse-workspace/249Assignment2/part1_input_file_names.txt"));
		int numOfFiles = scanner.nextInt();
		scanner.nextLine();
		// hard coded output files
		String[] outputFiles = { "Cartoons_Comics.csv", "Hobbies_Collectibles.csv", "Movies_TV_Books.csv",
				"Music_Radio_Books.csv", "Nostalgia_Eclectic_Books.csv", "Old_Time_Radio_Books.csv",
				"Sports_Sports_Memorabilia.csv", "Trains_Planes_Automobiles.csv", "syntax_error_file.txt" };
		// array of print writers to create the output files from the array above
		PrintWriter[] writers = new PrintWriter[outputFiles.length];
		for (int i = 0; i < writers.length; i++) {
			writers[i] = new PrintWriter(new File(outputFiles[i]));
			System.out.println("Created output file: " + outputFiles[i]);
		}
		// loop to read from each file
		for (int i = 0; i < numOfFiles; i++) {
			String inputFileName = scanner.nextLine();
			Scanner inputFile = null;
			try {
				inputFile = new Scanner(new File("/Users/ahmadsaadawi/eclipse-workspace/249Assignment2/" + inputFileName));
				// loop to read from each line of each file
				while (inputFile.hasNextLine()) { 

					String line = inputFile.nextLine();
					String[] fields = null;
					// Check if the line does not start with a quote
					if (line.charAt(0) != '"') {
						// Split the line into 6 fields, the -1 is so that the last field is not ignored if it is empty
						fields = line.split(",", -1);
					}
					// Check if the line starts with a quote
					else if (line.charAt(0) == '"') {
						// Split the line into 2 fields, the -1 is so that the last field is not ignored if it is empty
						String[] tempArray1 = line.split("\",", 2); // split the line into 2 fields
						fields = new String[1 + tempArray1[1].split(",", -1).length]; // create a new array with the correct length
						fields[0] = tempArray1[0].substring(1); // copy the first field
						String[] tempArray2 = tempArray1[1].split(",", -1); // split the second field into an array
						for (int j = 0; j < tempArray2.length; j++) { // copy the second field into the new array
							fields[j + 1] = tempArray2[j];
						}
					}

					try {
						// Check if there are too many, too few fields, or missing fields
						if (fields.length > 6) {
							throw new TooManyFieldsException(
									"Exception: Too many fields in file: " + inputFileName + " record: " + line);
						} else if (fields.length < 6) {
							throw new TooFewFieldsException(
									"Exception: Too few fields in file: " + inputFileName + " record: " + line);
						} else {
							for (int j = 0; j < fields.length; j++) {
								if (("".equals(fields[j]) || fields[j] == null) && fields.length == 6) {
									throw new MissingFieldException(
											"Exception: Missing Field at: " + inputFileName + " record: " + line);
								}
							}
						}
					} catch (TooManyFieldsException | TooFewFieldsException | MissingFieldException e) {
						System.out.println(e.getMessage());
						try {
							// if there are too many, too few, or missing fields, write to error file
							writeToSyntaxErrorFile(inputFileName, e, fields, syntaxErrorCounter++);
						} catch (FileNotFoundException ex) {
							System.out.println("Error writing to error file: " + ex.getMessage());
						}
						continue;
					}
					// for display purposes
					for (int j = 0; j < fields.length; j++) {
						System.out.println(fields[j]);

					}
					// Check if the genre is valid after for too many, too few, and missing fields
					if (fields.length == 6) {

						if (isValidGenre(fields[4])) {
							System.out.println("This Record is valid: " + line);

							if (fields[4].equals("CCB")) {
								writeToGenreFile("CCB", fields, ccbCounter++, syntaxErrorCounter);
							}
							if (fields[4].equals("HCB")) {
								writeToGenreFile("HCB", fields, hcbCounter++, syntaxErrorCounter);
							}
							if (fields[4].equals("MTV")) {
								writeToGenreFile("MTV", fields, mtvCounter++, syntaxErrorCounter);
							}
							if (fields[4].equals("MRB")) {
								writeToGenreFile("MRB", fields, mrbCounter++, syntaxErrorCounter);

							}
							if (fields[4].equals("NEB")) {
								writeToGenreFile("NEB", fields, nebCounter++, syntaxErrorCounter);

							}
							if (fields[4].equals("OTR")) {
								writeToGenreFile("OTR", fields, otrCounter++, syntaxErrorCounter);
							}
							if (fields[4].equals("SSM")) {
								writeToGenreFile("SSM", fields, ssmCounter++, syntaxErrorCounter);

							}
							if (fields[4].equals("TPA")) {
								writeToGenreFile("TPA", fields, tpaCounter++, syntaxErrorCounter);

							}

						}
						// if the genre is not valid, write to error file
						else {
							UnknownGenreException exception = new UnknownGenreException("Unknown genre: " + fields[4]);
							try {
								writeToSyntaxErrorFile(inputFileName, exception, fields, syntaxErrorCounter++);
							} catch (FileNotFoundException e) {

							}
						}

					}

				}
				inputFile.close();
			} catch (FileNotFoundException e) {
				System.out.println("File not found: " + inputFileName);
				continue;
			} finally {
				if (inputFile != null) {
					inputFile.close();
				}
			}
		}
		for (PrintWriter writer : writers) {
			writer.close();
		}

	}
	/**
	 * This method will read from the files created in part 1 and check for semantic errors.
	 * It will then write the valid records to a binary file and the errors to another file.
	 * @throws BadIsbn10Exception
	 * @throws BadIsbn13Exception
	 * @throws BadPriceException
	 * @throws BadYearException
	 */
	public static void do_part2() throws BadIsbn10Exception, BadIsbn13Exception, BadPriceException, BadYearException {
		// hard coded input and output files
		String[] csvFiles = { "Cartoons_Comics.csv", "Hobbies_Collectibles.csv", "Movies_TV_Books.csv",
				"Music_Radio_Books.csv", "Nostalgia_Eclectic_Books.csv", "Old_Time_Radio_Books.csv",
				"Sports_Sports_Memorabilia.csv", "Trains_Planes_Automobiles.csv" };
		String[] binaryFiles = { "Cartoons_Comics.csv.ser", "Hobbies_Collectibles.csv.ser", "Movies_TV_Books.csv.ser",
				"Music_Radio_Books.csv.ser", "Nostalgia_Eclectic_Books.csv.ser", "Old_Time_Radio_Books.csv.ser",
				"Sports_Sports_Memorabilia.csv.ser", "Trains_Planes_Automobiles.csv.ser" };
		// object output stream to write to binary files and scanner to read from csv files
		ObjectOutputStream oos = null;
		Scanner scanner = null;

		// loop to read from each file
		for (int i = 0; i < binaryFiles.length; i++) {
			try {
				scanner = new Scanner(new File(csvFiles[i]));
				int count = 0;
				// loop to count the number of lines in each file
				while (scanner.hasNextLine()) {
					scanner.nextLine();
					count++;
				}
				// create a temporary array of books to store the books (this is to get the size of the array and disregard the null values that were errors written to the error file)
				Book[] tempBooks = new Book[count];
				scanner = new Scanner(new File(csvFiles[i])); 
				int index = 0;
				// loop to read from each line of each file
				while (scanner.hasNextLine()) {
					// split the line into fields (tab delimited)
					String line = scanner.nextLine();
					String[] fields = line.split("\t");
					try {
						// check for semantic errors
						if (!(validateIsbn10(fields[3]) || validateIsbn13(fields[3]))) {
							System.out.println("ISBN: " + fields[3]);
							if (!validateIsbn10(fields[3])) {
								throw new BadIsbn10Exception("Invalid ISBN 10 in file: " + csvFiles[i]);
							}
							if (!validateIsbn13(fields[3])) {
								throw new BadIsbn13Exception("Invalid ISBN 13 in file: " + csvFiles[i]);
							}
						}
						if (!validatePrice(fields[2])) {
							System.out.println("Price: " + fields[2]);
							throw new BadPriceException("Invalid price in file: " + csvFiles[i]);
						}
						if (!validateYear(fields[5])) {
							System.out.println("Year: " + fields[5]);
							throw new BadYearException("Invalid year in file: " + csvFiles[i]);
						}
						// create a book object and add it to the array
						Book book = new Book(line, csvFiles[i]);
						// add the book to the temp array
						tempBooks[index++] = book;
						System.out.println(book);
					} catch (BadIsbn10Exception | BadIsbn13Exception | BadPriceException | BadYearException e) {
						System.out.println("Error creating book: " + e.getMessage());
						writeToSemanticErrorFile(csvFiles[i], e, fields, semanticErrorCounter++);

					}
				}
				// create a new array of books with the correct size
				Book[] books = new Book[index];
				for (int j = 0; j < index; j++) { // copy the books from the temp array to the new array
					books[j] = tempBooks[j];
				}
				// create a new object output stream and write the array of books to the binary file
				oos = new ObjectOutputStream(new FileOutputStream(binaryFiles[i]));
				oos.writeObject(books); 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (scanner != null) {
					scanner.close();
				}
				if (oos != null) {
					try {
						oos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
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

	/**
	 * This method will write to the genre files the valid records.
	 * @param genre
	 * @param fields
	 * @param counter
	 * @param syncounter
	 * @throws FileNotFoundException
	 * @throws UnknownGenreException
	 * @throws TooFewFieldsException
	 * @throws TooManyFieldsException
	 * @throws MissingFieldException
	 */
	public static void writeToGenreFile(String genre, String[] fields, int counter, int syncounter)
			throws FileNotFoundException, UnknownGenreException, TooFewFieldsException, TooManyFieldsException,
			MissingFieldException {
		String fileName = "";
		if (genre.equals("CCB")) {
			fileName = "Cartoons_Comics.csv";
		} else if (genre.equals("HCB")) {
			fileName = "Hobbies_Collectibles.csv";
		} else if (genre.equals("MTV")) {
			fileName = "Movies_TV_Books.csv";
		} else if (genre.equals("MRB")) {
			fileName = "Music_Radio_Books.csv";
		} else if (genre.equals("NEB")) {
			fileName = "Nostalgia_Eclectic_Books.csv";
		} else if (genre.equals("OTR")) {
			fileName = "Old_Time_Radio_Books.csv";
		} else if (genre.equals("SSM")) {
			fileName = "Sports_Sports_Memorabilia.csv";
		} else if (genre.equals("TPA")) {
			fileName = "Trains_Planes_Automobiles.csv";
		}

		FileOutputStream fw = new FileOutputStream(fileName, true);
		PrintWriter pw = new PrintWriter(fw);

		for (int i = 0; i < fields.length; i++) {
			pw.print(fields[i]);
			if (i < fields.length - 1) {
				pw.print("\t");
			}
		}
		pw.println();
		pw.close();
	}

	/**
	 * This method will write to the syntax error file the errors.
	 * @param fileName
	 * @param e
	 * @param fields
	 * @param counter
	 * @throws FileNotFoundException
	 * @throws TooFewFieldsException
	 * @throws TooManyFieldsException
	 * @throws MissingFieldException
	 * @throws UnknownGenreException
	 */
	public static void writeToSyntaxErrorFile(String fileName, Exception e, String[] fields, int counter)
			throws FileNotFoundException, TooFewFieldsException, TooManyFieldsException, MissingFieldException,
			UnknownGenreException {

		FileOutputStream fw = new FileOutputStream("syntax_error_file.txt", true);
		PrintWriter pw = new PrintWriter(fw);
		pw.println("syntax error number " + counter + " in file: " + fileName);
		pw.println("=======================");
		pw.println(e.getMessage());
		if (fields[0] == "")
			pw.println("Missing field: title");
		if (fields[1] == "")
			pw.println("Missing field: author");
		if (fields[2] == "")
			pw.println("Missing field: price");
		if (fields[3] == "")
			pw.println("Missing field: isbn");
		if (fields[4] == "")
			pw.println("Missing field: genre");
		;
		if (fields[5] == "")
			pw.println("Missing field: year");
		pw.print("Record: ");
		for (int i = 0; i < fields.length; i++) {
			pw.print(fields[i]);
			if (i < fields.length - 1) {
				pw.print(", ");
			}
		}
		pw.println();
		pw.println();
		pw.close();
	}

	/**
	 * This method will check if the genre is valid.
	 * @param genre
	 * @return true if valid false if invalid
	 * @throws UnknownGenreException
	 */
	public static boolean isValidGenre(String genre) throws UnknownGenreException {
		try {
			if (genre.equals("CCB") || genre.equals("HCB") || genre.equals("MTV") || genre.equals("MRB")
					|| genre.equals("NEB") || genre.equals("OTR") || genre.equals("SSM") || genre.equals("TPA")) {
				return true;
			} else {
				throw new UnknownGenreException("Unknown Genre");
			}
		} catch (UnknownGenreException e) {
			System.out.println("Unknown Genre");
			return false;
		}
	}
	/**
	 * This method will check if the isbn10 is valid.
	 * @param isbn
	 * @return true if valid false if invalid
	 */
	public static boolean validateIsbn10(String isbn) {
		if (isbn.length() != 10) {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < 10; i++) {
			char digit = isbn.charAt(i);
			if (digit == 'X' || digit == 'x') {
				if (i == 9) {
					sum += 10;
				} else {
					return false;
				}
			} else if (Character.isDigit(digit)) {
				int x = Character.getNumericValue(digit);
				sum += (10 - i) * x;
			} else {
				return false;
			}
		}

		return sum % 11 == 0;
	}
	/**
	 * This method will check if the isbn13 is valid.
	 * @param isbn
	 * @return true if valid false if invalid
	 */
	public static boolean validateIsbn13(String isbn) {
		if (isbn.length() != 13) {
			return false;
		}

		int sum = 0;
		int multiplier = 1;
		for (int i = 0; i < 13; i++) {
			char digit = isbn.charAt(i);
			if (Character.isDigit(digit)) {
				int x = Character.getNumericValue(digit);
				sum += multiplier * x;
				multiplier = 4 - multiplier;
			} else {
				return false;
			}
		}

		return sum % 10 == 0;
	}
	/**
	 * This method will check if the price is valid.
	 * @param price
	 * @return true if valid false if invalid
	 * @throws BadPriceException
	 */
	public static boolean validatePrice(String price) throws BadPriceException {
		if (price == null) {
			return false;
		}
		double priceDouble = Double.parseDouble(price);
		return priceDouble >= 0;
	}
	/**
	 * This method will check if the year is valid.
	 * @param year
	 * @return true if valid false if invalid
	 * @throws BadYearException
	 */
	public static boolean validateYear(String year) throws BadYearException {
		if (year == null) {
			return false;
		}
		int yearInt = Integer.parseInt(year);
		return yearInt >= 1995 && yearInt <= 2010;
	}
	/**
	 * This method will write to the semantic error file the errors.
	 * @param fileName
	 * @param e
	 * @param fields
	 * @param counter
	 * @throws BadIsbn10Exception
	 * @throws BadIsbn13Exception
	 * @throws BadPriceException
	 * @throws BadYearException
	 * @throws FileNotFoundException
	 */
	public static void writeToSemanticErrorFile(String fileName, Exception e, String[] fields, int counter)
			throws BadIsbn10Exception, BadIsbn13Exception, BadPriceException, BadYearException, FileNotFoundException {

		FileOutputStream fw = new FileOutputStream("semantic_error_file.txt", true);
		PrintWriter pw = new PrintWriter(fw);
		pw.println("semantic error number " + counter + " in file: " + fileName);
		pw.println("=======================");
		pw.println("Error: " + e.toString());
		if (!validatePrice(fields[2]))
			pw.println("Invalid price");
		if (!validateIsbn10(fields[3]) || !validateIsbn13(fields[3]))
			pw.println("Invalid isbn");
		if (!validateYear(fields[5]))
			pw.println("Invalid year");
		pw.print("Record: ");
		for (int i = 0; i < fields.length; i++) {
			pw.print(fields[i]);
			if (i < fields.length - 1) {
				pw.print(", ");
			}
		}
		pw.println();
		pw.println();
		pw.close();

	}

	/**
	 * This is the main method that will call the methods.
	 */
	public static void main(String[] args) {
		/*
		try {
			do_part1();
		} catch (FileNotFoundException | TooManyFieldsException | TooFewFieldsException | MissingFieldException
				| UnknownGenreException e) {
			
			e.printStackTrace();
		}
		
		try {
			do_part2();
		} catch (BadIsbn10Exception | BadIsbn13Exception | BadPriceException | BadYearException e) {
			
			e.printStackTrace();
		}
		*/
		do_part3();

	}
}
/**
 * These is the exception classes that will be thrown if there are too many fields, too few fields, missing fields, or unknown genre.
 */
class TooManyFieldsException extends Exception {
	
	public TooManyFieldsException(String s) {
		super(s);
	}
	public String getMessage() {
		return super.getMessage();
	}
}

class TooFewFieldsException extends Exception {

	public TooFewFieldsException(String s) {
		super(s);
	}
	public String getMessage() {
		return super.getMessage();
	}

}

class MissingFieldException extends Exception {

	public MissingFieldException(String s) {
		super(s);
	}
	public String getMessage() {
		return super.getMessage();
	}
}

class UnknownGenreException extends Exception {

	public UnknownGenreException(String s) {
		super(s);
	}
	public String getMessage() {
		return super.getMessage();
	}
}

/**
 * This is the Book class that will be used to create book objects.
 * It implements the Serializable interface so that it can be written to a binary file.
 */
class Book implements Serializable {

	// attributes
	private String title;
	private String authors;
	private double price;
	private String isbn;
	private String genre;
	private int year;

	/**
	 * This is the default constructor
	 */
	public Book() {

	}
	/**
	 * This is the parameterized constructor
	 * @param title
	 * @param authors
	 * @param price
	 * @param isbn
	 * @param genre
	 * @param year
	 */
	public Book(String title, String authors, double price, String isbn, String genre, int year) {
		this.title = title;
		this.authors = authors;
		this.price = price;
		this.isbn = isbn;
		this.genre = genre;
		this.year = year;
	}
	/**
	 * This is another parameterized constructor that will read from a file, split the line into fields, and create a book object.
	 * @param line
	 * @param fileName
	 * @throws BadIsbn10Exception
	 * @throws BadIsbn13Exception
	 * @throws BadPriceException
	 * @throws BadYearException
	 * @throws FileNotFoundException
	 */
	public Book(String line, String fileName)
			throws BadIsbn10Exception, BadIsbn13Exception, BadPriceException, BadYearException, FileNotFoundException {
		String[] fields = line.split("\t");
		this.title = fields[0];
		this.authors = fields[1];
		this.price = Double.parseDouble(fields[2]);
		this.isbn = fields[3];
		this.genre = fields[4];
		this.year = Integer.parseInt(fields[5]);

	}

	/**
	 * This method will set the title
	 * @param t
	 */
	public void setTitle(String t) {
		title = t;
	}
	/**
	 * This method will set the authors
	 * @param a
	 */
	public void setAuthors(String a) {
		authors = a;
	}
	/**
	 * This method will set the price
	 * @param pr
	 */
	public void setPrice(double pr) {
		price = pr;
	}
	/**
	 * This method will set the isbn
	 * @param i
	 */
	public void setISBN(String i) {
		isbn = i;
	}
	/**
	 * This method will set the genre
	 * @param g
	 */
	public void setGenre(String g) {
		genre = g;
	}

	public void setYear(int y) {
		year = y;
	}

	/**
	 * This method will get the title
	 * @return String
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * This method will get the authors
	 * @return String
	 */
	public String getAuthors() {
		return authors;
	}
	/**
	 * This method will get the price
	 * @return double
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * This method will get the isbn
	 * @return String
	 */
	public String getISBN() {
		return isbn;
	}
	/**
	 * This method will get the genre
	 * @return String
	 */
	public String getGenre() {
		return genre;
	}
	/**
	 * This method will get the year
	 * @return int
	 */
	public int getYear() {
		return year;
	}
	/**
	 * equals method that will check if two book objects are equal
	 * @param x
	 * @Override equals method from Object class
	 * @return boolean
	 */
	public boolean equals(Object x) {
		if (x == null || x.getClass() != this.getClass()) {
			return false;
		} else {
			Book book = (Book) x;
			return book.title.equals(this.title) && book.authors.equals(this.authors) && book.price == this.price
					&& book.isbn.equals(this.isbn) && book.genre.equals(this.genre) && book.year == this.year;
		}
	}
	/**
	 * toString method that will return a string representation of the book object
	 * @Override toString method from Object class
	 * @return String
	 */
	public String toString() {
		return "The Book \"" + title + "\" is written by " + authors + ". It costs $" + price + ", with an ISBN: "
				+ isbn + ". The genre of the book is " + genre + " and it was created in " + year;
	}

}

/**
 * These are the exception classes that will be thrown if there are bad isbn10, bad isbn13, bad price, or bad year.
 */
class BadIsbn10Exception extends Exception {

	public BadIsbn10Exception(String s) {
		super(s);
	}

}

class BadIsbn13Exception extends Exception {

	public BadIsbn13Exception(String s) {
		super(s);
	}
	public String getMessage() {
		return super.getMessage();
	}
}

class BadPriceException extends Exception {

	public BadPriceException(String s) {
		super(s);
	}
	public String getMessage() {
		return super.getMessage();
	}
}

class BadYearException extends Exception {

	public BadYearException(String s) {
		super(s);
	}
	public String getMessage() {
		return super.getMessage();
	}
}
