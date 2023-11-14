FROM python:3.10

COPY . /app
RUN pip3 install -r requirements.txt -y
WORKDIR /app

CMD ["python3", "-m", "flask", "run", "--host=0.0.0.0"]