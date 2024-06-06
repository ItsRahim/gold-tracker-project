import hvac
from hvac.exceptions import Unauthorized, InvalidPath

from app.config.load_config import Config
from app.config.logging import log


class Hvac:
    def __init__(self):
        self.url = Config.get_vault_url()
        self.token = Config.get_vault_token()
        self.path = Config.get_vault_backend_path()
        self.client = hvac.Client(url=self.url, token=self.token)

        if not self.client.is_authenticated():
            log.error("Unable to authenticate to Vault. Incorrect token and/or url")
            raise Unauthorized("Unable to authenticate to Vault")
        else:
            log.info("Vault authenticated")

    def read_secret(self, key):
        try:
            response = self.client.read(f"{self.path}")
            if response is None or 'data' not in response:
                log.error(f"No data found in Vault at path '{self.path}'")
                return None
            else:
                data = response['data']
                if key in data:
                    return data[key]
                else:
                    log.error(f"Key '{key}' not found in Vault data")
                    return None
        except InvalidPath as e:
            log.error(f"Invalid path: {e}")
            return None
