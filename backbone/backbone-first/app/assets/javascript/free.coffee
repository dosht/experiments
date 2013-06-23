class Message extends Backbone.Model
	# constructor: -> _
	url: "http://localhost:9000/rest/lolo"

	initialize: ->
		this.on 'sync', (e)-> _

	sync: (method, model, options)->
		console.log model.changed
		this	

	defaults:
		name: "someone"
		age: 18
		email: "someone@gogo.com"

window.msg = new Message