package queuing.core.global.security;

public final class Constants {
    private Constants() {}

    public static final class Paths {
        private Paths() {}

        public static final String AUTH_BASE = "/api/auth";
        public static final String AUTH_ALL  = AUTH_BASE + "/**";
        public static final String OIDC_LOGIN_BASE = AUTH_BASE + "/login";
        public static final String OIDC_LOGIN_PATTERN = OIDC_LOGIN_BASE + "/**";
        public static final String OIDC_CALLBACK_BASE = AUTH_BASE + "/callback/*";
        public static final String LOGOUT = AUTH_BASE + "/logout";
        public static final String CSRF_ISSUE = AUTH_BASE + "/csrf";

        public static final String API_ALL = "/api/**";
        public static final String API_USER_PROFILE_ONBOARDING = "/api/v1/user-profiles/me/onboarding";
        public static final String APP_APP_INFO = "/api/v1/app/**";

        public static final String[] PERMIT_ALL = {
            AUTH_ALL,
            APP_APP_INFO
        };
    }

    public static final class Cookies {
        private Cookies() {}

        public static final String PARAM_CONTINUE = "continue";
        public static final String REDIRECT_URL = "queuing.login.redirectUrl";
        public static final String SESSION_ATTR_REDIRECT_URL = "queue.login.session.redirectUrl";
    }

    public static final class Frontend {
        private Frontend() {}

        public static final String LOGIN_ERROR_PATH = "/login?error";
    }
}
