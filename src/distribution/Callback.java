/**
 * 
 */
package distribution;


/**
 * @author avss
 *
 */
public interface Callback {
	void onReceive();
	void onReceive(String msg); 
	void onTimeOut();
}

