FROM python:3.9

COPY . /app
WORKDIR /app

RUN apt-get update
RUN apt-get -y install libgl1-mesa-glx

RUN pip3 install -r requirements.txt

RUN cp ./data/hip_main.dat /usr/local/lib/python3.9/site-packages/tetra3/hip_main.dat

RUN python3 create_database.py

EXPOSE 5000
#CMD ["python3", "-m", "flask", "run", "--host=0.0.0.0"]
CMD ["gunicorn", "app:app", "-w", "4", "--bind", "0.0.0.0:5000"]
