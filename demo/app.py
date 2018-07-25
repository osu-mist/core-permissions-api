import requests
import json
from flask import Flask
from flask import request
from flask import render_template
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
    try:
        session.get(app.config["API_URL"])
    except ConnectionError:
        return render_template("index.html", data=None, queried=None,
                               responded=False)
    queried = False
    data = None
    if request.method == "POST":
        queried = True
        data = get_permissions_data(request.form)
    return render_template("index.html", data=data, queried=queried,
                           responded=True)


def get_permissions_data(form):
    # Try ONID
    res = session.get(
            url="{}/core-permissions".format(app.config["API_URL"]),
            params={"username": form["search"]}
        )
    assert res.status_code == 200
    if not res.json()["data"]:
        # Try ID
        res = session.get(
                url="{}/core-permissions".format(app.config["API_URL"]),
                params={"id": form["search"]}
            )
        assert res.status_code == 200
    # Return data even if empty
    return res.json()["data"]

global session
init_session()
