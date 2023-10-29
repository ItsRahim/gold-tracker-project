# import yaml
# from kafka import KafkaConsumer
#
# with open('config.yaml', 'r') as config_file:
#     config = yaml.safe_load(config_file)['kafka']
#
# consumer = KafkaConsumer(config['consumer_topic'],
#                          group_id=config['group_id'],
#                          bootstrap_servers=config['bootstrap_servers'],
#                          auto_offset_reset=config['auto_offset_reset'])
#
# for message in consumer:
#     print(f"Received message: {message.value.decode('utf-8')}")
#
# consumer.close()

# TODO: Add consumer here to establish connection with SpringBoot application
