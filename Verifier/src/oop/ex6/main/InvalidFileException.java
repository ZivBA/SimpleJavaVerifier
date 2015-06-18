package oop.ex6.main;

import java.io.IOException;

/**
 * Created by user on 6/16/2015.
 */
public class InvalidFileException extends IOException {

	public InvalidFileException(String detailMessage) {
		super(detailMessage);
	}
}
