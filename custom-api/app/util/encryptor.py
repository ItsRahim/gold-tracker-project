import base64

from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes

from app.config.load_config import Config
from app.config.logging import log
from app.util.vault import Hvac


def load_key_from_vault():
    hvac_instance = Hvac()
    key_name = Config.get_encryption_key_name()
    value = hvac_instance.read_secret(key_name)
    if value is None:
        log.error("Key not found in vault")
    else:
        return value.encode('utf-8')


def pad_data(text):
    pad_size = AES.block_size - len(text) % AES.block_size
    return text + (chr(pad_size) * pad_size).encode()


def unpad_data(text):
    pad_size = text[-1]
    return text[:-pad_size]


class EncryptionHandler:
    def __init__(self):
        self.key = load_key_from_vault()

    def encrypt_value(self, plaintext):
        if not self.key:
            raise ValueError("No key available. Generate or retrieve a key first.")
        iv = get_random_bytes(AES.block_size)
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        padded_text = pad_data(plaintext.encode())
        encrypted_text = cipher.encrypt(padded_text)
        encrypted_data = iv + encrypted_text
        return base64.b64encode(encrypted_data).decode()

    def decrypt_value(self, encrypted_base64_text):
        if not self.key:
            raise ValueError("No key available. Generate or retrieve a key first.")
        encrypted_data = base64.b64decode(encrypted_base64_text.encode())
        iv = encrypted_data[:AES.block_size]
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        decrypted_text = cipher.decrypt(encrypted_data[AES.block_size:])
        unpadded_text = unpad_data(decrypted_text)
        return unpadded_text.decode()

