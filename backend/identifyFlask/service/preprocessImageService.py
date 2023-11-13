from PIL import Image
import cv2
import numpy as np


# 이미지 채널이 1개(흑백) 3개(RGB)가 아니라 4개(RGBA)라면 변환 후 반환
def preprocess_image(image_in):
    # 파일 객체를 OpenCV가 인식할 수 있는 형태로 변환
    image_stream = image_in.stream
    image_np = np.frombuffer(image_stream.read(), dtype=np.uint8)
    image_cv2 = cv2.imdecode(image_np, cv2.IMREAD_UNCHANGED)

    # image 채널에 따라 변환 혹은 그대로 두기
    if image_cv2.shape[2] not in (1, 3):
        print("shape2")
        print(image_cv2.shape[2])
        processed = cv2.cvtColor(image_cv2, cv2.COLOR_RGBA2RGB)
    else:
        processed = image_cv2

    # openCV로 처리한 걸 pil image로 변환
    processed_rgb = cv2.cvtColor(processed, cv2.COLOR_BGR2RGB)
    pil_image = Image.fromarray(processed_rgb)

    return pil_image
