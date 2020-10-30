FROM alpine:3.12.1

ENV JAVA_HOME="/usr/lib/jvm/default-jvm/"
RUN apk add --update --no-cache openjdk11
ENV PATH=$PATH:${JAVA_HOME}/bin

RUN apk add --no-cache --virtual .build-deps sed

RUN wget -O wildfly-21.0.0.Final.tar.gz https://download.jboss.org/wildfly/21.0.0.Final/wildfly-21.0.0.Final.tar.gz && \
	tar -xvf wildfly-21.0.0.Final.tar.gz -C /opt/ && \
	rm -v wildfly-21.0.0.Final.tar.gz && \
	mv /opt/wildfly-21.0.0.Final /opt/wildfly && \
	chown root:root -R /opt/wildfly/

ENV JBOSS_HOME="/opt/wildfly"

RUN wget -O eclipselink-3.0.0-M1.jar https://search.maven.org/remotecontent?filepath=org/eclipse/persistence/eclipselink/3.0.0-M1/eclipselink-3.0.0-M1.jar && \
	mv eclipselink-3.0.0-M1.jar /opt/wildfly/modules/system/layers/base/org/eclipse/persistence/main/ && \
	sed -i '/<resources>/a\\t<resource-root path="eclipselink.jar"><filter><exclude path="javax/**"/></filter></resource-root>' /opt/wildfly/modules/system/layers/base/org/eclipse/persistence/main/module.xml && \
	sed -i '/<dependencies>/a\\t<module name="javax.ws.rs.api"/>\n\t<module name="javax.json.api"/>' /opt/wildfly/modules/system/layers/base/org/eclipse/persistence/main/module.xml && \
	sed -i 's|<inet-address value="${jboss.bind.address.*:127.0.0.1}"/>|<any-address/>|g' /opt/wildfly/standalone/configuration/standalone.xml && \
	/opt/wildfly/bin/add-user.sh admin admin

RUN apk del -f .build-deps && \
	rm -rf /tmp/* /var/cache/apk/*

ENTRYPOINT ["/bin/sh", "/opt/wildfly/bin/standalone.sh"]
