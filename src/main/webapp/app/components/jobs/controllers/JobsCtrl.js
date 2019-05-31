(function() {
  "use strict";
  angular
    .module("Matchmakers")
    .controller("JobsCtrl", [
      "$rootScope",
      "$scope",
      "$http",
      "$state",
      "$sce",
      JobsCtrl
    ]);

  function JobsCtrl($rootScope, $scope, $http, $state, $sce) {
    var ctrl = this;
    var matchingUrl =
      $rootScope.backend_protocol +
      "://" +
      $rootScope.backend_ip +
      ":" +
      $rootScope.backend_port +
      "/" +
      $rootScope.backend_context_path +
      "/jobs";
    var applyUrl =
      $rootScope.backend_protocol +
      "://" +
      $rootScope.backend_ip +
      ":" +
      $rootScope.backend_port +
      "/" +
      $rootScope.backend_context_path +
      "/jobs";

    if (
      $state.params.publisher !== undefined &&
      $state.params.publisher.id !== undefined
    ) {
      matchingUrl = matchingUrl + "?userId=" + $state.params.publisher.id;
      $scope.publisher = $state.params.publisher.name;
    }

    // Initialization
    ctrl.init = function() {
      $scope.loading = Boolean($rootScope.globals.currentUser);
      ctrl.listJobs();
    };

    // Sorting states
    var sortedByLikesDesc = false;
    var sortedByHatesDesc = false;
    var sortedByDatesDesc = false;

    ctrl.sortLikes = function() {
      if (ctrl.sortedByLikesDesc === true) {
        ctrl.sortLikesAsc();
      } else {
        ctrl.sortLikesDesc();
      }
    };

    ctrl.sortHates = function() {
      if (ctrl.sortedByHatesDesc === true) {
        ctrl.sortHatesAsc();
      } else {
        ctrl.sortHatesDesc();
      }
    };

    ctrl.sortDates = function() {
      if (ctrl.sortedByDatesDesc === true) {
        ctrl.sortDatesAsc();
      } else {
        ctrl.sortDatesDesc();
      }
    };

    ctrl.sortLikesDesc = function() {
      ctrl.sortedByLikesDesc = true;
      ctrl.sortedByHatesDesc = false;
      ctrl.sortedByDatesDesc = false;
      $scope.model.data.sort(function(a, b) {
        var matchA = a && a.match ? a.match : 0;
        var matchB = b && b.match ? b.match : 0;
        if (matchA < matchB) return 1;
        if (matchB < matchA) return -1;
        return 0;
      });
    };

    ctrl.sortHatesDesc = function() {
      ctrl.sortedByLikesDesc = false;
      ctrl.sortedByHatesDesc = true;
      ctrl.sortedByDatesDesc = false;
      $scope.model.data.sort(function(a, b) {
        var hatesA = a && a.hates ? a.hates : 0;
        var hatesB = b && b.hates ? b.hates : 0;
        if (hatesA < hatesB) return 1;
        if (hatesB < hatesA) return -1;
        return 0;
      });
    };

    ctrl.sortDatesDesc = function() {
      ctrl.sortedByLikesDesc = false;
      ctrl.sortedByHatesDesc = false;
      ctrl.sortedByDatesDesc = true;
      $scope.model.data.sort(function(a, b) {
        var dateA = a && a.publicationDate ? a.publicationDate : 0;
        var dateB = b && b.publicationDate ? b.publicationDate : 0;
        if (dateA < dateB) return 1;
        if (dateB < dateA) return -1;
        return 0;
      });
    };

    ctrl.sortLikesAsc = function() {
      ctrl.sortedByLikesDesc = false;
      ctrl.sortedByHatesDesc = false;
      ctrl.sortedByDatesDesc = false;
      $scope.model.data.sort(function(a, b) {
        var likesA = a && a.likes ? a.likes : 0;
        var likesB = b && b.likes ? b.likes : 0;
        if (likesA < likesB) return -1;
        if (likesB < likesA) return 1;
        return 0;
      });
    };

    ctrl.sortHatesAsc = function() {
      ctrl.sortedByHatesDesc = false;
      ctrl.sortedByHatesDesc = false;
      ctrl.sortedByDatesDesc = false;
      $scope.model.data.sort(function(a, b) {
        var hatesA = a && a.hates ? a.hates : 0;
        var hatesB = b && b.hates ? b.hates : 0;
        if (hatesA < hatesB) return -1;
        if (hatesB < hatesA) return 1;
        return 0;
      });
    };

    ctrl.sortDatesAsc = function() {
      ctrl.sortedByDatesDesc = false;
      ctrl.sortedByHatesDesc = false;
      ctrl.sortedByDatesDesc = false;
      $scope.model.data.sort(function(a, b) {
        var dateA = a && a.publicationDate ? a.publicationDate : 0;
        var dateB = b && b.publicationDate ? b.publicationDate : 0;
        if (dateA < dateB) return -1;
        if (dateB < dateA) return 1;
        return 0;
      });
    };

    /**
     *  List Matchmakers jobs!
     */

    // $rootScope.globals = $cookies.getObject("globals") || {};
    // if ($rootScope.globals.currentUser) {
    //   $http.defaults.headers.common["Authorization"] =
    //     "Bearer " + $rootScope.globals.currentUser.token;
    // }

    ctrl.listJobs = function() {
      $http({
        url: matchingUrl,
        headers: {
          Authorization: $http.defaults.headers.common["Authorization"]
        }
      }).then(
        function successCallback(response) {
          response.data.forEach(e => {
            // e.match = 100 - Math.floor(Math.random() * 40 + 1);
            if (e.skills && e.skills.length > 5) {
              e.skills.slice(5);
            }
          });
          $scope.model = {
            data: response.data.map(d => ctrl.mapProgressModel(d))
          };
          $scope.loading = false;
          ctrl.sortLikesDesc();
        },
        function errorCallback(response) {
          //response.data
          console.error("Jobs request failed");
          $scope.loading = false;
        }
      );
    };

    ctrl.apply = function(item) {
      console.log(
        `${matchingUrl}/${item.jobId}?account=${item.company_subdomain}`
      );
      $http({
        method: "POST",
        url: `${matchingUrl}/${item.jobId}?account=${item.company_subdomain}`,
        headers: {
          Authorization: $http.defaults.headers.common["Authorization"]
        }
      }).then(
        function successCallback(response) {
          $scope.createToast(response.data.description);
          item.applied = true;
        },
        function errorCallback(response) {
          //response.data
          $scope.createToast(
            "The application for Job " + item.title + " failed!"
          );
          $scope.loading = false;
        }
      );
    };

    ctrl.isAuthenticated = function() {
      return !$http.defaults.headers.common.Authorization;
    };

    /**
     *  List Matchmakers jobs!
     */
    ctrl.listJobsMock = function() {
      $scope.model = $http.defaults.headers.common.Authorization
        ? {
            data: [
              {
                logo: "https://alternative.me/icons/workable.jpg",
                company: "Workable",
                title: "Software Engineer (Remote)",
                description: `We are looking for a passionate Software Engineer to design, develop and install software solutions.

            Software Engineer responsibilities include gathering user requirements, defining system functionality and writing code in various languages, like Java, Ruby on Rails or .NET programming languages (e.g. C++ or JScript.NET.) Our ideal candidates are familiar with the software development life cycle (SDLC) from preliminary system analysis to tests and deployment.
            
            Ultimately, the role of the Software Engineer is to build high-quality, innovative and fully performing software that complies with coding standards and technical design.`,
                jobUrl: "https://careers.workable.com/j/9525118267?viewed=true",
                match: 90,
                publisher: {
                  name: "Workable",
                  date: "21/5/2019"
                }
              },
              {
                logo: "https://alternative.me/icons/workable.jpg",
                company: "Workable",
                title: "Test title",
                description: "Test description",
                jobUrl: "https://careers.workable.com/j/9525118267?viewed=true",
                match: 90,

                publisher: {
                  name: "Workable",
                  date: "21/5/2019"
                }
              },
              {
                logo: "https://alternative.me/icons/workable.jpg",
                company: "Workable",
                title: "Test title",
                description: "Test description",
                match: 80,
                jobUrl: "https://workable.com",
                publisher: {
                  name: "Workable"
                }
              },
              {
                logo: "https://alternative.me/icons/workable.jpg",
                company: "Workable",
                title: "Test title",
                description: "Test description",
                jobUrl: "",
                match: 78,
                publisher: {
                  name: "Workable"
                }
              },
              {
                logo: "https://alternative.me/icons/workable.jpg",
                company: "Workable",
                title: "Test title",
                description: "Test description",
                jobUrl: "",
                match: 60,
                publisher: {
                  name: "Workable"
                }
              }
            ]
          }
        : {};
    };

    /**
     *  List Matchmakers jobs by passed Publisher!
     */
    ctrl.filterByPublisher = function(job) {
      var publisher = job.publisher;
      if (
        $state.params.publisher &&
        publisher.id === $state.params.publisher.id
      ) {
        $state.reload();
      } else {
        $state.go("jobs", {
          publisher: {
            id: publisher.id,
            name: publisher.name
          }
        });
      }
    };

    /**
     *  Apply for a job!
     *
     * @param item
     */
    ctrl.like = function(item) {
      $scope
        .modalWarning(
          "Are you sure you want to apply '" + item.title + "' ?",
          "APPLY"
        )
        .then(function(response) {
          if (response === true) {
            var headers = {
              "Content-Type": "application/json",
              Authorization: $http.defaults.headers.common.Authorization // $cookies.global.accessToken
            };
            var body = {
              job: item.id
            };
            $http.put(applyUrl, body, { headers: headers }).then(
              function successCallback(response) {
                $scope.createToast(response.data.description);
                //ctrl.listJobs();
              },
              function errorCallback(response) {
                $scope.createToast(response.data.description);
              }
            );
          }
        });
    };

    /**
     *  Retract vote!
     *
     * @param item
     */
    ctrl.retract = function(item) {
      $scope
        .modalAlert(
          "Do you want to retract the application for '" + item.title + "' ?",
          "RETRACT"
        )
        .then(function(response) {
          if (response === true) {
            var headers = {
              Authorization: $http.defaults.headers.common.Authorization
            };
            $http
              .delete(applyUrl + "?job=" + item.id, null, { headers: headers })
              .then(
                function successCallback(response) {
                  console.log("INFO:" + response.data);
                  $scope.createToast(response.data.description);
                  //ctrl.listJobs();
                  $scope.scrollTop();
                },
                function errorCallback(response) {
                  console.log("ERROR: " + response.data);
                  $scope.createToast(response.data.description);
                  $scope.scrollTop();
                }
              );
          }
        });
    };

    /**
     *  Applies to the Job!
     *
     * @param item
     */
    ctrl.applyLastItem = function(item) {
      // TODO
      $scope.lastItem = item;
    };

    /**
     *  Hide voting buttons to un-subscribed users!
     *
     * @param item
     */
    ctrl.hideVotingButtons = function(item) {
      return false; //!$http.defaults.headers.common.Authorization;
    };

    /**
     *  Retrieves the user's application to the Job!
     *
     * @param item
     */
    ctrl.findVote = function(item) {
      $http({
        url: applyUrl + "?job=" + item.id
      }).then(function successCallback(response) {
        item.like = response.data.like;
      });
    };

    /**
     *  Hide like button to fans!
     *
     * @param item
     */
    ctrl.hideApplyButton = function(item) {
      return item.applied;
    };

    /**
     *  Hide retract like button to users that have not hated yet!
     *
     * @param item
     */
    ctrl.hideRetractButton = function(item) {
      return item.like === undefined || item.like === false;
    };

    ctrl.decideColor = function(item) {
      let color = "#95a5a6"; //Concrete;
      if (item.match >= 85) {
        color = "#00756a"; //Pine-green;
      } else if (item.match >= 70) {
        color = "#ff5e16"; //Lava
      }
      return color;
    };

    ctrl.trustSrcurl = function(data) {
      return $sce.trustAsResourceUrl(data);
    };

    ctrl.mapProgressModel = function(job) {
      return {
        ...job,
        current: Number(job.match),
        total: 100,
        color: ctrl.decideColor(job),
        duration: Number(job.match) * 8
      };
    };
  }
})();
