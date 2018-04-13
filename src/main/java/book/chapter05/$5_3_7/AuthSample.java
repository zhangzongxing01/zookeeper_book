package book.chapter05.$5_3_7;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import static book.Constant.ZK_SERVER_ADD;

//使用含权限信息的ZooKeeper会话创建数据节点
public class AuthSample {

    final static String PATH = "/zk-book-auth_test";
    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper = new ZooKeeper(ZK_SERVER_ADD,50000,null);
        zookeeper.addAuthInfo("digest", "foo:true".getBytes());
        zookeeper.create( PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL );
        Thread.sleep( Integer.MAX_VALUE );
    }
}