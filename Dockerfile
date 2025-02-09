FROM gradle:8.12

WORKDIR /app

COPY . /app

RUN chmod +x /app/misc/generate-ssl.sh

CMD ["sh", "-c", "/app/misc/generate-ssl.sh && ./gradlew bootRun"]