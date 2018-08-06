# Core Permissions API Demo
This is a frontend demo for the Core Permissions API that uses Flask and Python 3.

## Usage
1. Copy configuration_example.py to configuration.py and make any necessary changes.
2. Install requirements:
```bash
$ pip3 install -r requirements.txt
```
3. Set Flask environment:
```bash
$ export FLASK_ENV=development
```
4. Start the app on http://localhost:5000:
```bash
$ python3 app.py
```

## Docker
The demo may be run in a Docker container:
```bash
$ docker build -t core-permissions-demo .

$ docker run -d \
      -p 5000:5000 \
      -v "$PWD"/configuration.py:/usr/src/app/configuration.py:ro \
      --name core-permissions-demo \
      core-permissions-demo
```
