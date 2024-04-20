import os
from contextlib import contextmanager

from dotenv import load_dotenv
from sqlalchemy import create_engine, text
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import sessionmaker

from app.config.logging import log


class DatabaseManager:

    def __init__(self):
        load_dotenv()
        dbname = os.getenv('DB_NAME')
        user = os.getenv('DB_USER')
        password = os.getenv('DB_PASSWORD')
        host = os.getenv('DB_HOST')
        port = os.getenv('DB_PORT')

        connection_url = f'postgresql://{user}:{password}@{host}:{port}/{dbname}'

        self.engine = create_engine(connection_url, pool_size=10, max_overflow=20)
        self.Session = sessionmaker(bind=self.engine)

    def execute_query(self, query, params=None):
        try:
            with self.engine.connect() as conn:
                result = conn.execute(text(query), params)
                return result.fetchall()
        except SQLAlchemyError as e:
            log.error(f"Error executing query: {query}. Error: {e}")
            raise

    @contextmanager
    def session_scope(self):
        session = self.Session()
        try:
            yield session
            session.commit()
        except Exception as e:
            session.rollback()
            log.error(f"Error during session operation: {e}")
            raise
        finally:
            session.close()

    def close_connection(self):
        self.engine.dispose()
