(function () {
	"use strict";
	angular
		.module("Matchmakers")
		.controller("JobsCtrl", ["$rootScope", "$scope", "$http", "$state", JobsCtrl]);

	function JobsCtrl($rootScope, $scope, $http, $state) {
		var ctrl = this;
        var matchingUrl = $rootScope.backend_protocol + "://" + $rootScope.backend_ip + ":" + $rootScope.backend_port + "/" + $rootScope.backend_context_path + "/jobs";
        var applyUrl = $rootScope.backend_protocol + "://" + $rootScope.backend_ip + ":" + $rootScope.backend_port + "/" + $rootScope.backend_context_path + "/applications";

        if ($state.params.publisher !== undefined && $state.params.publisher.id !== undefined) {
            matchingUrl = matchingUrl + "?userId=" + $state.params.publisher.id;
            $scope.publisher = $state.params.publisher.name
        }

        // Initialization
        ctrl.init = function () {
            ctrl.listJobs();
        }

        // Sorting states
        var sortedByLikesDesc = false;
        var sortedByHatesDesc = false;
        var sortedByDatesDesc = false;

        ctrl.sortLikes = function () {
		    if (ctrl.sortedByLikesDesc === true) {
                ctrl.sortLikesAsc();
            } else {
                ctrl.sortLikesDesc();
            }
        }

        ctrl.sortHates = function () {
            if (ctrl.sortedByHatesDesc === true) {
                ctrl.sortHatesAsc();
            } else {
                ctrl.sortHatesDesc();
            }
        }

        ctrl.sortDates = function () {
            if (ctrl.sortedByDatesDesc === true) {
                ctrl.sortDatesAsc();
            } else {
                ctrl.sortDatesDesc();
            }
        }

		ctrl.sortLikesDesc = function () {
            ctrl.sortedByLikesDesc = true;
            ctrl.sortedByHatesDesc = false;
            ctrl.sortedByDatesDesc = false;
			$scope.model.data.sort(function (a, b) {
				var likesA = a && a.likes ? a.likes : 0;
				var likesB = b && b.likes ? b.likes : 0;
				if (likesA < likesB)
					return 1;
				if (likesB < likesA)
					return -1;
				return 0;
			});
		}

        ctrl.sortHatesDesc = function () {
            ctrl.sortedByLikesDesc = false;
            ctrl.sortedByHatesDesc = true;
            ctrl.sortedByDatesDesc = false;
            $scope.model.data.sort(function (a, b) {
                var hatesA = a && a.hates ? a.hates : 0;
                var hatesB = b && b.hates ? b.hates : 0;
                if (hatesA < hatesB)
                    return 1;
                if (hatesB < hatesA)
                    return -1;
                return 0;
            });
        }

        ctrl.sortDatesDesc = function () {
            ctrl.sortedByLikesDesc = false;
            ctrl.sortedByHatesDesc = false;
            ctrl.sortedByDatesDesc = true;
            $scope.model.data.sort(function (a, b) {
                var dateA = a && a.publicationDate ? a.publicationDate : 0;
                var dateB = b && b.publicationDate ? b.publicationDate : 0;
                if (dateA < dateB)
                    return 1;
                if (dateB < dateA)
                    return -1;
                return 0;
            });
        }

        ctrl.sortLikesAsc = function () {
            ctrl.sortedByLikesDesc = false;
            ctrl.sortedByHatesDesc = false;
            ctrl.sortedByDatesDesc = false;
            $scope.model.data.sort(function (a, b) {
                var likesA = a && a.likes ? a.likes : 0;
                var likesB = b && b.likes ? b.likes : 0;
                if (likesA < likesB)
                    return -1;
                if (likesB < likesA)
                    return 1;
                return 0;
            });
        }

        ctrl.sortHatesAsc = function () {
            ctrl.sortedByHatesDesc = false;
            ctrl.sortedByHatesDesc = false;
            ctrl.sortedByDatesDesc = false;
            $scope.model.data.sort(function (a, b) {
                var hatesA = a && a.hates ? a.hates : 0;
                var hatesB = b && b.hates ? b.hates : 0;
                if (hatesA < hatesB)
                    return -1;
                if (hatesB < hatesA)
                    return 1;
                return 0;
            });
        }

        ctrl.sortDatesAsc = function () {
            ctrl.sortedByDatesDesc = false;
            ctrl.sortedByHatesDesc = false;
            ctrl.sortedByDatesDesc = false;
            $scope.model.data.sort(function (a, b) {
                var dateA = a && a.publicationDate ? a.publicationDate : 0;
                var dateB = b && b.publicationDate ? b.publicationDate : 0;
                if (dateA < dateB)
                    return -1;
                if (dateB < dateA)
                    return 1;
                return 0;
            });
        }

        /**
         *  List Matchmakers jobs!
         */
        ctrl.listJobs = function () {
            $http({
                url: matchingUrl
            }).then(function successCallback(response) {
                $scope.model = {data: response.data};
                ctrl.sortLikesDesc();
            }, function errorCallback(response) {
                $scope.model = {data: response.data};
            });
        }

        /**
         *  List Matchmakers jobs by passed Publisher!
         */
        ctrl.filterByPublisher = function (job) {
            var publisher = job.publisher;
            if ($state.params.publisher && publisher.id === $state.params.publisher.id) {
                $state.reload();
            } else {
                $state.go("jobs", {
                    publisher: {
                        id: publisher.id,
                        name: publisher.name
                    }
                });
            }
        }

        /**
         *  Apply for a job!
         *
         * @param item
         */
        ctrl.like = function (item) {
            $scope.modalWarning("Are you sure you want to apply '" + item.title + "' ?", "APPLY")
                .then(function (response) {
                    if (response === true) {

                    	var headers = {
                            "Content-Type": "application/json",
							"Authorization": $http.defaults.headers.common.Authorization // $cookies.global.accessToken
						}
                    	var body = {
                            "job": item.id
						}
                        $http.put(applyUrl, body, { headers: headers })
                            .then(function successCallback(response) {
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                ctrl.listJobs()
                            }, function errorCallback(response) {
                                $scope.createToast(response.data.result + "! " + response.data.description)
                            });
                    }
                });
        }

        /**
         *  Retract vote!
         *
         * @param item
         */
        ctrl.retract = function (item) {
            $scope.modalAlert("Do you want to retract the application for '" + item.title + "' ?", "RETRACT")
                .then(function (response) {
                    if (response === true) {

                        var headers = {
                            "Authorization": $http.defaults.headers.common.Authorization
                        }
                        $http.delete(applyUrl + "?job=" + item.id, null, { headers: headers })
                            .then(function successCallback(response) {
                                console.log("INFO:" + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                ctrl.listJobs()
                                $scope.scrollTop();
                            }, function errorCallback(response) {
                                console.log("ERROR: " + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                $scope.scrollTop();
                            });
                    }
                });
        }

        /**
         *  Applies to the Job!
         *
         * @param item
         */
        ctrl.apply= function (item) {
            // TODO
        }

        /**
         *  Hide voting buttons to un-subscribed users!
         *
         * @param item
         */
        ctrl.hideVotingButtons= function (item) {
            return !$http.defaults.headers.common.Authorization
        }

        /**
         *  Retrieves the user's application to the Job!
         *
         * @param item
         */
        ctrl.findVote= function (item) {
            $http({
                url: applyUrl + "?job=" + item.id
            }).then(function successCallback(response) {
                item.like = response.data.like;
            });
        }

        /**
         *  Hide like button to fans!
         *
         * @param item
         */
        ctrl.hideApplyButton= function (item) {
            return item.like !== undefined && item.like === true
        }

        /**
         *  Hide retract like button to users that have not hated yet!
         *
         * @param item
         */
        ctrl.hideRetractButton = function (item) {
            return item.like === undefined || item.like === false
        }
	}
}());
