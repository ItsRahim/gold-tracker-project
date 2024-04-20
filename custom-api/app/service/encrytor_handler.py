import os
from app.config.logging import log
from cryptography.fernet import Fernet
key_file_path = os.path.join("app", "resources", "encryption_key.txt")


def save_key_to_file(key):
    with open(key_file_path, "wb") as key_file:
        key_file.write(key)


def load_key_from_file():
    if os.path.exists(key_file_path):
        with open(key_file_path, "rb") as key_file:
            return key_file.read()
    else:
        return None


class EncryptionHandler:

    def __init__(self, key=None):
        if key is None:
            self.key = load_key_from_file()
            if not self.key:
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
