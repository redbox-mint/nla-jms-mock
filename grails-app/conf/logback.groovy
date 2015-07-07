import grails.util.BuildSettings
import grails.util.Environment


// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}

root(ERROR, ['STDOUT'])
def targetDir

if(Environment.current == Environment.DEVELOPMENT) {
    targetDir = BuildSettings.TARGET_DIR
} else {
	targetDir = System.getProperty("catalina.base") + "/logs"
}
if(targetDir) {
	appender("FULL_STACKTRACE", FileAppender) {

		file = "${targetDir}/stacktrace.log"
		append = true
		encoder(PatternLayoutEncoder) {
			pattern = "%level %logger - %msg%n"
		}
	}
	appender("MAIN_LOG", FileAppender) {
		
		file = "${targetDir}/nla_mock_main.log"
		append = true
		encoder(PatternLayoutEncoder) {
			pattern = "%d{HH:mm:ss.SSS} |- %level %logger - %msg%n"
		}
	}
	logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false )
	logger "au.com.redboxresearchdata.cm", DEBUG, ['STDOUT', 'MAIN_LOG'], false
}
