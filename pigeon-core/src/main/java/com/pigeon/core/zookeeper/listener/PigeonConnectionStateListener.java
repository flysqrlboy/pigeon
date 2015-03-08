package com.pigeon.core.zookeeper.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigeon.core.initialization.Initialization;

/**
 * 
 * <b><code>PigeonConnectionStateListener</code></b>
 * <p>
 * Comment here.
 * </p>
 * <b>Creation Time:</b> 2015-3-5
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public class PigeonConnectionStateListener implements ConnectionStateListener{
	
	private final static Logger logger = LoggerFactory.getLogger(PigeonConnectionStateListener.class);
	
	private Initialization initialization; 

	
	public PigeonConnectionStateListener(Initialization initialization) {
		super();
		this.initialization = initialization;
	}

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		
		if (newState.equals(ConnectionState.RECONNECTED)) {
			logger.info("ZK RECONNECTED, Now is going to re-init" );
			initialization.reInit();
			logger.info("Successfully re-inited the zk client!" );
		}
		
	}

}
