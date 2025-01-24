package esea.esea_api.dynamodb_repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;

@Repository
public class ChatHistoryRepository {
    private final AmazonDynamoDB dynamoDB;

    private final String tableName;

    public ChatHistoryRepository(AmazonDynamoDB dynamoDB, Environment env) {
        this.dynamoDB = dynamoDB;
        this.tableName = env.getProperty("AI_AWS_DYNAMODB_TABLE");
    }

    // 동적 데이터 저장
    public void save(Map<String, AttributeValue> item) {
        PutItemRequest putItemRequest = new PutItemRequest()
                .withTableName(tableName)
                .withItem(item);
        dynamoDB.putItem(putItemRequest);
    }

    // 동적 데이터 조회
    public Map<String, Object> findById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PK", new AttributeValue().withS(id));

        GetItemRequest request = new GetItemRequest()
                .withTableName(tableName)
                .withKey(key);

        try {
            GetItemResult result = dynamoDB.getItem(request);
            return convertAttributeValueToObject(result.getItem());
        } catch (AmazonDynamoDBException e) {
            throw new RuntimeException("DynamoDB 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> findAll() {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(tableName);

        ScanResult result = dynamoDB.scan(scanRequest);
        return result.getItems().stream()
                .map(this::convertAttributeValueToObject)
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertAttributeValueToObject(Map<String, AttributeValue> item) {
        if (item == null)
            return null;

        Map<String, Object> converted = new HashMap<>();
        for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
            converted.put(entry.getKey(), extractValue(entry.getValue()));
        }
        return converted;
    }

    private Object extractValue(AttributeValue attr) {
        if (attr == null)
            return null;

        if (attr.getS() != null) {
            return attr.getS();
        } else if (attr.getN() != null) {
            return attr.getN();
        } else if (attr.getBOOL() != null) {
            return attr.getBOOL();
        } else if (attr.getM() != null) {
            // Map 타입 처리 (중첩된 객체)
            return convertAttributeValueToObject(attr.getM());
        } else if (attr.getL() != null) {
            // List 타입 처리 (중첩된 배열)
            return attr.getL().stream()
                    .map(this::extractValue)
                    .collect(Collectors.toList());
        } else if (attr.getNULL() != null) {
            return null;
        } else if (attr.getSS() != null) {
            return attr.getSS(); // String Set
        } else if (attr.getNS() != null) {
            return attr.getNS(); // Number Set
        } else if (attr.getBS() != null) {
            return attr.getBS(); // Binary Set
        }

        return null;
    }

    // PK로 모든 관련 항목 조회
    public List<Map<String, Object>> findByPK(String pk) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", new AttributeValue().withS(pk));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withKeyConditionExpression("PK = :pk")
                .withExpressionAttributeValues(expressionAttributeValues);

        try {
            QueryResult result = dynamoDB.query(queryRequest);
            return result.getItems().stream()
                    .map(this::convertAttributeValueToObject)
                    .collect(Collectors.toList());
        } catch (AmazonDynamoDBException e) {
            throw new RuntimeException("DynamoDB 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // 2. 내림차순 정렬 쿼리
    public List<Map<String, Object>> descendingQuery(String pk) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", new AttributeValue().withS(pk));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withKeyConditionExpression("PK = :pk")
                .withExpressionAttributeValues(expressionAttributeValues);

        try {
            QueryResult result = dynamoDB.query(queryRequest);
            return result.getItems().stream()
                    .map(this::convertAttributeValueToObject)
                    .collect(Collectors.toList());
        } catch (AmazonDynamoDBException e) {
            throw new RuntimeException("DynamoDB 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // PK와 SK로 특정 항목 조회
    public Map<String, Object> findByPKAndSK(String pk, String sk) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PK", new AttributeValue().withS(pk));
        key.put("SK", new AttributeValue().withS(sk));

        GetItemRequest request = new GetItemRequest()
                .withTableName(tableName)
                .withKey(key);

        try {
            GetItemResult result = dynamoDB.getItem(request);
            return convertAttributeValueToObject(result.getItem());
        } catch (AmazonDynamoDBException e) {
            throw new RuntimeException("DynamoDB 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> findSortedChatHistory(String pk) {
        OffsetDateTime sevenDaysAgo = OffsetDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(7);
        String sevenDaysAgoStr = sevenDaysAgo.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", new AttributeValue().withS(pk));
        expressionAttributeValues.put(":sevenDaysAgo", new AttributeValue().withS(sevenDaysAgoStr));
        expressionAttributeValues.put(":emptyString", new AttributeValue().withS(""));

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#title", "title");

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withIndexName("PK-create_time-index")
                .withKeyConditionExpression("PK = :pk AND create_time >= :sevenDaysAgo")
                .withFilterExpression("attribute_exists(#title) AND #title <> :emptyString")
                .withExpressionAttributeNames(expressionAttributeNames)
                .withExpressionAttributeValues(expressionAttributeValues)
                .withScanIndexForward(false)
                .withLimit(20);

        try {
            QueryResult result = dynamoDB.query(queryRequest);
            return result.getItems().stream()
                    .map(this::convertAttributeValueToObject)
                    .collect(Collectors.toList());
        } catch (AmazonDynamoDBException e) {
            throw new RuntimeException("DynamoDB 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public void updateChatTitle(String pk, String sk, String title) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PK", new AttributeValue().withS(pk));
        key.put("SK", new AttributeValue().withS(sk));

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":title", new AttributeValue().withS(title));

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#title", "title");

        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withUpdateExpression("SET #title = :title")
                .withExpressionAttributeNames(expressionAttributeNames)
                .withExpressionAttributeValues(expressionAttributeValues);

        try {
            dynamoDB.updateItem(updateItemRequest);
        } catch (AmazonDynamoDBException e) {
            throw new RuntimeException("DynamoDB 업데이트 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public void deleteByPKAndSK(String pk, String sk) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("PK", new AttributeValue().withS(pk));
        key.put("SK", new AttributeValue().withS(sk));

        DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
                .withTableName(tableName)
                .withKey(key);

        try {
            dynamoDB.deleteItem(deleteItemRequest);
        } catch (AmazonDynamoDBException e) {
            throw new RuntimeException("DynamoDB 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
