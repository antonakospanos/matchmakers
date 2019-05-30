/* global angular, _, configData,moment,$,waitingDialog */
(function () {
    "use strict";
    angular
        .module("Matchmakers")
        .controller("HeaderCtrl", ["$scope", "$http", HeaderCtrl]);

    function HeaderCtrl($scope, $http) {

        // Auto refresh footer every 5 seconds to re-calculate session changes
        $scope.intervalTimer = setInterval(function () {
            $scope.refreshHeader()
        }, 5000);

        $scope.refreshHeader = function() {
            refreshHeader();
        }

        function refreshHeader() {
            var header = $http.defaults.headers.common.Authorization;
            console.log("Authorization header: " + header)
            if (header === undefined || header === "") {
                $scope.loggedOut()
            } else {
                $scope.loggedIn()
            }
        }

        $scope.initHeader = function initHeader() {
            $scope.refreshHeader()
        }

        $scope.loggedIn = function() {
            setSubscriptionMenus('initial');
            setLoginButton('none')
            setRegisterButton('none')
            setLogoutButton('initial');
        }

        $scope.loggedOut = function() {
            setSubscriptionMenus('none');
            setLoginButton('initial');
            setRegisterButton('initial');
            setLogoutButton('none');
        }

        function setLoginButton(display) {
            var alert = document.getElementById("login-button");
            alert.style.display = display;
        }

        function setRegisterButton(display) {
            var alert = document.getElementById("register-button");
            alert.style.display = display;
        }

        function setLogoutButton(display) {
            var alert = document.getElementById("logout-button");
            alert.style.display = display;
        }

        function setSubscriptionMenus(display) {
            var alert = document.getElementById("subscription-menus");
            alert.style.display = display;
        }
    }
}());
