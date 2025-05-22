# 변수 설정 (사용자 환경에 맞게 수정)
$NAME_IMG = "interlocking-dev"       # 생성할 이미지 이름
$NAME_CONTAIN = "interlocking-dev" # 실행할 컨테이너 이름
$HOST_PORT = 5001                 # 호스트 머신의 포트
$CONTAINER_PORT = 8080            # 컨테이너 내부의 포트 (애플리케이션이 리스닝하는 포트)
$NETWORK_NAME = "nw-springboot"   # 연결할 Docker 네트워크 이름


# 기존 컨테이너가 실행 중이면 중지 및 제거
Write-Host "CONTAIN stop&rm"
docker stop $NAME_CONTAIN 2>$null
docker rm $NAME_CONTAIN 2>$null


# 이미지 빌드
Write-Host "    docker build"
docker build -t ${NAME_IMG} .


# 컨테이너 실행 및 네트워크 연결
Write-Host "    docker run"
docker run --name ${NAME_CONTAIN} -p "${HOST_PORT}:8080" --network ${NETWORK_NAME} -e ASPNETCORE_ENVIRONMENT="Development" ${NAME_IMG}

Write-Host "FINISH. build & deploy"
Start-Sleep -Seconds 15