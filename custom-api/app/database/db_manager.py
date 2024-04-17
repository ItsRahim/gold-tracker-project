from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker
from dotenv import load_dotenv
import os


class DatabaseManager:

    def __init__(self):
        load_dotenv()
        dbname = os.getenv('DB_NAME')
        user = os.getenv('DB_USER')
        password = os.getenv('DB_PASSWORD')
        host = os.getenv('DB_HOST')
        port = os.getenv('DB_PORT')

        connection_url = f'postgresql://{user}:{password}@{host}:{port}/{dbname}'

        self.engine = create_engine(connection_url)
        self.Session = sessionmaker(bind=self.engine)

    def execute_query(self, query, params=None):
        with self.engine.connect() as conn:
            result = conn.execute(text(query), params)
            return result.fetchall()

    def close_connection(self):
        self.engine.dispose()
