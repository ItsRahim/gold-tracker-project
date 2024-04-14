import yaml


def load_config(element: str) -> str:
    with open('app/resources/app_config.yaml', 'r') as config_file:
        return yaml.safe_load(config_file)[element]


def load_vault_config(element: str) -> str:
    with open('app/resources/vault_config.yaml', 'r') as vault_config:
        return yaml.safe_load(vault_config)[element]
