/**
 * 
 */
package infrastructure;

import java.net.InetSocketAddress;

import distribution.Callback;

/**
 * @author Beto
 *
 */
public interface ServerCallback extends Callback {
	void onDisconnect(InetSocketAddress iAddress);
}
