jQuery.extend( jQuery.fn.dataTableExt.oSort, {
	/**
	 * European dates dd.mm(.yyyy) or dd/mm(/yyyy), will be detected automatically
	 */
	'date-eu-pre': function ( date ) {
//		var year = '', // year is optional
//			month, day,
//			character = '/';
//		if ( date.indexOf('.') > 0 ) {
//			character = '.'; // if found, use . as the separator, otherwise use /
//		}
//		date = date.replace( ' ', '' ).split( character );
//		if ( date[2] ) {
//			year = date[2];
//		}
//		month = date[1];
//		if ( 1 === month.length ) {
//			month = '0' + month;
//		}
//		day = date[0];
//		if ( 1 === day.length ) {
//			day = '0' + day;
//		}
//		return ( year + month + day ) * 1;
                //TODO moment string to unixtimestamp
//                todo=true;
//                var mom=moment(date, "DD/MM/YYYY HH:mm:ss ZZ");
                return date;
	},
	'date-eu-asc': function ( a, b ) {
                var moma=moment(a, "DD/MM/YYYY HH:mm:ss ZZ").valueOf();
                var momb=moment(b, "DD/MM/YYYY HH:mm:ss ZZ").valueOf();
		return moma - momb;
	},
	'date-eu-desc': function ( a, b ) {
		var moma=moment(a, "DD/MM/YYYY HH:mm:ss ZZ").valueOf();
                var momb=moment(b, "DD/MM/YYYY HH:mm:ss ZZ").valueOf();
		return momb - moma;
	},

	/**
	 * Formatted numbers, currency and percentage values, will be detected automatically
	 */
	'formatted-num-pre': function ( a ) {
		a = ( '-' === a ) ? 0 : a.replace( /[^\d\-\.]/g, "" );
		return parseFloat( a );
	},
	'formatted-num-asc': function ( a, b ) {
		return a - b;
	},
	'formatted-num-desc': function ( a, b ) {
		return b - a;
	},

	/**
	 * Numeric Comma (numbers like 0,5), NOT detected automatically!
	 */
	'numeric-comma-pre': function ( a ) {
		a = ( '-' === a ) ? 0 : a.replace( /[^\d\-\,]/g, "" ).replace( /,/, "." );
		return parseFloat( a );
	},
	'numeric-comma-asc': function ( a, b ) {
		return a - b;
	},
	'numeric-comma-desc': function ( a, b ) {
		return b - a;
	},

	/**
	 * Numbers and text mixed in a column, text is treated as infinity
	 */
	'numbers+text-pre': function ( a ) {
		if ( ! isNaN( parseFloat( a ) ) && isFinite( a ) ) {
			return parseFloat(a);
		} else {
			return Number.MAX_VALUE;
		}
	 },
	'numbers+text-asc': function ( a, b ) {
		return a - b;
	},
	'numbers+text-desc': function ( a, b ) {
		return b - a;
	}
} );


