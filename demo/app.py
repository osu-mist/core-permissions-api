import re
import sys
import xml.etree.ElementTree as elementTree

import requests
from flask import Flask, request, render_template, redirect, session
from requests.exceptions import ConnectionError

app = Flask(__name__)
app.config.from_pyfile("configuration.py")


@app.route("/", methods=["GET", "POST"])
def base():
    cas_url = app.config["CAS_URL"]
    self_url = request.url_root
    authorized_users = app.config["AUTHORIZED_USERS"]

    if request.method == "POST":
        return get_response(request.form["search"])
    elif "ticket" in request.args:
        try:
            user = validate_cas(cas_url, request.args["ticket"], self_url)
        except CASError as e:
            return "login failed {}".format(str(e)), 403
        except Exception as e:
            return "login failed", 403
        session["user"] = user
        return redirect(request.path)
    elif "user" not in session:
        return redirect(cas_url + "/login?service=" + self_url)
    if session["user"] not in authorized_users:
        return render_template("index.html", unauthorized=True), 403
    else:
        return render_template("index.html")


@app.route("/logout", methods=['GET'])
def logout():
    if 'user' in session:
        del session['user']
    return redirect(app.config['CAS_URL'] + "/logout")


@app.errorhandler(ConnectionError)
def handle_connection_error(e):
    return render_template("index.html", alert="connection-error.html")


@app.errorhandler(AssertionError)
def handle_internal_server_error(e):
    return render_template("index.html", alert="server-error.html")


class CASError(Exception):
    def __init__(self, msg, code=''):
        self.msg = msg
        self.code = code

    def __str__(self):
        if self.code and self.msg:
            return str(self.msg) + " (" + str(self.code) + ")"
        if self.msg:
            return str(self.msg)
        return str(self.code)


def validate_cas(cas_url, ticket, service):
    r = requests.get(cas_url+'/serviceValidate',
                     params={'ticket': ticket, 'service': service})

    if r.status_code != 200:
        raise CASError('invalid response')

    # note: processing xml from untrusted sources is unsafe
    # https://docs.python.org/2/library/xml.html#xml-vulnerabilities
    try:
        root = elementTree.fromstring(r.text)
    except elementTree.ParseError:
        raise CASError('invalid response')

    namespace = '{http://www.yale.edu/tp/cas}'
    if root.tag != namespace+'serviceResponse':
        raise CASError('invalid response')

    authentication_failure = root.find(namespace+'authenticationFailure')
    authentication_success = root.find(namespace+'authenticationSuccess')

    if authentication_failure is not None:
        raise CASError(authentication_failure.text.strip(),
                       authentication_failure.get('code'))

    if authentication_success is not None:
        user = authentication_success.find(namespace+'user')
        return user.text

    raise CASError('invalid response')


def get_response(query):
    # Try ONID
    if onid_regex.match(query):
        res = py_session.get(
            url="{}/core-permissions".format(app.config["API_URL"]),
            params={"username": query}
        )
        if token_needs_refresh(res):
            return get_response(query)
    # Try ID
    elif osuID_regex.match(query):
        res = py_session.get(
                url="{}/core-permissions".format(app.config["API_URL"]),
                params={"id": query}
            )
        if token_needs_refresh(res):
            return get_response(query)
    # Otherwise, input is invalid
    else:
        return render_template("index.html", alert="invalid-input.html")

    data = res.json()["data"]
    if data:
        return render_template("index.html", data=data)
    else:
        return render_template("index.html", alert="no-credentials.html")


def token_needs_refresh(res):
    if res.status_code == 401:
        py_session.headers = get_oauth2_headers()
        retry_res = py_session.get(
            url="{}/core-permissions".format(app.config["API_URL"]),
            params={}
        )
        # Even though query is bad, a 400 will confirm that the token is valid
        assert retry_res.status_code == 400
        return True
    else:
        assert res.status_code == 200
        return False


def init_py_session():
    global py_session
    py_session = requests.Session()
    if app.config["USE_BASIC_AUTH"]:
        py_session.auth = (
            app.config["AUTH_USERNAME"], app.config["AUTH_PASSWORD"]
        )
        py_session.verify = False
    else:
        py_session.headers = get_oauth2_headers()


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
    return {"Authorization": "Bearer {}".format(res.json()[token])}


onid_regex = re.compile("[a-zA-Z]")
osuID_regex = re.compile("\d{9}")
init_py_session()

if __name__ == "__main__":
    app.run(host="0.0.0.0")
