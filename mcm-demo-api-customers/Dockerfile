FROM quay.io/wildfly/wildfly

ENV WILDFLY_HOME=/opt/jboss/wildfly

USER root
RUN microdnf install -y wget && \
    wget -O /tmp/postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.7.3.jar && \
    mkdir -p $WILDFLY_HOME/modules/org/postgresql/main && \
    mv /tmp/postgresql.jar $WILDFLY_HOME/modules/org/postgresql/main/ && \
    echo '<module name="org.postgresql" slot="main" xmlns="urn:jboss:module:1.1">' \
         '<resources>' \
         '<resource-root path="postgresql.jar"/>' \
         '</resources>' \
         '<dependencies>' \
         '<module name="javax.api"/>' \
         '<module name="javax.transaction.api"/>' \
         '</dependencies>' \
         '</module>' > $WILDFLY_HOME/modules/org/postgresql/main/module.xml && \
    microdnf clean all

USER jboss

COPY configure-wildfly.cli /tmp/configure-wildfly.cli

RUN $WILDFLY_HOME/bin/jboss-cli.sh --file=/tmp/configure-wildfly.cli

RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/

COPY target/*.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080

# CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-microprofile.xml", "-b", "0.0.0.0"]