var mongoose = require('mongoose');

var rentalSchema = mongoose.Schema({
	rental_id:{
		type:String,
		required:true
	},
	renter:{
		type:String
	},
	owner:{
		type:String
	},
	item:{
		//type:String
		type:String,
		ref: 'Item'
	},
	rental_status:{
		type:Number
	},
	booked_start_date:{
		type:Date
	},
	booked_end_date:{
		type:Date
	},
	booked_period:{
		type:Number
	},
	estimated_total:{
		type:Number
	},
	image:{
		type:Number
	},
	rental_started_date:{
		type:Date
	},
	rental_end_date:{
		type:Date
	},
	payment_status:{
		type:Number
	},
	daily_rate:{
		type:Number
	},
	rental_period:{
		type:Number
	},
	service_fee:{
		type:Number
	},
	tax:{
		type:Number
	},
	total:{
		type:Number
	},
	created_date:{
		type: Date,
		default: Date.now
	}
});

var Rental =  module.exports = mongoose.model('Rental',rentalSchema);


//Get All Rentals
module.exports.getRentals = function(callback, limit){
	Rental.find(callback).limit(limit);
}

//Get item IDs of rentals for trade list - not using
module.exports.getContactedRentalsItemIDs = function(renter, callback){
	var query = {renter:renter,rental_status: 1};
	Rental.find(query)
		.sort({created_date:-1})
		.select('-_id item')
		.exec(function(err, items){
			if(err){
				throw err;
			}else{
				// extract the item ids from the items
				var itemIDs=[];
				for(var i=0;i<items.length;i++){
				   itemIDs.push(items[i].item);
				}
				callback(err, itemIDs);
			}
			
		});
}
//using
module.exports.getContactedRentalsItems = function(renter, callback){
	var query = {renter:renter,rental_status: 1};
	Rental.find(query)
		  .sort({created_date:-1})
		  .populate('item')
		  .exec(callback);
}

//
module.exports.getRentalWithItemByRentalId = function(rentalid, callback){
	var query = {rental_id:rentalid};
	Rental.findOne(query)
		  .populate('item')
		  .exec(callback);
}

//Add Rental
module.exports.addRental = function(rental, callback){
	Rental.create(rental, callback);
}

//Update Item
/*module.exports.updateItem = function(id, item, options, callback){
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
}*/






