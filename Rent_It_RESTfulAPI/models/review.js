var mongoose = require('mongoose');

var reviewSchema = mongoose.Schema({
	item:{
		type:String
	},
	owner:{
		type: String
	},
	item_rating:{
		type: Number
	},
	title:{
		type: String
	},
	item_comment:{
		type: String
	},
	owner_rating:{
		type: Number
	},
	owner_comment:{
		type: String
	},
	reviewer:{
		type: String
	},
	date_created:{
		type: Date,
		default: Date.now
	},
});

var Review = module.exports = mongoose.model('Review',reviewSchema);

//Get All Reviews
module.exports.getReviews = function(callback, limit){
	Review.find(callback).limit(limit);
}

//Get a latest Review
module.exports.getLatestReviewByItemId = function(item, callback){
	Review.findOne({'item': item})
		.sort({date_created: -1})
		.exec(callback);
}

//Get Reviews by Item Id
module.exports.getReviewsByItemId = function(item, callback){
	Review.find()
		.where('item').equals(item)
		.exec(callback);
}

//Get Reviews by Owner Id
module.exports.getReviewsByOwnerId = function(owner, callback){
	Review.find()
		.where('owner').equals(owner)
		.exec(callback);
}
