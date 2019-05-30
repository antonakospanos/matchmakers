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
        debugger;
      UserService.Create($scope.user).then(function(response) {
        $rootScope.createToast(
          response.data.result + "! " + response.data.description
        );
        if (response.data.result === "SUCCESS") {
          AuthenticationService.Authorize($scope.user.email, response.data.data.id, $scope.user.name);
          $scope.loggedIn();
          $state.go("cv");
          // $location.path("/");
        }
      });
    }
  }
})();
