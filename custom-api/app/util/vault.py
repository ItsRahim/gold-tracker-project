import hvac
from hvac.exceptions import Unauthorized, InvalidPath

from app.config.load_config import load_config
from app.config.logging import log

config = load_config('vault')


class Hvac:
    def __init__(self):
        self.url = config['url']
        self.token = config['token']
        self.path = config['backend-path']
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
                log.error(f"Key '{key}' not found in Vault")
                return None
            else:
                data = response['data']['data']
                if key in data:
                    return data[key]
                else:
                    log.error(f"Key '{key}' not found in Vault data")
                    return None
        except InvalidPath as e:
            log.error(f"Invalid path: {e}")
            return None

