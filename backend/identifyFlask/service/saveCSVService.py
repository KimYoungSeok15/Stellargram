import os
import sqlite3
import csv


def create_star_table():
    # SQLite 연결
    conn = sqlite3.connect('stars.db')
    cursor = conn.cursor()

    #기존 테이블 삭제
    cursor.execute('''
    DROP TABLE IF EXISTS stars_info
    ''')

    # 테이블 생성
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS stars_info (
            id INTEGER,
            hipid INTEGER,
            ra REAL,
            dec REAL,
            mag REAL,
            absmag REAL,
            rarad REAL,
            decrad REAL,
            con TEXT
        )
    ''')

    # 커밋 및 연결 종료
    conn.commit()
    conn.close()


# csv 데이터 넣기
def input_csv_data():
    # # SQLite 데이터베이스 연결
    # conn = sqlite3.connect('stars.db')
    # cursor = conn.cursor()
    #
    # cursor.execute('''SELECT COUNT(*) FROM stars_info''')
    #
    # # 현재 스크립트의 디렉토리를 기준으로 상대 경로 해석
    # current_directory = os.path.dirname(os.path.realpath(__file__))
    # file_path = os.path.join(current_directory, '../include/data/hyg_v37.csv')
    #
    # row_count = cursor.fetchone()[0]
    #
    # # 행이 비어있으면
    # if row_count==0:
    #     # CSV 파일에서 데이터 읽어오기
    #     with open(file_path, 'r') as csvfile:
    #         csvreader = csv.reader(csvfile)
    #         # 첫 번째 행 헤더이므로 건너뛰기
    #         next(csvreader)
    #         # for row in csvreader:
    #         #     cursor.execute('''
    #         #         INSERT INTO stars_info (id, hipid, ra, dec,mag,absmag, rarad, decrad , con)
    #         #         VALUES (?, ?,?, ?,?,?,?,?,?)
    #         #     ''', (row[0],row[1], row[7], row[8], row[13], row[14], row[23], row[24], row[29]))
    #
    # # 커밋 및 연결 종료
    # conn.commit()
    # conn.close()
    return