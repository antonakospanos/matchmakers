(function() {
  "use strict";

  angular.module("Matchmakers").controller("LoginCtrl", LoginCtrl);

  LoginCtrl.$inject = [
    "$location",
    "AuthenticationService",
    "$rootScope",
    "$scope",
    "$state"
  ];
  function LoginCtrl(
    $location,
    AuthenticationService,
    $rootScope,
    $scope,
    $state
  ) {
    var ctrl = this;
    ctrl.login = login;

    (function initController() {
      // reset credentials
      AuthenticationService.Logout();
    })();

    function login() {
      AuthenticationService.Login(ctrl.email, ctrl.password).then(
        function successCallback(response) {
          if (response.data && response.data.id !== undefined) {
            AuthenticationService.Authorize(ctrl.email, response.data.id, response.data.name);
            $scope.loggedIn();
            $rootScope.createToast(
              "User '" + ctrl.email + "' logged in successfully"
            );
            $state.go("jobs");
            // $location.path("/");
          } else {
            $rootScope.createToast(
               response.data.description
            );
          }
        }
      );
    }
  }
})();
