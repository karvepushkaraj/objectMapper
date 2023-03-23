package mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonMapper<T> {

    private final String json;
    private final ExpressionParser parser;
    private final EvaluationContext context;
    private final ObjectMapper mapper;

    public JsonMapper(T source, String destinationJson) {
        parser = new SpelExpressionParser();
        context = new StandardEvaluationContext(source);
        mapper = new ObjectMapper();
        json = destinationJson;
    }

    public String getMappedJson() throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(json);
        if (jsonNode.isObject())
            return processObject((ObjectNode) jsonNode).toString();
        else if (jsonNode.isArray())
            return processArray((ArrayNode) jsonNode).toString();
        throw new IllegalArgumentException("Improper json");
    }

    private JsonNode processObject(ObjectNode objectNode) {
        Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode jsonNode = entry.getValue();
            if (jsonNode instanceof ObjectNode)
                entry.setValue(processObject((ObjectNode) jsonNode));
            else if (jsonNode instanceof ArrayNode)
                entry.setValue(processArray((ArrayNode) jsonNode));
            else if (jsonNode instanceof ValueNode)
                entry.setValue(processField((ValueNode) jsonNode));
        }
        return objectNode;
    }

    private ArrayNode processArray(ArrayNode arrayNode) {
        ArrayNode array = new ArrayNode(mapper.getNodeFactory());
        for (Object node : arrayNode) {
            addArrayElements(array, node);
        }
        return array;
    }

    private JsonNode processField(ValueNode valueNode) {
        if (valueNode instanceof TextNode) {
            String textValue = valueNode.asText();
            Object value = textValue.equals("") ? textValue : parser.parseExpression(textValue).getValue(context);
            if (value instanceof String)
                return new TextNode((String) value);
            else if (value instanceof Number)
                return createNumberNode(value);
            else if (value instanceof List) {
                ArrayNode arrayNode = new ArrayNode(mapper.getNodeFactory());
                for (Object member : (List<?>) value) {
                    addArrayElements(arrayNode, member);
                }
                return arrayNode;
            } else
                return new POJONode(value);
        }
        return valueNode;
    }

    private ValueNode createNumberNode(Object value) {
        if (value instanceof Short)
            return new ShortNode((short) value);
        else if (value instanceof Integer)
            return new IntNode((int) value);
        else if (value instanceof Long)
            return new LongNode((long) value);
        else if (value instanceof Float)
            return new FloatNode((float) value);
        else if (value instanceof Double)
            return new DoubleNode((double) value);
        else if (value instanceof BigInteger)
            return new BigIntegerNode((BigInteger) value);
        else if (value instanceof BigDecimal)
            return new DecimalNode((BigDecimal) value);
        throw new IllegalArgumentException("Not a Number datatype");
    }

    private void addArrayElements(ArrayNode array, Object node) {
        if (node instanceof ObjectNode)
            array.add(processObject((ObjectNode) node));
        else if (node instanceof ArrayNode)
            array.add(processArray((ArrayNode) node));
        else if (node instanceof ValueNode)
            array.add(processField((ValueNode) node));
        else
            array.add(processField(new POJONode(node)));
    }

}
