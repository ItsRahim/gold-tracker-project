from contextlib import contextmanager

from sqlalchemy import create_engine, text
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import sessionmaker

from app.config.load_config import load_config
from app.config.logging import log
from app.util.encryptor import EncryptionHandler

config = load_config('database')

encryption_handler = EncryptionHandler()


def get_credentials(encrypted_value:  str) -> str:
    return encryption_handler.decrypt_value(encrypted_value)


class DatabaseManager:

    def __init__(self):
        dbname = config['name']
        host = config['host']
        port = config['port']
        user = None
        password = None

        if 'username' in config and 'password' in config:
            user = get_credentials(config['username'])
            password = get_credentials(config['password'])

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

