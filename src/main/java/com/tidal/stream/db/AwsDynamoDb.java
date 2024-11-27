package com.tidal.stream.db;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.tidal.utils.propertieshandler.PropertiesFinder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * This is a finder class built in similar to a builder class
 */
public class AwsDynamoDb {

    private String accessKey;
    private String secretKey;
    private String region;
    private String tableName;
    private Map<String, String> partitionKeySet;
    private Map<String, String> sortKeySet;

    //Private constructor to prevent instantiation
    private AwsDynamoDb() {
    }

    /**
     * Method to return an optional value of a single item from the Dynamo DB table.
     * If you are seeking multiple values from the same item, use <code>getItems</code>
     *
     * @param key Data Table Key
     * @return Optional value if present, else empty optional value
     */
    public Optional<String> getItem(String key) {
        return getDbItem().isPresent() ? Optional.of(getDbItem().get().get(key).getS()) : Optional.empty();
    }

    /**
     * Method to return the whole item content as map of key value pairs.
     * This method should be used if there are  multiple verifications as it would return all the values.
     * The getItem method will query the DB every time and will be resource intensive
     *
     * @return map of item value pairs
     */
    public Optional<Map<String, String>> getItems() {
        Map<String, String> items = new HashMap<>();
        if (getDbItem().isPresent()) {
            getDbItem().get().forEach((k, v) -> items.put(k, v.getS()));
            return Optional.of(items);
        }
        return Optional.empty();
    }


    private AmazonDynamoDB clientBuilder() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    private Optional<Map<String, AttributeValue>> getDbItem() {
        Map.Entry<String, String> partitionEntry = partitionKeySet.entrySet().iterator().next();
        Map.Entry<String, String> sortEntry = sortKeySet.entrySet().iterator().next();

        GetItemRequest request =
                new GetItemRequest().withTableName(tableName)
                        .addKeyEntry(partitionEntry.getKey(), new AttributeValue().withS(partitionEntry.getValue()))
                        .addKeyEntry(sortEntry.getKey(), new AttributeValue().withS(sortEntry.getValue()));

        GetItemResult result = clientBuilder().getItem(request);

        if (!result.toString().equals("{}")) {
            return Optional.of(result.getItem());
        }
        return Optional.empty();
    }

    @SuppressWarnings("FieldCanBeLocal")
    public static class Finder {
        private static final String PARTITION_KEY_DEFAULT = "businessdomain";
        private static final String PARTITION_VALUE_DEFAULT = "ABC:Default";
        private static final String SORT_KEY_DEFAULT = "id";
        private static final String TABLE_NAME_DEFAULT = "ddb_ErrorAlerts";

        UnaryOperator<String> getProperty = PropertiesFinder::getProperty;
        private final String accessKey = getProperty.apply("aws.api.accessKey");
        private final String secretKey = getProperty.apply("aws.api.secretKey");
        private final String region = getProperty.apply("aws.s3.region");
        private String tableName;
        private Map<String, String> partitionKeySet;
        private Map<String, String> sortKeySet;

        public Finder fromTable(String tableName) {
            this.tableName = tableName;
            return this;
        }

        /**
         * Method to add the sort keys
         *
         * @param key   the sort key
         * @param value the sort value
         * @return this instance of Finder
         */
        public Finder withSortKeySet(String key, String value) {
            Map<String, String> map = new HashMap<>();
            map.put(key, value);
            this.sortKeySet = map;
            return this;
        }

        /**
         * Method to add the sort value. A default sort key is specified
         *
         * @param value the sort value
         * @return this instance of Finder
         */
        public Finder withSortValue(String value) {
            Map<String, String> map = new HashMap<>();
            map.put(SORT_KEY_DEFAULT, value);
            this.sortKeySet = map;
            return this;
        }

        /**
         * Method to add the partition key set
         *
         * @param key   the partition key
         * @param value the partition value
         * @return this instance of Finder
         */
        public Finder withPartitionKeySet(String key, String value) {
            Map<String, String> map = new HashMap<>();
            map.put(key, value);
            partitionKeySet = map;
            return this;
        }

        private Map<String, String> withDefaultPartitionKeySet() {
            Map<String, String> map = new HashMap<>();
            map.put(PARTITION_KEY_DEFAULT, PARTITION_VALUE_DEFAULT);
            return map;
        }

        public AwsDynamoDb find() {
            AwsDynamoDb awsDynamoDb = new AwsDynamoDb();

            awsDynamoDb.accessKey = accessKey;
            awsDynamoDb.secretKey = secretKey;
            awsDynamoDb.region = region;
            awsDynamoDb.tableName = tableName == null ? TABLE_NAME_DEFAULT : tableName;
            awsDynamoDb.partitionKeySet = partitionKeySet == null ? withDefaultPartitionKeySet() : partitionKeySet;
            awsDynamoDb.sortKeySet = sortKeySet;

            return awsDynamoDb;
        }

    }
}
