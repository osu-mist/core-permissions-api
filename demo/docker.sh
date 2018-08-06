docker build -t core-permissions-demo .

docker run -d -it --rm \
    -p 5000:5000 \
    -v "$PWD"/configuration.py:/usr/src/app/configuration.py:ro \
    --name core-permissions-demo \
    core-permissions-demo
