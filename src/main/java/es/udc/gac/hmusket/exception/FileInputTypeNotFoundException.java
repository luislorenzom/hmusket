package es.udc.gac.hmusket.exception;

public class FileInputTypeNotFoundException extends Exception {

	private static final long serialVersionUID = 3506166480001784972L;

	public FileInputTypeNotFoundException(String msg) {
		super("Invalid File type " + msg + " this value must be \"a\" for FastA files or \"q\" for FastQ files");
	}
}
