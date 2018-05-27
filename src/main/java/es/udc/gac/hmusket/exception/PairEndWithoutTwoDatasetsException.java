package es.udc.gac.hmusket.exception;

public class PairEndWithoutTwoDatasetsException extends Exception {

	private static final long serialVersionUID = -2666187328589286899L;
	
	public PairEndWithoutTwoDatasetsException() {
		super("Pair-end mode needs two datasets, check \"fileIn\" parameter out");
	}
}
