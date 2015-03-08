package com.pigeon.core.initialization;

/**
 * 
 * <b><code>Initialization</code></b>
 * <p>
 * Comment here.
 * </p>
 * <b>Creation Time:</b> 2015-3-5
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public abstract class Initialization {

	protected String nodeName;

	/**
	 * 
	 * 初始化.
	 *
	 */
	public abstract void init();

	/**
	 * 当zookeeper client 和 server Reconnect 时， 重新初始化
	 *
	 */
	public abstract void reInit();

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
