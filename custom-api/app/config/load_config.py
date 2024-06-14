import os
import yaml

from dotenv import load_dotenv

load_dotenv()


def load_config(element: str) -> str:
    with open('app/resources/app_config.yaml', 'r') as config_file:
        return yaml.safe_load(config_file)[element]


class Config:

    @staticmethod
    def get_deployment_type():
        return os.getenv('DEPLOYMENT_TYPE')

    @staticmethod
    def get_encryption_key():
        return os.getenv('ENCRYPTION_KEY')

    @staticmethod
    def get_db_host():
        return os.getenv('DB_HOST')

    @staticmethod
    def get_db_port():
        return os.getenv('DB_PORT')

    @staticmethod
    def get_db_name():
        return os.getenv('DB_NAME')

    @staticmethod
    def get_db_user():
        return os.getenv('DB_USERNAME')

    @staticmethod
    def get_db_password():
        return os.getenv('DB_PASSWORD')

    @staticmethod
    def get_kafka_bootstrap_servers():
        return os.getenv('KAFKA_BOOTSTRAP_SERVERS')

    @staticmethod
    def get_kafka_topic():
        return os.getenv('KAFKA_TOPIC')

    @staticmethod
    def get_vault_url():
        return os.getenv('VAULT_URL')

    @staticmethod
    def get_vault_token():
        return os.getenv('VAULT_TOKEN')

    @staticmethod
    def get_vault_backend_path():
        return os.getenv('VAULT_BACKEND_PATH')

    @staticmethod
    def get_encryption_key_name():
        return os.getenv('VAULT_ENCRYPTION_KEY_NAME')
