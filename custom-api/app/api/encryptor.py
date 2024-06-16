from fastapi import APIRouter, HTTPException
from app.config.load_config import Config
from app.util.encryptor import EncryptionHandler
from app.config.logging import log

encryptor_router = APIRouter()

DEPLOYMENT_TYPE = Config.get_deployment_type()

if DEPLOYMENT_TYPE == "local":
    encryptor = EncryptionHandler()
else:
    encryptor = None


@encryptor_router.post("/encrypt")
async def encrypt_keys(data: dict[str, str]) -> dict[str, str]:
    if not encryptor:
        raise HTTPException(status_code=400, detail="Encryption service is not available in this deployment type.")

    encrypted_data = {}
    for key, value in data.items():
        try:
            encrypted_value = encryptor.encrypt_value(value)
            encrypted_data[key] = encrypted_value
        except Exception as e:
            log.error(f"Encryption failed for key '{key}': {e}")
            raise HTTPException(status_code=500, detail=f"Encryption failed for key '{key}': {str(e)}")

    return encrypted_data


@encryptor_router.post("/decrypt")
async def decrypt_keys(data: dict[str, str]) -> dict[str, str]:
    if not encryptor:
        raise HTTPException(status_code=400, detail="Decryption service is not available in this deployment type.")

    decrypted_data = {}
    for key, value in data.items():
        try:
            decrypted_value = encryptor.decrypt_value(value)
            decrypted_data[key] = decrypted_value
        except Exception as e:
            log.error(f"Decryption failed for key '{key}': {e}")
            raise HTTPException(status_code=500, detail=f"Decryption failed for key '{key}': {str(e)}")

    return decrypted_data
