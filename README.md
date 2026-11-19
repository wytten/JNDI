Discovers an LDAP server's supported SASL mechanisms.

See https://docs.oracle.com/javase/tutorial/jndi/ldap/sasl.html

Build (runs the unit tests against an in-memory LDAP server):

    mvn package

Run:

    java -jar target/jndi-1.0-SNAPSHOT.jar url username password

e.g.

    java -jar target/jndi-1.0-SNAPSHOT.jar ldap://localhost:389 "cn=Directory Manager" secret
