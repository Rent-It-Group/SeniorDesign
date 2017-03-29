var mongoose = require('mongoose');

var tagSchema = mongoose.Schema({
	keywords:{
		type:Array
	}
});

var Tag = module.exports = mongoose.model('Tag',tagSchema);

//Get Tag
module.exports.getTags = function(callback, limit){
	Tag.find(callback).limit(limit);
}


//Add Category
/*module.exports.addCategory = function(category, callback){
	Category.create(category, callback);
}*/

//Update Category
/*module.exports.updateCategory = function(id, category, options, callback){
	var query = {_id: id};
	var update = {
		name: category.name
	}
	Category.findOneAndUpdate(query, update, options, callback);
}*/

//Delete category
/*module.exports.removeCategory = function(id, callback){
	var query = {_id:id};
	Category.remove(query, callback);
}*/