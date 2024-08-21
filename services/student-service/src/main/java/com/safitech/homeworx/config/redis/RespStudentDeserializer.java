package com.safitech.homeworx.config.redis;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.safitech.homeworx.dto.RespStudent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RespStudentDeserializer extends StdDeserializer<RespStudent> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RespStudentDeserializer() {
        this(null);
    }

    public RespStudentDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RespStudent deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        long studentId = node.get("studentId").asLong();
        String fullName = node.get("fullName").asText();

        LocalDate dateOfBirth = node.has("dateOfBirth") ? LocalDate.parse(node.get("dateOfBirth").asText(), DATE_FORMATTER) : null;

        String email = node.get("email").asText();
        String primaryMobileNumber = node.get("primaryMobileNumber").asText();
        String secondaryMobileNumber = node.get("secondaryMobileNumber").asText();
        String address = node.get("address").asText();

        return RespStudent.builder()
                .studentId(studentId)
                .fullName(fullName)
                .dateOfBirth(dateOfBirth)
                .email(email)
                .primaryMobileNumber(primaryMobileNumber)
                .secondaryMobileNumber(secondaryMobileNumber)
                .address(address)
                .build();
    }
}
