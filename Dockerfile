FROM quay.io/wildfly/wildfly
RUN /opt/jboss/wildfly/bin/add-user.sh michael michael --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-server-config", "standalone-full.xml"]
ADD build/libs/MusicShopBackend-1.0-SNAPSHOT.jar /opt/jboss/wildfly/standalone/deployments/

# Use startscript as entrypoint to hide username and password