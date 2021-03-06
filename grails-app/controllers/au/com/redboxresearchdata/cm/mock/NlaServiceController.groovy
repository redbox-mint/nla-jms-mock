package au.com.redboxresearchdata.cm.mock

/**
 * 
 * NlaServiceController 
 * 
 * Will always return a mocked NLA party ID. 
 * 
 * <br/>
 * 
 * Sample URL: http://www.nla.gov.au/apps/srw/search/peopleaustralia?version=1.1&recordSchema=urn%3Aisbn%3A1-931666-33-4&recordPacking=xml&operation=searchRetrieve&query=rec.identifier%3D%22c2a0533fc6de694eb7dd64026e7cfdfa%22
 *
 * @author <a target='_' href='https://github.com/shilob'>Shilo Banihit</a>
 *
 */
class NlaServiceController {

	def poorMansCtr = 0 // this must be a singleton
	/**
	 * 
	 * @return NLA Party ID that is a concatenation of the 'rec.identifier' 
	 */
    def search() { 
		def query = params.query
		log.debug "Query: ${query}"
		def recId = query.split('=')[1].replaceAll('\"', '')
		log.debug "Record ID: ${recId}"
		def data = ''
		def templateFile = 'templates/srutemplate.xml'
		if (recId.startsWith('integration-test-delay-me')) {
			// when running on this mode, ensure that no other requests are hitting this controller at the same time
			// so when in Jenkins or any CI environment, do your homework in properly setting up the right build configuration.
			def recIdParts = recId.split("_")
			def maxCtr = Integer.parseInt(recIdParts[1])
			println "Poor Man's ctr: ${poorMansCtr}, Max Ctr: ${maxCtr}"
			if (poorMansCtr == maxCtr) {
				poorMansCtr = 0
			} else {
				poorMansCtr++
				templateFile = 'templates/sru_norecords.xml'
			}
		}
		if (recId) {
			data = this.class.classLoader.getResourceAsStream(templateFile).text
			data = data.replaceAll('OID', recId) 
		}
		render(text: data, contentType:'text/xml', encoding:'UTF-8')
	}
}
