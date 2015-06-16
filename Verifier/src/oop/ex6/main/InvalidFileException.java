package oop.ex6.main;

/**
 * Created by user on 6/16/2015.
 */
public class InvalidFileException extends Throwable {
	protected static final String FILE_EXCEPTION_PRINT = "2";

	protected void printErrorMessage(){
		System.err.println(FILE_EXCEPTION_PRINT);
	}

}
