# 디버그 컨테이너를 사용자 지정하는 방법과 Visual Studio 이 Dockerfile을 사용하여 더 빠른 디버깅을 위해 이미지를 빌드하는 방법을 알아보려면 https://aka.ms/customizecontainer를 참조하세요.

# 이 스테이지는 VS에서 빠른 모드로 실행할 때 사용됩니다(디버그 구성의 기본값).
FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS base
USER $APP_UID
WORKDIR /app
EXPOSE 5001


# 이 스테이지는 서비스 프로젝트를 빌드
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
ARG BUILD_CONFIGURATION=Debug
WORKDIR /src
COPY ["Interlocking.csproj", "."]
RUN dotnet restore "./Interlocking.csproj"
COPY . .
WORKDIR "/src/."
RUN dotnet build "./Interlocking.csproj" -c $BUILD_CONFIGURATION -o /app/build


FROM build AS publish
ARG BUILD_CONFIGURATION=Debug
RUN dotnet publish "./Interlocking.csproj" -c $BUILD_CONFIGURATION -o /app/publish /p:UseAppHost=false

# 디버그 구성을 사용하지 않는 경우 기본값
FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Interlocking.dll"]