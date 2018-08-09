import json
import re
import requests
import sys
from flask import Flask, request, render_template
from requests.exceptions import ConnectionError


app = Flask(__name__)
app.config.from_pyfile("configuration.py")


@app.route("/", methods=["GET", "POST"])
def base():
    if request.method == "POST":
        return get_response(request.form["search"])
    else:
        return render_template("index.html")


@app.errorhandler(ConnectionError)
def handle_connection_error(e):
    return render_template("index.html", alert="connection-error.html")


@app.errorhandler(AssertionError)
def handle_internal_server_error(e):
    return render_template("index.html", alert="server-error.html")


def get_response(query):
    # Try ONID
    if onid_regex.match(query):
        res = session.get(
            url="{}/core-permissions".format(app.config["API_URL"]),
            params={"username": query}
        )
        assert res.status_code == 200
    # Try ID
    elif osuID_regex.match(query):
        res = session.get(
                url="{}/core-permissions".format(app.config["API_URL"]),
                params={"id": query}
            )
        assert res.status_code == 200
    # Otherwise, input is invalid
    else:
        return render_template("index.html", alert="invalid-input.html")

    data = res.json()["data"]
    if data:
        return render_template("index.html", data=data)
    else:
        return render_template("index.html", alert="no-credentials.html")


def init_session():
    global session
    session = requests.Session()
    if app.config["USE_BASIC_AUTH"]:
        session.auth = (
            app.config["AUTH_USERNAME"], app.config["AUTH_PASSWORD"]
        )
        session.verify = False
    else:
        session.headers = get_oauth2_headers()


def get_oauth2_headers():
    token = "access_token"
    data = {
        "client_id": app.config["CLIENT_ID"],
        "client_secret": app.config["CLIENT_SECRET"],
        "grant_type": "client_credentials"
    }
    res = requests.post(app.config["TOKEN_API_URL"], data=data)
    if token not in res.json():
        sys.exit("Error: invalid OAUTH2 credentials")
    return {"Authorization:": "Bearer {}".format(res.json()[token])}


onid_regex = re.compile("[a-zA-Z]")
osuID_regex = re.compile("\d{9}")
init_session()

if __name__ == "__main__":
    app.run(host="0.0.0.0")
