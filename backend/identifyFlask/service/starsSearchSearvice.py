import math
import sqlite3
import numpy as np


# 좌표 1개에 대해 테스트용. 현재 사용하지 않음
def findByRaDec(rarad_in, decrad_in):
    # SQLite 연결
    conn = sqlite3.connect('stars.db')
    cursor = conn.cursor()

    # 오차 0.001 이하인 좌표 매칭
    cursor.execute('''SELECT * FROM stars_info WHERE abs(rarad-?)<0.001 AND abs(decrad-?)<0.001''',
                   (rarad_in, decrad_in))

    # TODO: 테스트
    result = cursor.fetchall()
    # for row in result:
    #     print(row)

    # 커밋 및 연결 종료
    conn.commit()
    conn.close()

    return result


# 인식된 좌표 전체에 대해 매칭 후 반환
def findAllStarsByRaDec(matched_stars, matched_centroids):
    # SQLite 연결
    conn = sqlite3.connect('stars.db')
    cursor = conn.cursor()

    # 좌표 값을 numpy 배열로 변환 후 radian 변환
    coords_array = np.array(matched_stars)
    corrds_array_rad = np.deg2rad(coords_array)

    # 검색된 별 정보 딕셔너리를 저장할 리스트
    star_found = []

    # 각 별에 대해 검색 후 리스트에 추가
    for index, star in enumerate(corrds_array_rad):
        # 별 1개에 대해 일치하는 정보 검색
        cursor.execute('''        SELECT * FROM stars_info     WHERE abs(rarad-?)<0.001 AND abs(decrad-?)<0.001    ''',
                       (star[0], star[1]))
        result = cursor.fetchone()
        # 검색 결과가 있다면 형식에 맞춰 변환 후 저장
        if result is not None:
            combined_dict = {'id': result[0],'hipid':result[1], 'mag': result[3], 'absmag': result[4], 'con': result[7],
                             'pixelx': math.trunc(matched_centroids[index][0]),
                             'pixely': math.trunc(matched_centroids[index][1])}
            star_found.append(combined_dict)

    # # TODO: 테스트
    # print("------ 전체 좌표에 대해 검색")
    # print(star_found)

    # for data in star_found:
    #     print(data)

    # 커밋 및 연결 종료
    conn.commit()
    conn.close()

    return star_found
