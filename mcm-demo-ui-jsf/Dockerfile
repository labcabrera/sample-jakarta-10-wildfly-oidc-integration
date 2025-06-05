# FROM quay.io/wildfly/wildfly:36.0.1.Final
FROM quay.io/wildfly/wildfly

ENV WILDFLY_HOME=/opt/jboss/wildfly

COPY configure-wildfly.cli /tmp/configure-wildfly.cli

RUN $WILDFLY_HOME/bin/jboss-cli.sh --file=/tmp/configure-wildfly.cli

RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/

COPY target/*.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080

# Utilizamos la configuracion standalone.xml por defecto

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone.xml", "-b", "0.0.0.0"]