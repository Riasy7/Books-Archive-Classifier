# Books Archive Classifier

This Java project sorts through a list of files, checks for syntax and semantic errors, writes valid records to a file, writes errors to another file, creates a binary file, reads from it, and displays the records in the binary file in an interactive menu.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. You might need to tweek the directories a little bit and change where you setup ur files.

### Prerequisites

You need to have Java and a Java IDE (like IntelliJ IDEA or Eclipse) installed on your machine.

### Installing

1. Clone the repo: `git clone https://github.com/Riasy7/Books-Archive-Classifier.git`
2. Open the project in your Java IDE.
3. Make sure that the `files_needed_for_project` directory is in the project's root directory. This directory contains the files that the program will organize.

### Running the program

To run the program, simply run the `Driver` class in your IDE. The program will automatically execute the three parts in order.

## Classes

The project consists of several classes:

1. `ValidatingSyntax`: This class validates the syntax of the records in the input files. It checks for too many fields, too few fields, missing fields, and unknown genres. It writes valid records to a file and errors to another file.

2. `ValidatingSemanticsAndSerializeBooks`: This class validates the semantics of the records in the input files. It checks for bad ISBN-10, bad ISBN-13, bad price, and bad year. It writes valid records to a binary file.

3. `ReadBinaryAndDeserialize`: This class reads from the binary files created by `ValidatingSemanticsAndSerializeBooks`, deserializes the arrays of `Book` objects, and provides an interactive menu for the user to navigate through the books.

4. `Book`: This class represents a book. It implements the `Serializable` interface, which allows instances of the class to be written to and read from a binary file.

5. `Driver`: This is the main class that runs the program. It creates instances of `ValidatingSyntax`, `ValidatingSemanticsAndSerializeBooks`, and `ReadBinaryAndDeserialize` and calls their respective methods to execute the three parts of the program.

## Exceptions

The project also includes several custom exceptions to handle different types of errors:

1. `TooManyFieldsException`: Thrown when a record has too many fields.
2. `TooFewFieldsException`: Thrown when a record has too few fields.
3. `MissingFieldException`: Thrown when a record is missing a field.
4. `UnknownGenreException`: Thrown when a record has an unknown genre.
5. `BadIsbn10Exception`: Thrown when a record has a bad ISBN-10.
6. `BadIsbn13Exception`: Thrown when a record has a bad ISBN-13.
7. `BadPriceException`: Thrown when a record has a bad price.
8. `BadYearException`: Thrown when a record has a bad year.

## Usage

To run the program, simply run the `Driver` class. The program will automatically execute the three parts in order.

## Author

Ahmad Saadawi

## GitHub

[https://github.com/Riasy7/Books-Archive-Classifier](https://github.com/Riasy7/Books-Archive-Classifier)
