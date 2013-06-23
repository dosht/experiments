(function() {

  window.Album = Backbone.Model.extend({
  	urlRoot: "/rest/albums"
  });

  window.AlbumView = Backbone.View.extend({
  	tagName: 'li',
  	className: 'album',

  	initialize: function() {
  		_.bindAll(this, 'render');
  		this.model.bind('change', this.render);

  		this.template = _.template($('#album-template').html());
  	},
  	render: function() {
  		var renderContent = this.template(this.model.toJSON());
  		$(this.el).html(renderContent);
  		return this;
  	}
  });

  window.Albums = Backbone.Collection.extend({
  	model: Album,
  	url: "/rest/albums"
  });

  window.LibraryAlbumView = AlbumView.extend({

  });

  window.LibraryView = Backbone.View.extend({
  	tagName: 'section',
  	className: 'library',
  	initialize: function() {
  		_.bindAll(this, 'tender');
  		this.template = _.template($('#library-template').html());
  		this.collection.bind('reset', this.render);
  	},
  	render: function() {
  		var $albums,
  			collection = this.collection;

  		this.template = _.template($('#library-template').html());
  		$(this.el).html(this.template({}));
  		$albums = $('.albums');
  		this.collection.each(function (album) {
  			var view = new LibraryAlbumView({
  				model: album,
  				collection: collection
  			});
	  		$albums.append(view.render().el);
  		});
  		return this;
  	}

  });

  album = new Album({title:'Abbey Road', artist:'The Beatles', tracks:[{title:'Track A'}]});
  album.save();
  // albumView = new AlbumView({model:album});
  // $('#container').append(albumView.render().el);
  // albums = new Albums();
  // albums.fetch();
  library = new Albums();
  libraryView = new LibraryView({collection: library});
  $('#container').append(libraryView.render().el)
})();