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

    private Get addColumn(Get get, String colName) {
        get.addColumn(Bytes.toBytes("cf"), Bytes.toBytes(colName));
        return get;
    }

    private Integer getValue(Result result, String colName) {
        return Integer.valueOf(Bytes.toString(result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(colName))));
    }

    private TimerConfig parseRes(Result result) {
        return new TimerConfig(
               getValue(result, "focusLength"),
               getValue(result, "shortBreakLength"),
               getValue(result, "longBreakLength"),
               getValue(result, "focusCntBeforeLongBreak")
        );
    }

    public TimerConfig get(String userId) throws IOException {
        final var get = new Get(Bytes.toBytes(userId));
        addColumn(get, "focusLength");
        addColumn(get, "shortBreakLength");
        addColumn(get, "longBreakLength");
        addColumn(get, "focusCntBeforeLongBreak");
        Result res = sourceTable.get(get);
        return parseRes(res);
    }
}
