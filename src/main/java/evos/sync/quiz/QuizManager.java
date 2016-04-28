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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.websocket.Session;

/**
 * This class manages the active quizzes.
 *
 * @author Christian Wansart
 */
@Singleton
public class QuizManager {

    /**
     * Contains the active quizzes. The keys are the ids of the quiz. There
     * should always be just one quiz with the same id.
     */
    private Map<Integer, Quiz> activeQuizzes = Collections.synchronizedMap(new HashMap<Integer, Quiz>());

    @Inject
    private Database database;

    /**
     * Starts a quiz with the given quizId of the User with the given userId.
     * 
     * @param quizId the id of the Quiz
     * @param userId the id of the User
     * @param sessionString the session string of the User (for auth reasons)
     * @param userSession Session object of the User/owner of the Quiz
     * @throws IllegalArgumentException when the User is not the Owner of the Quiz
     */
    public void startQuiz(int quizId, int userId, String sessionString, Session userSession) throws IllegalArgumentException{
        Quiz quiz = new BaseQuiz(quizId, sessionString, userSession);
        
        // Check if User is owner of Quiz
        if(!database.isOwner(quizId, userId)) {
            throw new IllegalArgumentException("User is not owner of Quiz");
        }
        
        activeQuizzes.put(quizId, quiz);
    }
}
