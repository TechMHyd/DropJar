module.service('homeService', function ($http) {
	var flag='invalid';
	this.uploadFile = function(file,callback){
			
		var fd = new FormData();
        fd.append('file', file);
			var responsePromise = $http({	url: "http://localhost:8080/drop-jar/rest/uploadfile/document", 
											method: "POST" ,// In this case it is POST
											headers: { 'Content-Type': undefined },
											transformRequest: angular.identity,
											data: fd
			 		});
					responsePromise.success(function(data, status, headers, config) {
						flag = 'success';
						callback(flag,data);
					});
					responsePromise.error(function(data, status, headers, config) {
						flag = 'error';
						callback(flag);
					});
	}

	/*this.download = function(documentData){
		
		var responsePromise = $http({	url: "http://localhost:8080/drop-jar/rest/download/documentFile/", 
										method: "POST" ,// In this case it is POST
										headers: { 'Content-Type': 'application/json' },
										responseType: 'arraybuffer',
										data: documentData
		 		});
				responsePromise.success(function(data, status, headers, config) {
					alert("Download success");
					var blob = new Blob([data], {type: "image/jpeg"});
		            var objectUrl = URL.createObjectURL(blob);
		            window.open(objectUrl);
				});
				responsePromise.error(function(data, status, headers, config) {
					alert("AJAX failed! because no webservice is attached yet");      					 
				});
	}  */
});