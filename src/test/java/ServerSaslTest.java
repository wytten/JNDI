import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.naming.AuthenticationException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;

import static org.junit.jupiter.api.Assertions.*;

class ServerSaslTest {
    private static final String BIND_DN = "cn=Directory Manager";
    private static final String PASSWORD = "secret";

    private static InMemoryDirectoryServer server;
    private static String url;

    @BeforeAll
    static void startServer() throws Exception {
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=example,dc=com");
        config.addAdditionalBindCredentials(BIND_DN, PASSWORD);
        config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("default", 0));
        server = new InMemoryDirectoryServer(config);
        server.startListening();
        url = "ldap://localhost:" + server.getListenPort();
    }

    @AfterAll
    static void stopServer() {
        server.shutDown(true);
    }

    @Test
    void readsSupportedSaslMechanisms() throws Exception {
        DirContext ctx = ServerSasl.connect(url, BIND_DN, PASSWORD);
        try {
            Attribute mechanisms = ServerSasl.supportedSaslMechanisms(ctx).get("supportedSASLMechanisms");
            assertNotNull(mechanisms, "root DSE should advertise supportedSASLMechanisms");
            assertTrue(mechanisms.contains("PLAIN"), "expected PLAIN in " + mechanisms);
        } finally {
            ctx.close();
        }
    }

    @Test
    void rejectsBadPassword() {
        assertThrows(AuthenticationException.class, () -> ServerSasl.connect(url, BIND_DN, "wrong"));
    }
}
