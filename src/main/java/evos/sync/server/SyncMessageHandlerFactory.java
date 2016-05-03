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

import evos.sync.quiz.QuizManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.websocket.Session;

/**
 * This factory creates new MessageHandlers. We need it because injecting the
 * QuizManager in the SyncMessageHandler doesn't work when we create a new
 * instance with "new" keyword. So, we inject an instance of this factory in the
 * SyncServer class which will have an instance of the QuizManager.
 *
 * @author Christian Wansart
 */
@Singleton
public class SyncMessageHandlerFactory {

    @Inject
    private QuizManager quizManager;

    /**
     * Creates a new instance of SyncMessageHandler. It passes the injected
     * QuizManager instance.
     *
     * @param session User's/Attendee's Session
     * @return new instance of SyncMessageHandler
     */
    public SyncMessageHandler createSyncMessageHandler(Session session) {
        return new SyncMessageHandler(session, quizManager);
    }
}
