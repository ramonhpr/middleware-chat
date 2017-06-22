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
		if (array == null) {
	          return;
	      }
	      byte yarra;
	      for (int i = 0,j = array.length - 1 ; j > i; j--, i++) {
	          yarra = array[j];
	          array[j] = array[i];
	          array[i] = yarra;
	      }
	}
}
