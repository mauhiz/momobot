/* 
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of JBouncer.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: ClientListener.java,v 1.3 2004/03/01 19:13:37 pjm2 Exp $

 */
package bouncer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ClientListener extends ServerSocket implements Runnable {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(ClientListener.class);
    /**
     * 
     */
    private JBouncerManager     manager;

    /**
     * @param manager1
     * @param port
     * @throws IOException
     */
    public ClientListener(final JBouncerManager manager1, final int port) throws IOException {
        super(port);
        this.manager = manager1;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        boolean running = true;
        while (running) {
            try {
                Socket socket = super.accept();
                LOG.info("Connection received from " + socket.getInetAddress().getHostName());
                new Thread(new ClientConnection(socket, this.manager)).start();
            } catch (IOException e) {
                LOG.debug("Error establishing socket. Don't panic.", e);
            }
        }
    }
}
