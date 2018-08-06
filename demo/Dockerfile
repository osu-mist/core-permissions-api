FROM python:3

WORKDIR /usr/src/app

COPY . .

RUN pip3 install -r requirements.txt
RUN export 

ENTRYPOINT [ "python3" ]
CMD [ "app.py" ]
