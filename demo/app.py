import json
import re
import requests
from flask import Flask, request, render_template
from requests.exceptions import ConnectionError


app = Flask(__name__)
app.config.from_pyfile("configuration.py")


def init_session():
    global session
    session = requests.Session()
    session.auth = (
        app.config["AUTH_USERNAME"],
        app.config["AUTH_PASSWORD"]
    )
    session.verify = False


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


onid_regex = re.compile("[a-zA-Z]")
osuID_regex = re.compile("\d{9}")
init_session()
