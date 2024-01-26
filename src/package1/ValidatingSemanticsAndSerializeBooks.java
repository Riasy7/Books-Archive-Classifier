package package1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class ValidatingSemanticsAndSerializeBooks {
	private static int semanticErrorCounter = 1;

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

}
