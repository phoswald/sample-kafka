
# sample-kafka

## Maven

~~~
$ mvn clean verify
$ mvn liberty:run
~~~

- http://localhost:8080/

## Database

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
