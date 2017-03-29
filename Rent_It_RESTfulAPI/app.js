require('dotenv').config();
var braintree = require("braintree");
var gcm = require('node-gcm');

var gateway = braintree.connect({
  environment: braintree.Environment.Sandbox,
  merchantId: process.env.BRAINTREE_MERCHANT_ID,
  publicKey: process.env.BRAINTREE_PUBLIC_KEY,
  privateKey: process.env.BRAINTREE_PRIVATE_KEY
});

// Module dependencies
var express = require('express');
// create instance
var app = express();

var bodyParser = require('body-parser');
var mongoose = require('mongoose');

app.use(bodyParser.json());

Category = require('./models/category');
Item = require('./models/item');
Review = require('./models/review');
Claim = require('./models/claim');
Tag = require('./models/tag');
User = require('./models/user')
Rental = require('./models/rental');

//Connect to Mongoose
var connection_options = {
	user: process.env.MONGO_DB_USER,
	pass: process.env.MONGO_DB_PASS
};
mongoose.connect(process.env.MONGO_DB_URL,
					connection_options);
//mongoose.connect('mongodb://localhost/rent_it');
//mongoose.connect('mongodb://localhost/bookstore');
var db = mongoose.connection;

/*app.get('/', function(req,res){
	res.send('Hello World! Please user /api/books or /api/genres');
});*/

//get all categories
app.get('/api/categories',function(req,res){
	console.log("reached the server");
	Category.getCategories(function(err,categories){
		if(err){
			throw err;
		}
		res.json(categories);
	});
});

//get all tags -no
app.get('/api/tags',function(req,res){
	Tag.getTags(function(err,tags){
		if(err){
			throw err;
		}
		res.json(tags);
	});
});

/*app.post('/api/categories',function(req,res){
	var genre = req.body;
	Genre.addGenre(genre,function(err,genre){
		if(err){
			throw err;
		}
		res.json(genre);
	});
});*/

/*app.put('/api/categories/:_id',function(req,res){
	var id = req.params._id;
	var category = req.body;
	Category.updateCategory(id, category, {}, function(err,category){
		if(err){
			throw err;
		}
		res.json(category);
	});
});*/

/*app.delete('/api/genres/:_id',function(req,res){
	var id = req.params._id;
	var genre = req.body;
	Genre.removeGenre(id, function(err,genre){
		if(err){
			throw err;
		}
		res.json(genre);
	});
});*/

/*Item.find({}, function(err,items){
	if (err) throw err;

	console.log(items);
});*/
//get all items
app.get('/api/items',function(req,res){
	console.log("reached the server");
	Item.getItems(function(err, items){
		if(err){
			throw err;
		}
		res.json(items);
	});
});
//get tags
app.get('/api/item/tags', function(req, res) {
    var array = [];
	Item.getTags(function(err, items){
		if(err){
			throw err;
		}
		//console.log(items);
		for(var i = 0, len = items.length; i < len; i++){
			console.log(items[i]);
		}
		res.json(items);
	});
});
//get item by tag
app.get('/api/items/tag/:tag',function(req,res){
	Item.getItemsByTag(req.params.tag,function(err,items){
		if(err){
			throw err;
		}
		res.json(items);
	})
});

//get item by id
app.get('/api/item/:_id',function(req,res){
	Item.getItemById(req.params._id,function(err,item){
		if(err){
			throw err;
		}
		res.json(item);
	})
});
//get item by category
app.get('/api/items/category/:category',function(req,res){
	Item.getItemsByCategoryId(req.params.category,function(err,items){
		if(err){
			throw err;
		}
		res.json(items);
	})
});
//get one item by uid
app.get('/api/items/user/:uid',function(req,res){
	Item.getItemsByUid(req.params.uid,function(err,items){
		if(err){
			throw err;
		}
		res.json(items);
	})
});
//add new item
app.post('/api/items',function(req,res){
	var item = req.body;
	Item.addItem(item,function(err,item){
		if(err){
			throw err;
		}
		res.json(item);
	});
});

//update item
/*app.put('/api/item/:_id',function(req,res){
	var id = req.params._id;
	var item = req.body;
	Item.updateItem(id, item, {}, function(err,item){
		if(err){
			throw err;
		}
	});
});*/

/*app.put('/api/item/:_id',function(req,res){
	var id = req.param.id;
	Item.findOne({_id:id}, function(err,foundItem) {
		if(err) {
			console.log(err);
			res.status(500).send();
		} else {
			if(!foundItem) {
				res.status(404).send();
			} else {
				if(req.body.value) {
					foundItem.value = req.body.value;
				}

				foundItem.save(function(err, updatedItem){
					if(err) {
						console.log(err);
						res.status(500).send();
					} else {
						res.send(updatedItem);
					}
				});
			}
		}
	});
});*/
app.put('/api/item/:_id',function(req,res){
	var id = req.params._id;
	var item = req.body;
	Item.updateItem(id, item, {}, function(err,item){
		if(err){
			throw err;
		}
		res.json(item);
	});
});


