dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
	flush.mode='commit'
}

// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			driverClassName = "com.mysql.jdbc.Driver"
			dialect = org.hibernate.dialect.MySQL5InnoDBDialect
			url = "jdbc:mysql://localhost/eckCore?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=utf8"
			username = "k-int"
			password = "k-int"
			pooled = true
			// To see the sql that is being executed enable the following 2 settings, along with the trace setting in the log4j block in config.groovy
			//logSql = true
			//formatSql = true
			properties {
				maxActive = 50
				maxIdle = 10
				minIdle = 3
				initialSize = 3
				minEvictableIdleTimeMillis = 60000
				timeBetweenEvictionRunsMillis = 60000
				maxWait = 10000
				validationQuery = "/* ping */"
			}
		}
	}
	test {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			driverClassName = "com.mysql.jdbc.Driver"
			username = "k-int"
			password = "k-int"
			url = "jdbc:mysql://localhost/eckCore"
			//&autoReconnect=true&characterEncoding=utf8"
		}
	}
	production {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			driverClassName = "com.mysql.jdbc.Driver"
			dialect = org.hibernate.dialect.MySQL5InnoDBDialect
			url = "jdbc:mysql://localhost/eckCore?zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=utf8"
			//url = "jdbc:mysql://localhost/eckCore"
			//&autoReconnect=true&characterEncoding=utf8"
			username = "k-int"
			password = "k-int"
			pooled = true
			properties {
				maxActive = 50
				maxIdle = 10
				minIdle = 3
				initialSize = 3
				minEvictableIdleTimeMillis = 60000
				timeBetweenEvictionRunsMillis = 60000
				maxWait = 10000
				validationQuery = "/* ping */"
			}
		}
	}
}
