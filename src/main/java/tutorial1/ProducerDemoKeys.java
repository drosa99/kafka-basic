package tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {

		public static void main(String[] args) {
				final Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);


				String bootstrapServers = "127.0.0.1:9092";

				//create Producer properties
				Properties properties = new Properties();
				properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
				properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
				properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

				//create producer
				KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);


				for (int i = 0; i < 10; i++) {
						//create a producer record
						String firstTopic = "first_topic";
						String value = "hello world " + i;
						String key = "id_" + i;

						ProducerRecord<String, String> record = new ProducerRecord<String, String>(firstTopic, key, value);

						//send data - async
						producer.send(record, new Callback() {
								public void onCompletion(RecordMetadata recordMetadata, Exception e) {
										if (e == null) {
												logger.info("Receveid new metadata. \n" +
														"Topic: " + recordMetadata.topic() + "\n" +
														"Partition: " + recordMetadata.partition() + "\n" +
														"Offset: " + recordMetadata.offset() + "\n" +
														"Timestamp " + recordMetadata.timestamp());
										} else {
												logger.error("Error while producing", e);
										}
								}
						});
				}
				//flush data
				producer.flush();
				producer.close();

		}
}
