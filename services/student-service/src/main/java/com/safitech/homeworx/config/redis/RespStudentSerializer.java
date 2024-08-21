package com.safitech.homeworx.config.redis;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.safitech.homeworx.dto.RespStudent;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class RespStudentSerializer extends StdSerializer<RespStudent> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RespStudentSerializer() {
        this(null);
    }

    public RespStudentSerializer(Class<RespStudent> t) {
        super(t);
    }

    @Override
    public void serialize(RespStudent respStudent, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("studentId", respStudent.getStudentId());
        gen.writeStringField("fullName", respStudent.getFullName());

        if (respStudent.getDateOfBirth() != null) {
            gen.writeStringField("dateOfBirth", respStudent.getDateOfBirth().format(DATE_FORMATTER));
        }

        gen.writeStringField("email", respStudent.getEmail());
        gen.writeStringField("primaryMobileNumber", respStudent.getPrimaryMobileNumber());
        gen.writeStringField("secondaryMobileNumber", respStudent.getSecondaryMobileNumber());
        gen.writeStringField("address", respStudent.getAddress());
        gen.writeEndObject();
    }
}