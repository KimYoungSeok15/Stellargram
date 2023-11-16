# SSAFY 9기 자율 프로젝트 - 천구 투영 및 별사진 인식 앱 Stellargram

### 별들을 더 가까이, 별들을 소장하고 싶은 사람들을 위하여


## 팀원 소개

| NAME | ROLE            | EMAIL                    |
|------|-----------------|--------------------------|
| 김소희  | Leader, Backend | livre6491@gmail.com                         |
| 김수현  | Backend, Infra  | ppsracchriskim@gmail.com |
| 허재   | Backend         | huhwo00@gmail.com                         |
| 오정빈  | Backend         | ohjb93@gmail.com                         |
| 이현도  | Frontend        |  hyundo1995@gmail.com                        |
| 김영석  | Frontend        | kimyoungseok15@gmail.com                         |

## ✨ 기획 배경
### 1. 기존 어플리케이션이 천체 관측에 대한 진입장벽을 낮춰주지 못 함.
- 기존의 어플리케이션은 대다수 사용자가 천체 관측을 진행할 수 있도록 도와주는 가이드 기능이 부재되어 있음.
### 2. 고가의 천체관측기기
- 현재 망원경, DSLR 등을 구매하려고 하면 수십만원의 고비용이 발생함. 그러나 대다수 사용자가 보유한 스마트폰으로 양질의 천체 사진을 촬영할 수 있음. 
### 3. 정보 공유를 위한 커뮤니티의 부재
- 천체 관측을 하기 위해서는 많은 가이드가 필요하나, 이러한 정보를 얻을 수 있는 커뮤니티가 부족함.

## 📆 프로젝트 소개
* **프로젝트명**

  별들을 더 가까이, 별들을 소장하고 싶은 사람들을 위한 앱 Stellargram

* **서비스 특징** 

  * C++ NDK를 이용한 부드러운 3D 천체 투영 구현.
  * CameraX 및 Camera2 기능을 활용한 천체 사진용 인앱 카메라 제공.
  * Google Maps를 이용한 관측 포인트 정보 제공과 웹소켓과 Redis Pub/Sub을 이용한 관측포인트 당 채팅 기능 구현.

* **UCC 영상**

  * 

## 🙌주요 기능
### 1. 천체 투영기능
다양한 조작을 통해 원하는 시각, 배율, 방향에 따른 하늘을 투영해줌.

- 스크롤을 통한 방향 전환, 줌인 줌아웃을 통한 배율을 조정할 수 있음.
- 하단의 시간 조절 탭을 이용해 원하는 시각의 하늘을 투영시킬 수 있음.
- 검색 탭을 이용해 원하는 천체를 검색해 볼 수 있음. 

### 2. 천체 사진 기능
사용자에게 천체 사진을 촬영할 수 있도록 안내를 제공하고 직접 촬영할 수 있도록 해 줌. 

- ISO, 노출 시간, WB(White Balance) 조정을 제공해주고 하단의 안내 문구를 통해 사용자가 간단하게 천체 사진을 촬영할 수 있도록 해 줌.
- 촬영한 사진에 어떠한 천체가 포함되어 있는지 확인할 수 있음.
- 사진에 별자리 표시 등 커스텀 설정을 제공하여 천체 사진을 더욱 이쁘게 꾸밀 수 있음.
- 촬영한 사진을 다양한 사용자와 공유할 수 있음. 

### 3. 천체 관측소 정보 공유
사용자에게 천체 관측에 적합한 장소 정보를 제공해주고, 다양한 사용자와 정보 공유를 할 수 있는 채팅 기능을 제공해 줌.

- Google Maps에 다양한 관측지를 망원경 모양의 아이콘으로 표시해주고 누를 시 상세 정보와 함께 채팅에 입장할 수 있도록 함.
- 길게 누르기와 버튼 선택이라는 행동을 통해 간단하게 관측 포인트 등록을 할 수 있고, 별점 및 코멘트를 통해 관측지에 대한 평가를 진행할 수 있음. 
- 채팅 기능을 통해 다양한 사용자와 해당 관측지에 대한 정보를 공유할 수 있음. 

# 📄 포팅 매뉴얼
어플리케이션 상세 화면 구성 및 개발환경, 빌드에 관련된 정보는 모두 포팅 매뉴얼 참조

- 포팅 매뉴얼 URL : [A101 포팅 매뉴얼](www.naver.com)

