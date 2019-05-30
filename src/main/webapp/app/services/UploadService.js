(function() {
  "use strict";

  angular.module("Matchmakers").factory("UploadService", UploadService);

  UploadService.$inject = ["$http", "$rootScope"];
  function UploadService($http, $rootScope) {
    // var S3_URL = "https://s3.amazonaws.com/workable-tmp/";
    var S3_URL = "https://file.io/";
    var W_PATH_URL = "https://careers.workable.com/resume_caches";

    var service = {};
    service.UploadResumeFile = UploadResumeFile;
    service.SendUploadedCV = SendUploadedCV;
    service.GetResumeUrl = GetResumeUrl;

    return service;

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

    function SendUploadedCV({ userToken, url }) {
      var headers = {
        "Content-Type": "application/json;charset=utf-8;"
      };
      return $http
        .post(service.GetResumeUrl(userToken), { cvUrl: url }, { headers })
        .then(
          function successHandler(response) {
            console.log(response);
            return response;
          },
          function errorHandler(response) {
            console.log(response);
            $rootScope.createToast(response.data.description);
            throw response;
          }
        );
    }

    function UploadResumeFile({ userToken, file }) {
      var formData = new FormData();

      var blob = new Blob([file], { type: "multipart/form-data" });
    //   formData.append('file', blob);
      formData.append("file", file, file.name);

      return $http
        .post(service.GetResumeUrl(userToken), formData, {
          transformRequest: angular.identity,
          method: "POST",
          responseType: "application/json",
          headers: {
            "Content-Type": undefined,
            "Access-Control-Allow-Headers": "*",
            "Access-Control-Allow-Methods": "*",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Headers": "*"
          }
        })
        .then(
          function successCallback(response) {
            debugger
            return response;
          },
          function errorCallback(response) {
            throw response;
          }
        );

      // var blob = new Blob([file], { type: "multipart/form-data" });
      // formData.append(file.name, blob);
      // return fetch(S3_URL, {
      //   method: "POST",
      //   cache: "no-cache",
      //   credentials: "same-origin",
      //   headers: {
      //     "Content-Type": "multipart/form-data"
      //   },
      //   body: formData
      // });
    }
  }
})();
