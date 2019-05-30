(function() {
  "use strict";
  angular
    .module("Matchmakers")
    .controller("ProfileCtrl", [
      "UploadService",
      "CandidateService",
      "$rootScope",
      "$scope",
      "$http",
      "$state",
      ProfileCtrl
    ]);

  function ProfileCtrl(UploadService, CandidateService, $rootScope, $scope, $http, $state) {
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
      $scope.user = $rootScope.globals.currentUser || {};
      $scope.objective = {};
      $scope.initialModel = angular.copy($scope.candidate);
      CandidateService.Read($scope.user.token).then(function setObjective(response) {
        console.log(response)
        var {roles, locationsSecondary, locationsPrimary} = response.data.objective || {roles: [], locationsSecondary: [], locationsPrimary: []};
        $scope.objective = {role: roles[0], locationsSecondary: locationsSecondary[0], locationsPrimary: locationsPrimary[0]};
      })
    };

    ctrl.getFormCtrl = function() {
      var retval = $scope.$$childHead;
      if (retval) {
        retval = retval.formCtrl;
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
      UploadService.UploadResumeFile({userToken: $rootScope.globals.currentUser.token, file: cvInput.files[0]})
        .then(function(response) {
          console.log(response);
          return response;
          return null;
          return UploadService.SendUploadedCV({
            user: $rootScope.globals.currentUser.token,
            url: response.data
          });
        })
        .then(
          function successCallback(response) {
            // Reload footer's img to switch from alert to check-mark!
            $scope.createToast(
              response.data.description
            );
          },
          function errorCallback(response) {
            $scope.createToast(
              response.data.description
            );
          }
        );
    };

    ctrl.add = function() {
      var requestData = {
        ...$scope.user,
        objective: {
          roles: [$scope.objective.role],
          locationsPrimary: [$scope.objective.locationsPrimary],
          locationsSecondary: [$scope.objective.locationsSecondary]
        }
      };

      CandidateService.Update(requestData).then(
        function successCallback(response) {
          $state.go("jobs");
          // Reload footer's img to switch from alert to check-mark!
          $scope.createToast(
             response.data.description
          );
          if ($rootScope.jobs === 0) {
            location.reload();
          } else {
            $scope.scrollTop();
          }
        },
        function errorCallback(response) {
          $scope.createToast(
             response.data.description
          );
        }
      );
    };
  }
})();
