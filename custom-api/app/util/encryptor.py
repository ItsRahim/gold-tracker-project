import os
import sys
from datetime import datetime
from cryptography.fernet import Fernet
from app.config.logging import log

from app.util.vault import Hvac


def delete_existing_key():
    dirname = os.path.join("app", "keys")
    for filename in os.listdir(dirname):
        root, ext = os.path.splitext(filename)
        if root.startswith('encryption') and ext == '.txt':
            os.remove(os.path.join(dirname, filename))
            log.debug(f"Deleted existing key file: {filename}")


def generate_and_save_key():
    delete_existing_key()
    key = Fernet.generate_key()
    now = datetime.now().strftime('%Y-%m-%d_%H_%M_%S')
    key_file_name = f"encryption_key_{now}.txt"
    key_file_path = os.path.join("app", "keys", key_file_name)
    with open(key_file_path, "wb") as key_file:
        key_file.write(key)
    log.debug(f"Generated and saved new key to file path {key_file_path}")
    sys.exit(f"Add key to vault as: 'gold-api-encryption-key' then encrypt DB username & password before restarting")


def load_key_from_vault():
    hvac_instance = Hvac()
    key_name = "gold-api-encryption-key"
    value = hvac_instance.read_secret(key_name)
    if value is None:
        log.error("Key not found in vault")
    else:
        delete_existing_key()
        return value


class EncryptionHandler:
    def __init__(self):
        self.key = load_key_from_vault() or generate_and_save_key()
        self.cipher_suite = Fernet(self.key)

    def encrypt_value(self, value):
        encrypted_value = self.cipher_suite.encrypt(value.encode())
        return encrypted_value

    def decrypt_value(self, encrypted_value):
        try:
            decrypted_value_bytes = self.cipher_suite.decrypt(encrypted_value)
            decrypted_value = decrypted_value_bytes.decode('utf-8')
            return decrypted_value
        except Exception as e:
            log.error("Decryption error:", e)
            return None
