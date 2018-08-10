import os
# Configuration
API_URL = "https://localhost:8080/api/v1"
USE_BASIC_AUTH = False
SECRET_KEY = os.urandom(32)

TOKEN_API_URL = "https://api.oregonstate.edu/oauth2/token"
CLIENT_ID = "client_id"
CLIENT_SECRET = "client_secret"

AUTH_USERNAME = "username"
AUTH_PASSWORD = "password"

CAS_URL = "https://login.oregonstate.edu/cas-dev"
AUTHORIZED_USERS = ["smithj"]
