package webapp.lovelove.member.domain.memberprofiledomain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum Alcohol {
    전혀, 가끔먹어요, 자주먹어요;

    @JsonCreator
    public static Alcohol create(String requestValue) {
        return Stream.of(values())
                .filter(v -> v.toString().equalsIgnoreCase(requestValue))
                .findFirst()
                .orElse(null);
    }
}


