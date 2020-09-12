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
    ])
    .directive("chooseFile", function() {
      return {
        link: function(scope, elem, attrs) {
          var button = elem.find("button");
          var inputDisplay = elem.find("input.input-display");
          var input = angular.element(elem[0].querySelector("input#fileInput"));
          button.bind("click", function() {
            input[0].click();
          });
          inputDisplay.bind("click", function() {
            input[0].click();
          });
          input.bind("change", function(e) {
            scope.$apply(function() {
              var files = e.target.files;
              if (files[0]) {
                scope.fileName = files[0].name;
              } else {
                scope.fileName = null;
              }
            });
          });
        }
      };
    });

  function ProfileCtrl(
    UploadService,
    CandidateService,
    $rootScope,
    $scope,
    $http,
    $state
  ) {
    var ctrl = this;
    ctrl.profileUrl = $rootScope.backend_api + "/candidates";

    ctrl.init = function() {
      $scope.candidate = {};
      $scope.user = $rootScope.globals.currentUser || {};
      $scope.objective = {};
      $scope.initialModel = angular.copy($scope.candidate);
      CandidateService.Read($scope.user.token).then(function setObjective(
        response
      ) {
        console.log(response);
        var { roles, locationsSecondary, locationsPrimary } = response.data
          .objective || {
          roles: [],
          locationsSecondary: [],
          locationsPrimary: []
        };
        $scope.objective = {
          role: roles[0],
          locationsSecondary: locationsSecondary[0],
          locationsPrimary: locationsPrimary[0]
        };
        $scope.fileName = response.data.cvName || undefined;
      });
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
      UploadService.UploadResumeFile(
        $rootScope.globals.currentUser.token,
        cvInput.files[0]
      )
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
            $scope.createToast(response.data.description);
          },
          function errorCallback(response) {
            $scope.createToast(response.data.description);
          }
        );
    };

    ctrl.add = function() {
      var requestData = {
        ...$scope.user,
        objective: {
          roles: [$scope.objective.role],
          locationsSecondary: [$scope.objective.locationsSecondary]
        }
      };

      CandidateService.Update(requestData).then(
        function successCallback(response) {
          $state.go("jobs");
          // Reload footer's img to switch from alert to check-mark!
          $scope.createToast(response.data.description);
          $scope.scrollTop();
        },
        function errorCallback(response) {
          $scope.createToast(response.data.description);
          $state.go("jobs");
        }
      );
    };
  }
})();
