package book.chapter05.$5_3_4;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

import static book.Constant.ZK_SERVER_ADD;

// ZooKeeper API 获取子节点列表，使用同步(sync)接口。
public class ZooKeeper_GetChildren_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static boolean loop=true;
    public static void main(String[] args) throws Exception{

    	String path = "/zk-book";
        zk = new ZooKeeper(ZK_SERVER_ADD,
				5000, //
				new ZooKeeper_GetChildren_API_Sync_Usage());
        connectedSemaphore.await();
        zk.create(path, "".getBytes(), 
        		  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create(path+"/c1", "".getBytes(), 
        		  Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        
        List<String> childrenList = zk.getChildren(path, true);
        System.out.println(childrenList);
        int i=2;
        while (loop){
            Thread.sleep( 1000*3);
            zk.create(path+"/c"+i, "".getBytes(),
                    Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            i++;
        }

        Thread.sleep( Integer.MAX_VALUE );
    }
    @Override
    public void process(WatchedEvent event) {
      if (KeeperState.SyncConnected == event.getState()) {
        if (EventType.None == event.getType() && null == event.getPath()) {
            connectedSemaphore.countDown();
        } else if (event.getType() == EventType.NodeChildrenChanged) {
            try {
                System.out.println("ReGet Child:"+zk.getChildren(event.getPath(),true));
            } catch (Exception e) {}
        }
      }
    }


}
