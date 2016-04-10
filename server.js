var WebSocketServer = require('ws').Server;
var Database = require('./sql.js');

var wss = new WebSocketServer({port:8000});
var db = new Database();
var sessions = [];

wss.on('connection', function(ws) {
    ws.on('message', function(message) {
        switch(message.type) {
            case 'start':
                startQuiz(message);
                break;
        }
    });
});

// Starts a quiz
function startQuiz(message) {
    // Check if the message contains the required data
    if(message.quiz_id === undefined || message.session_id === undefined || user_id === undefined) {
        console.log('Invalid start message:');
        console.log(message);
        return false;
    }

    // Check if the session is already "online"
    if(sessions[message.quiz_id] !== undefined) {
        console.log('Quiz is already started:');
        console.log(message);
        return false;
    }

    // Check if user is owner of quiz. If so, add the quiz to the
    // sessions list.
    db.isOwner(message.user_id, message.quiz_id, function() {
        sessions[message.quiz_id] = {
            session_id: message.session_id,
            attendees: []
        };

        sendResponse({
            type: 'accept'
        });
    }, function() {
        console.log('User is not the owner of the quiz:');
        console.log(message);
    });
}

// Directly answer to the current client
function sendResponse(message) {
    try {
        ws.send(message);
    } catch(e) {
        console.log('Error while sending response:');
        console.log(e);
    }
}