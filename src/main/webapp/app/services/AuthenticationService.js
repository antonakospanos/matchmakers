(function () {
    'use strict';

    angular
        .module('Matchmakers')
        .factory('AuthenticationService', AuthenticationService);

    AuthenticationService.$inject = ['$http', '$cookies', '$rootScope', '$timeout', 'UserService'];
    function AuthenticationService($http, $cookies, $rootScope, $timeout, UserService) {
        var service = {};
        service.Login = Login;
        service.Authorize = Authorize;
        service.Logout = Logout;

        return service;

        function Authorize(username, accessToken) {
            $rootScope.globals = {
                currentUser: {
                    username: username,
                    token: accessToken
                }
            };

            console.log("Adding header: " + accessToken)

            // set default auth header for http requests
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + accessToken;

            // store user details in globals cookie that keeps user logged in for 1 week (or until they logout)
            var cookieExp = new Date();
            cookieExp.setDate(cookieExp.getDate() + 7);
            $cookies.putObject('globals', $rootScope.globals, { expires: cookieExp });
        }

        function Login(username, password) {
            return $http
                .get(UserService.GetUrl() + "/id?username=" + username + "&password=" + password)
                .then(function successCallback(response) {
                    return response;
                }, function errorCallback(response) {
                    return response;
                });
        }

        function Logout() {
            $rootScope.globals = {};
            $cookies.remove('globals');
            $http.defaults.headers.common.Authorization = '';
        }
    }
})();
