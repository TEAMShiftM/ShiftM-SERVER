package com.shiftm.shiftm.infra.holiday;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemsDeserializer extends JsonDeserializer<HolidayResponse.Items> {

    @Override
    public HolidayResponse.Items deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        final JsonNode itemsNode = parser.readValueAsTree();
        if (itemsNode.isTextual() && itemsNode.asText().isBlank()) {
            return new HolidayResponse.Items(Collections.emptyList());
        }

        final JsonNode itemNode = itemsNode.get("item");
        if (itemNode == null || itemNode.isNull()) {
            return new HolidayResponse.Items(Collections.emptyList());
        }

        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final List<HolidayResponse.Item> itemList = new ArrayList<>();
        if (itemNode.isArray()) {
            for (final JsonNode elementNode : itemNode) {
                itemList.add(mapper.treeToValue(elementNode, HolidayResponse.Item.class));
            }
        } else if (itemNode.isObject()) {
            itemList.add(mapper.treeToValue(itemNode, HolidayResponse.Item.class));
        }

        return new HolidayResponse.Items(itemList);
    }
}