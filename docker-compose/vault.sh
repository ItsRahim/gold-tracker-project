# Setting Vault Env Variable
export VAULT_ADDR='http://0.0.0.0:8200'

# Login to Vault
vault login dev_token

# Create new secrets path
vault secrets enable -path=kv kv

# Adding secrets
vault kv put kv/config-server \
    gold-api-encryption-key=b67a5e0887d90c3b6749b486f5586e54 \
	encryption.key = \
	salt.key=da95bc4003b551c6bf0d65a7d2b4bfc543eea89b55c0197bd7c7cb8d4f9912c4