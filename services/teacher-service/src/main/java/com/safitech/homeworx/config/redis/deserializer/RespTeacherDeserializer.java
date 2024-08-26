package com.safitech.homeworx.config.redis.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safitech.homeworx.constant.ContactType;
import com.safitech.homeworx.model.ReqCareerDetail;
import com.safitech.homeworx.model.ReqEducationDetail;
import com.safitech.homeworx.model.RespTeacher;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RespTeacherDeserializer extends JsonDeserializer<RespTeacher> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public RespTeacher deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectNode node = (ObjectNode) p.getCodec().readTree(p);

        String teacherId = node.has("teacherId") ? node.get("teacherId").asText() : null;
        String fullName = node.has("fullName") ? node.get("fullName").asText() : null;

        // Deserialize birthdate
        LocalDate birthdate = null;
        if (node.has("birthdate")) {
            birthdate = LocalDate.parse(node.get("birthdate").asText(), DATE_FORMATTER);
        }

        // Deserialize teachingSubjects
        Set<String> teachingSubjects = new HashSet<>();
        if (node.has("teachingSubjects")) {
            for (JsonNode subjectNode : node.get("teachingSubjects")) {
                teachingSubjects.add(subjectNode.asText());
            }
        }

        // Deserialize educationHistory
        Set<ReqEducationDetail> educationHistory = new HashSet<>();
        if (node.has("educationHistory")) {
            for (JsonNode eduNode : node.get("educationHistory")) {
                educationHistory.add(p.getCodec().treeToValue(eduNode, ReqEducationDetail.class));
            }
        }

        // Deserialize workHistory
        Set<ReqCareerDetail> workHistory = new HashSet<>();
        if (node.has("workHistory")) {
            for (JsonNode workNode : node.get("workHistory")) {
                workHistory.add(p.getCodec().treeToValue(workNode, ReqCareerDetail.class));
            }
        }

        // Deserialize contacts
        Map<ContactType, String> contacts = new HashMap<>();
        if (node.has("contacts")) {
            JsonNode contactsNode = node.get("contacts");
            Iterator<Map.Entry<String, JsonNode>> fields = contactsNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();
                String value = entry.getValue().asText();

                ContactType contactType;
                try {
                    contactType = ContactType.valueOf(key);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                contacts.put(contactType, value);
            }
        }

        return RespTeacher.builder()
                .teacherId(teacherId)
                .fullName(fullName)
                .birthdate(birthdate)
                .teachingSubjects(teachingSubjects)
                .educationHistory(educationHistory)
                .workHistory(workHistory)
                .contacts(contacts)
                .build();
    }
}