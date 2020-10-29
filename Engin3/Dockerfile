FROM python:3.7

COPY . /Engin3
COPY requirements.txt .

RUN apt-get update
RUN apt-get install -y python
RUN pip install -r requirements.txt

ADD engin3.py /inf3995-02/Engin3/engin3.py

ENV FLASK_APP /inf3995-02/Engin3/engin3.py

ENTRYPOINT ["python", "-m", "flask", "run", "--host=0.0.0.0"]

EXPOSE 5000
