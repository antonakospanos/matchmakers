(function () {
	"use strict";
	angular
		.module("MovieRamaUi")
		.controller("MoviesReviewCtrl", ["$rootScope", "$scope", "$http", "$state", MoviesReviewCtrl]);

	function MoviesReviewCtrl($rootScope, $scope, $http, $state) {
		var ctrl = this;
        var voteMoviesUrl = $rootScope.backend_protocol + "://" + $rootScope.backend_ip + ":" + $rootScope.backend_port + "/" + $rootScope.backend_context_path + "/votes";
        var listMoviesUrl = $rootScope.backend_protocol + "://" + $rootScope.backend_ip + ":" + $rootScope.backend_port + "/" + $rootScope.backend_context_path + "/movies";

        if ($state.params.publisher !== undefined && $state.params.publisher.id !== undefined) {
            listMoviesUrl = listMoviesUrl + "?userId=" + $state.params.publisher.id;
            $scope.publisher = $state.params.publisher.name
        }

        // Initialization
        ctrl.init = function () {
            ctrl.listMovies();
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
         *  List Matchmakers movies!
         */
        ctrl.listMovies = function () {
            $http({
                url: listMoviesUrl
            }).then(function successCallback(response) {
                $scope.model = {data: response.data};
                ctrl.sortLikesDesc();
            }, function errorCallback(response) {
                $scope.model = {data: response.data};
            });
        }

        /**
         *  List Matchmakers movies by passed Publisher!
         */
        ctrl.filterByPublisher = function (movie) {
            var publisher = movie.publisher;
            if ($state.params.publisher && publisher.id === $state.params.publisher.id) {
                $state.reload();
            } else {
                $state.go("movies_publisher_review", {
                    publisher: {
                        id: publisher.id,
                        name: publisher.name
                    }
                });
            }
        }

        /**
         *  Vote for a movie!
         *
         * @param item
         */
        ctrl.like = function (item) {
            $scope.modalWarning("Are you sure you like '" + item.title + "' ?", "LIKE")
                .then(function (response) {
                    if (response === true) {

                    	var headers = {
                            "Content-Type": "application/json",
							"Authorization": $http.defaults.headers.common.Authorization // $cookies.global.accessToken
						}
                    	var body = {
                            "like": true,
                            "movie": item.id
						}
                        $http.put(voteMoviesUrl, body, { headers: headers })
                            .then(function successCallback(response) {
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                ctrl.listMovies()
                            }, function errorCallback(response) {
                                $scope.createToast(response.data.result + "! " + response.data.description)
                            });
                    }
                });
        }

        /**
         *  Vote against a movie!
         *
         * @param item
         */
        ctrl.hate = function (item) {
            $scope.modalAlert("Are you sure you hate '" + item.title + "' ?", "HATE")
                .then(function (response) {
                    if (response === true) {

                        var headers = {
                            "Content-Type": "application/json",
                            "Authorization": $http.defaults.headers.common.Authorization
                        }
                        var body = {
                            "like": false,
                            "movie": item.id
                        }
                        $http.put(voteMoviesUrl, body, { headers: headers })
                            .then(function successCallback(response) {
                                console.log("INFO:" + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                ctrl.listMovies()
                            }, function errorCallback(response) {
                                console.log("ERROR: " + response.data);
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
            item.vote = item.like ? "positive vote" : "negative vote";
            $scope.modalAlert("Do you want to retract the " +item.vote+ " to '" + item.title + "' ?", "RETRACT")
                .then(function (response) {
                    if (response === true) {

                        var headers = {
                            "Authorization": $http.defaults.headers.common.Authorization
                        }
                        $http.delete(voteMoviesUrl + "?movie=" + item.id, null, { headers: headers })
                            .then(function successCallback(response) {
                                console.log("INFO:" + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                ctrl.listMovies()
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
         *  Hide voting buttons to un-subscribed users!
         *
         * @param item
         */
        ctrl.hideVotingButtons= function (item) {
            return !$http.defaults.headers.common.Authorization
        }

        /**
         *  Retrieves the user's vote to the Movie!
         *
         * @param item
         */
        ctrl.findVote= function (item) {
            $http({
                url: voteMoviesUrl + "?movie=" + item.id
            }).then(function successCallback(response) {
                item.like = response.data.like;
            });
        }

        /**
         *  Hide like button to fans!
         *
         * @param item
         */
        ctrl.hideLikeButton= function (item) {
            return item.like !== undefined && item.like === true
        }

        /**
         *  Hide hate button haters!
         *
         * @param item
         */
        ctrl.hideHateButton= function (item) {
            return item.like !== undefined && item.like === false
        }

        /**
         *  Hide retract like button to users that have not hated yet!
         *
         * @param item
         */
        ctrl.hideRetractLikeButton = function (item) {
            return item.like === undefined || item.like === false
        }

        /**
         *  Hide retract hate button to users that have not hated yet!
         *
         * @param item
         */
        ctrl.hideRetractHateButton = function (item) {
            return item.like === undefined || item.like === true
        }
	}
}());
