package es.udc.gac.hmusket;

public class MusketCaller {

	static {
		System.loadLibrary("musketcaller");
	}

	public native void callMusket(String args);

	public static void main (String[] args) {
		new MusketCaller().callMusket("Luis");
	}

}
