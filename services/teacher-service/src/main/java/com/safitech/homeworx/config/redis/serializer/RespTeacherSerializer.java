package com.safitech.homeworx.config.redis.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.safitech.homeworx.model.ReqCareerDetail;
import com.safitech.homeworx.model.ReqEducationDetail;
import com.safitech.homeworx.model.RespTeacher;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class RespTeacherSerializer extends JsonSerializer<RespTeacher> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void serialize(RespTeacher respTeacher, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("teacherId", respTeacher.getTeacherId());
        gen.writeStringField("fullName", respTeacher.getFullName());

        // Serialize birthdate
        if (respTeacher.getBirthdate() != null) {
            gen.writeStringField("birthdate", respTeacher.getBirthdate().format(DATE_FORMATTER));
        }

        // Serialize teachingSubjects
        if (respTeacher.getTeachingSubjects() != null) {
            gen.writeArrayFieldStart("teachingSubjects");
            for (String subject : respTeacher.getTeachingSubjects()) {
                gen.writeString(subject);
            }
            gen.writeEndArray();
        }

        // Serialize educationHistory
        if (respTeacher.getEducationHistory() != null) {
            gen.writeArrayFieldStart("educationHistory");
            for (ReqEducationDetail education : respTeacher.getEducationHistory()) {
                gen.writeObject(education);
            }
            gen.writeEndArray();
        }

        // Serialize workHistory
        if (respTeacher.getWorkHistory() != null) {
            gen.writeArrayFieldStart("workHistory");
            for (ReqCareerDetail career : respTeacher.getWorkHistory()) {
                gen.writeObject(career);
            }
            gen.writeEndArray();
        }

        // Serialize contacts
        if (respTeacher.getContacts() != null) {
            gen.writeObjectField("contacts", respTeacher.getContacts());
        }

        gen.writeEndObject();
    }
}