/*
app.put('/api/books/:_id',function(req,res){
	var id = req.params._id;
	var book = req.body;
	Book.updateBook(id, book, {}, function(err,book){
		if(err){
			throw err;
		}
		res.json(book);
	});
});
*/
//Delete Item
/*app.delete('/api/items/:_id',function(req,res){
	var id = req.params._id;
	var item = req.body;
	Item.removeItem(id, function(err,item){
		if(err){
			throw err;
		}
		res.json(item);
	});
});*/
//get all claims
app.get('/api/claims',function(req,res){
	console.log("reached the server");
	Claim.getClaims(function(err, claims){
		if(err){
			throw err;
		}
		res.json(claims);
	});
});

//add new claim
app.post('/api/claims',function(req,res){
	var claim = req.body;
	Claim.addClaim(claim,function(err,claim){
		if(err){
			throw err;
		}
		res.json(claim);
	});
});

//get all reviews
app.get('/api/reviews',function(req,res){
	console.log("reached the server");
	Review.getReviews(function(err, reviews){
		if(err){
			throw err;
		}
		res.json(reviews);
	});
});

//get latest review by item id
app.get('/api/review/item/:item',function(req,res){
	Review.getLatestReviewByItemId(req.params.item,function(err,review){
		if(err){
			throw err;
		}
		if(!review){
			//(review == null) works too
			console.log("review is null 1");
			review =[];
			console.log("review is now " + review);
		}
	
		res.json(review);
	})
});

//get review by item id
app.get('/api/reviews/item/:item',function(req,res){
	Review.getReviewsByItemId(req.params.item,function(err,reviews){
		if(err){
			throw err;
		}

			console.log("review is  " + reviews);
		res.json(reviews);
	})
});

//get review by owner id
app.get('/api/reviews/owner/:owner',function(req,res){
	Review.getReviewsByOwnerId(req.params.owner,function(err,reviews){
		if(err){
			throw err;
		}
		res.json(reviews);
	})
});
//User Model
//get all reviews
app.get('/api/users',function(req,res){
	User.getUsers(function(err, users){
		if(err){
			throw err;
		}
		res.json(users);
	});
});
//create a new user
app.post('/api/users', function(req,res){
	var user = req.body;
	User.createUser(user,function(err,user){
		if(err){
			throw err;
		}
		res.json(user);
	});
});
//get user by uid
app.get('/api/user/:uid',function(req,res){
	User.getUserByUid(req.params.uid,function(err,user){
		if(err){
			throw err;
		}
		res.json(user);
	})
});

//update user
app.put('/api/user/:uid',function(req,res){
	var uid = req.params.uid;
	var user = req.body;
	User.updateUser(uid, user, {}, function(err,user){
		if(err){
			throw err;
		}
		res.json(user);
	});
});



//Rental Model
//get all rentals
app.get('/api/rentals',function(req,res){
	Rental.getRentals(function(err, rentals){
		if(err){
			throw err;
		}
		res.json(rentals);
	});
});

//create a new rental
app.post('/api/rentals', function(req,res){
	var rental = req.body;
	Rental.addRental(rental,function(err,rental){
		if(err){
			throw err;
		}
		res.json(rental);
	});
});

//get contacted rentals by renter
app.get('/api/rentals/renter/:renter',function(req,res){
	Rental.getContactedRentalsItems(req.params.renter, function(err,rentalsItems){
		if(err){
			throw err;
		}else{
			res.json(rentalsItems);
		}
	});
	/*Rental.getContactedRentalsItemIDs(req.params.renter,function(err,itemIDs){
		if(err){
			throw err;
		}else{
			Item.getItemsByItemIDs(itemIDs, function(err, items){
				if(err){
					throw err;
				}else{
					res.json(items);
				}
			});
		}
		//res.json(rentals);
	})*/
});

//get rentals by rental id
app.get('/api/rental/:rental_id',function(req,res){
	Rental.getRentalWithItemByRentalId(req.params.rental_id, function(err,rentalsItems){
		if(err){
			throw err;
		}else{
			res.json(rentalsItems);
		}
	});
});

