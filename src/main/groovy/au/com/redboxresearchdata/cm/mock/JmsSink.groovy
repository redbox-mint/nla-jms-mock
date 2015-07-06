/*******************************************************************************
 * Copyright (C) 2015 Queensland Cyber Infrastructure Foundation (http://www.qcif.edu.au/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 ******************************************************************************/
package au.com.redboxresearchdata.cm.mock

import org.apache.activemq.ActiveMQConnectionFactory
import net.sf.gtools.jms.JmsCategory
import org.apache.activemq.broker.*
/**
 * JmsSink
 *
 * @author <a target='_' href='https://github.com/shilob'>Shilo Banihit</a>
 *
 */
class JmsSink {
	
	def shouldRun = new Boolean(true)
	def t
	BrokerService broker
	
	def start(portNum) {
		Thread.start {
			broker = new BrokerService();
			broker.addConnector("tcp://localhost:${portNum}");
			broker.start();
		}
		t = Thread.start {
			use(JmsCategory) {
				def jms = new ActiveMQConnectionFactory("tcp://localhost:${portNum}")
				jms.connect { c ->
				  c.queue("oaiPmhFeed") { q ->
					  println "Starting to listen on port: ${portNum}"
					  q.listen { msg ->
						  println "Received Message: ${msg}"
					  }
					  synchronized(shouldRun) {
						  if (shouldRun) {
							  Thread.sleep(10)
						  }
					  }
				  }
				}
			}
		}
	}
	
	def stop() {
		synchronized(shouldRun) {
			shouldRun = new Boolean(false)
		}
	}	
}
