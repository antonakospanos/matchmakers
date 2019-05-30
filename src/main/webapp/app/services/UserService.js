(function () {
    'use strict';

    angular
        .module('Matchmakers')
        .factory('UserService', UserService);

    UserService.$inject = ['$http', "$rootScope"];
    function UserService($http, $rootScope) {
        var service = {};
        service.GetUrl = GetUrl;
        service.GetById = GetById;
        service.Create = Create;

        return service;

        function GetUrl() {
            return $rootScope.backend_protocol + "://" + $rootScope.backend_ip + ":" + $rootScope.backend_port + "/" + $rootScope.backend_context_path + "/candidates";
        }

        function GetById(id) {
            return $http
                .get(service.GetUrl() + '/' + id)
                .then(handleResponse, handleResponse());
        }

        function Create(user) {
           var headers = {
                'Content-Type': 'application/json;charset=utf-8;'
            }
            return $http
                .post(service.GetUrl(), user, { headers: headers })
                // .then(handleResponse, handleResponse());
                .then(function successCallback(response) {
                    return response;
                }, function errorCallback(response) {
                    return response;
                });
        }

        // Private Util
        function handleResponse(res) {
            return res;
        }
    }
})();
