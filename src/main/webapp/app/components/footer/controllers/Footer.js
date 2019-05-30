/* global angular, _, configData,moment,$,waitingDialog */
(function () {
    "use strict";
    angular
        .module("Matchmakers")
        .controller("FooterCtrl", ["$rootScope", "$scope", "$http", "$mdToast", FooterCtrl]);

    function FooterCtrl($rootScope, $scope, $http, $mdToast) {

        // Auto refresh footer every 5 seconds to re-calculate backend changes
        // $scope.intervalTimer = setInterval(function () {
        //     $scope.refreshJobs()
        // }, 5000);

        $scope.refreshJobs = function() {
            refreshJobs();
        }

        $scope.initFooter = function initFooter() {
            setConfigCheck('none')
            setConfigAlert('initial')
            setConfigPrompt("The backend server is starting...");
            setProgressBar('none')
            refreshJobs();
        }

        function refreshJobs() {
            var jobsUrl = $rootScope.backend_protocol + "://" + $rootScope.backend_ip + ":" + $rootScope.backend_port + "/" + $rootScope.backend_context_path + "/jobs";

            // Lookup for /jobs
            $http.get(jobsUrl)
                .then(function successCallback(response) {
                    $rootScope.jobs = response.data.length;
                    setConfigCheck('initial')
                    setConfigAlert('none')
                    setConfigPrompt($rootScope.jobs + " jobs matched to your profile");
                }, function errorCallback(response) {
                   $rootScope.jobs = response.data.length;
                    setConfigCheck('none')
                    setConfigAlert('initial')
                    if ($rootScope.jobs === undefined) {
                        setConfigPrompt("Couldn't find jobs for your profile");
                    } else {
                        setConfigPrompt($rootScope.jobs + " jobs matched to your profile");
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
