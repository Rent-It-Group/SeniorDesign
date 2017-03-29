var mongoose = require('mongoose');

var claimSchema = mongoose.Schema({
	role:{
		type:Number
	},
	meeting_date:{
		type: String
	},
	item:{
		type: String
	},
	reason:{
		type: String
	},
	image:{
		type: String
	},
	issue:{
		type: String
	},
	status:{
		type: Number
	},
	submitted_by:{
		type: String
	},
	rental_id:{
		type: String
	},
	date_created:{
		type: Date,
		default: Date.now
	}
});

var Claim = module.exports = mongoose.model('Claim',claimSchema);

//Get All Reviews
module.exports.getClaims = function(callback, limit){
	Claim.find(callback).limit(limit);
}

//Add Claim
module.exports.addClaim = function(claim, callback){
	Claim.create(claim, callback);
}

//Get a latest Review
/*module.exports.getLatestReviewByItemId = function(item, callback){
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
*/