//retrieve client token if given a customer id
//retrieve client token
app.get('/api/bt/client_token/:user_id', function(req,res){
	//find the user based on user_id
	console.log('/api/bt/client_token/:user_id');

	User.getUserByUid(req.params.user_id, function(err,user){
		if(err){
			throw err;
		}
		console.log(user);

		//if no customer id found, generate one for the user
		gateway.customer.find(user.braintree_customer_id, function(err, customer){
			if (err) {
			    console.log(err.type); // "notFoundError"
			    console.log(err.name); // "notFoundError"
			    console.log(err.message); // "Not Found"

			    gateway.customer.create({
			    	id: user.braintree_customer_id
			    }, function(err, result){
			    	if(err){
		    			console.log(err.type);
				    	console.log(err.name); 
				    	console.log(err.message); 
			    	}else{
		    			console.log(result);

		    			//if they have a customer_id, use it
						//generate client token and return it
						gateway.clientToken.generate({
							customerId: user.braintree_customer_id
						}, function (err, response) {
							if(err){
								throw err;
							}else{
								console.log(response.clientToken);
								var clientToken = response.clientToken;
				  				res.send(response.clientToken);
				  			}
						});
			    	}
			    });
			  } else {
			    console.log(customer);

			    //if they have a customer_id, use it
				//generate client token and return it
				gateway.clientToken.generate({
					customerId: user.braintree_customer_id
				}, function (err, response) {
					if(err){
						throw err;
					}else{
						console.log(response.clientToken);
						var clientToken = response.clientToken;
		  				res.send(response.clientToken);
		  			}
				});
			  }		
		});

		

	});
});

app.post('/api/rental/:rental_id', function(req,res){
	var rental_id = req.params.rental_id;
	Rental.getRentalWithItemByRentalId(rental_id, function(err, rental){
		if(err){
			throw err;
		}else{
			console.log('rental:'+rental);
			User.getUserByUid(rental.renter, function(err, renter){
				if(err){
					throw err;
				}else{
					console.log('renter:'+renter);
					User.getUserByUid(rental.owner, function(err, lender){
						if(err){
							throw err;
						}else{
							// We now have the item (rental.item), renter, and lender info
							console.log('lender:'+lender);
							sendFCM(rental, renter, lender, function(err, response){
								res.json(response);
							});
						}
					});
				}
			});
		}
	});

	//sendFCM();
	
});

/*function sendFCM(rental_id){
	var lenderID = 'onBNW00rlNg9S1CmBWDHTOu0j3Z2';	// gotten from a DB call via item

	User.getUserByUid(lenderID, function(err, lender){
		if(err){
			console.log(err);
		} else{
			console.log(lender);

			var renter = 'Fred';	// replace this with either a DB lookup here
									// or the parameter of renter name should be
									// passed into the function
			var item = 'Speedboat';	// replace this with either a DB lookup here
									// or the parameter of renter name should be
									// passed into the function
			var rental_request = 	renter +
									' would like to rent your ' +
									item +
									'!';

			var message = new gcm.Message({
			    collapseKey: 'demo',
			    priority: 'high',
			    contentAvailable: true,
			    data: {
			        rentalId: rental_id
			    },
			    notification: {
			        title: 'Rental Request!',
			        icon: 'ic_launcher',
			        body: rental_request
			    }
			});

			console.log(lender.uid);
			console.log(lender.fcm_token);

			// Set up the sender with you API key, prepare your recipients' registration tokens. 
			var sender = new gcm.Sender(process.env.FCM_API_KEY);
			var regTokens = [lender.fcm_token];
			 
			sender.send(message, { registrationTokens: regTokens }, function (err, response) {
			    if(err) {
			    	console.error(err);
			    }else {
			    	console.log(response);
			    }
			});
		}

		
	});

	
}*/

function sendFCM(rental, renter, lender, callback){

	var renter_name = renter.display_name;
	var item_name = rental.item.title;
	var rental_request = 	renter_name +
							' would like to rent your ' +
							item_name +
							'!';

	var message = new gcm.Message({
	    collapseKey: 'demo',
	    priority: 'high',
	    contentAvailable: true,
	    data: {
	        rentalId: rental.rental_id
	    },
	    notification: {
	        title: 'Rental Request!',
	        icon: 'ic_launcher',
	        body: rental_request
	    }
	});

	console.log(lender.uid);
	console.log(lender.fcm_token);

	// Set up the sender with you API key, prepare your recipients' registration tokens. 
	var sender = new gcm.Sender(process.env.FCM_API_KEY);
	var regTokens = [lender.fcm_token];
	 
	sender.send(message, { registrationTokens: regTokens }, callback);
}

//sendFCM('15b2888c-837d-4fd5-ae1d-093aac5ec5f4');

app.listen(process.env.PORT_NO);
console.log('Running on port 3000...');

// Router
/*var router = require('./routes/router.js')
app.use('/', router);*/

// Setup Server configuration
/*var port = process.env.PORT || 3000;
app.listen(port);
console.log('Node is running on port ' + port);*/

