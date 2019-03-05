var app = angular.module('app', ['ngRoute', 'firebase', 'chart.js']);

// Initialize Firebase
var config = {
    apiKey: "the api key",
    authDomain: "the authentication domain",
    databaseURL: "the url of the database",
    projectId: "the id of the firebase project",
    storageBucket: "the bucket storage",
    messagingSenderId: "the messaing sender id"
};
firebase.initializeApp(config);

app.controller('MyController', ['$scope', '$firebaseObject', '$firebaseArray', "$filter", function ($scope, $firebaseObject, $firebaseArray, $filter) {
    const rootRef = firebase.database().ref().child('items');
    var myFirebaseArray = $firebaseArray(rootRef);
    $scope.currentlabels = [];
    $scope.series = ['Series A'];
    $scope.testsNames = [];
    $scope.testsTime = [];
    $scope.measurements = [];
    var i = 0;

    myFirebaseArray.$loaded().then(function () {
        angular.forEach(myFirebaseArray, function (test) {
            if ($scope.testsNames.indexOf(test.testName) == -1)
                $scope.testsNames.push(test.testName);
            if (test.time != null)
                $scope.testsTime.push(test.time);
        });
        $scope.measurements.push("cpu");
        $scope.measurements.push("memory");
    });

    $scope.createGraph = function (selectedTest, timeSelected, measurementSelected) {
        myFirebaseArray.$loaded().then(function () {
            angular.forEach(myFirebaseArray, function (test) {
                if (test.time == timeSelected && test.testName == selectedTest) {
                    $scope.currentData = new Array();
                    for (var key in test) {
                        var specimentRecord = test[key];

                        if (specimentRecord != null && typeof specimentRecord !== "undefined") {
                            $scope.currentData.push(specimentRecord);
                            $scope.currentlabels.push(specimentRecord.time);
                        }
                    }
                    $scope.currentData = $filter('orderBy')($scope.currentData, "time");
                    $scope.data = new Array();
                    $scope.labels = new Array();
                    for (var key in $scope.currentData) {
                        if (measurementSelected == "cpu" && typeof $scope.currentData[key].cpu !== "undefined")
                            $scope.data.push($scope.currentData[key].cpu);
                        if (measurementSelected == "memory" && typeof $scope.currentData[key].mem !== "undefined")
                            $scope.data.push($scope.currentData[key].mem);
                        if (typeof $scope.currentData[key].cpu !== "undefined" || typeof $scope.currentData[key].mem !== "undefined")
                            $scope.labels.push($scope.currentData[key].time);
                    }
                }
            });
        });
    };
}]);