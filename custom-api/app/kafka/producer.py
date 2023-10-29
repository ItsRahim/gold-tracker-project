from kafka import KafkaProducer, KafkaAdminClient
from kafka.admin import NewTopic
from app.config.logging import log
import json

from app.config.load_config import load_config


class KafkaHandler:
    _init_has_run = False

    def __init__(self):
        if not KafkaHandler._init_has_run:
            self.config = load_config('kafka')
            self.KAFKA_SERVER = self.config['bootstrap_servers']
            self.TOPIC_NAME = self.config['topic']

            self.kafka_producer = KafkaProducer(
                bootstrap_servers=[self.KAFKA_SERVER],
                value_serializer=lambda v: json.dumps(v).encode('utf-8')
            )

            self.create_kafka_topic()

            KafkaHandler._init_has_run = True

    def create_kafka_topic(self):
        NUM_PARTITIONS = 3
        REPLICATION_FACTOR = 1

        admin_client = KafkaAdminClient(bootstrap_servers=self.KAFKA_SERVER)
        if self.TOPIC_NAME not in admin_client.list_topics():
            log.info(f"Kafka Topic {self.TOPIC_NAME} does not exist. Creating...")

            new_topic = NewTopic(self.TOPIC_NAME, num_partitions=NUM_PARTITIONS, replication_factor=REPLICATION_FACTOR)
            admin_client.create_topics([new_topic])
            
            log.info(f"Kafka topic '{self.TOPIC_NAME}' created successfully")

    def send_price(self, data):
        message = json.dumps(data.to_dict())
        try:
            self.kafka_producer.send(self.TOPIC_NAME, message)
            log.info(f"Sent message to Kafka topic: {self.TOPIC_NAME}")
        except Exception as e:
            log.error(f"Failed to send message to Kafka: {e}")