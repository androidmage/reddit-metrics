package com.androidmage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Component
public class RedditProducer {

    @Autowired
    private RestTemplate restTemplate;

    ObjectMapper mapper = new ObjectMapper();

    KafkaProducer<String, String> producer;

    private String url = "https://api.pushshift.io/reddit/search/comment/?subreddit=torontoraptors&after=1445m&before=1440m&size=100";
    private String topic = "reddit";

    public RedditProducer() {
        producer = createKafkaProducer();
    }
//    @Scheduled(fixedRate = 1000)
//    public void execute() {
//        System.out.println("hey");
//    }

    public void produceRedditData() {
        PushshiftList pushshiftList = restTemplate.getForObject(url, PushshiftList.class);
        System.out.println(pushshiftList.toString());

        List<RedditComment> data = pushshiftList.getData();

        for (RedditComment comment : data) {
            String jsonString = null;
            try {
                // Java objects to JSON string - compact-print
                jsonString = mapper.writeValueAsString(comment);

                System.out.println(jsonString);

//                // Java objects to JSON string - pretty-print
//                String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pushshiftList);
//
//                System.out.println(jsonInString2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (jsonString != null) {
                producer.send(new ProducerRecord<>(topic, null, jsonString), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null) {
                            System.err.println("Something bad happened with kafka \n" + e.getMessage());
                        }
                    }
                });
            }
        }
    }

    public KafkaProducer<String, String> createKafkaProducer() {
        String bootstrapServers = "localhost:9092";

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        // create safe Producer
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        // high throughput producer (at the expense of a bit of latency and CPU usage)
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024));

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        return producer;
    }
}
