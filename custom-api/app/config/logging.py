import os
import logging

LOG_DIR = os.path.join(os.getcwd(), '/app/logs')
LOG_FILE = os.path.join(LOG_DIR, 'gold-scraping-api.log')

print(LOG_DIR)

def configure_logging():
    if not os.path.exists(LOG_DIR):
        os.makedirs(LOG_DIR)

    logger = logging.getLogger()
    logger.setLevel(logging.DEBUG)

    file_handler = logging.FileHandler(LOG_FILE)
    file_handler.setLevel(logging.INFO)

    formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s', datefmt='%Y-%m-%d %H:%M:%S')

    file_handler.setFormatter(formatter)

    logger.addHandler(file_handler)

    return logger


log = configure_logging()