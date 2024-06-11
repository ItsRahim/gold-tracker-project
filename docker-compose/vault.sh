# Setting Vault Env Variable
export VAULT_ADDR='http://0.0.0.0:8200'

# Login to Vault
vault login dev_token

# Create new secrets path
vault secrets enable -path=kv kv

# Adding secrets
vault kv put kv/config-server \
    gold.api.encryption.key=b67a5e0887d90c3b6749b486f5586e54 \
    encryption.key=dev_key \
    salt.key=dev_salt