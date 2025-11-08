package com.reserve.illoomung.core.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.reserve.illoomung.dto.business.OperatingInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OpeningHoursDeserializer extends JsonDeserializer<Map<String, OperatingInfo>> {

    @Override
    public Map<String, OperatingInfo> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Map<String, OperatingInfo> result = new HashMap<>();
        JsonNode rootNode = jp.getCodec().readTree(jp);

        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();

        while(fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String day = field.getKey();
            JsonNode value = field.getValue();

            OperatingInfo operatingInfo = new OperatingInfo();

            if(value.isTextual()) {
                operatingInfo.setTime(value.asText());
            }
            else if(value.isObject()) {
                JsonNode timeNode = value.get("time");
                JsonNode breakTimeNode = value.get("breakTime");

                if(timeNode != null && breakTimeNode != null) {
                    operatingInfo.setTime(timeNode.asText());
                }
                if(breakTimeNode != null && breakTimeNode.isTextual()) {
                    operatingInfo.setBreakTime(breakTimeNode.asText());
                }
            }
            result.put(day, operatingInfo);
        }
        return result;
    }
}
