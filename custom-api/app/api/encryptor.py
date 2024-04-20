from fastapi import APIRouter

from app.service.encryptor import EncryptionHandler

encryptor_router = APIRouter()

encryptor = EncryptionHandler()


@encryptor_router.post("/encrypt")
async def encrypt_keys(data: dict[str, str]) -> dict[str, str]:

    encrypted_data = {}
    for k, v in data.items():
        encrypted_value = encryptor.encrypt_value(v)
        encrypted_data[k] = encrypted_value

    return encrypted_data
