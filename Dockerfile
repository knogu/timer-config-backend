# ベースイメージ
FROM openjdk:17

# 作業ディレクトリの設定
WORKDIR /application

# 依存関係のコピー
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# 実行権限の付与
RUN chmod +x ./gradlew

# 環境変数の設定
ENV PORT=80

# ポートの公開
EXPOSE 80

# アプリケーションの実行
CMD ["./gradlew", "bootRun"]
