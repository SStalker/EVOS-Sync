var mysql = require('mysql');

var Database = function() {
	this.connection = mysql.createConnection({
		host: 'localhost',
		user: 'evos',
		password: 'evos',
		database: 'evos'
	});

	this.connection.connect();
}

Database.prototype.close = function() {
	this.connection.end();
};

// Checks if the given user is the owner of the quiz.
Database.prototype.isOwner = function(userId, quizId, callbackTrue, callbackFalse) {
	var query = 'SELECT users.name\
		FROM quizzes, categories, users\
		WHERE quizzes.category_id = categories.id\
		AND categories.user_id = users.id\
		AND users.id = ' + parseInt(userId) + '\
		AND quizzes.id = ' + parseInt(quizId);

	this.connection.query(query,
		function(err, rows, fields) {
			if(err === null) {
				if(rows.length == 1) {
					if(callbackTrue !== undefined) {
						callbackTrue();
					}
				} else {
					if(callbackFalse !== undefined) {
						callbackFalse();
					}
				}
			}
		}
	);
};

module.exports = Database;