package Exceptions;

public class AlreadyInitException extends Exception {
	public AlreadyInitException(){
		super("This process has already been init.");
	}
}