# 💁 설계
- [🧱 서비스아키텍쳐](https://potent-light-313.notion.site/60bdd82c4c284937828c88f02d6b705b?pvs=4)
  
- [📱 와이어프레임](https://www.figma.com/file/8HSSYFTxHEk9PhSd8cHjbh/Stellargram?type=design&node-id=0-1&mode=design)
  
- [🎨 ERD](https://potent-light-313.notion.site/ERD-7b2964c873e14f5ca58acee060f8f0a4?pvs=4)
  
- [📬 API 명세서](https://potent-light-313.notion.site/API-10bb1c1e8cbd40e9b7701a2e90bad9d8?pvs=4)
  
- [📋 컨벤션](https://potent-light-313.notion.site/Convention-bdade896368546fe8ae8f57ac4e02a4b?pvs=4)  


# 향후 계획

### 1. 천체 투영기능
- 은하수 사진 및 다양한 딥스카이 사진을 추가하여 더욱 생동감 있고 사실적인 화면 구현
- SIMD 알고리즘 및 분산 연산 기술 등을 활용하여 더욱 많은 양의 데이터를 부드럽게 보여줄 수 있게 구현
- KD-Tree를 활용하여 연산량을 줄여 하드웨어 부하를 줄이고 앱 안정성을 높임.

### 2. 천체 사진 기능
- 필터 기능을 구현하여 더욱 생동감 있고 사진 보정 기능 구현
- 더욱 빠른 알고리즘을 통한 사진 속 천체 인식 기능 최적화
- 일주 사진 및 다양한 사진 기능 추가
- 일반 천체 장비에 대한 정보 공유 기능 추가

### 3. 천체 관측소 정보 공유
- 전세계 관측소 공유 기능 추가
- 다양한 채팅 기능 추가

# ⚙ 개발 환경 및 IDE

### Frontend  
> ![Jetpack compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![CMAKE](https://img.shields.io/badge/Cmake-064F8C?style=for-the-badge&logo=cmake&logoColor=white)
![Android](https://img.shields.io/badge/android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Android Studio](https://img.shields.io/badge/android_studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white)
![C++](https://img.shields.io/badge/C++-00599C?style=for-the-badge&logo=cplusplus&logoColor=white)
![NDK](https://img.shields.io/badge/ndk-%23593d88.svg?style=for-the-badge&logo=android&logoColor=white)
![Room](https://img.shields.io/badge/room-%23593d88.svg?style=for-the-badge&logo=android&logoColor=white)
![KaKao OAuth](https://img.shields.io/badge/kakao_oauth-ffcd00?style=for-the-badge&logo=kakao&logoColor=white)

### Backend
> ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://camo.githubusercontent.com/d04cac57f1f6d0a39ebd084333a6e4d93081a42c9e5aa1b3b511e75ad1a1e20f/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f537072696e675f426f6f742d3644423333463f7374796c653d666f722d7468652d6261646765266c6f676f3d537072696e67426f6f74266c6f676f436f6c6f723d7768697465)
![Gradle](https://camo.githubusercontent.com/ce0bfcaef68659861b497d6dfc5b8b783c2955705472b4e55151f196347d9271/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f477261646c652d4633373434303f7374796c653d666f722d7468652d6261646765266c6f676f3d477261646c65266c6f676f436f6c6f723d7768697465)
![Maria DB](https://img.shields.io/badge/mariadb-003545?style=for-the-badge&logo=mariadb&logoColor=#003545)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![IntelliJ](https://camo.githubusercontent.com/d479352761a86806b779129f4be8909d1c8c1fb1e2805bbd86cacd276f831cfa/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f496e74656c6c696a5f494445412d3337373641423f7374796c653d666f722d7468652d6261646765266c6f676f3d496e74656c6c696a49444541266c6f676f436f6c6f723d7768697465)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Amazon api gateway](https://img.shields.io/badge/amazon_api_gateway-FF4F8B?style=for-the-badge&logo=amazonapigateway&logoColor=white)
![Kafka](https://img.shields.io/badge/apache_kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![Netflix Eureka](https://img.shields.io/badge/netflix_eureka-E50914?style=for-the-badge&logo=netflix&logoColor=white)
![Python](https://img.shields.io/badge/python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![Flask](https://img.shields.io/badge/flask-000000?style=for-the-badge&logo=flask&logoColor=white)

### Server
> ![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Ubuntu](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Jenkins](https://camo.githubusercontent.com/9cadc6063746e385b3916ea6ee991ec8eea88306de9208ccf5a94111c0ddf6ee/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4a656e6b696e732d4432343933393f7374796c653d666f722d7468652d6261646765266c6f676f3d4a656e6b696e73266c6f676f436f6c6f723d7768697465)

### Team Work
> ![Mattermost](https://camo.githubusercontent.com/04ce7d705b23f2f899a4acd58de46152ea9ab352ce310182432c59db1bd3e74e/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4d61747465724d4f53542d3030393638383f7374796c653d666f722d7468652d6261646765266c6f676f3d4d61747465726d6f7374266c6f676f436f6c6f723d7768697465)
![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitLab](https://camo.githubusercontent.com/cb99570e6da369466c9991c5400a2516cec86a958fc80bc152dcdc020b5e581f/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6769746c61622d4643364432363f7374796c653d666f722d7468652d6261646765266c6f676f3d4769744c6162266c6f676f436f6c6f723d7768697465)
![Notion](https://camo.githubusercontent.com/e6016a8747f69a4f7c5cac44f04f81136a1294f2973f25a8d4c53651337a3d78/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4e6f74696f6e2d4546313937303f7374796c653d666f722d7468652d6261646765266c6f676f3d4e6f74696f6e266c6f676f436f6c6f723d7768697465)
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)