(function() {
  "use strict";

  angular.module("Matchmakers").factory("CandidateService", UserService);

  UserService.$inject = ["$http", "$rootScope"];
  function UserService($http, $rootScope) {
    var service = {};
    service.GetUrl = GetUrl;
    service.Update = Update;
    service.Read = Read;

    return service;

    function GetUrl(candidate) {
      return (
        $rootScope.backend_protocol +
        "://" +
        $rootScope.backend_ip +
        ":" +
        $rootScope.backend_port +
        "/" +
        $rootScope.backend_context_path +
        "/candidates/" +
        candidate
      );
    }

    function GetResumeUrl(candidateToken) {
      return (
        $rootScope.backend_protocol +
        "://" +
        $rootScope.backend_ip +
        ":" +
        $rootScope.backend_port +
        "/" +
        $rootScope.backend_context_path +
        "/candidates" +
        "/" +
        candidateToken +
        "/cv"
      );
    }

    function Update({ token, ...candidateData }) {
      var config = {
        headers: {
          "Content-Type": "application/json;charset=utf-8;"
        }
      };
      return $http.put(
        service.GetUrl(token),
        { id: token, ...candidateData },
        config
      );
    }

    function Read(user) {
      var config = {
        headers: {
          "Content-Type": "application/json;charset=utf-8;"
        }
      };
      return $http.get(service.GetUrl(user), config);
    }
  }
})();
