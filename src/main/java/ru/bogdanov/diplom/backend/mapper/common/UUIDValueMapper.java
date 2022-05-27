package ru.bogdanov.diplom.backend.mapper.common;

import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public interface UUIDValueMapper {

    default String mapToString(UUID value) {
        if (value == null) {
            return null;
        }

        return value.toString();
    }

    default UUID mapToUUID(String value) {
        if (value == null) {
            return null;
        }

        return UUID.fromString(value);
    }
}
