var ws = new WebSocket('ws://127.0.0.1:8000/sync');
ws.onerror = function(err) {
	console.log('ERROR');
	console.log(err);
}

ws.onopen = function() {
	var msg = {
	    type: 'logon',
	    session_id: '8dsuf98a89fahs98fg7a8wf87gaf',
	    quiz_id: 1,
	    nickname: 'Larrrrrr argh'
	}

	ws.send(JSON.stringify(msg));
}

ws.onclose = function() {
	console.log('nope nope nope nope');
}

ws.onmessage = function(message) {
	console.log('MESSAGE:');
	console.log(message.data);
}