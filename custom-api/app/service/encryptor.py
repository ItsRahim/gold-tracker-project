import os
from datetime import datetime
from cryptography.fernet import Fernet
from app.config.logging import log
from app.util.vault import Hvac

now = datetime.now()
hvac_instance = Hvac()
key_name = "gold-api-encryption-key"
key_file_name = f"encryption_key_{now.strftime('&Y-%m-%d_%H_%M_%S')}.txt"
key_file_path = os.path.join("app", "keys", key_file_name)


def save_key_to_file(key):
    log.debug(f"Generating new key for {key_name}")
    with open(key_file_path, "wb") as key_file:
        key_file.write(key)
    log.debug(f"Saved key to file path {key_file_path}. Add to vault as: {key_name}")


def delete_existing_key():
    if os.path.exists(key_file_path):
        os.remove(key_file_path)
        log.debug(f"Deleted existing key file: {key_file_path}")


def load_key_from_vault():
    value = hvac_instance.read_secret(key_name)
    if value is None:
        log.error("Key not found in vault")
    else:
        delete_existing_key()
        return value


class EncryptionHandler:
    def __init__(self, key=None):
        if key is None:
            self.key = load_key_from_vault()
            if not self.key:
                log.warning("Key not found in vault. Encryption key not loaded")
                self.key = Fernet.generate_key()
                save_key_to_file(self.key)
        else:
            self.key = key
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
