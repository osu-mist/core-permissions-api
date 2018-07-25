import requests
import json
from flask import Flask
from flask import request
from flask import render_template


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
    queried = False
    data = None
    if request.method == "POST":
        queried = True
        data = get_permissions_data(request.form)
    return render_template("index.html", data=data, queried=queried)


def get_permissions_data(form):
    if form["osuID"]:
        res = session.get(
            url="{}/core-permissions".format(app.config["API_URL"]),
            params={"id": form["osuID"]}
        )
        return res.json()["data"]
    elif form["onid"]:
        res = session.get(
            url="{}/core-permissions".format(app.config["API_URL"]),
            params={"username": form["onid"]}
        )
        return res.json()["data"]
    else:
        return None
    assert res.status_code == 200
    return res.json()["data"]

global session
init_session()
