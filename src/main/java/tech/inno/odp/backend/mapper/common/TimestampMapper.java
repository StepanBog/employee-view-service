package tech.inno.odp.backend.mapper.common;

import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import tech.inno.odp.utils.TimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface TimestampMapper {

    default Timestamp mapToProto(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return TimeUtils.toProtobufTimestamp(dateTime);
    }

    default LocalDateTime mapToLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return TimeUtils.toLocalDateTime(timestamp);
    }

    default Timestamp mapToProto(LocalDate dateTime) {
        if (dateTime == null) {
            return null;
        }
        return TimeUtils.toProtobufTimestamp(dateTime.atStartOfDay());
    }

    default LocalDate mapToLocalDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return TimeUtils.toLocalDateTime(timestamp).toLocalDate();
    }
}
