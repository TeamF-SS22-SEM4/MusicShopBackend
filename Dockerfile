FROM quay.io/wildfly/wildfly
# -a -u admin -p admin --> Can't login to webconsole
RUN /opt/jboss/wildfly/bin/add-user.sh -a -u michael -p michael --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-server-config", "standalone-full.xml"]
ADD build/libs/MusicShopBackend-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

# TODO Use startscript as entrypoint to hide username and password