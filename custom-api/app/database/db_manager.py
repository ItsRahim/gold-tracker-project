import psycopg2
from psycopg2 import sql
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

        self.connection = psycopg2.connect(
            dbname=dbname,
            user=user,
            password=password,
            host=host,
            port=port
        )

    def execute_query(self, query, params=None):
        cursor = self.connection.cursor()
        if params is not None:
            cursor.execute(query, params)
        else:
            cursor.execute(query)
        result = cursor.fetchall()
        cursor.close()

        return result

    def execute_prepared_query(self, query, params):
        cursor = self.connection.cursor()
        prepared_query = sql.SQL(query).format(
            *[sql.Identifier(param) for param in params.keys()]
        )
        cursor.execute(prepared_query, list(params.values()))
        result = cursor.fetchall()
        cursor.close()

        return result

    def close_connection(self):
        self.connection.close()
