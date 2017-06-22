/**
 * 
 */
package utils;

/**
 * @author avss
 *
 */
public class Cryptographer {
	public static void codec(byte[] array){
		int size = array.length;
		byte[] yarra = new byte[size];
		for(int i = 0; i < size; i++){
			yarra[i] = array[size - i - 1];
		}
		array = yarra;
	}
}
