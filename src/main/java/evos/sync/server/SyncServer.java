/**
 * This file is part of EVOS-Sync.
 *
 * EVOS-Sync is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EVOS-Sync is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EVOS-Sync.  If not, see <http://www.gnu.org/licenses/>.
 */
package evos.sync.server;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * This is the entry class for our sync server.
 *
 * @author Christian Wansart
 */
@ServerEndpoint("/sync")
public class SyncServer {
    
    @Inject
    private SyncMessageHandlerFactory syncMessageHandlerFactory;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Opened session: " + session.getId());
        
        System.out.println("HANDLER: " + syncMessageHandlerFactory);

        SyncMessageHandler messageHandler = syncMessageHandlerFactory.createSyncMessageHandler(session);
        session.addMessageHandler(messageHandler);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Closed Session:" + session.getId());
    }

    @OnError
    public void onError(Throwable t) {
        System.err.println("An error occurred: " + t.getMessage());
        t.printStackTrace();
    }

}
