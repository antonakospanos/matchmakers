(function() {
  var app = angular
    .module("Matchmakers", [
      "ui.bootstrap",
      "ui.router",
      "ngSanitize",
      "ngTable",
      "ngCookies",
      "ngCookies",
      "ngMaterial",
      "ui.sortable",
      "ngMessages",
      "md.time.picker"
    ])
    .config(config)
    .run(run);

  /**
   *  Main Application Configuration (UI-Router)
   */
  config.$inject = ["$stateProvider", "$urlRouterProvider"];
  function config($stateProvider, $urlRouterProvider) {
    var routes = [
      {
        name: "jobs",
        url: "/",
        templateUrl: "app/components/jobs/views/jobs.html"
      },

      {
        name: "cv",
        url: "/profile",
        templateUrl: "app/components/profile/views/profile.html"
      },

      {
        name: "login",
        url: "/login",
        templateUrl: "app/components/login/views/login.html"
      },

      {
        name: "register",
        url: "/register",
        templateUrl: "app/components/register/views/register.html"
      },

      {
        name: "logout",
        url: "/",
        controller: "LogoutCtrl"
      }
    ];

    routes.forEach(route => $stateProvider.state(route));

    // Homepage
    $urlRouterProvider.otherwise("/");
  }

  /**
   *  Main Application Execution upon page (re)load:
   *  a) Initialization
   *  b) Root scope setup
   */
  run.$inject = [
    "$rootScope",
    "$location",
    "$cookies",
    "$http",
    "$modal",
    "$q"
  ];
  function run($rootScope, $location, $cookies, $http, $modal, $q) {
    $rootScope.safeApply = function() {
      if (!$rootScope.$$phase) $rootScope.$apply();
    };

    // Add Configuration on $rootScope
    var config = YAML.load("app/conf/params.yml");
    $rootScope.backend_protocol = config.backend_protocol;
    $rootScope.backend_ip = config.backend_ip;
    $rootScope.backend_port = config.backend_port;
    $rootScope.backend_context_path = config.backend_context_path;

    /**
     * Keep user logged in after page refresh!
     *
     *  Tip:
     *  Uses stored 'globals.currentUser' information from his cookie
     */
    $rootScope.globals = $cookies.getObject("globals") || {};
    if ($rootScope.globals.currentUser) {
      $http.defaults.headers.common["Authorization"] =
        "Bearer " + $rootScope.globals.currentUser.token;
    }

    /**
     *  Redirect to homepage if the user is not logged in and is trying to access a restricted page!
     *
     *  Tip:
     *  Uses stored 'globals.currentUser' information from his cookie
     */
    $rootScope.$on("$locationChangeStart", function() {
      var restrictedPage =
        $.inArray($location.path(), [
          "/",
          "/login",
          "/register"
        ]) === -1;

      var loggedIn = $rootScope.globals.currentUser;
      if (restrictedPage && !loggedIn) {
        $location.path("/");
      }
    });

    /**
     * INFO Message Prompt
     *
     * @param message
     * @param size
     */
    $rootScope.modalInfo = function(message, size) {
      var sz = "sm";
      if (!!size) sz = size;
      var modalInstance = $modal.open(
        angular.extend(
          {
            templateUrl: "common/views/infoModal.html",
            controller: "MessageInstanceCtrl",
            size: sz,
            resolve: {
              message: function() {
                return message;
              }
            }
          },
          Settings.ModalSettings
        )
      );
      return modalInstance.result;
    };

    /**
     * WARN Confirmation Prompt
     *
     * @param message
     * @param size
     */
    $rootScope.modalWarning = function(message, choice) {
      var deferred = $q.defer();
      var modalInstance = $modal.open(
        angular.extend({
          templateUrl: "app/common/views/warningModal.html",
          controller: "ConfirmInstanceCtrl",
          resolve: {
            message: function() {
              return message;
            },
            choice: function() {
              return choice;
            }
          }
        })
      );
      modalInstance.result.then(function(res) {
        if (res === "yes") {
          deferred.resolve(true);
        } else {
          deferred.reject(false);
        }
      });
      return deferred.promise;
    };

    /**
     * ALERT Confirmation Prompt
     *
     * @param message
     * @param size
     */
    $rootScope.modalAlert = function(message, choice) {
      var deferred = $q.defer();
      var modalInstance = $modal.open(
        angular.extend({
          templateUrl: "app/common/views/alertModal.html",
          controller: "ConfirmInstanceCtrl",
          resolve: {
            message: function() {
              return message;
            },
            choice: function() {
              return choice;
            }
          }
        })
      );
      modalInstance.result.then(function(res) {
        if (res === "yes") {
          deferred.resolve(true);
        } else {
          deferred.resolve(false);
        }
      });
      return deferred.promise;
    };

    /**
     * ERROR Message Prompt
     *
     * @param message
     * @param size
     */
    $rootScope.modalError = function(message, size) {
      var sz = "sm";
      if (!!size) sz = size;
      var modalInstance = $modal.open(
        angular.extend({
          templateUrl: "app/common/views/errorModal.html",
          controller: "MessageInstanceCtrl",
          size: sz,
          resolve: {
            message: function() {
              return message;
            }
          }
        })
      );
      return modalInstance.result;
    };
  }

  /**
   * Main Application controllers (RootController interpreting HeaderCtrl and FooterCtrl too)
   */
  app.controller("RootController", [
    "$rootScope",
    "$scope",
    "$cookies",
    "$http",
    "$mdToast",
    "$controller",
    "$state",
    function(
      $rootScope,
      $scope,
      $cookies,
      $http,
      $mdToast,
      $controller,
      $state
    ) {
      // Header Controller
      $controller("HeaderCtrl", {
        $scope: $scope
      });
      $scope.initHeader();

      // Footer Controller
      $controller("FooterCtrl", {
        $scope: $scope
      });
      $scope.initFooter();

      // State transition marker
      $scope.$on("$stateChangeSuccess", function(ev, to, toParams, from) {
        $scope.previousState = from.name;
        $scope.currentState = to.name;
      });

      // Reload state
      $scope.reloadState = function() {
        $state.go($state.current, {}, { reload: true });
      };

      $scope.scrollTop = function() {
        window.scrollTo(0, 0);
      };
    }
  ]);
})();

/**
 * Main Application
 */
$(document).ready(function() {
  htmlbodyHeightUpdate();
  $(window).resize(function() {
    htmlbodyHeightUpdate();
  });
  $(window).scroll(function() {
    height2 = $(".main").height();
    htmlbodyHeightUpdate();
  });
});

function htmlbodyHeightUpdate() {
  var height3 = $(window).height();
  var height1 = $(".nav").height() + 50;
  height2 = $(".main").height();
  if (height2 > height3) {
    $("html").height(Math.max(height1, height3, height2) + 10);
    $("body").height(Math.max(height1, height3, height2) + 10);
  } else {
    $("html").height(Math.max(height1, height3, height2));
    $("body").height(Math.max(height1, height3, height2));
  }
}
