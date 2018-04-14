package book.chapter05.$5_4_2;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import static book.Constant.ZK_SERVER_ADD;

public class NodeCache_Sample {

    static String path = "/zk-book/nodecache";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(ZK_SERVER_ADD)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
	
	public static void main(String[] args) throws Exception {
		client.start();
		client.create()
		      .creatingParentsIfNeeded()
		      .withMode(CreateMode.EPHEMERAL)
		      .forPath(path, "init".getBytes());
	    final NodeCache cache = new NodeCache(client,path);
		cache.start(true);
		cache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("dddd");
				System.out.println("Node data update, new data: " + 
			    new String(cache.getCurrentData().getData()));
			}
		});
		client.setData().forPath( path, "u".getBytes() );
		Thread.sleep( 5000 );
		client.delete().deletingChildrenIfNeeded().forPath( path );
		Thread.sleep( Integer.MAX_VALUE );
	}
}