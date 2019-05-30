package com.workable.matchmakers.web.validator;

public class RegexValidator {

    public static final String EMAIL_VALIDATOR = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public static final String URL_VALIDATOR = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
    // "@(https?|http?|ftp)://(-\\.)?([^\\s/?\\.#-]+\\.?)+(/[^\\s]*)?$@iS";
    //"^(https?|http?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    //"^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";

    public static final String FACEBOOK_URL_VALIDATOR = "((http|https)://)?(www[.])?facebook.com/.+";
    public static final String LINKEDIN_URL_VALIDATOR = "((http|https)://)?(www[.])?linkedin.com/.+";
}
