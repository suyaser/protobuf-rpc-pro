/**
 *   Copyright 2010-2013 Peter Klauser
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
*/
package com.googlecode.protobuf.pro.duplex.example.program;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.protobuf.pro.duplex.RpcClientChannel;
import com.googlecode.protobuf.pro.duplex.example.execution.CancellingNonBlockingPingClient;
import com.googlecode.protobuf.pro.duplex.example.execution.SimpleBlockingPingClient;
import com.googlecode.protobuf.pro.duplex.example.wire.ClientExecutor;
import com.googlecode.protobuf.pro.duplex.example.wire.DemoDescriptor;
import com.googlecode.protobuf.pro.duplex.example.wire.ExecutableClient;
import com.googlecode.protobuf.pro.duplex.example.wire.ExecutableProgram;
import com.googlecode.protobuf.pro.duplex.server.RpcClientRegistry;

public class ShortTests implements ExecutableProgram {

	private static Log log = LogFactory.getLog(ShortTests.class);

	public ShortTests() {
	}
	
	@Override
	public void execute(RpcClientRegistry registry) throws Throwable {
		List<RpcClientChannel> channels = registry.getAllClients();
		if ( channels.size() <= 0) {
			log.info("No clients currently connected.");
		}
		for( RpcClientChannel channel : channels ) {
			doReverseTests(channel);
		}
	}

	protected void doReverseTests(RpcClientChannel channel) throws Throwable {
    	ExecutableClient c = null;
    	ClientExecutor exec = new ClientExecutor();
    	DemoDescriptor config = null;
    	
    	// cancelled ping
    	c = new CancellingNonBlockingPingClient(new DemoDescriptor(10, 100, new DemoDescriptor.CallDescriptor(100,0,false,false)));
    	exec.execute(c,channel);

    	// simple ping - no timeout, no processing time, no percentComplete
    	c = new SimpleBlockingPingClient(new DemoDescriptor(1, 100, new DemoDescriptor.CallDescriptor(0,0,false,false)));
    	exec.execute(c,channel);
    	
    	// simple ping - 10s timeout, 1s processing time, no percentComplete
    	c = new SimpleBlockingPingClient(new DemoDescriptor(1, 100, new DemoDescriptor.CallDescriptor(1000,10000,false,false)));
    	exec.execute(c,channel);
    	
    	// simple ping - 10s timeout, 5s processing time, percentComplete
    	c = new SimpleBlockingPingClient(new DemoDescriptor(1, 100, new DemoDescriptor.CallDescriptor(5000,10000,false,true)));
    	exec.execute(c,channel);
    	
    	// server non blocking ping - no timeout, no processing time
    	config = new DemoDescriptor(1000, 100, new DemoDescriptor.CallDescriptor(0,0,false,false));
    	c = new SimpleBlockingPingClient(config);
    	exec.execute(c,channel);
    	
	}
}
