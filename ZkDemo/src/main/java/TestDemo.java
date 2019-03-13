import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class TestDemo {
    public static void main(String[] args) throws Exception {
      //  ls("/");
        setData();
    }

    public static void ls(String path) throws Exception {
        ZooKeeper zk = new ZooKeeper("s110:2181", 5000, null);
        List<String> list = zk.getChildren(path, null);
        for (String s : list) {
            System.out.println(s);
        }
    }

    public static  void create() throws  Exception{
        ZooKeeper zk = new ZooKeeper("s110:2181", 5000, null);
        zk.create("/b","tom".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);

    }

    public static void setData() throws Exception{
        final ZooKeeper zk = new ZooKeeper("s110:2181",5000,null);


        Watcher w = new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println("data is modified");
                    zk.getData("/a",this,null);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        zk.getData("/a",w,null);


        String data = "liuzj2";
 //       zk.setData("/a",data.getBytes(),0);
        while(true){
            Thread.sleep(1000);
        }

    }


}
