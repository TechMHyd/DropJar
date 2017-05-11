module.controller('homeController', function ($scope,$sce,$location,homeService) {
	$scope.successFlag=false;
	$scope.errorFlag=false;
	$scope.goHomeFlag=true;
    $scope.reset = function () {
        $scope.file="";
        $scope.receiversEmail="";
        $scope.sendersEmail="";
        $scope.form.email.$dirty=false;
        $scope.form.receiversEmail.$dirty=false;
    }
    $scope.send = function (file) {
    	if(file != null){
    		homeService.uploadFile(file,function(flag,data){
    			if(flag == 'success'){
    				$scope.goHomeFlag= false;
    				$scope.successFlag=true;
    				var url='http://localhost:8080/drop-jar/rest/download/documentFile/'+data.documentIdentifier;
    				$scope.downloadUrl=$sce.trustAsResourceUrl(url);
    			}else if(flag == 'error'){
    				$scope.goHomeFlag= false;
    				$scope.errorFlag=true;
    			}else{
    				$location.path('/');
    			}
    		});
    	}else{
    		alert("Please Upload a File first!");
    	}
    	
    }
    $scope.close=function(){
    	$location.path('/try');
    }
})