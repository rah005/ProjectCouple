/**
 * @auhor Henry Mao
 * @since 5/4/16.
 */
"use strict";

var Route = require("./route");
var App = require("../app");
var User = require("../user");

/**
 * A route that handles registration calls. This is called when a client
 * registers their email with a device ID.
 */
class RegistrationRoute extends Route {
	constructor() {
		super("registration");
	}

	receive(messageId, from, data) {
		var id = data.id;
		var name = data.name;
		var email = data.email;
		if (id && name && email) {
			console.log("User " + name + " (" + email + ") registering with server...");
			App.addUser(new User(from, id, name, email));
		} else {
			console.error("Invalid data sent to route: " + this.id);
		}
	}
}

module.exports = RegistrationRoute;