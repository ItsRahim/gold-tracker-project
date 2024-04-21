from fastapi import APIRouter, HTTPException

from app.util.encryptor import EncryptionHandler

encryptor_router = APIRouter()

encryptor = EncryptionHandler()


@encryptor_router.post("/encrypt")
async def encrypt_keys(data: dict[str, str]) -> dict[str, str]:
    encrypted_data = {}
    for key, value in data.items():
        try:
            encrypted_value = encryptor.encrypt_value(value)
            encrypted_data[key] = encrypted_value
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Encryption failed for key '{key}': {str(e)}")
    return encrypted_data


@encryptor_router.post("/decrypt")
async def decrypt_keys(data: dict[str, str]) -> dict[str, str]:
    decrypted_data = {}
    for key, value in data.items():
        try:
            decrypted_value = encryptor.decrypt_value(value)
            decrypted_data[key] = decrypted_value
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Decryption failed for key '{key}': {str(e)}")
    return decrypted_data
