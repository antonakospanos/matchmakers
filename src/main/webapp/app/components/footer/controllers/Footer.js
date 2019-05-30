/* global angular, _, configData,moment,$,waitingDialog */
(function () {
    "use strict";
    angular
        .module("MovieRamaUi")
        .controller("FooterCtrl", ["$rootScope", "$scope", "$http", "$mdToast", FooterCtrl]);

    function FooterCtrl($rootScope, $scope, $http, $mdToast) {

        // Auto refresh footer every 5 seconds to re-calculate backend changes
        // $scope.intervalTimer = setInterval(function () {
        //     $scope.refreshMovies()
        // }, 5000);

        $scope.refreshMovies = function() {
            refreshMovies();
        }

        $scope.initFooter = function initFooter() {
            setConfigCheck('none')
            setConfigAlert('initial')
            setConfigPrompt("The backend server is starting...");
            setProgressBar('none')
            refreshMovies();
        }

        function refreshMovies() {
            var moviesUrl = $rootScope.backend_protocol + "://" + $rootScope.backend_ip + ":" + $rootScope.backend_port + "/" + $rootScope.backend_context_path + "/movies";

            // Lookup for /movies
            $http.get(moviesUrl)
                .then(function successCallback(response) {
                    $rootScope.movies = response.data.length;
                    setConfigCheck('initial')
                    setConfigAlert('none')
                    setConfigPrompt($rootScope.movies + " movies stored in Matchmakers");
                }, function errorCallback(response) {
                   $rootScope.movies = response.data.length;
                    setConfigCheck('none')
                    setConfigAlert('initial')
                    if ($rootScope.movies === undefined) {
                        setConfigPrompt("Could not find movies in Matchmakers");
                    } else {
                        setConfigPrompt($rootScope.movies + " movies stored in Matchmakers");
                    }
                });
        }

        function setConfigCheck(display) {
            var check = document.getElementById("configCheck");
            check.style.display = display;
        }

        function setConfigAlert(display) {
            var alert = document.getElementById("configAlert");
            alert.style.display = display;
        }

        function setConfigPrompt(msg) {
            var divElement = document.getElementById("configPrompt");
            for (var i = 0; i < divElement.childNodes.length; ++i) {
                divElement.removeChild(divElement.childNodes[i]);
            }
            var textNode = document.createTextNode(msg);
            divElement.appendChild(textNode);
        }

        function setProgressBar(display) {
            var actions = document.getElementById("progressBar");
            actions.style.display = display;
        }

        $scope.createToast = function(message) {
            $mdToast.show(
                $mdToast.simple()
                    .textContent(message)
                    .parent(document.querySelectorAll('#toaster'))
                    .hideDelay(5000)
                    .action('x')
                    .capsule(true)
            );
        };
    }
}());
