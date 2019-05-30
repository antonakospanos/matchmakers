(function () {
    'use strict';

    angular
        .module('Matchmakers')
        .controller('LogoutCtrl', LogoutCtrl);

    LogoutCtrl.$inject = ['$location', '$scope', 'AuthenticationService'];
    function LogoutCtrl($location, $scope, AuthenticationService) {
        var ctrl = this;
        ctrl.logout = logout;

        (function initController() {
            // reset credentials
            ctrl.logout();
        })();

        function logout() {
            AuthenticationService.Logout();
            $scope.loggedOut();
        };
    }

})();
