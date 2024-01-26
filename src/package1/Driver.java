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

import java.io.*;
/**
 * This is the main Driver Class that will run the program and call the methods
 */
public class Driver {

	/**
	 * This is the main method that will call the methods.
	 */
	public static void main(String[] args) {
		ValidatingSyntax p1 = new ValidatingSyntax();
		ValidatingSemanticsAndSerializeBooks p2 = new ValidatingSemanticsAndSerializeBooks();
		ReadBinaryAndDeserialize p3 = new ReadBinaryAndDeserialize();
		try {
			p1.do_part1();
		} catch (FileNotFoundException | TooManyFieldsException | TooFewFieldsException | MissingFieldException
				| UnknownGenreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			p2.do_part2();
		} catch (BadIsbn10Exception | BadIsbn13Exception | BadPriceException | BadYearException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p3.do_part3();
		
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