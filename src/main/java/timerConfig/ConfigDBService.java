package timerConfig;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

@Service
public class ConfigDBService {
    Connection conn;
    Table sourceTable;

    public ConfigDBService() throws IOException {
        final var config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "34.67.244.204");
        config.set("hbase.zookeeper.property.clientPort","2181");

        conn = ConnectionFactory.createConnection(config);
        final var sourceTableName = Bytes.toBytes("timer:timer_config");
        sourceTable = conn.getTable(TableName.valueOf(sourceTableName));
    }

    public TimerConfig get(String userId) throws IOException {
        final var get = new Get(Bytes.toBytes(userId));
        get.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("focusLength"));
        Result res = sourceTable.get(get);
        String resStr = Bytes.toString(res.getValue(Bytes.toBytes("cf"), Bytes.toBytes("focusLength")));
        return new TimerConfig(Integer.valueOf(resStr), 4, 15, 3);
    }
}
