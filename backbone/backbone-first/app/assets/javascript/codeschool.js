TodoModel = Backbone.Model.extend({
	urlRoot: "/res/todos/",
	defaults: {
		description: "Empty...",
		status: "Incomplete"
	}
});

TodoView = Backbone.Model.extend({
	template: _.template('<h3><input type="checkbox" /> <%= description %></h3>'),
	tagName: "li",
	render: function() {
		$(this.el).html(this.template(this.toJSON().model.toJSON()));
	},
	events: {
		"change input" : "toggleStatus"
	},
	toggleStatus: function() {
		alert(9);
	}

});
todoModel = new TodoModel({description: 'Pick up milk', status: 'incomplete'});
todoView = new TodoView({model: todoModel});
$("#todos").append(todoView.render());