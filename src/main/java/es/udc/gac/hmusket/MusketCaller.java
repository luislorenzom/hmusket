package es.udc.gac.hmusket;

public class MusketCaller {

	static {
		System.loadLibrary("musket");
	}

	public native void callMusket(String args);
}
