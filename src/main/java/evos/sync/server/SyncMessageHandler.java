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

import evos.sync.database.Database;
import evos.sync.quiz.Quiz;
import evos.sync.quiz.QuizManager;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 * This class handles the request for each session.
 *
 * @author Christian Wansart
 */
public class SyncMessageHandler implements MessageHandler.Whole<String> {

    @Inject
    private QuizManager quizManager;
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private Session userSession;

    public SyncMessageHandler(Session session) {
        this.userSession = session;
    }

    /**
     * Gets invoked when the server receives a message.
     *
     * @param message JSON formatted message
     */
    @Override
    public void onMessage(String message) {
        JsonObject jsonMessage = Json.createReader(new StringReader(message)).readObject();
        String messageType = jsonMessage.getString("type");

        // The messageType is null, if type was not in the message.
        if (messageType == null) {
            messageType = "";
        }

        switch (messageType) {
            case "start":
                handleStart(jsonMessage);
                break;
            default:
        }
    }

    /**
     * Handles the start message type.
     *
     * User -> Server { type: 'start', user_id: <UserID>, session_id:
     * <SessionID>, quiz_id: <QuizID> }
     *
     * @param message the message.
     */
    private void handleStart(JsonObject message) {
        int userId;
        int quizId;
        String sessionString;

        try {
            userId = message.getInt("user_id");
            quizId = message.getInt("quiz_id");
            sessionString = message.getString("session_id");
        } catch (NullPointerException ex) {
            String msg = "missing parameters in message";
            LOGGER.log(Level.WARNING, msg, ex);
            sendResponse(createErrorString("start", msg));
            return;
        }

        try {
            quizManager.startQuiz(quizId, userId, sessionString, userSession);
        } catch (IllegalArgumentException ex) {
            String msg = "user is not owner of quiz";
            LOGGER.log(Level.WARNING, msg, ex);
            sendResponse(createErrorString("start", msg));
            return;
        }

        // So far so good, now we need to inform the User
        JsonObjectBuilder response = Json.createObjectBuilder();
        response.add("type", true);
        response.add("successful", true);
        sendResponse(response.build().toString());
    }

    /**
     * Handles incoming logon messages from Attendees. Attendees can only logon
     * to running quizzes.
     *
     * Attendee -> Server { type: 'logon', session_id: <SessionID>, quiz_id:
     * <QuizID>, nickname: <Nickname> }
     *
     * @param message incoming message
     */
    private void handleLogon(JsonObject message) {
        int quizId;
        String nickname;

        try {
            quizId = message.getInt("quiz_id");
            nickname = message.getString("nickname");
        } catch (NullPointerException ex) {
            String msg = "missing parameters in message";
            LOGGER.log(Level.WARNING, msg, ex);
            sendResponse(createErrorString("logon", msg));
            return;
        }

        Quiz quiz;
        try {
            quizManager.signUp(quizId, nickname, this.userSession);
            quiz = quizManager.getQuiz(quizId);
        } catch (IllegalArgumentException ex) {
            String msg = "quiz is not active";
            LOGGER.log(Level.WARNING, msg, ex);
            sendResponse(createErrorString("logon", msg));
            return;
        }

        // Inform Attendee about successful logon
        JsonObjectBuilder response = Json.createObjectBuilder();
        response.add("type", "logon");
        response.add("successful", true);
        sendResponse(response.build().toString());

        // Inform User about new Attendee
        Session userSession = quiz.getSession();
        response = Json.createObjectBuilder();
        response.add("type", "logon");
        response.add("nickname", nickname);
        try {
            userSession.getBasicRemote().sendText(response.build().toString());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not send response to User!");
        }
    }

    /**
     * Sends a message to the current client.
     *
     * @param message the message that should be sent to the client.
     */
    private void sendResponse(String message) {
        try {
            userSession.getBasicRemote().sendText(message);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not send response to User!");
        }
    }

    /**
     * Creates an JSON formatted error string.
     *
     * @param type message type
     * @param reason error reason (short description)
     * @return error string
     */
    private String createErrorString(String type, String reason) {
        JsonObjectBuilder response = Json.createObjectBuilder();
        response.add("type", type);
        response.add("successful", false);
        response.add("reason", reason);
        return response.build().toString();
    }

}
