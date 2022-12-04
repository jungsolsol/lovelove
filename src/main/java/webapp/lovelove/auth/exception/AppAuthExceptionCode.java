package webapp.lovelove.auth.exception;

import lombok.Getter;

public enum AppAuthExceptionCode {

    ACCESS_TOKEN_EXPIRED(401,"액세스 토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(401,"유효하지 않는 액세스 토큰 입니다."),
    ACCESS_TOKEN_NOT_EXIST(401,"액세스 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(401,"리프레시 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(401,"리프레시 토큰이 비어있습니다."),
    REFRESH_TOKEN_NOT_EXIST(401,"리프레시 토큰이 존재하지 않습니다."),
    INVALID_ID_OR_PASSWORD(401,"유효하지 않은 아이디 혹은 비밀번호 입니다."),
    DATA_DESERIALIZE_ERROR(500,"Failed to login request body deserialization"),
    EXISTS_MEMBER(401,"이미 존재하는 아이디 입니다."),
    OAUTH2_AUTH_FAILURE(401,"OAuth2 인증에 실패했습니다."),
    INVALID_OAUTH2_PROVIDER(401,"유효하지 않은 OAuth2 Provider입니다."),
    UNDEFINED(500,"알 수 없는 오류가 발생했습니다.");

    @Getter
    private final int status;

    @Getter
    private final String message;

    AppAuthExceptionCode(int status, String message){
        this.status = status;
        this.message = message;
    }
}
