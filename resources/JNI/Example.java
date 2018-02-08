package jni_examples.es.dec.mubics;

public class Example {
	static {
		System.loadLibrary("hello");
	}
	
	private native void sayHello(String name);
	
	public static void main(String[] args) {
		new Example().sayHello("Luis");
	}

}
