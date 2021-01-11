package no.nav.melosys.melosysmock.ldap

import com.unboundid.ldap.listener.InMemoryDirectoryServer
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig
import com.unboundid.ldap.listener.InMemoryListenerConfig
import com.unboundid.ldif.LDIFChangeRecord
import com.unboundid.ldif.LDIFReader
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class LdapServer {

    private val listenerPortLdap =
        Integer.valueOf(System.getProperty("ldap.port", "8389")) // 389 er default port for LDAP

    val directoryServer: InMemoryDirectoryServer

    private fun readLdifFilesFromClasspath(server: InMemoryDirectoryServer) {
        val ldifs = javaClass.classLoader.getResources(BASEDATA_USERS_LDIF)
        while (ldifs.hasMoreElements()) {
            val ldif = ldifs.nextElement()
            ldif.openStream().use { `is` ->
                val r = LDIFReader(`is`)
                var readEntry: LDIFChangeRecord?
                while (r.readChangeRecord().also { readEntry = it } != null) {
                    log.info(
                        "Read entry from path {} LDIF: {}", ldif.path, Arrays.toString(
                            readEntry!!.toLDIF()
                        )
                    )
                    readEntry!!.processChange(server)
                }
            }
        }
    }

    @PostConstruct
    fun start() {
        directoryServer.startListening()
        log.info("LDAP-server startet på port {}", listenerPortLdap)
    }

    companion object {
        private val log = LoggerFactory.getLogger(LdapServer::class.java)
        private const val BASEDATA_USERS_LDIF = "users.ldif"
    }

    init {
        val cfg = InMemoryDirectoryServerConfig("DC=local")
        cfg.setEnforceAttributeSyntaxCompliance(false)
        cfg.setEnforceSingleStructuralObjectClass(false)
        cfg.schema = null // dropper valider schema slik at vi slipper å definere alle object classes
        val ldapConfig = InMemoryListenerConfig.createLDAPConfig("LDAP", listenerPortLdap)
        cfg.setListenerConfigs(ldapConfig)
        directoryServer = InMemoryDirectoryServer(cfg)
        readLdifFilesFromClasspath(directoryServer)
    }
}
