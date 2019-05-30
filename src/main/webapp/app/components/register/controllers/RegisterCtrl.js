(function() {
  "use strict";

  angular.module("Matchmakers").controller("RegisterCtrl", RegisterCtrl);

  RegisterCtrl.$inject = [
    "UserService",
    "AuthenticationService",
    "$location",
    "$rootScope",
    "$scope",
    "$state"
  ];
  function RegisterCtrl(
    UserService,
    AuthenticationService,
    $location,
    $rootScope,
    $scope,
    $state
  ) {
    var ctrl = this;
    ctrl.register = register;

    function register() {
      UserService.Create(ctrl.user).then(function(response) {
        console.log(response);
        $rootScope.createToast(
          response.data.result + "! " + response.data.description
        );
        if (response.data.result === "SUCCESS") {
          AuthenticationService.Authorize(ctrl.email, response.data.data.id);
          $scope.loggedIn();
          $state.go("cv");
          // $location.path("/");
        }
      });
    }
  }
})();
