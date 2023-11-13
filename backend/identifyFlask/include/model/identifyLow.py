from flask import send_file
from PIL import Image
import numpy

import io
import tetra3

from service.starsSearchSearvice import findByRaDec, findAllStarsByRaDec

tetraModule = tetra3.Tetra3(load_database='default_database')


def identify_with_tetra(imageIn):
    # 인식결과 저장할 변수 글로벌 선언
    identified_star = None

    # # 들어온 이미지를 PIL 이미지로 변환
    # image_byte = imageIn.read()
    # image_stream = io.BytesIO(image_byte)
    # pil_image = Image.open(image_stream)

    pil_image = imageIn

    # 식별
    result = tetraModule.solve_from_image(image=pil_image, return_matches=True, return_visual=True)
    # result = tetraModule.solve_from_image(image=pil_image, return_matches=True)
    # result = tetraModule.solve_from_image(image=pil_image, pattern_checking_stars=4, return_matches=True,
    #                                       return_visual=True)

    # 식별 성공해서 이미지가 생성됐다면 결과 매칭 시작
    if (result.get('visual')):
        # 식별 결과에서 인식 표시된 PIL이미지 분리
        pil_image_identified = result.pop('visual')

        # TODO: 로컬 확인용 사진띄우기
        pil_image_identified.show()

        # 인식된 전체 좌표에 대해 찾아서 병합 후 반환
        matched_stars = result.get("matched_stars")
        matched_centroids = result.get('matched_centroids')
        identified_star = findAllStarsByRaDec(matched_stars=matched_stars, matched_centroids=matched_centroids)

    # print(result)

    if identified_star is not None and len(identified_star) != 0:
        result['matched'] = identified_star
        result.pop("matched_stars")
        result.pop("matched_centroids")
    else:
        result['matched'] = None

    return result

# # 현재 사용하지 않음
# def identify_test_noshow(imageIn):
#
#     # 들어온 이미지를 PIL 이미지로 변환
#     image_byte = imageIn.read()
#     image_stream = io.BytesIO(image_byte)
#     pil_image = Image.open(image_stream)
#
#     # 식별
#     result = tetraModule.solve_from_image(image = pil_image, return_matches = True)
#     # result = tetraModule.solve_from_image(image=pil_image, return_matches=True, return_visual=True)
#
#     return result
