from flask import send_file
from PIL import Image
import numpy

import io
import tetra3

from service.starsSearchSearvice import findByRaDec, findAllStarsByRaDec

t3 = tetra3.Tetra3()
# t3.generate_database(max_fov=50, min_fov=0.1, star_max_magnitude=5, save_as='default_database_custom_5_max50')
tetraModule = tetra3.Tetra3(load_database='default_database_custom_5_max50')
# tetraModule = tetra3.Tetra3(load_database='default_database')

tetraModuleDefault = tetra3.Tetra3(load_database='default_database')

def identify_with_tetra_make_image(imageIn):
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

        # 로컬 확인용 사진띄우기
        # pil_image_identified.show()

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

# pil이미지로 변환된 이미지에 대해 식별, 이미지 생성 안함
def identify_with_tetra_no_image(imageIn):
    # 인식결과 저장할 변수 글로벌 선언
    identified_star = None

    pil_image = imageIn

    # 식별
    result = tetraModule.solve_from_image(image=pil_image, return_matches=True)

    # 5등급 모듈로 인식이 안되면 7등급 모듈 실행
    if result.get("matched_stars") is None:
        result = tetraModuleDefault.solve_from_image(image=pil_image, return_matches=True)

    # 식별 성공해서 이미지가 생성됐다면 결과 매칭 시작
    if result.get("matched_stars") is not None:
        # 인식된 전체 좌표에 대해 찾아서 병합 후 반환
        matched_stars = result.get("matched_stars")
        matched_centroids = result.get('matched_centroids')
        identified_star = findAllStarsByRaDec(matched_stars=matched_stars, matched_centroids=matched_centroids)

    # 식별된 별이 있다면 응답용 결과에 담기
    if identified_star is not None and len(identified_star) != 0:
        result['matched'] = identified_star
        result.pop("matched_stars")
        result.pop("matched_centroids")
    else:
        result['matched'] = None

    return result
