from fastapi import APIRouter

from app.util.encryptor import EncryptionHandler

encryptor_router = APIRouter()

encryptor = EncryptionHandler()


@encryptor_router.post("/encrypt")
async def encrypt_keys(data: dict[str, str]) -> dict[str, str]:

    encrypted_data = {}
    for key, value in data.items():
        encrypted_value = encryptor.encrypt_value(value)
        encrypted_data[key] = encrypted_value

    return encrypted_data


@encryptor_router.post("/decrypt")
async def encrypt_keys(data: dict[str, str]) -> dict[str, str]:
    decrypted_data = {}
    for key, value in data.items():
        decrypted_value = encryptor.decrypt_value(value)
        decrypted_data[key] = decrypted_value

    return decrypted_data
