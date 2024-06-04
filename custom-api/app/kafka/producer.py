import json
import uuid

from kafka import KafkaProducer
from app.config.logging import log

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

            KafkaHandler._init_has_run = True

    def send_price(self, data):
        random_uuid = str(uuid.uuid4())
        message = random_uuid + "," + json.dumps(data.to_dict())
        try:
            self.kafka_producer.send(self.TOPIC_NAME, message)
            log.info(f"Sent message to Kafka topic: {self.TOPIC_NAME}")
        except Exception as e:
            log.error(f"Failed to send message to Kafka: {e}")
