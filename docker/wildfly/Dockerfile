FROM alpine:3.12.1

# Setup Java 14
ENV JAVA_HOME="/usr/lib/jvm/default-jvm/"
RUN apk add --update --no-cache openjdk14 --repository=http://dl-cdn.alpinelinux.org/alpine/edge/testing
ENV PATH=$PATH:${JAVA_HOME}/bin

# Build dependencies
RUN apk add --no-cache --virtual .build-deps sed curl

# Download wildfly
RUN wget -O wildfly-21.0.0.Final.tar.gz https://download.jboss.org/wildfly/21.0.0.Final/wildfly-21.0.0.Final.tar.gz && \
	tar -xvf wildfly-21.0.0.Final.tar.gz -C /opt/ && \
	rm -v wildfly-21.0.0.Final.tar.gz && \
	mv /opt/wildfly-21.0.0.Final /opt/wildfly && \
	chown root:root -R /opt/wildfly/

ENV JBOSS_HOME="/opt/wildfly"

# EclipseLink integration
ENV ECLIPSELINK_VERSION 2.7.7

RUN wget -O eclipselink.jar https://search.maven.org/remotecontent?filepath=org/eclipse/persistence/eclipselink/${ECLIPSELINK_VERSION}/eclipselink-${ECLIPSELINK_VERSION}.jar && \
	mv eclipselink.jar /opt/wildfly/modules/system/layers/base/org/eclipse/persistence/main/
COPY ./persistence/module.xml /opt/wildfly/modules/system/layers/base/org/eclipse/persistence/main/

# Add user to wildfly management platform
RUN /opt/wildfly/bin/add-user.sh pudim pudim

# Copy wildfly standalone configuration
ENV WILDFLY_STANDALONE_CONFIG_DIR="/opt/wildfly/standalone/configuration"
COPY ./configuration/standalone.xml ${WILDFLY_STANDALONE_CONFIG_DIR}

# Configure keystore for Token-based auth
COPY ./bin/configure-elytron.cli /opt/wildfly/bin/
RUN ( \
		cd "${WILDFLY_STANDALONE_CONFIG_DIR}" && \
		echo -e "localhost\nestg\nipl\nLeria\nLeiria\nPT\nyes" | keytool -genkey -alias alias -keyalg RSA -keysize 2048 -keystore jwt.keystore -storepass pudimdecafe -keypass pudimdecafe \
	) && \
	(/opt/wildfly/bin/standalone.sh &) && \
	# Do health check to Wildfly admin console
	# This is needed to run the next command without a chance of failure
	(while ! curl -sSf http://localhost:9990 > /dev/null; do sleep 1; done) && \
	/opt/wildfly/bin/jboss-cli.sh --connect --file=/opt/wildfly/bin/configure-elytron.cli && \
	/opt/wildfly/bin/jboss-cli.sh --connect --command=:shutdown

# PostgreSQL integration
ENV JDBC_POSTGRESQL_VERSION 42.2.18

RUN wget -O postgresql.jar https://jdbc.postgresql.org/download/postgresql-${JDBC_POSTGRESQL_VERSION}.jar && \
	mkdir -p /opt/wildfly/modules/org/postgresql/main/ && \
	mv postgresql.jar /opt/wildfly/modules/org/postgresql/main/
COPY ./postgresql/module.xml /opt/wildfly/modules/org/postgresql/main/

# Remove cache
RUN apk del -f .build-deps && \
	rm -rf /tmp/* /var/cache/apk/*

ENTRYPOINT ["/bin/sh", "/opt/wildfly/bin/standalone.sh"]
