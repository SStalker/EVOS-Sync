var mysql = require('mysql');
var connection = mysql.createConnection({
	host: 'localhost',
	user: 'evos',
	password: 'evos',
	database: 'evos'
});

var quizId = 1;
var userId = 1;

var query = 'SELECT users.name\
	FROM quizzes, categories, users\
	WHERE quizzes.category_id = categories.id\
	AND categories.user_id = users.id\
	AND users.id = ' + userId + '\
	AND quizzes.id = ' + quizId + '\
	AND quizzes.isActive = 1';

connection.connect();
connection.query(query,
	function(err, rows, fields) {
		if(err == null) {
			console.log(err);
		} else {
			if(rows != null) {
				console.log(rows);
			}
		}
	}
);
connection.end();