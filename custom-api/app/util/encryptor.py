from cryptography.fernet import Fernet

from app.config.logging import log
from app.util.vault import Hvac


def load_key_from_vault():
    hvac_instance = Hvac()
    key_name = "gold-api-encryption-key"
    value = hvac_instance.read_secret(key_name)
    if value is None:
        log.error("Key not found in vault")
    else:
        return value


class EncryptionHandler:
    def __init__(self):
        self.key = load_key_from_vault()
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
