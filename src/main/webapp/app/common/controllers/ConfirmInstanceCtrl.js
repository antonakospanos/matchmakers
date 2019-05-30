(function () {
    'use strict';

    /**
     * Application wide Confirmation Message Controller
     */
    angular
        .module('Matchmakers')
        .controller('ConfirmInstanceCtrl', function ($scope, $modalInstance, message, choice, $sce) {

            $scope.message = $sce.trustAsHtml(message);
            $scope.choice = choice;

            $scope.ok = function (choice) {
                $modalInstance.close('yes');
            };

            $scope.cancel = function (choice) {
                $modalInstance.close('');
            };
    });
})();
