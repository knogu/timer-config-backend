package timerConfig;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
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

    private Put addColumn(Put put, String colName, Integer val) {
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes(colName), Bytes.toBytes(String.valueOf(val)));
        return put;
    }

    private Integer getValue(Result result, String colName) {
        return Integer.valueOf(Bytes.toString(result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(colName))));
    }

    private TimerConfig parseRes(Result result) {
        return new TimerConfig(
               getValue(result, "focusLength"),
               getValue(result, "shortBreakLength"),
               getValue(result, "longBreakLength"),
               getValue(result, "focusCntBeforeLongBreak"),
               getValue(result, "goalMinutesPerDay")
        );
    }

    public TimerConfig get(String userId) throws IOException {
        final var get = new Get(Bytes.toBytes(userId));
        addColumn(get, "focusLength");
        addColumn(get, "shortBreakLength");
        addColumn(get, "longBreakLength");
        addColumn(get, "focusCntBeforeLongBreak");
        addColumn(get, "goalMinutesPerDay");
        Result res = sourceTable.get(get);
        if (res.isEmpty()) {
            return null;
        }
        return parseRes(res);
    }

    public void put(String userId, TimerConfig timerConfig) throws IOException {
        final var put = new Put(Bytes.toBytes(userId));
        addColumn(put, "focusLength", timerConfig.focusLength);
        addColumn(put, "shortBreakLength", timerConfig.shortBreakLength);
        addColumn(put, "longBreakLength", timerConfig.longBreakLength);
        addColumn(put, "focusCntBeforeLongBreak", timerConfig.focusCntBeforeLongBreak);
        addColumn(put, "goalMinutesPerDay", timerConfig.goalMinutesPerDay);
        sourceTable.put(put);
    }
}
