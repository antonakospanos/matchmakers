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
            AuthenticationService.Login(ctrl.email, ctrl.password)
                .then(function successCallback(response) {
                    console.log(response)
                    if (response.data && response.data.id !== undefined) {
                        AuthenticationService.Authorize(ctrl.email, response.data.id);
                        $scope.loggedIn();
                        $scope.createToast("User '" + ctrl.email + "' logged in successfully")
                        $state.go("jobs");
                        // $location.path("/");
                    } else {
                        $scope.createToast(response.data.result + "! " + response.data.description)
                    }
            });
        };
    }

})();
