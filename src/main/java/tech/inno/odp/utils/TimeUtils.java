package tech.inno.odp.utils;

import com.google.protobuf.Timestamp;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@UtilityClass
public class TimeUtils {

    /**
     * Преобразовать к localDateTime timestamp использующийся в grpc
     *
     * @param timestamp grpc timestamp
     * @return localDateTime
     */
    public static LocalDateTime toLocalDateTime(@NotNull final Timestamp timestamp) {
        return LocalDateTime.ofEpochSecond(
                timestamp.getSeconds(),
                timestamp.getNanos(),
                ZoneOffset.UTC);
    }

    /**
     * Преобразовать LocalDateTime к protobuf.Timestamp
     *
     * @param dateTime localDateTime
     * @return timestamp grpc timestamp
     * @see Timestamp
     */
    public static Timestamp toProtobufTimestamp(@NotNull final LocalDateTime dateTime) {
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano()).build();
    }
}
