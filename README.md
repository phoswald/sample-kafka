
# sample-kafka

Experiments with OpenLiberty and Apache Kafka

TODO:

- Shutdown does not complete
- Exception in consumer on shutdown

## Maven

~~~
$ mvn clean verify
$ mvn liberty:run
~~~

- http://localhost:8080/

## PostreSQL

~~~
CREATE TABLE ORDER_ (
  ORDER_ID_      VARCHAR(40) NOT NULL,
  ORDER_DETAILS_ VARCHAR(255),
  PAYMENT_ID_    VARCHAR(40) NOT NULL,
  STATUS_        VARCHAR(40) NOT NULL,
  TIMESTAMP_     TIMESTAMP NOT NULL,
  PRIMARY KEY (ORDER_ID_)
);
~~~

## Kafka

Links: 

- https://kafka.apache.org/quickstart

Installation:

- Download https://dlcdn.apache.org/kafka/3.4.0/kafka_2.13-3.4.0.tgz
- Extract and run as follows:
- Console producer and consumers are line based, and can be quit using Ctrl+C

~~~
$ tar -xzf ~/Downloads/Linux/Java/kafka_2.13-3.4.0.tgz
$ cd kafka_2.13-3.4.0/

$ KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
$ bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c config/kraft/server.properties
$ bin/kafka-server-start.sh config/kraft/server.properties

$ bin/kafka-topics.sh --create   --topic payment-events    --bootstrap-server localhost:9092
$ bin/kafka-topics.sh --create   --topic order-events      --bootstrap-server localhost:9092
$ bin/kafka-topics.sh --describe --topic payment-events    --bootstrap-server localhost:9092
$ bin/kafka-topics.sh --describe --topic order-events      --bootstrap-server localhost:9092

$ bin/kafka-console-consumer.sh --topic payment-events    --from-beginning --bootstrap-server localhost:9092
$ bin/kafka-console-consumer.sh --topic order-events      --from-beginning --bootstrap-server localhost:9092

$ bin/kafka-console-producer.sh --topic payment-events    --bootstrap-server localhost:9092 <<EOF
{"paymentId":"O-001"}
EOF

$ rm -rf /tmp/kraft-combined-logs
~~~
