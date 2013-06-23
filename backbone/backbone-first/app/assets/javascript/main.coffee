window.TodoItem = Backbone.Model.extend(
  urlRoot: '/rest/todos'
  defaults:
  	description: "Empty todo..."
  	status: 'incomplete'
  toggleStatus: () ->
  	if this.get('status') == 'incomplete'
  	  this.set({'status': 'complete'})
  	else
  	  this.set({'status': 'incomplete'})
)
window.todo = new TodoItem({description: 'Pick up milk', status: 'incomplete'})

window.TodoView = Backbone.View.extend(
  template: _.template('<h3><input type="checkbox" /> <%= description %></h3>')
  tagName: 'li'
  render: () ->
    $(this.el).html(this.template(this.model.toJSON()))
  toggleStatus = () ->
  	alert(9)
  events:
  	'change input': 'toggleStatus'
)
window.todoView = new TodoView({model: todo})
$("#todos").append(todoView.render())