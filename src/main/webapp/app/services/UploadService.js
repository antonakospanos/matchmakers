(function() {
  "use strict";

  angular.module("Matchmakers").factory("UploadService", UploadService);

  UploadService.$inject = ["$http", "$rootScope"];
  function UploadService($http, $rootScope) {
    // var S3_URL = "https://s3.amazonaws.com/workable-tmp/";
    var S3_URL = "https://file.io/";
    var W_PATH_URL = "https://careers.workable.com/resume_caches";

    var service = {};
    service.UploadToS3 = UploadToS3;
    service.SendUploadedCV = SendUploadedCV;
    service.GetResumeUrl = GetResumeUrl;

    return service;

    function getUploadExpires() {
      var EXPIRE_DURATION = 900000;
      var date = new Date(Date.now() + EXPIRE_DURATION);
      return date.toUTCString();
    }

    function createS3() {
      var albumBucketName = "workable-s3-pdf";
      var bucketRegion = "us-east-2";
      var IdentityPoolId = "a5576f84-7ac1-47ed-aeb2-914c371e69fb";

      AWS.config.update({
        region: bucketRegion,
        credentials: new AWS.CognitoIdentityCredentials({
          IdentityPoolId: IdentityPoolId
        })
      });

      return new AWS.S3({
        apiVersion: "2006-03-01",
        params: { Bucket: albumBucketName }
      });
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

    function UploadToS3({user, file}) {
      var headers = {
        "Content-Type": "multipart/form-data;",
        Expires: getUploadExpires()
      };

      var formData = new FormData();
      var blob = new Blob([file], {type: 'multipart/form-data'});
      formData.append(file.name, blob);
      return fetch(service.GetResumeUrl(user), {
        method: 'POST',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        body: formData
      });
    }
  }
})();
