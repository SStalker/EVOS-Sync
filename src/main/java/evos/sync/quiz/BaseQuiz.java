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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.websocket.Session;

/**
 * This is the default implementation of the Quiz interface.
 *
 * @author Christian Wansart
 */
public class BaseQuiz implements Quiz {

    /*
     * TODO: We may need to create setters for userSession and the
     * userSessionString, if the User has a disconnection.
     */
    private final int quizId;
    private Session userSession;
    private String userSessionString;
    private final List<Session> attendeeList;

    public BaseQuiz(int quizId, String userSessionString, Session userSession) {
        this.quizId = quizId;
        this.userSession = userSession;
        this.userSessionString = userSessionString;
        this.attendeeList = Collections.synchronizedList(new ArrayList<Session>());
    }

    /**
     * Returns the id of the Quiz.
     *
     * @return Quiz's id
     */
    @Override
    public int getId() {
        return quizId;
    }

    /**
     * Returns User's (professor) session string.
     *
     * @return User's session string
     */
    @Override
    public String getUserSessionString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Returns the WebSocket session of the user. It's required to send the User
     * messages.
     *
     * @return User's Session
     */
    @Override
    public Session getSession() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Returns the attendee list that logged on to the quiz.
     *
     * @return Attendee list
     */
    @Override
    public List<Session> getAttendeeList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
