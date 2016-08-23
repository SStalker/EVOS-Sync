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

import evos.sync.database.Database;
import evos.sync.server.AttendeeAlreadySignedIn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.websocket.Session;

/**
 * This class manages the active quizzes.
 *
 * @author Christian Wansart, Lukas Hannigbrinck
 */
@Singleton
public class QuizManager {

    /**
     * Contains the active quizzes. The keys are the ids of the quiz. There
     * should always be just one quiz with the same id.
     */
    private final Map<Integer, Quiz> activeQuizzes = Collections.synchronizedMap(new HashMap<Integer, Quiz>());

    /**
     * Contains a list of session as the key value and it's corresponding
     * quizzes it attends to.
     */
    private final Map<Session, Quiz> sessions = Collections.synchronizedMap(new HashMap<Session, Quiz>());

    /**
     * Contains all php session ids from all logged on attendees.
     */
    private final Map<Quiz, List<String>> attendeeSessionStringList = Collections.synchronizedMap(new HashMap<Quiz, List<String>>());
    
    @Inject
    private Database database;

    /**
     * Starts a quiz with the given quizId of the User with the given userId.
     *
     * @param quizId the id of the Quiz
     * @param userId the id of the User
     * @param sessionString the session string of the User (for auth reasons)
     * @param userSession Session object of the User/owner of the Quiz
     * @throws IllegalArgumentException when the User is not the Owner of the
     * Quiz
     */
    public void startQuiz(int quizId, int userId, String sessionString, Session userSession) throws IllegalArgumentException {
        Quiz quiz = new BaseQuiz(quizId, sessionString, userSession);

        // Check if User is owner of Quiz
        if (!database.isOwner(quizId, userId)) {
            throw new IllegalArgumentException("User is not owner of Quiz");
        }

        activeQuizzes.put(quizId, quiz);
        attendeeSessionStringList.put(quiz, Collections.synchronizedList(new ArrayList<String>()) );
    }

    /**
     * Ends an active Quiz.
     *
     * @param quizId Quiz's id
     * @param sessionString User's session string for authentication
     * @throws IllegalArgumentException will be thrown if the Quiz is not active
     */
    public void endQuiz(int quizId, String sessionString) throws IllegalArgumentException {
        Quiz quiz = getQuiz(quizId);

        if (!quiz.getUserSessionString().equals(sessionString)) {
            throw new IllegalArgumentException("User is not owner of Quiz");
        }

        this.activeQuizzes.remove(quizId);
        attendeeSessionStringList.remove(quiz);
    }

    /**
     * Signs up an Attendee to an active Quiz.
     *
     * @param quizId Quiz's id
     * @param nickname Attendee's nickname
     * @param attendeeSession Attendees Session
     * @param attendeeSessionString PHP Session (Needed for reconnect purpose)
     * @throws IllegalArgumentException if the given Quiz is not active
     */
    public void signUp(int quizId, String nickname, Session attendeeSession, String attendeeSessionString) throws IllegalArgumentException, AttendeeAlreadySignedIn {
        Quiz quiz = activeQuizzes.get(quizId);

        if (quiz == null) {
            throw new IllegalArgumentException("given Quiz is not active");
        }
        
        quiz.addAttendee(attendeeSession);
        sessions.put(attendeeSession, quiz);
        
        if(attendeeSessionStringList.get(quiz).contains(attendeeSessionString)){
            throw new AttendeeAlreadySignedIn();
        }else{
            attendeeSessionStringList.get(quiz).add(attendeeSessionString);
        }
    }

    /**
     * Returns an active quiz.
     *
     * @param quizId Quiz' id
     * @return the wanted active Quiz object.
     * @throws IllegalArgumentException
     */
    public Quiz getQuiz(int quizId) throws IllegalArgumentException {
        Quiz quiz = activeQuizzes.get(quizId);

        if (quiz == null) {
            throw new IllegalArgumentException("quiz is not active");
        }

        return quiz;
    }

    /**
     * Informs the User of a Quiz that a User disconnected. It also removes the
     * given Session from the session's list.
     *
     * @param session disconnecting session
     */
    public void userDisconnected(Session session) {
        Quiz quiz = sessions.get(session);

        if (quiz != null) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("type", "disconnect");

            try {
                quiz.getSession().getBasicRemote().sendText(response.build().toString());
            } catch (IOException ex) {
                Logger.getLogger(QuizManager.class.getName()).log(Level.SEVERE, "Could not inform the User of a session disconnection.", ex);
            }

            sessions.remove(session);
        }
    }
}
