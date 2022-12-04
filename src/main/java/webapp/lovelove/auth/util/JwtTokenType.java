package webapp.lovelove.auth.util;

import lombok.Getter;

public enum JwtTokenType {
    ACCESS("jwt-access-token"),
    REFRESH("jwt-refresh-token");

    @Getter
    private final String subject;

    JwtTokenType(String subject) {this.subject = subject;}
}
