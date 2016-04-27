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
package evos.sync.quiz;

import java.util.List;
import javax.websocket.Session;

/**
 * This file is the interface for the Quiz.
 *
 * @author Christian Wansart
 */
public interface Quiz {

    /**
     * Returns the Quiz's id.
     *
     * @return Quiz id
     */
    public int getId();

    /**
     * Returns User's (professor) session string.
     *
     * @return User's session string
     */
    public String getUserSessionString();

    /**
     * Returns the WebSocket session of the user. It's required to send the User
     * messages.
     *
     * @return User's Session
     */
    public Session getSession();

    /**
     * Returns the attendee list that logged on to the quiz.
     *
     * @return Attendee list
     */
    public List<Session> getAttendeeList();
}
