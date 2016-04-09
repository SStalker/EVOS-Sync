var WebSocketServer = require('ws').Server;
var wss = new WebSocketServer({port:8000});

var sessions = [];

wss.on('connection', function(ws) {
	ws.on('message', function(message) {

	});
});