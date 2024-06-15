# OFFROADER
<!-- ![OFFROADER Logo](https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F6088bcf9-c972-45c0-a664-02ab6cfe52b4%2Ff26e141d-b052-4903-bf93-3e194bd369fd%2FUntitled.png?table=block&id=e065dac3-420e-47c9-b64b-f0d05c3c153a&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2) -->

![Slide 2](https://github.com/SpartaFinalProjectAndroid/OFFROADER/assets/93506475/65828d22-ed51-4b77-83e9-e665a70a58e7)

## 바로가기 링크

- [**앱 소개**](#앱-소개)
- [**화면 별 적용 기술 소개**](#화면-별-적용-기술-소개)
- [**기술적 의사결정**](#기술적-의사결정)
- [**트러블슈팅**](#트러블슈팅)


## 앱 소개

**최신 등산 정보 및 유용한 등산 팁을 제공하여 등산을 더욱 쉽고 즐겁게 만들어주는 앱**

| 분류 | 내용 |
|-|-|
| 아키텍쳐 | `MVVM` |
| 라이브러리 | `LifeCycle`, `LiveData`, `Lottie`, `Media3`, `Navigation`, `Splash`, `ViewModel` |
| 비동기 처리 | `Coroutine` |
| 데이터 처리 | `Gson`, `Parcelize`, `SharedPreferences` |
| 데이터베이스| `Google Authentication`, `Google Firestore`, `Google Storage`, `RoomDatabase` |
| API 통신 | `Google Login`, `KBS API`, `MBC API`, `NaverMaps API`, `OkHttp`, `OpenAi API`, `OpenWeather API`, `Retrofit`, `SBS API` |
| 이미지 로더 | `Bitmap`, `Glide`, `ImagePicker` |
| UI | `Accordion UI`, `AlertDialog`, `BLUR`, `BottomSheetDialog`, `CoordinatorLayout`, `CustomDialog`, `DialogFragment`, `ExoPlayer`, `FloatingActionBubble`, `Fragment`, `ListAdapter`, `MaterialDesign`, `MotionLayout`, `MultiViewType`, `RecyclerViewAdapter`, `TapLayout`, `ViewPager2`, `XML` |


## 홈 페이지

<img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F6088bcf9-c972-45c0-a664-02ab6cfe52b4%2F656bbbe0-4ddd-477f-bbdb-e80818d07af6%2FScreen_recording_20240326_153434-ezgif.com-video-to-webp-converter.webp?table=block&id=099d962f-ed7a-4f08-b7a8-a867ef0a9624&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=580&userId=&cache=v2" width="400">

<img src="" width="400">


- 추천 산 확인
- 추천 산을 클릭하면 해당 산 정보 페이지로 이동
- 진행 중인 이벤트 확인 및 해당 이벤트를 클릭하여 자세한 내용 확인

## 산 리스트 페이지

<img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F6088bcf9-c972-45c0-a664-02ab6cfe52b4%2Ff4cde5cd-88ef-4e30-955a-5570bcedf282%2FScreen_recording_20240326_155529-ezgif.com-video-to-webp-converter.webp?table=block&id=a7f7dd13-2a34-454d-b5f3-dcff67627393&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=580&userId=&cache=v2" width="400">

- 산 리스트를 확인
- 산을 선택하고 해당 산의 상단에 있는 프로필 사진을 클릭하면 산 정보 페이지로 이동

## 지도 페이지

<img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F6088bcf9-c972-45c0-a664-02ab6cfe52b4%2Fbe7d2a15-2630-4066-9323-692e12606657%2FScreen_recording_20240326_160508-ezgif.com-video-to-webp-converter_(1).webp?table=block&id=aef476c6-4068-42a6-9d59-c84e44c0bc4a&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=660&userId=&cache=v2" width="400"> <img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F6088bcf9-c972-45c0-a664-02ab6cfe52b4%2F5148e15a-3856-4da7-afd7-ae3a7b55a333%2FScreen_recording_20240326_155954-ezgif.com-video-to-webp-converter.webp?table=block&id=7f24f560-bc55-46b2-a368-781cf9fd5c18&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=880&userId=&cache=v2" width="400">

- 지도를 통해 산의 위치를 확인
- 산의 마커를 클릭하면 해당 산의 간단한 정보를 볼 수 있는 정보 창 확인
- 정보 창을 클릭하면 해당 산의 정보 페이지로 이동
- 등산 시작을 클릭하면 현재 위치의 좌표가 계속 저장되고 등산 종료를 클릭하면 기록을 확인할 수 있는 화면으로 이동

## 커뮤니티 페이지

<img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F6088bcf9-c972-45c0-a664-02ab6cfe52b4%2F195f6e4b-bced-43c0-8553-2a4a6a86dd4a%2FScreen_recording_20240326_142354-ezgif.com-video-to-webp-converter.webp?table=block&id=8567f890-a9ef-4823-a3b0-31003e7c6028&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=580&userId=&cache=v2" width="400">

- 현재 올라온 게시물 목록 확인
- 연필 모양의 아이콘을 클릭하여 게시물을 작성
- 게시물 작성은 회원 가입을 해야 작성 가능

## 내 정보 페이지

<img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F6088bcf9-c972-45c0-a664-02ab6cfe52b4%2F743d177a-61a2-4c3f-87c0-2b8a381ed11e%2FScreen_recording_20240326_143651-ezgif.com-video-to-webp-converter.webp?table=block&id=a2ade8f3-1783-4b49-bcfd-26c6445cfec0&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=580&userId=&cache=v2" width="400"> <img src="https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/0c06ff92-4132-4f50-aab0-22d3bc17928b/Screen_recording_20240326_171855-ezgif.com-crop.gif?id=238ae260-7e98-4c46-aee7-cf07c3656fa5&table=block&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1711771200000&signature=jOV7eM0_3N9ctote6CUxdpAjPC5YsiK68PU_VjZcwMQ" width="400">

- 내가 추가한 산 목록 확인
- 로그인 페이지로 이동을 클릭하여 로그인
- 로그인을 하면 나의 레벨, 업적, 내가 올린 게시글, 등산 기록을 확인
- 로그아웃을 클릭하여 로그아웃

## 산 정보 페이지

<img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F6088bcf9-c972-45c0-a664-02ab6cfe52b4%2Ffba1e298-62f8-4dda-a2cb-89f987438905%2FScreen_recording_20240326_141838-ezgif.com-video-to-webp-converter_(1).webp?table=block&id=6ddbe2f1-05c7-459f-a902-eb9c0b996f18&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=580&userId=&cache=v2" width="400">

- 산의 정보들을 확인
- 산 이름, 이미지, 주소, 해발고도, 난이도, 등산 시간, 개요, 날씨 확인
- 북마크를 찍으면 내 정보 페이지에서 내가 추가한 산 목록에 추가 및 삭제

## 챗봇 페이지

<img src="https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/3b00e98c-45dd-4054-b92c-e62ff5fc7df0/Screen_recording_20240326_155354-ezgif.com-crop.gif?id=434f2f2c-dfd8-450c-8d7b-3a290579ac74&table=block&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1711771200000&signature=ncYoRFPXBZRiuIlR2VLy3WEIUyjKr_ghoxBRNiaNRyM" width="400"> <img src="https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/6ca05556-5ce0-4ebd-94e7-f7bc156dbf25/Screen_recording_20240326_155001-ezgif.com-crop.gif?id=7a72e761-5e42-4f6e-ac71-4dde12773030&table=block&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1711771200000&signature=2AZet8wGzyXBn2CDyzMlrdZ6fGBHq-TSSSuIxVH65Lk" width="400">

- 우측 하단에 챗봇 플로팅 버튼을 클릭하면 챗봇 대화 창으로 이동
- 플로팅 버튼의 위치는 조절 가능
- 챗봇은 MBTI T/F 두 가지 성격으로 구분해서 중앙 상단에 스피너를 통해 선택 하여 대화

## 라디오 페이지
![Screen_recording_20240326_161208-ezgif com-resize](https://github.com/SpartaFinalProjectAndroid/OFFROADER/assets/93506475/8f721ef0-6006-40c7-9e73-0386527b6173)
![Screen_recording_20240326_160813-ezgif com-resize](https://github.com/SpartaFinalProjectAndroid/OFFROADER/assets/93506475/d55a3946-2556-4af1-9730-319c597986e9)

- 하단 바텀 플레이어를 통해 현재 재생 중인 라디오 채널 재생/일시 정지
- 바텀 플레이어를 위로 드래그 하면 각 방송국의 채널 리스트 화면으로 전환
- 채널 클릭 시 해당 채널 재생
- 알림 창에서 재생/일시 정지
- 하트 버튼을 클릭하여 즐겨 찾기에 저장 및 취소

## 화면 별 적용 기술 소개

### MainActivity

각 방송국의 해당하는 채널, 좋아요 한 채널 리스트 표시

- 라디오 관련?
    - JETPACK.Media3 의 ExoPlayer, MediaSessionService 사용하여 라디오 재생 구현
    - MotionLayout 사용하여 초기 상태는 바텀 플레이어 형태로 유지, 위로 스와이프 하면 방송국 채널 리스트 레이아웃 표시
    - 각 방송국의 라디오 채널들은 OkHttp 사용하여 API 호출
    - 좋아요 한 채널은 ViewModel의 좋아요 한 채널 리스트에 추가, SharedPreferences 사용하여 local에 저장

- ViewPager2, TabLayout
    - 

### HomeFragment

추천 산 및 이벤트 등 다양한 정보를 진입시 표시

- Multi View Type RecyclerView를 사용해 다양한 아이템 및 스크롤 뷰 추가 가능
- Google Cloud Firebase에서 데이터를 받아와 화면에 추가해 줌
- UI에 사용한 아이콘 출처 표기

### SanListFragment

100대 명산 리스트를 표시

- Firebase Firestore에 저장되어 있는 산 리스트를 RecyclerView를 통해 보여
- RecyclerView를 통해 Firebase의 산 리스트 데이터를 표시

### SanMapFragment

지도에서 산 위치에 마커를 표시

- 네이버 지도 API 사용해 100대 명산의 산 위치를 표시
- 산 이름을 검색해 해당 산으로 이동하고 간단 설명창을 표시, 클릭 시 상세 페이지로 이동
- 산을 선택하고 등산 시작을 눌러 등산내용을 기록할 수 있고 RecordActivity에서 기록을 확인

### CommunityFragment

로그인 사용자만 이용할 수 있는 커뮤니티

- 실시간으로 게시글을 볼 수 있고 게시글을 삭제 및 수정 기능
- 게시글 정보는 Firestore Database에 저장
- 사진은 Google Storage에 저장해 두었다.
- downloadUrl을 이용하여 저장되어있는 사진을 접근하고 Glide로 이미지 표시

### MyDetailFragment

내 정보 페이지 및 북마크 리스트

- 로그인/로그아웃을 할 수 있음
- 내가 지금까지 오르면서 남은 기록들을 레벨, 업적, 오른 횟수 등으로 표시
- SanDetailActivity에서 북마크를 찍은 산의 목록을 볼 수 있음

### SanDetailActivity

산의 정보들을 표시

- 산의 모습, 해발고도, 주소, 걸리는 시간, 개요, 추천 이유, 날씨
- 북마크를 누르면 MyDetailFragment에서 찜한 산을 확인할 수 있음
- 기본적으로 CoordinatorLayout으로 구성되어 있고, 산의 이미지는 Appbar를 내리면 멈추고 올리면 다시 재생되는 형식
- 날씨는 OpenWeather API를 사용

### AddPostActivity

게시글 업로드

- 사진을 촬영하거나 사진을 업로드 기능
- 간단한 이미지 편집 기능 지원
- 제목 및 이미지가 선택되어야만 확인 버튼 활성화

### MyPostActivity

내 글 목록

- 내가 작성한 게시글을 한번에 확인할 수 있고 편집 및 삭제 가능

### ChatBotActivity

챗봇

- 두 가지의 인격을 가진 캐릭터로 같은 질문을 다른 느낌으로 답변 받을 수 있다

### RecordActivity

등산 경로에 대한 정보를 확인할 수 있다.

지도 프래그먼트에서 기록 종료 시 해당 액티비티로 넘어와 등산시각, 이동거리, 소요시간, 경로를 확인할 수 있다.

## 기술적 의사결정

### 1. Firebase RealTime DB  VS  Firestore

클라우드에 저장하는 방법이 두 가지 있어서 시작하기에 앞서 둘 중 어느 것을 사용할 지 고민. 

Firestore을 선택한 이유

1. 더 계층이 복합적인 데이터를 저장할 수 있다.
2. 쿼리가 인덱싱이 되어있고 정렬 및 필터링 기능에 제한이 없다.
3. uptime이 상대적으로 빠르다
4. 보안이 더 강하다
5. 사용 가격이 더 저렴하다
6. Firebase 측에서 이용을 권한다.

### 2. BottomNavigationView  VS  TapLayout/ViewPager2

BottomNavigationView 와 TapLayout/ViewPager2의 가장 큰 차이점은 좌우 스크롤 기능의 유무이다. 앱을 처음 기획할 때 양옆 슬라이드 기능이 홈화면의 리사이클러뷰와 겹치고 몇 안드로이드 기기에는 옆으로 슬라이드하여 뒤로가기 기능이 생겼기 때문에 UX를 고려하여 슬라이드 기능을 넣지 말자고 이야기가 나왔고 따라서 BottomNavigationView로 진행하였다.

하지만 나중에 많은 데이터 및 사진을 표시해야 하는 화면(SanListFragment)에서 이미지 로딩 시간이 너무 길어지는 문제가 생겼다. ViewPager를 사용하면 첫 메인 화면에서 양 옆에 있는 프래그먼트를 미리 로딩해주기 때문에 이미지 로딩 시간을 단축 해 줄 수 있다는 장점이 있다. 따라서 ViewPager로 수정하고 슬라이드 기능을 제거하는 방향으로 진행하였다.

### 3. List Adapter  VS  RecyclerView Adapter

List Adapter는 DIFFUTIL을 사용하여 submit만 해주면 그 안의 아이템과 아이템 내용을 비교하여 자동으로 업데이트 시켜주기 때문에 RecyclerView Adapter 대신에 무조건 List Adapter가 나을 것이라고 판단하고 있었는데 똑같은 어댑터를 다른 액티비티 또는 프래그먼트에서 재사용하게 될 경우에는 List Adapter의 재사용이 불가능하다는 점을 배웠다. 그래서 앞으로 Adapter 재사용의 유무에 의사결정을 진행하기로 하였다. 

### 4. documentSnapShotListener  VS  addOnSuccessListener

DocumentSnapShotListener은 실시간 업데이트를 해주고 addOnSuccessListener은 함수가 실행 될때에만 한 번 값을 가져오기 때문에 실시간 업데이트가 필요한 커뮤니티에서는 DocumentSnapShotListener을 사용하고 산 리스트를 받아오는 부분 및 산의 좌표를 받아오는 기능은 addOnSuccessListener을 사용하였다. 

### 5. Firestore Storage : 메모리  VS  로컬 파일  VS  URL

로컬 파일 저장은 사용자가 오프라인에서도 파일을 접근할 수 있다는 장점이 있지만 용량을 많이 차지한다. 오프로더 앱의 커뮤니티는 기획 상 온라인 상에서만 사용 가능하도록 했기 때문에 로컬 파일은 사용하지 않았다. 그럼 이제 메모리 또는 URL을 사용해주어야하는데 메모리를 사용해보니 파일 다운로드는 용이하지만 메모리에 전체 파일 콘텐츠를 로드해야 하기 때문에 앱의 가용 메모리보다 큰 파일을 요청하면 앱이 다운되는 문제가 생길 수 있다. 이 때문에 다운로드할 최대 바이트 수를 지정하는데 이미지의 크기가 이보다 크면 저장이 안되는 오류가 떴다. 따라서 용량 및 메모리에 큰 제약 없이 원하는 사진을 올릴 수 있는 URL 다운로드 방식을 선택하였다.

### **6. 네이버지도 API  VS  구글 지도 API**

국내 사용자 기준에 맞춰 익숙한 네이버지도를 사용

레이어 중에 등산로가 제공되어 확인이 용이함

### 7. Glide  VS  Coil

글라이드와 코일의 결정적 차이점은 현대 이미지 포맷팅 지원 기능 및 캐싱 기능의 차이이다. 우선 오프로더 앱은 산리스트 부분에서 이미지 로딩을 매우 많이 해주고 이때 이미지가 로딩하는 초기 시간이 많이 걸린다는 단점이 있다. 같은 이미지를 로딩하는 시간이 다시 사용되지 않으려면 어느정도의 캐싱 기능을 필요로 하기 때문에 글라이드를 선택하게 되었다. 

![Untitled](https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F6088bcf9-c972-45c0-a664-02ab6cfe52b4%2F2dad49b9-5275-407c-bf47-baa241cd69bb%2FUntitled.png?table=block&id=2f244e8e-74e9-4666-9daa-1ca54b624998&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1680&userId=&cache=v2)

## 트러블슈팅

### 1. 홈

- **멀티 뷰 타입 리사이클러뷰 문제**: 스크롤 뷰 내의 리사이클러뷰 사용 시 별도의 스크롤 적용 문제.
    - **해결**: 멀티 뷰 타입 리사이클러뷰를 사용하여 아이템 추가, 가로 스크롤 구현으로 문제 해결.
- **Firebase Firestore 문제**: 권한 설정 미흡으로 인한 데이터 로드 실패.
    - **해결**: Firebase 권한을 올바르게 설정하여 문제 해결.

### 2. 산 리스트

- **라이브 데이터 초기값 누락 문제**: 초기값 미부여로 인한 빌드 오류 발생.
    - **해결**: 라이브 데이터 값 부여 또는 널러블 타입으로 변경하여 오류 해결.
- **선택된 아이템 깜빡임 문제**: DiffUtil의 아이템 동일성 판단 오류.
    - **해결**: 아이템 동일성 판단 로직 수정 및 itemAnimator를 null로 설정하여 해결.

### 3. 지도

- **등산 경로선 그리기 문제**: 실제 기기에서의 작동 불량.
    - **해결**: 좌표 저장 후 경로선 그리기 로직 수정으로 문제 해결.
- **경로 기록 재사용 문제**: 단일 위치 정보 요청으로 인한 재사용 불가.
    - **해결**: 위치 정보 주기적 요청 변경으로 재사용 가능하도록 개선.

### 4. 챗봇

- **챗봇 명령 이해도 문제**: 복잡한 명령으로 인한 이해 부족.
    - **해결**: 캐릭터 컨셉 명확화 및 추가 커스터마이징 필요.
- **챗봇 응답 대기 문제**: 응답 대기 중 진행 상태 불명확.
    - **해결**: 로딩 애니메이션 사용으로 사용자 경험 개선.
- **챗봇 캐릭터 선택 안내 부재 문제**: 사용자의 캐릭터 선택 인지 부족.
    - **해결**: 채팅 시작 전 안내 문구 추가로 해결.
- **채팅 글 길이 문제**: 대화창이 화면 밖으로 넘어가는 현상.
    - **해결**: MaxWidth 설정으로 길이 제한 적용.
- **채팅 입력창 디자인 문제**: 기기 코너 곡률에 따른 디자인 잘림.
    - **해결**: 플랫 디자인 입력창으로 변경하여 해결.
- **플로팅 액션 버튼 위치 문제**: 고정된 위치로 인한 버튼 가림 현상.
    - **해결**: 델타 좌표 계산으로 이동 가능하게 구현하여 해결.

### 5. 라디오

- **공식 API 부재 문제**: 방송국 공식 API 미제공.
    - **해결**: 크롬 개발자 도구 및 OkHttp 사용으로 비공식 API 호출하여 해결.
- API 호출

### 6. 산 정보

- **산 정보 로딩 시간 문제**: 페이지 진입 시 데이터 로딩 지연.
    - **해결**: Firestore 데이터베이스 최적화 및 정보 분류로 로딩 시간 단축.

### 7. 마이 페이지

- **비로그인 상태 접근 문제**: 로그인하지 않은 사용자의 마이 페이지 접근.
- **해결**: 로그인 안내와 블러 처리로 비로그인 사용자의 접근 제한.
- **위젯 제약 조건 문제**: 루트 뷰에 모든 제약 조건을 설정하여 유지보수성 저하.
    - **해결**: 인접 위젯 종속성 강화로 유지보수성 향상.

### 8. 커뮤니티

- **최신 게시글 표시 문제**: 최근 게시글이 맨 위에 표시되지 않음.
    - **해결**: Firestore 데이터베이스 정렬 기능 사용으로 문제 해결.
- **필터링 후 정렬 문제**: 필터링과 정렬 동시 적용의 코틀린 반영 미비.
    - **해결**: **`sortedByDescending`** 함수 사용으로 직접 정렬 적용.
- **큰 용량 이미지 로드 문제**: 지정된 용량 초과 시 로드 실패.
    - **해결**: 이미지 `**downloadURL**` 사용으로 메모리 문제 해결.
- **로그아웃 시 앱 튕김 문제**: 커뮤니티 탭 접근 시 사용자 정보 누락으로 인한 오류.
    - **해결**: 콜백 이용하여 안정적인 정보 관리 구현.
- **실시간 게시글 보기 문제**: 다른 사용자 게시글의 실시간 업데이트 미표시.
    - **해결**: **`onSnapshotListener`** 사용으로 데이터 실시간 동기화 적용.

| 이름   | 구분   | MBTI | 블로그                                                                                       | 깃허브                                   | 한마디           |
|------|-------|------|---------------------------------------------------------------------------------------------|----------------------------------------|-----------------|
| 김은이 | 리더   | ISTP | [블로그](http://occhiolism.tistory.com)                                                      | [깃허브](http://github.com/eun-24k)      | 잘 해보아요! :))) |
| 이민용 | 부리더 | INFP | [블로그](https://yongfelatte.tistory.com/)                                                   | [깃허브](https://github.com/CodingVirus) | 화이팅 화이팅!   |
| 김기원 | 팀원   | INTP | [블로그](https://mydailycoding.tistory.com/)                                                 | [깃허브](https://github.com/kiwonkim11)  | 윈 드디어        |
| 박상우 | 팀원   | ENTP | [블로그](https://nowcoffee.notion.site/Today-I-Learned-ae16ec6bd18449a3a4f1f030ae3cdfdf?pvs=4) | [깃허브](https://github.com/Box-o/TIL)   | 가 보자구요!     |
| 한병철 | 팀원   | ISTJ | [블로그](https://rihan530.tistory.com/)                                                      | [깃허브](https://github.com/rihan530)    |                  |


## 개선 예정

1. 산 리스트 좋아요 클릭
2. 업적 (산 실시간 위치 마킹이 이제 막 구현되어 추후 업적과 연동할 예정입니다.)
3. 등산 기록 모아보기
