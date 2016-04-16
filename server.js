var WebSocketServer = require('ws').Server;
var Database = require('./sql.js');

var wss = new WebSocketServer({path: '/sync', port: 8000});
var db = new Database();
var sessions = [];

wss.on('connection', function(ws) {
    ws.on('message', function(message) {
        message = JSON.parse(message);
        switch(message.type) {
            case 'start':
                startQuiz(ws, message);
                break;
            case 'logon':
                logon(ws, message);
            default:
                console.log('Received an invalid message.');
        }
    });
});

// Starts a quiz
function startQuiz(ws, message) {
    // Check if the message contains the required data
    if(message.quiz_id === undefined || message.session_id === undefined || message.user_id === undefined) {
        console.log('Invalid start message:');
        console.log(message);
        sendResponse(ws, {
            type: 'start',
            successful: false,
            reason: 'message is invalid'
        });
        return false;
    }

    // Check if the session is already "online"
    if(sessions[message.quiz_id] === undefined) {
        // Check if user is owner of quiz. If so, add the quiz to the
        // sessions list.
        db.isOwner(message.user_id, message.quiz_id, function() {
            sessions[message.quiz_id] = {
                session_id: message.session_id,
                user_ws: ws,
                attendees: []
            };
        }, function() {
            console.log('User is not the owner of the quiz:');
            console.log(message);
            sendResponse(ws, {
                type: 'start',
                successful: false,
                reason: 'You are not the owner of the quiz.'
            });
            return false;
        });
    }

    sendResponse(ws, {
        type: 'start',
        successful: true
    });
}

// Adds an attendee to the sessions list
function logon(ws, message) {
    if(message.quiz_id === undefined) {
        console.log('Invalid logon message');
        console.log(message);
        sendResponse(ws, {
            type: 'logon',
            successful: false,
            reason: 'Missing quiz_id.'
        });
        return false;
    }

    // If not nickname was given, we assign a default one.
    nickname = nickname || 'anon Alfred;

    if(session[message.quiz_id] === undefined) {
        sendResponse(ws, {
            type: 'logon',
            successful: false,
            reason: 'Quiz is not open.'
        });
        return false;
    }

    // Infom Attendee
    sendResponse(ws, {
        type: 'logon',
        successful: true
    });

    // Inform the User of new logon
    sendResponse(sessions[message.quiz_id].user_ws, {
        type: 'logon',
        nickname: nickname
    });
}

// Directly answer to the current client
function sendResponse(ws, message) {
    try {
        ws.send(JSON.stringify(message));
    } catch(e) {
        console.log('Error while sending response:');
        console.log(e);
    }
}