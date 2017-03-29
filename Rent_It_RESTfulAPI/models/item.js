var mongoose = require('mongoose');

var itemSchema = mongoose.Schema({
	uid:{
		type:String,
		required:true
	},
	title:{
		type:String,
		required:true
	},
	description:{
		type:String
	},
	condition:{
		type:String
	},
	category:{
		type:String
	},
	location:{
		type:Array
	},
	city:{
		type:String,
		required:true
	},
	zipcode:{
		type:Number,
		required:true
	},
	tags:{
		type:Array
	},
	value:{
		type:Number
	},
	rate:{
		type:Number,
		required:true
	},
	image:{
		type:String
	},
	visible:{
		type:Boolean
	},
	created_date:{
		type: Date,
		default: Date.now
	}
});

var Item =  module.exports = mongoose.model('Item',itemSchema);

module.exports.getItemsByItemIDs = function(itemIDs, callback){
	Item.find()
		.where('_id')
		.in(itemIDs)
		.exec(callback);
}

//Get All Item
module.exports.getItems = function(callback, limit){
	Item.find(callback).limit(limit);
}

//get tags
module.exports.getTags = function(callback, limit){
	Item.find()
		.limit(limit)
		.select({ tags: 1, _id:0})
		.exec(callback);
		//.select({ tags: 1});
}

//get item by tag
module.exports.getItemsByTag = function(tag, callback){
	Item.find({tags: tag})
		.exec(callback);
}

//Get Item by Id
module.exports.getItemById = function(id, callback){
	Item.findById(id, callback);
}

//Get Item by Category Name
module.exports.getItemsByCategoryId = function(category, callback){
	Item.find()
		.where('category').equals(category)
		.exec(callback);
}

//Get one Item with uid
module.exports.getItemsByUid = function(uid, callback){
	Item.find()
		.where('uid').equals(uid)
		.exec(callback);
	//Item.findOne({'uid': uid}, callback);
	//Item.where('uid', uid).findOne(callback);
}



//Add Item
module.exports.addItem = function(item, callback){
	Item.create(item, callback);
}



//Update Item
/*module.exports.updateItem = function(id, item, options, callback){
	var query = {_id: id};
	// var update = {
	// 	title: item.title,
	// 	description: item.description,
	// 	condition: item.condition,
	// 	category: item.category,
	// 	city: item.city,
	// 	zipcode:item.zipcode,
	// 	tags: item.tags,
	// 	value: item.value,
	// 	image: item.image,
	// 	visible: item.visible
	// }

	Item.findOneAndUpdate(query, item, options, callback);
}*/
//Update Item
module.exports.updateItem = function(id, item, options, callback){
	var query = {_id: id};
	var update = {
		//category: item.category,
		city:item.city,
		condition:item.condition,
		description:item.description,
		//image:item.image,
		title: item.title,
		value: item.value,
		zipcode:item.zipcode,
		tags: item.tags,
		visible: item.visible,
		rate:item.rate
	}
	Item.findOneAndUpdate(query, update, options, callback);
}

/*
//Update Book
module.exports.updateBook = function(id, book, options, callback){
	var query = {_id: id};
	var update = {
		title: book.title,
		genre: book.genre,
		description: book.description,
		author: book.author,
		publisher: book.publisher,
		image_url: book.image_url,
		buy_url:book.buy_url
	}
	Book.findOneAndUpdate(query, update, options, callback);
}
*/
//Delete Item
module.exports.removeItem = function(id, callback){
	//var query = {_id:id};
	Item.remove({'_id':id}, callback);
}




