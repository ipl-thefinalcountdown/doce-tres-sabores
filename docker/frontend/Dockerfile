FROM alpine:3.12.1

RUN apk add --update --no-cache dub ldc build-base libressl-dev zlib-dev

ARG USER=docker
ARG UID=1000
ARG GID=1000

RUN adduser ${USER} -u ${UID} -D

USER ${UID}:${GID}

WORKDIR /home/${USER}/frontend/

ENTRYPOINT ["/usr/bin/dub"]
