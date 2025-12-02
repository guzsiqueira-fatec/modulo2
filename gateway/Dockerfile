# build -> instala maven -> copia os poms para cache -> copia codigo -> build completo
FROM eclipse-temurin:21-jdk-alpine AS builder

RUN apk add --no-cache maven

WORKDIR /app

COPY pom.xml .


# Baixa dependências (essa layer fica em cache enquanto o POM não mudar)
RUN mvn dependency:go-offline -B

# Copia todo o código fonte
COPY . .

RUN mvn clean package -DskipTests -B

# runtime -> cria usuario nao root -> copia o jar construido na etapa anterior -> seta permissao -> seta usuario -> entrypoint
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Alpine usa addgroup/adduser ao invés de groupadd/useradd
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
