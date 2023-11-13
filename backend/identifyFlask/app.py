from flask import Flask, jsonify, request
from flask_cors import CORS
from werkzeug.utils import secure_filename
from include.model.identifyLow import identify_test, identify_test_noshow
from service.makeDB import make_database

# 플라스크 객체 인스턴스 생성
app = Flask(__name__)

# CORS 설정. 웹 애플리케이션에서 Cross-Origin 요청에서 자격 증명을 허용하도록 설정하는 부분.
CORS(app, resources={r"/*": {"origins": "*"}})
app.config["CORS_SUPPORTS_CREDENTIALS"] = True

# URL 접두사
PREFIX = "/identify"


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


# @app.route(PREFIX + '/star', methods=['GET'])
# def identify_stars():  # put application's code here
#     result = identify_test()
#     return result

# 사진 업로드 테스트
@app.route(PREFIX + '/upload', methods=['GET', 'POST'])
def upload_file():  # put application's code here
    if request.method == 'POST':
        file = request.files['file']
        formdata = request.form
        file.save("./static/" + secure_filename(file.filename))
    return "done"


# 테트라 테스트 처리후 사진 띄움
@app.route(PREFIX + '/tetratest', methods=['GET', 'POST'])
def tetra_test():  # put application's code here
    print("tetra test")
    global result
    if request.method == 'POST':
        print("POST")
        file = request.files['file']
        print("recieved file")
        # 파일 인식
        result = identify_test(file)
        # result = identify_test_noshow(file)

    return result


# 테트라 테스트 처리후 사진 안띄움
@app.route(PREFIX + '/tetratest2', methods=['GET', 'POST'])
def tetra_test_noshow():  # put application's code here
    print("tetra test")
    global result
    if request.method == 'POST':
        print("POST")
        file = request.files['file']
        print("recieved file")
        # 파일 인식
        # result = identify_test(file)
        result = identify_test_noshow(file)

    return result


@app.route(PREFIX + '/makedb', methods=['GET'])
def tetra_make_database():  # put application's code here
    make_database()
    return "made database"


if __name__ == '__main__':
    app.run()
