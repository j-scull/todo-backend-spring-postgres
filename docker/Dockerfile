ARG ALPINE_VERSION
FROM alpine:$ALPINE_VERSION

RUN  apk update \
  && apk upgrade \
  && apk add ca-certificates \
  && update-ca-certificates \
  && apk add --update coreutils && rm -rf /var/cache/apk/* \
  && apk add --update openjdk17 curl bash \
  && apk add --no-cache nss \
  && rm -rf /var/cache/apk/*

WORKDIR /usr/share/round-up

ARG JAR_FILE
ADD $JAR_FILE service.jar

ENTRYPOINT /usr/bin/java -jar service.jar

EXPOSE 8080/tcp