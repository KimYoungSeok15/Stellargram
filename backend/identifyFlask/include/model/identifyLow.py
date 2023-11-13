from flask import send_file
from PIL import Image

import io
import tetra3

tetraModule = tetra3.Tetra3()


def identify_test(imageIn):
    print("method identify")
    # 들어온 이미지를 PIL 이미지로 변환
    image_byte = imageIn.read()
    image_stream = io.BytesIO(image_byte)
    pil_image = Image.open(image_stream)
    # 식별
    # result = tetraModule.solve_from_image(image=pil_image, return_matches=True)
    result = tetraModule.solve_from_image(image=pil_image, return_matches=True, return_visual=True)

    # # 식별 결과에서 인식 표시된 PIL이미지 분리
    pil_image_identified = result.get('visual')
    result.pop('visual')
    pil_image_identified.show()

    # # 표시된 PIL을 jpg로 변환 시작
    # # 데이터 스트림 저장할 객체
    # image_stream_identified = io.BytesIO()
    # pil_image_identified.save(image_stream_identified, format='JPEG')

    return result

    # return send_file(image_stream_identified, mimetype='image/jpeg')

def identify_test_noshow(imageIn):
    print("method identify")
    # 들어온 이미지를 PIL 이미지로 변환
    image_byte = imageIn.read()
    image_stream = io.BytesIO(image_byte)
    pil_image = Image.open(image_stream)
    # 식별
    result = tetraModule.solve_from_image(image=pil_image, return_matches=True)
    # result = tetraModule.solve_from_image(image=pil_image, return_matches=True, return_visual=True)

    return result
