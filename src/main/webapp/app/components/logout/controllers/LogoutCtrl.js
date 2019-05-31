(function() {
  "use strict";

  angular.module("Matchmakers").controller("LogoutCtrl", LogoutCtrl);

  LogoutCtrl.$inject = [
    "$location",
    "$scope",
    "$state",
    "AuthenticationService"
  ];
  function LogoutCtrl($location, $scope, $state, AuthenticationService) {
    var ctrl = this;
    ctrl.logout = logout;

    (function initController() {
      // reset credentials
      ctrl.logout();
    })();

    function logout() {
      AuthenticationService.Logout();
      $scope.loggedOut();
      $state.go("landing");
    }
  }
})();
