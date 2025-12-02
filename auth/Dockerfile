# build -> instala maven -> copia os poms para cache -> copia codigo -> build completo
FROM eclipse-temurin:21-jdk-alpine AS builder

# Instala Maven
RUN apk add --no-cache maven

WORKDIR /app

# Copia todos os POMs primeiro (para cache de dependências)
COPY pom.xml .
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY api/pom.xml api/

# Baixa dependências (essa layer fica em cache enquanto os POMs não mudarem)
RUN mvn dependency:go-offline -B

# Copia todo o código fonte
COPY domain/src domain/src
COPY application/src application/src
COPY api/src api/src

# Build completo a partir do pom pai - instala domain e application no repo local, depois faz package do api
RUN mvn clean install -DskipTests -B

# runtime -> cria usuario nao root -> copia o jar construido na etapa anterior -> seta permissao -> seta usuario -> entrypoint
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Alpine usa addgroup/adduser ao invés de groupadd/useradd
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/api/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
