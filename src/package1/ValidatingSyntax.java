package package1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class ValidatingSyntax {
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

}
