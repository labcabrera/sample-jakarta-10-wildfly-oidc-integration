# FROM quay.io/wildfly/wildfly:36.0.1.Final
FROM quay.io/wildfly/wildfly

COPY target/*.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080

# Utilizamos la configuracion standalone.xml por defecto

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone.xml", "-b", "0.0.0.0"]