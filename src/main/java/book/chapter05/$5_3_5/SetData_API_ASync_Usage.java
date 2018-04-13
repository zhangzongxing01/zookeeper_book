package book.chapter05.$5_3_5;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import static book.Constant.ZK_SERVER_ADD;

// ZooKeeper API 更新节点数据内容，使用异步(async)接口。
public class SetData_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {

    	String path = "/zk-book";
    	zk = new ZooKeeper(ZK_SERVER_ADD,
				5000, //
				new SetData_API_ASync_Usage());
    	connectedSemaphore.await();

    	String pathCreate=zk.create( path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL );
        System.out.println("pathCreate:::"+pathCreate);
        zk.setData( path, "456".getBytes(), -1, new IStatCallback(), null );
    	
    	Thread.sleep( Integer.MAX_VALUE );
    }
    @Override
    public void process(WatchedEvent event) {
        if (KeeperState.SyncConnected == event.getState()) {
            if (EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            }
        }
    }
}
class IStatCallback implements AsyncCallback.StatCallback{
	@Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (rc == 0) {
            System.out.println("SUCCESS");
        }
    }
}