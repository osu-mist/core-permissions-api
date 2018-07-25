import requests
from flask import Flask
from flask import request


app = Flask(__name__)
app.config.from_pyfile("configuration.py")


def init_session():
    global session
    session = requests.Session()
    session.auth = (
        app.config["AUTH_USERNAME"],
        app.config["AUTH_PASSWORD"]
    )


@app.route("/", methods=["GET"])
def base():
    permissions_data = get_permissions_data()
    return render_template


def get_permissions_data(form):
    if form["osuID"]:
        res = requests.get(
            url="{}/core-permissions".format(app.config["API_URL"]),
            id=form["osuID"]
        )
    elif form["onid"]:
        res = requests.get(
            url="{}/core-permissions".format(app.config["API_URL"]),
            username=form["onid"]
        )
    else:
        return []

    assert res.status_code == 200
    return res.json()["data"]

global session
init_session()
