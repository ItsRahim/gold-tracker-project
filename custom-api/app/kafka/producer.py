import json
import uuid

from kafka import KafkaProducer, KafkaAdminClient
from kafka.admin import NewTopic

from app.config.load_config import Config
from app.config.logging import log


class KafkaHandler:
    _init_has_run = False

    def __init__(self):
        if not KafkaHandler._init_has_run:
            self.server = Config.get_kafka_bootstrap_servers()
            self.topic = 'gold-price-stream'

            try:
                self.kafka_producer = KafkaProducer(
                    bootstrap_servers=[self.server],
                    value_serializer=lambda v: json.dumps(v).encode('utf-8')
                )
                self.create_kafka_topic()
                KafkaHandler._init_has_run = True
            except Exception as e:
                log.error(f"Failed to initialize Kafka producer: {e}")

    def create_kafka_topic(self):
        NUM_PARTITIONS = 3
        REPLICATION_FACTOR = 1

        admin_client = KafkaAdminClient(bootstrap_servers=self.server)
        if self.topic not in admin_client.list_topics():
            log.info(f"Kafka Topic {self.topic} does not exist. Creating...")

            new_topic = NewTopic(self.topic, num_partitions=NUM_PARTITIONS, replication_factor=REPLICATION_FACTOR)
            admin_client.create_topics([new_topic])

            log.info(f"Kafka topic '{self.topic}' created successfully")

    def send_price(self, data):
        random_uuid = str(uuid.uuid4())
        message = random_uuid + "_" + json.dumps(data.to_dict())
        try:
            self.kafka_producer.send(self.topic, message)
            log.info(f"Sent message to Kafka topic: {self.topic}")
        except Exception as e:
            log.error(f"Failed to send message to Kafka: {e}")
