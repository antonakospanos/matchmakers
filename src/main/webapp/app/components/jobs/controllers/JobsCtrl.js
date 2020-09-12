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
      "$timeout",
      JobsCtrl
    ]);

  function JobsCtrl($rootScope, $scope, $http, $state, $sce, $timeout) {
    var ctrl = this;
    var matchingUrl = $rootScope.backend_api + "/jobs";
    var applyUrl = $rootScope.backend_api + "/jobs";

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
      $scope.lastItemUrl = undefined;
      ctrl.listJobs();
    };

    ctrl.slice = function(word) {
      if (word.length > 35) {
        return word.slice(0, 35) + "...";
      }
      return word;
    };

    ctrl.getLoading = function() {
      return $scope.loading && $scope.model.data.length == 0;
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
      var response = $http.defaults.headers.common.Authorization
        ? {
            data: [
              {
                logo:
                  "https://workable-staging-workablestg5.s3.amazonaws.com/uploads/account/logo/591149101/small_logo",
                company: "Lenovo",
                title:
                  "QA Automation Engineer QA Automation Engineer QA Automation Engineer QA Automation Engineer",
                description:
                  "<p>We are looking for a Quality Assurance (QA) engineer to develop and execute exploratory and automated tests to ensure product quality. QA engineer responsibilities include designing and implementing tests, debugging and defining corrective actions. You will also review system requirements and track quality assurance metrics (e.g. defect densities and open defect counts.) The QA technician role plays an important part in our company’s product development process. Our ideal candidate will be responsible for conducting tests before product launches to ensure software runs smoothly and meets client needs, while being cost-effective. If you hold an engineering background and enjoy providing end-to-end solutions to software quality problems, we’d like to meet you. Ultimately, you should monitor all stages of software development to identify and resolve system malfunctions to meet quality standards.<strong>Responsibilities</strong></p><ul> <li>Review requirements, specifications and technical design documents to provide timely and meaningful feedback</li> <li>Create detailed, comprehensive and well-structured test plans and test cases</li> <li>Perform thorough regression testing when bugs are resolved</li> <li>Design, develop and execute automation scripts using open source tools</li> <li>Estimate, prioritize, plan and coordinate testing activities</li> <li>Liaise with internal teams (e.g. developers and product managers) to identify system requirements</li> <li>Investigate the causes of non-conforming software and train users to implement solutions</li> </ul>",
                jobUrl: "https://lenovo.workablestg5.com/jobs/1073526216",
                skills: ["selenium", "java", "testng", "jenkins"],
                match: 90,
                publisher: {
                  name: "Lenovo",
                  date: "31/5/2019"
                }
              },
              {
                logo:
                  "https://workable-staging-workablestg5.s3.amazonaws.com/uploads/account/logo/1072641667/large_logo",
                company: "Backbase",
                title: "QA Tester",
                description:
                  "<p>We are looking for a QA Tester to assess software quality through manual and automated testing. You will be responsible for finding and reporting bugs and glitches. In this role, you should have a keen eye for detail and excellent communication skills. If you are also competent in executing test cases and are passionate about quality, we’d like to meet you. Ultimately, you will ensure that our products, applications and systems work correctly.<strong>Responsibilities</strong></p><ul> <li>Review and analyze system specifications</li> <li>Collaborate with QA Engineers to develop effective strategies and test plans</li> <li>Execute test cases (manual or automated) and analyze results</li> <li>Evaluate product code according to specifications</li> <li>Create logs to document testing phases and defects</li> <li>Report bugs and errors to development teams</li> <li>Help troubleshoot issues</li> <li>Conduct post-release/ post-implementation testing</li> <li>Work with cross-functional teams to ensure quality throughout the software development lifecycle</li> </ul>",
                match: 85,
                jobUrl: "https://backbase.workablestg5.com/jobs/1073526212",
                skills: ["junit", "appium", "groovy"],
                publisher: {
                  name: "Backbase",
                  date: "31/5/2019"
                }
              },
              {
                logo:
                  "https://media.licdn.com/dms/image/C560BAQGKY64CQns2jQ/company-logo_200_200/0?e=2159024400&v=beta&t=s1G1Yc9DxScmxM6ix9Pp3zb8lkBCAlKQi4-jXCUZQmk",
                company: "Agile Actors",
                title: "QA Engineer",
                description:
                  "<p>We are looking for a Quality Assurance (QA) engineer to develop and execute exploratory and automated tests to ensure product quality. QA engineer responsibilities include designing and implementing tests, debugging and defining corrective actions. You will also review system requirements and track quality assurance metrics (e.g. defect densities and open defect counts.) The QA technician role plays an important part in our company’s product development process. Our ideal candidate will be responsible for conducting tests before product launches to ensure software runs smoothly and meets client needs, while being cost-effective. If you hold an engineering background and enjoy providing end-to-end solutions to software quality problems, we’d like to meet you. Ultimately, you should monitor all stages of software development to identify and resolve system malfunctions to meet quality standards.<strong>Responsibilities</strong></p><ul> <li>Review requirements, specifications and technical design documents to provide timely and meaningful feedback</li> <li>Create detailed, comprehensive and well-structured test plans and test cases</li> <li>Estimate, prioritize, plan and coordinate testing activities</li> </ul>",
                match: 85,
                jobUrl: "https://agile-actors.workablestg5.com/jobs/1073526213",
                skills: ["java", "cucumber", "agile"],
                publisher: {
                  name: "AgileActors",
                  date: "31/5/2019"
                }
              },
              {
                logo:
                  "https://media.licdn.com/dms/image/C560BAQGKY64CQns2jQ/company-logo_200_200/0?e=2159024400&v=beta&t=s1G1Yc9DxScmxM6ix9Pp3zb8lkBCAlKQi4-jXCUZQmk",
                company: "Agile Actors",
                title: "Back-end Developer",
                description:
                  "<p>We are looking for an experienced Back-end developer to join our IT team. You will be responsible for the server side of our web applications. If you have excellent programming skills and a passion for developing applications or improving existing ones, we would like to meet you. As a Back-end developer, you’ll work closely with our engineers to ensure system consistency and improve user experience. Ultimately, you should be able to develop and maintain functional and stable web applications to meet our company’s needs.<strong>Responsibilities</strong></p><ul> <li>Participate in the entire application lifecycle, focusing on coding and debugging</li> <li>Troubleshoot and debug applications</li> <li>Perform UI tests to optimize performance</li> </ul>",
                jobUrl: "https://agile-actors.workablestg5.com/jobs/1073526215",
                skills: ["java", "spring", "agile"],
                match: 78,
                publisher: {
                  name: "AgileActors",
                  date: "31/5/2019"
                }
              },
              {
                logo: "https://alternative.me/icons/workable.jpg",
                company: "Workable",
                title: "Software Engineer (Remote)",
                description: `We are looking for a passionate Software Engineer to design, develop and install software solutions.

            Software Engineer responsibilities include gathering user requirements, defining system functionality and writing code in various languages, like Java, Ruby on Rails or .NET programming languages (e.g. C++ or JScript.NET.) Our ideal candidates are familiar with the software development life cycle (SDLC) from preliminary system analysis to tests and deployment.
            
            Ultimately, the role of the Software Engineer is to build high-quality, innovative and fully performing software that complies with coding standards and technical design.`,
                jobUrl: "https://careers.workable.com/j/9525118267?viewed=true",
                match: 70,
                skills: ["mysql", "elastic", "kubernetes"],
                publisher: {
                  name: "Workable",
                  date: "21/5/2019"
                }
              }
            ]
          }
        : {};
      $scope.model = { data: [] };
      $timeout(function() {
        $scope.model = {
          data: response.data.map(d => ctrl.mapProgressModel(d))
        };
      }, 2500);
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
      $scope.lastItemUrl = ctrl.trustSrcurl(item.jobUrl);
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
