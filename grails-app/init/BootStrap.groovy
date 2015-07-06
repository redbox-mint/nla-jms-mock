import au.com.redboxresearchdata.cm.mock.*

class BootStrap {
	def sink
    def init = { servletContext ->
		sink = new JmsSink()
		sink.start(9302)
    }
	
    def destroy = {
		sink.stop()
    }
}
