(function () {
    'use strict';

    angular
        .module('Matchmakers')
        .controller('LoginCtrl', LoginCtrl);

    LoginCtrl.$inject = ['$location', 'AuthenticationService', "$scope", "$state"];
    function LoginCtrl($location, AuthenticationService, $scope, $state) {
        var ctrl = this;
        ctrl.login = login;

        (function initController() {
            // reset credentials
            AuthenticationService.Logout();
        })();

        function login() {
            AuthenticationService.Login(ctrl.username, ctrl.password)
                .then(function successCallback(response) {
                    console.log(response)
                    if (response.data && response.data.id !== undefined) {
                        AuthenticationService.Authorize(ctrl.username, response.data.id);
                        $scope.loggedIn();
                        $scope.createToast("User '" + ctrl.username + "' logged in successfully")
                        $state.go("jobs");
                        // $location.path("/");
                    } else {
                        $scope.createToast(response.data.result + "! " + response.data.description)
                    }
            });
        };
    }

})();
