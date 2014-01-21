grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.server.port.http = 28082
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime 'mysql:mysql-connector-java:5.1.22'
		runtime 'org.codehaus.groovy.modules.http-builder:http-builder:0.6'
		compile 'org.apache.httpcomponents:httpmime:4.2.3'
		runtime 'commons-io:commons-io:2.4'
        runtime "se.kb:oai4j:0.6b1"
        compile "org.apache.httpcomponents:httpclient:4.2.5"
        compile "commons-httpclient:commons-httpclient:3.0-rc3"
		compile "com.k-int.EUInside:ECKClient:0.3-SNAPSHOT"
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":database-migration:1.3.5"
		runtime ":jquery:1.8.3"
		runtime ":resources:1.2.RC2"

        compile ':cache:1.1.1'

        build ":tomcat:$grailsVersion"
    }
}
