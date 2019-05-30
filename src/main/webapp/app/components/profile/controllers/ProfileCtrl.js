(function() {
  "use strict";
  angular.module("Matchmakers").controller("ProfileCtrl", ProfileCtrl);

  ProfileCtrl.$inject = [
    "UploadService",
    "$rootScope",
    "$scope",
    "$http",
    "$state"
  ];

  function ProfileCtrl(UploadService, $rootScope, $scope, $http, $state) {
    var ctrl = this;
    ctrl.profileUrl =
      $rootScope.backend_protocol +
      "://" +
      $rootScope.backend_ip +
      ":" +
      $rootScope.backend_port +
      "/" +
      $rootScope.backend_context_path +
      "/candidates";

    ctrl.init = function() {
      $scope.candidate = {};
      $scope.initialModel = angular.copy($scope.candidate);
    };

    ctrl.reset = function() {
      var message = "This will reset the form. Proceed anyway?";
      $scope.modalWarning(message, "RESET").then(function(response) {
        if (response === true) {
          $scope.candidate = angular.copy($scope.initialModel);
          // location.reload();
          $scope.scrollTop();
        }
      });
    };

    ctrl.cancel = function() {
      var message = "Your work will be lost. Proceed anyway?";
      $scope.modalWarning(message, "PROCEED").then(function(response) {
        if (response === true) {
          $state.go("jobs");
          $scope.scrollTop();
        }
      });
    };

    ctrl.getFormCtrl = function() {
      var retval = $scope.$$childHead;
      if (retval) {
        retval = retval.profileForm;
      }
      return retval;
    };

    ctrl.isValid = function() {
      var formCtrl = ctrl.getFormCtrl();
      if (formCtrl && formCtrl.$valid) {
        return true;
      }

      return false;
    };

    ctrl.cvChanged = function(cvInput) {
      UploadService.UploadToS3({user: $rootScope.globals.currentUser.token, file: cvInput.files[0]})
        .then(function(response) {
          console.log(response);
          return null;
          return UploadService.SendUploadedCV({
            user: $rootScope.globals.currentUser.token,
            url: response.data
          });
        })
        .then(
          function successCallback(response) {
            $scope.refreshJobs();
            $state.go("jobs");
            // Reload footer's img to switch from alert to check-mark!
            $scope.createToast(
              response.data.result + "! " + response.data.description
            );
            if ($rootScope.jobs === 0) {
              location.reload();
            } else {
              $scope.scrollTop();
            }
          },
          function errorCallback(response) {
            $scope.createToast(
              response.data.result + "! " + response.data.description
            );
            // var message = response.data.result + "<br/>" + response.data.description;
            // $scope.modalError(message, "100");
          }
        );
    };

    ctrl.add = function() {
      var message = "This will updated your profile to Matchmakers. Proceed?";
      var config = {
        headers: {
          "Content-Type": "application/json;charset=utf-8;"
        }
      };

      $scope
        .modalWarning(message, "ADD")
        .then(function() {
          return UploadService.UploadToS3($file);
        })
        .then(function(response) {
          return UploadService.SendUploadedCV({
            user: $rootScope.globals.currentUser.token,
            url: response.data
          });
        })
        .then(
          function successCallback(response) {
            $scope.refreshJobs();
            $state.go("jobs");
            // Reload footer's img to switch from alert to check-mark!
            $scope.createToast(
              response.data.result + "! " + response.data.description
            );
            if ($rootScope.jobs === 0) {
              location.reload();
            } else {
              $scope.scrollTop();
            }
          },
          function errorCallback(response) {
            $scope.createToast(
              response.data.result + "! " + response.data.description
            );
            // var message = response.data.result + "<br/>" + response.data.description;
            // $scope.modalError(message, "100");
          }
        );
    };
  }
})();
