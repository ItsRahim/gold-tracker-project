import os
from contextlib import contextmanager

from dotenv import load_dotenv
from pymongo import MongoClient
from pymongo.errors import PyMongoError

from app.config.logging import log


class DatabaseManager:

    def __init__(self):
        load_dotenv()
        db_host = os.getenv('DB_HOST')
        db_port = int(os.getenv('DB_PORT'))
        db_name = os.getenv('DB_NAME')
        db_user = os.getenv('DB_USER')
        db_password = os.getenv('DB_PASSWORD')

        # MongoDB connection URL
        if db_user and db_password:
            connection_url = f"mongodb://{db_user}:{db_password}@{db_host}:{db_port}/{db_name}"
        else:
            connection_url = f"mongodb://{db_host}:{db_port}/{db_name}"

        self.client = MongoClient(connection_url)
        self.db = self.client[db_name]

    def execute_query(self, collection_name, query):
        try:
            collection = self.db[collection_name]
            result = collection.find(query)
            return list(result)
        except PyMongoError as e:
            log.error(f"Error executing query: {e}")
            raise

    @contextmanager
    def session_scope(self):
        try:
            yield self.client
        except PyMongoError as e:
            log.error(f"Error during session operation: {e}")
            raise

    def close_connection(self):
        self.client.close()
