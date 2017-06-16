/**
 * 
 */
package distribution;

/**
 * @author avss
 *
 */
public interface Callback {
	void onReceive(String msg);
	void onReceive(Message msg);
}
