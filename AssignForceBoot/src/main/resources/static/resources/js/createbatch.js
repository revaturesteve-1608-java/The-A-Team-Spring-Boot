/**
 * 
 */
var app = angular.module("batchApp");

app.controller('batchCtrl', function($scope, batchService, trainerService, locationService, transferService){


	$scope.submitButton = "Create Batch";
	
	//Autofill the create batch form with the batch data that was clicked on in view batches
	$scope.viewBatch = batchService.getBatch(
			//passed in callback
			function(response){

				transferService.set(null);
				setTimeout(initViewData(response), 30000);

			}, 
			transferService.get()
	);
	
	//Fills the batch JSON data from the view batch data
	function initViewData(response){
		//Initializing data
		$scope.batchID = response.data.batchID;
		
		$scope.batchName = response.data.bName;
		$scope.topic = $scope.topics[selectTopicByName(response.data.batchTopicID.topicName)];
		$scope.curr = $scope.currs[selectCurrByName(response.data.batchCurriculumID.curriculumName)];
		$scope.date = new Date(response.data.batchStartDate);
		$scope.date2 = new Date(response.data.batchEndDate);

		$scope.room = $scope.rooms[selectRoomByName(response.data.batchRoomID.roomName)];
		$scope.trainer = $scope.trainers[selectTrainerByName(response.data.batchTrainerID.trainerFirstName)];

	}
	
	//All "ByName" functions returns the index of the matching data
	//So we can populate initViewData
	function selectRoomByName(name){
		for(var i = 0; i < $scope.rooms.length; i++){
			if(name === $scope.rooms[i].roomName){
				return i;
			}
		}
	}
	
	function selectTrainerByName(name){
		for(var i = 0; i < $scope.trainers.length; i++){
			if(name === $scope.trainers[i].trainerFirstName){
				return i;
			}
		}
	}
	
	function selectCurrByName(currName){
		for(var i = 0; i < $scope.currs.length; i++){
			if(currName === $scope.currs[i].curriculumName){
				return i;
			}
		}
		
	}
	
	
	function selectTopicByName(topicName){
		for(var i = 0; i < $scope.topics.length; i++){
			if(topicName === $scope.topics[i].topicName){
				return i;
			}
		}
	}
	
	$scope.getCurrs = batchService.getCurrs(
			//passed in callback
			function(response){
				$scope.currs = response.data;
			})
	
	$scope.getTrainers = trainerService.getAllTrainers(
			function(response){
				$scope.trainers = response.data
			}
		);
	
	$scope.getTopics = batchService.getTopics(
			function(response){
				$scope.topics = response.data
			}
		);
	
	$scope.getRooms = batchService.getRooms(
			function(response){
				$scope.rooms = response.data
			}
		);
	
	//Sends the batch data from createbatch.html to a rest controller for database entry
	$scope.saveBatch = function(batchName, topic, curr, trainer, room, date, date2, batchID){
		$scope.updateTask = 
			batchService.saveBatch(batchName, topic, curr, trainer, room, date, date2, batchID);
	}

	
	//////Date Data for createbatch.html////////////////////////
			$scope.today = function(){
		$scope.dt = new Date();
	};
	
	$scope.today();
	
	$scope.clear = function(){
		$scope.dt = null;
	};
	
	$scope.inlineOptions = {
			customClass : getDayClass,
			minDate : new Date(),
			showWeeks : true
	};
	
	$scope.dateOptions = {
			dateDisabled: disabled,
			formatYear: 'yy',
			minDate: new Date(),
			startingDay: 1
	};
	
	//Disable Weekend
	function disabled(data){
		var date = data.date,
		mode = data.mode;
		
		return mode == 'day' && (date.getDay() === 0 || date.getDay() === 6);
	}
	
	$scope.toggleMin = function(){
		$scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
		$scope.dateOptions.minDate = $scope.inlineOptions.minDate;
	};
	
	$scope.toggleMin();
	
	$scope.open1 = function(){
		$scope.popup1.opened = true;
	};
	
	$scope.open2 = function(){
		$scope.popup2.opened = true;
	};
	
	$scope.setDate = function(year, month, day){
		$scope.dt = new Date(year, month, day);
	};
	
	$scope.format = 'dd-MMM-yy';
	
	$scope.popup1 = {
		opened: false
	};
	
	$scope.popup2 = {
			opened: false
		};
	
	  var tomorrow = new Date();
	  tomorrow.setDate(tomorrow.getDate() + 1);
	  var afterTomorrow = new Date();
	  afterTomorrow.setDate(tomorrow.getDate() + 1);
	  $scope.events = [
	    {
	      date: tomorrow,
	      status: 'full'
	    },
	    {
	      date: afterTomorrow,
	      status: 'partially'
	    }
	  ];
	
	
	function getDayClass(data){
		var date = data.date,
		mode = data.mode;
		
		if (mode === 'day'){
			var dayToCheck = new Date(date).setHours(0,0,0,0);
			
			for(var i = 0; i < $scope.events[i].length; i++){
				var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);
				
				if (dayToCheck === currentDay){
					return $scope.events[i].status;
				}
			}
		}
		return '';
	}
	
	
}); //End of date data

app.service('batchService', function($http, $q,$location){

	//Get a batch from database function
	this.getBatch = function(callback, id){

		if(typeof(id) == typeof(NaN)){
			//RestController for this path takes "bId" param
			//"params" converts it to bId
			$http.get('rest/getbatch', {params: {bId: id}}).then(callback);
		}
	}
	
	//The get() functions retrieves a list a JSON objects from the rest controller
	this.getCurrs = function(callback){
		$http.get('rest/curriculum').then(callback);
	}
	
	this.getTopics = function(callback){
		$http.get('rest/topics').then(callback);
	}
	
	this.getRooms = function(callback){
		$http.get('rest/rooms').then(callback);
	}
	
	//Gets data from createbatch.html
	this.saveBatch = function(batchName, topic, curr, trainer, room, date, date2, batchID){
		
		//Convert function params to a JSON object "batchObj"
		var batchObj = {};
		
		if(typeof(batchID) == typeof(NaN)){
			batchObj.batchId = batchID;
		}
		else{
			batchObj.batchId = 0;
		}
		
		batchObj.batchName = batchName;
		batchObj.topic = topic;
		batchObj.curr = curr;
		batchObj.trainer = trainer;
		batchObj.room = room;
		batchObj.date = date;
		batchObj.date2 = date2;
		
		var promise = $http.post('rest/savebatch', batchObj).
		then(
				function(response){
					//After hitting the "createbatch" button in createbatch.html
					//The page will route to the view all batches pages(allbatches.html)
					$location.path("/allbatches");
				},
				
				function(error){
					
				}
		)
		
	}
	
	
})
	
