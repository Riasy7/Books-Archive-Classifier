package package1;

import java.io.FileNotFoundException;
import java.io.Serializable;

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