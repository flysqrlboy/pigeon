package com.pigeon.provider;

/**
 * 
 * <b><code>Provider</code></b>
 * <p>
 * 把"向zookeeper服务端发送数据"抽象成provider 
 * </p>
 * <b>Creation Time:</b> 2015-3-5
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public interface Provider {

	/**
	 * 
	 * 把数据发送到zookeeper服务器的对应节点.
	 *
	 * @param deliveryPath znode节点的路径
	 * @param value
	 */
	public void send(String deliveryPath, String value);

}
