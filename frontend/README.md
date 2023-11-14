# Stellargram 설명문 for Developers

## API KEY 설정

1. local.properties에 `MAPS_API_KEY={GoogleMapApiKey}` 추가 (KEY값은 담당자 문의)

## Kakao sdk 사용권한 설정

1. 새 작업환경에서 디버깅시 logcat열고 keyhash값 복사해서 담당자에게 전달

## SharedPreference 사용 (String)
1. 저장 - StellargramApplication.prefs.setString(KEY,VALUE) 
2. 열람 - StellargramApplication.prefs.getString(KEY,DEFAULT)

- 로그인 절차 이후 저장된 목록
  memberId
  nickname
  profileImageUrl
  followCount
  followingCount
  cardCount


### 2023.11.13.

