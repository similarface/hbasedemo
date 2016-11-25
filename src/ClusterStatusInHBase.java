import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by similarface on 16/8/30.
 */
public class ClusterStatusInHBase {
    public static void main(String args[]) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        ClusterStatus status = admin.getClusterStatus();
        System.out.println("集群状态:\n--------------");
        System.out.println("HBase Version: " + status.getHBaseVersion());
        System.out.println("Version: " + status.getVersion());
        System.out.println("Cluster ID: " + status.getClusterId());
        System.out.println("Master: " + status.getMaster());
        System.out.println("No. Backup Masters: " + status.getBackupMastersSize());
        System.out.println("Backup Masters: " + status.getBackupMasters());
        System.out.println("No. Live Servers: " + status.getServersSize());
        System.out.println("Servers: " + status.getServers());
        System.out.println("No. Dead Servers: " + status.getDeadServers());
        System.out.println("Dead Servers: " + status.getDeadServerNames());
        System.out.println("No. Regions: " + status.getRegionsCount());
        System.out.println("Regions in Transition: " + status.getRegionsInTransition());
        System.out.println("No. Requests: " + status.getRequestsCount());
        System.out.println("Avg Load: " + status.getAverageLoad());
        System.out.println("Balancer On: " + status.getBalancerOn());
        System.out.println("Is Balancer On: " + status.isBalancerOn());
        System.out.println("Master Coprocessors: " + Arrays.asList(status.getMasterCoprocessors()));
        System.out.println("\nServer Info:\n--------------");
        for (ServerName server : status.getServers()) {
            System.out.println("Hostname: " + server.getHostname());
            System.out.println("Host and Port: " + server.getHostAndPort());
            System.out.println("Server Name: " + server.getServerName());
            System.out.println("RPC Port: " + server.getPort());
            System.out.println("Start Code: " + server.getStartcode());
            ServerLoad load = status.getLoad(server);
            System.out.println("\nServer Load:\n--------------");
            System.out.println("Info Port: " + load.getInfoServerPort());
            System.out.println("Load: " + load.getLoad());
            System.out.println("Max Heap (MB): " + load.getMaxHeapMB());
            System.out.println("Used Heap (MB): " + load.getUsedHeapMB());
            System.out.println("Memstore Size (MB): " + load.getMemstoreSizeInMB());
            System.out.println("No. Regions: " + load.getNumberOfRegions());
            System.out.println("No. Requests: " + load.getNumberOfRequests());
            System.out.println("Total No. Requests: " + load.getTotalNumberOfRequests());
            System.out.println("No. Requests per Sec: " + load.getRequestsPerSecond());
            System.out.println("No. Read Requests: " + load.getReadRequestsCount());
            System.out.println("No. Write Requests: " + load.getWriteRequestsCount());
            System.out.println("No. Stores: " + load.getStores());
            System.out.println("Store Size Uncompressed (MB): " + load.getStoreUncompressedSizeMB());
            System.out.println("No. Storefiles: " + load.getStorefiles());
            System.out.println("Storefile Size (MB): " + load.getStorefileSizeInMB());
            System.out.println("Storefile Index Size (MB): " + load.getStorefileIndexSizeInMB());
            System.out.println("Root Index Size: " + load.getRootIndexSizeKB());
            System.out.println("Total Bloom Size: " + load.getTotalStaticBloomSizeKB());
            System.out.println("Total Index Size: " + load.getTotalStaticIndexSizeKB());
            System.out.println("Current Compacted Cells: " + load.getCurrentCompactedKVs());
            System.out.println("Total Compacting Cells: " + load.getTotalCompactingKVs());
            System.out.println("Coprocessors1: " + Arrays.asList(load.getRegionServerCoprocessors()));
            System.out.println("Coprocessors2: " + Arrays.asList(load.getRsCoprocessors()));
            System.out.println("Replication Load Sink: " + load.getReplicationLoadSink());
            System.out.println("Replication Load Source: " + load.getReplicationLoadSourceList());
            System.out.println("\nRegion Load:\n--------------");
            for (Map.Entry<byte[], RegionLoad> entry : load.getRegionsLoad().entrySet()) {
                System.out.println("Region: " + Bytes.toStringBinary(entry.getKey()));
                RegionLoad regionLoad = entry.getValue();
                System.out.println("Name: " + Bytes.toStringBinary(regionLoad.getName()));
                System.out.println("Name (as String): " + regionLoad.getNameAsString());
                System.out.println("No. Requests: " + regionLoad.getRequestsCount());
                System.out.println("No. Read Requests: " + regionLoad.getReadRequestsCount());
                System.out.println("No. Write Requests: " + regionLoad.getWriteRequestsCount());
                System.out.println("No. Stores: " + regionLoad.getStores());
                System.out.println("No. Storefiles: " + regionLoad.getStorefiles());
                System.out.println("Data Locality: " + regionLoad.getDataLocality());
                System.out.println("Storefile Size (MB): " + regionLoad.getStorefileSizeMB());
                System.out.println("Storefile Index Size (MB): " + regionLoad.getStorefileIndexSizeMB());
                System.out.println("Memstore Size (MB): " + regionLoad.getMemStoreSizeMB());
                System.out.println("Root Index Size: " + regionLoad.getRootIndexSizeKB());
                System.out.println("Total Bloom Size: " + regionLoad.getTotalStaticBloomSizeKB());
                System.out.println("Total Index Size: " + regionLoad.getTotalStaticIndexSizeKB());
                System.out.println("Current Compacted Cells: " + regionLoad.getCurrentCompactedKVs());
                System.out.println("Total Compacting Cells: " + regionLoad.getTotalCompactingKVs());
                System.out.println();

            }
        }
    }
}
/**
 Cluster Status:
 --------------
 HBase Version: 1.2.2
 Version: 2
 Cluster ID: 1c63c596-9726-4330-ac25-fd9025982ca5
 Master: master,16000,1472179760642
 No. Backup Masters: 0
 Backup Masters: []
 No. Live Servers: 3
 Servers: [master,16020,1472179761303, slave1,16020,1472179759594, slave2,16020,1472179760945]
 No. Dead Servers: 0
 Dead Servers: []
 No. Regions: 55
 Regions in Transition: {}
 No. Requests: 0
 Avg Load: 18.333333333333332
 Balancer On: true
 Is Balancer On: true
 Master Coprocessors: []

 Server Info:
 --------------
 Hostname: master
 Host and Port: master:16020
 Server Name: master,16020,1472179761303
 RPC Port: 16020
 Start Code: 1472179761303

 Server Load:
 --------------
 Info Port: 16030
 Load: 18
 Max Heap (MB): 30442
 Used Heap (MB): 417
 Memstore Size (MB): 0
 No. Regions: 18
 No. Requests: 0
 Total No. Requests: 73
 No. Requests per Sec: 0.0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 34
 Store Size Uncompressed (MB): 0
 No. Storefiles: 1
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0
 Coprocessors1: []
 Coprocessors2: []
 Replication Load Sink: org.apache.hadoop.hbase.replication.ReplicationLoadSink@1684342c
 Replication Load Source: []

 Region Load:
 --------------
 Region: hbase:namespace,,1472120873306.923cfe8ecf41b432694b79d4fd1e23b5.
 Name: hbase:namespace,,1472120873306.923cfe8ecf41b432694b79d4fd1e23b5.
 Name (as String): hbase:namespace,,1472120873306.923cfe8ecf41b432694b79d4fd1e23b5.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 1
 No. Storefiles: 1
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2,,1472525695812.dc0b9e9a0b51eeb18eb698cd88c1562c.
 Name: testtable2,,1472525695812.dc0b9e9a0b51eeb18eb698cd88c1562c.
 Name (as String): testtable2,,1472525695812.dc0b9e9a0b51eeb18eb698cd88c1562c.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 1
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x00\x01,1472548084737.a482c8a8b64c06f465007678a710b06b.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x00\x01,1472548084737.a482c8a8b64c06f465007678a710b06b.
 Name (as String): testtable2b,       ,1472548084737.a482c8a8b64c06f465007678a710b06b.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x04\x11,1472548084737.18c2f266524a5c570b36daf1775afaeb.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x04\x11,1472548084737.18c2f266524a5c570b36daf1775afaeb.
 Name (as String): testtable2b,      ,1472548084737.18c2f266524a5c570b36daf1775afaeb.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x07Q,1472548084737.531a90445d39af2dd8b035bb76244e22.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x07Q,1472548084737.531a90445d39af2dd8b035bb76244e22.
 Name (as String): testtable2b,      Q,1472548084737.531a90445d39af2dd8b035bb76244e22.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x0A\x91,1472548084737.c9e429e9c51097ef28cd06c74730e376.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x0A\x91,1472548084737.c9e429e9c51097ef28cd06c74730e376.
 Name (as String): testtable2b,      
 �,1472548084737.c9e429e9c51097ef28cd06c74730e376.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x0Ba,1472548084737.ee18a72f051dea79e5ad75adbb5f8953.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x0Ba,1472548084737.ee18a72f051dea79e5ad75adbb5f8953.
 Name (as String): testtable2b,      a,1472548084737.ee18a72f051dea79e5ad75adbb5f8953.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x10A,1472548084737.7882e9ddcfa0d67eb26873affe16f7e3.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x10A,1472548084737.7882e9ddcfa0d67eb26873affe16f7e3.
 Name (as String): testtable2b,      A,1472548084737.7882e9ddcfa0d67eb26873affe16f7e3.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x12\xB1,1472548084737.72b1a49bbaa35fd1c30fdb7542c25364.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x12\xB1,1472548084737.72b1a49bbaa35fd1c30fdb7542c25364.
 Name (as String): testtable2b,      �,1472548084737.72b1a49bbaa35fd1c30fdb7542c25364.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x14Q,1472548084737.d508013aa619f603abeab1aa53a1a03f.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x14Q,1472548084737.d508013aa619f603abeab1aa53a1a03f.
 Name (as String): testtable2b,      Q,1472548084737.d508013aa619f603abeab1aa53a1a03f.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x191,1472548084737.6403b4864ade4f7be0ddafe792be7097.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x191,1472548084737.6403b4864ade4f7be0ddafe792be7097.
 Name (as String): testtable2b,      1,1472548084737.6403b4864ade4f7be0ddafe792be7097.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x1Cq,1472548084737.d8905f8e79f42162a19f320391b5cce8.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x1Cq,1472548084737.d8905f8e79f42162a19f320391b5cce8.
 Name (as String): testtable2b,      q,1472548084737.d8905f8e79f42162a19f320391b5cce8.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x1DA,1472548084737.cb866741cae704e7170147e97e654cc3.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x1DA,1472548084737.cb866741cae704e7170147e97e654cc3.
 Name (as String): testtable2b,      A,1472548084737.cb866741cae704e7170147e97e654cc3.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x1E\xE1,1472548084737.5b4a38589ad7a401917dbe2ac3ab7203.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x1E\xE1,1472548084737.5b4a38589ad7a401917dbe2ac3ab7203.
 Name (as String): testtable2b,      �,1472548084737.5b4a38589ad7a401917dbe2ac3ab7203.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00!Q,1472548084737.91b988613acab8edf8c27bd5879c1403.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00!Q,1472548084737.91b988613acab8edf8c27bd5879c1403.
 Name (as String): testtable2b,      !Q,1472548084737.91b988613acab8edf8c27bd5879c1403.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00"!,1472548084737.9e465e09d48824a2204a53f9e8a593d0.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00"!,1472548084737.9e465e09d48824a2204a53f9e8a593d0.
 Name (as String): testtable2b,      "!,1472548084737.9e465e09d48824a2204a53f9e8a593d0.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00&1,1472548084737.aa85ceaa210136574eb485c6784bc8ee.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00&1,1472548084737.aa85ceaa210136574eb485c6784bc8ee.
 Name (as String): testtable2b,      &1,1472548084737.aa85ceaa210136574eb485c6784bc8ee.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,,1472548084737.d4b77d0c4f3598df20364544a191caa2.
 Name: testtable2b,,1472548084737.d4b77d0c4f3598df20364544a191caa2.
 Name (as String): testtable2b,,1472548084737.d4b77d0c4f3598df20364544a191caa2.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Hostname: slave1
 Host and Port: slave1:16020
 Server Name: slave1,16020,1472179759594
 RPC Port: 16020
 Start Code: 1472179759594

 Server Load:
 --------------
 Info Port: 16030
 Load: 19
 Max Heap (MB): 30483
 Used Heap (MB): 208
 Memstore Size (MB): 0
 No. Regions: 19
 No. Requests: 0
 Total No. Requests: 4179
 No. Requests per Sec: 0.0
 No. Read Requests: 3573
 No. Write Requests: 173
 No. Stores: 36
 Store Size Uncompressed (MB): 0
 No. Storefiles: 1
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Root Index Size: 1
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 387
 Total Compacting Cells: 387
 Coprocessors1: [MultiRowMutationEndpoint]
 Coprocessors2: [MultiRowMutationEndpoint]
 Replication Load Sink: org.apache.hadoop.hbase.replication.ReplicationLoadSink@73d55dd
 Replication Load Source: []

 Region Load:
 --------------
 Region: T_admin,,1472543051429.b655c88179510274044e72fd201f127d.
 Name: T_admin,,1472543051429.b655c88179510274044e72fd201f127d.
 Name (as String): T_admin,,1472543051429.b655c88179510274044e72fd201f127d.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 1
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: hbase:meta,,1
 Name: hbase:meta,,1
 Name (as String): hbase:meta,,1
 No. Requests: 3746
 No. Read Requests: 3573
 No. Write Requests: 173
 No. Stores: 1
 No. Storefiles: 1
 Data Locality: 1.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 1
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 387
 Total Compacting Cells: 387

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x00\xD1,1472548084737.7bdf04d202e3e13d2b72210101d318f5.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x00\xD1,1472548084737.7bdf04d202e3e13d2b72210101d318f5.
 Name (as String): testtable2b,       �,1472548084737.7bdf04d202e3e13d2b72210101d318f5.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x05\xB1,1472548084737.1e603e741a578d5add7acfe3c49a13c1.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x05\xB1,1472548084737.1e603e741a578d5add7acfe3c49a13c1.
 Name (as String): testtable2b,      �,1472548084737.1e603e741a578d5add7acfe3c49a13c1.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x06\x81,1472548084737.c3df8c6bd925ddc240ce23bec5544a18.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x06\x81,1472548084737.c3df8c6bd925ddc240ce23bec5544a18.
 Name (as String): testtable2b,      �,1472548084737.c3df8c6bd925ddc240ce23bec5544a18.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x08!,1472548084737.42d51cf87af4ee7b066c45af89a1af79.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x08!,1472548084737.42d51cf87af4ee7b066c45af89a1af79.
 Name (as String): testtable2b,      !,1472548084737.42d51cf87af4ee7b066c45af89a1af79.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x08\xF1,1472548084737.233c4875ec9a3c0caf5aee8d19070531.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x08\xF1,1472548084737.233c4875ec9a3c0caf5aee8d19070531.
 Name (as String): testtable2b,      �,1472548084737.233c4875ec9a3c0caf5aee8d19070531.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x0C1,1472548084737.9b5eb146b793e6d51df527773403d512.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x0C1,1472548084737.9b5eb146b793e6d51df527773403d512.
 Name (as String): testtable2b,      1,1472548084737.9b5eb146b793e6d51df527773403d512.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x0D\x01,1472548084737.5ed65f7b6b8e264cec9ced0a6579ea54.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x0D\x01,1472548084737.5ed65f7b6b8e264cec9ced0a6579ea54.
 ,1472548084737.5ed65f7b6b8e264cec9ced0a6579ea54.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x0Fq,1472548084737.6342671531b8eb26968b80fe81096070.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x0Fq,1472548084737.6342671531b8eb26968b80fe81096070.
 Name (as String): testtable2b,      q,1472548084737.6342671531b8eb26968b80fe81096070.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x13\x81,1472548084737.1ada0fa40891e4649f8f61bed8379c75.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x13\x81,1472548084737.1ada0fa40891e4649f8f61bed8379c75.
 Name (as String): testtable2b,      �,1472548084737.1ada0fa40891e4649f8f61bed8379c75.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x15!,1472548084737.93844281749a3eb97c58581ba810c887.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x15!,1472548084737.93844281749a3eb97c58581ba810c887.
 Name (as String): testtable2b,      !,1472548084737.93844281749a3eb97c58581ba810c887.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x16\xC1,1472548084737.5d097992ec0a6c3706ed8301c5203497.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x16\xC1,1472548084737.5d097992ec0a6c3706ed8301c5203497.
 Name (as String): testtable2b,      �,1472548084737.5d097992ec0a6c3706ed8301c5203497.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x18a,1472548084737.63cbd9e5960bf7893adde1369dd9c15f.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x18a,1472548084737.63cbd9e5960bf7893adde1369dd9c15f.
 Name (as String): testtable2b,      a,1472548084737.63cbd9e5960bf7893adde1369dd9c15f.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x1B\xA1,1472548084737.c725b994d76aa6d9ac328ce6995c0a4f.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x1B\xA1,1472548084737.c725b994d76aa6d9ac328ce6995c0a4f.
 Name (as String): testtable2b,      �,1472548084737.c725b994d76aa6d9ac328ce6995c0a4f.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x1E\x11,1472548084737.ced7ed12e245b25428fa40ed9b0eab66.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x1E\x11,1472548084737.ced7ed12e245b25428fa40ed9b0eab66.
 Name (as String): testtable2b,      ,1472548084737.ced7ed12e245b25428fa40ed9b0eab66.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00 \x81,1472548084737.944dafbf491d6e782976c6481e6a061a.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00 \x81,1472548084737.944dafbf491d6e782976c6481e6a061a.
 Name (as String): testtable2b,       �,1472548084737.944dafbf491d6e782976c6481e6a061a.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00#\xC1,1472548084737.4c3b30de1ffd4a7b493fba13c5bac27a.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00#\xC1,1472548084737.4c3b30de1ffd4a7b493fba13c5bac27a.
 Name (as String): testtable2b,      #�,1472548084737.4c3b30de1ffd4a7b493fba13c5bac27a.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00%a,1472548084737.73ffbea25556983a61ad2a2e59de70a0.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00%a,1472548084737.73ffbea25556983a61ad2a2e59de70a0.
 Name (as String): testtable2b,      %a,1472548084737.73ffbea25556983a61ad2a2e59de70a0.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Hostname: slave2
 Host and Port: slave2:16020
 Server Name: slave2,16020,1472179760945
 RPC Port: 16020
 Start Code: 1472179760945

 Server Load:
 --------------
 Info Port: 16030
 Load: 18
 Max Heap (MB): 15930
 Used Heap (MB): 126
 Memstore Size (MB): 0
 No. Regions: 18
 No. Requests: 0
 Total No. Requests: 304
 No. Requests per Sec: 0.0
 No. Read Requests: 186
 No. Write Requests: 40
 No. Stores: 36
 Store Size Uncompressed (MB): 0
 No. Storefiles: 2
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0
 Coprocessors1: []
 Coprocessors2: []
 Replication Load Sink: org.apache.hadoop.hbase.replication.ReplicationLoadSink@4bd023e1
 Replication Load Source: []

 Region Load:
 --------------
 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x01\xA1,1472548084737.9b49e548865d67e486c14ed7bdd096f2.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x01\xA1,1472548084737.9b49e548865d67e486c14ed7bdd096f2.
 Name (as String): testtable2b,      �,1472548084737.9b49e548865d67e486c14ed7bdd096f2.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x02q,1472548084737.637abdd9d483774aede71a2f91dfc027.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x02q,1472548084737.637abdd9d483774aede71a2f91dfc027.
 Name (as String): testtable2b,      q,1472548084737.637abdd9d483774aede71a2f91dfc027.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x03A,1472548084737.edf4fa3b0c3b824facfeb0991e9fca37.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x03A,1472548084737.edf4fa3b0c3b824facfeb0991e9fca37.
 Name (as String): testtable2b,      A,1472548084737.edf4fa3b0c3b824facfeb0991e9fca37.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x04\xE1,1472548084737.6ceb225fbc8022a4478a896f263095be.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x04\xE1,1472548084737.6ceb225fbc8022a4478a896f263095be.
 Name (as String): testtable2b,      �,1472548084737.6ceb225fbc8022a4478a896f263095be.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x09\xC1,1472548084737.5dea95759452e75e594bf2b7684c22c3.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x09\xC1,1472548084737.5dea95759452e75e594bf2b7684c22c3.
 Name (as String): testtable2b,      	�,1472548084737.5dea95759452e75e594bf2b7684c22c3.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x0D\xD1,1472548084737.2d9c3a239165587e81fd69811f28d1d7.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x0D\xD1,1472548084737.2d9c3a239165587e81fd69811f28d1d7.
 �,1472548084737.2d9c3a239165587e81fd69811f28d1d7.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x0E\xA1,1472548084737.e762fdfa6fafe92582e5bbbf4354548a.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x0E\xA1,1472548084737.e762fdfa6fafe92582e5bbbf4354548a.
 Name (as String): testtable2b,      �,1472548084737.e762fdfa6fafe92582e5bbbf4354548a.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x11\x11,1472548084737.e6a1c603057be2ed2cf406d59dcb6951.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x11\x11,1472548084737.e6a1c603057be2ed2cf406d59dcb6951.
 Name (as String): testtable2b,      ,1472548084737.e6a1c603057be2ed2cf406d59dcb6951.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x11\xE1,1472548084737.b56938e81c232911060ba69ea5f13577.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x11\xE1,1472548084737.b56938e81c232911060ba69ea5f13577.
 Name (as String): testtable2b,      �,1472548084737.b56938e81c232911060ba69ea5f13577.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x15\xF1,1472548084737.f15024a5e80d18045c9c2a1f9013252f.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x15\xF1,1472548084737.f15024a5e80d18045c9c2a1f9013252f.
 Name (as String): testtable2b,      �,1472548084737.f15024a5e80d18045c9c2a1f9013252f.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x17\x91,1472548084737.87dfb86909fc1bd51d2ab55b20c3c678.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x17\x91,1472548084737.87dfb86909fc1bd51d2ab55b20c3c678.
 Name (as String): testtable2b,      �,1472548084737.87dfb86909fc1bd51d2ab55b20c3c678.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x1A\x01,1472548084737.e50b4c5a88b52ed9febdad897a4a9dfb.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x1A\x01,1472548084737.e50b4c5a88b52ed9febdad897a4a9dfb.
 Name (as String): testtable2b,      ,1472548084737.e50b4c5a88b52ed9febdad897a4a9dfb.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x1A\xD1,1472548084737.69452b834d6996599f5da50bdb35e47a.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x1A\xD1,1472548084737.69452b834d6996599f5da50bdb35e47a.
 Name (as String): testtable2b,      �,1472548084737.69452b834d6996599f5da50bdb35e47a.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00\x1F\xB1,1472548084737.5cfd3e093b2471e48d1241f949463a38.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00\x1F\xB1,1472548084737.5cfd3e093b2471e48d1241f949463a38.
 Name (as String): testtable2b,      �,1472548084737.5cfd3e093b2471e48d1241f949463a38.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00"\xF1,1472548084737.fb42a92981368ae93468612522a23f1a.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00"\xF1,1472548084737.fb42a92981368ae93468612522a23f1a.
 Name (as String): testtable2b,      "�,1472548084737.fb42a92981368ae93468612522a23f1a.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00$\x91,1472548084737.55af17f5d990e8f3fc31707eb940e333.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00$\x91,1472548084737.55af17f5d990e8f3fc31707eb940e333.
 Name (as String): testtable2b,      $�,1472548084737.55af17f5d990e8f3fc31707eb940e333.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: testtable2b,\x00\x00\x00\x00\x00\x00'\x10,1472548084737.7b142527a0635a37e0a65f40d20e4c73.
 Name: testtable2b,\x00\x00\x00\x00\x00\x00'\x10,1472548084737.7b142527a0635a37e0a65f40d20e4c73.
 Name (as String): testtable2b,      ',1472548084737.7b142527a0635a37e0a65f40d20e4c73.
 No. Requests: 0
 No. Read Requests: 0
 No. Write Requests: 0
 No. Stores: 2
 No. Storefiles: 0
 Data Locality: 0.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0

 Region: user,,1472196181127.eadb514a4a44d684b473c5916a262bf6.
 Name: user,,1472196181127.eadb514a4a44d684b473c5916a262bf6.
 Name (as String): user,,1472196181127.eadb514a4a44d684b473c5916a262bf6.
 No. Requests: 226
 No. Read Requests: 186
 No. Write Requests: 40
 No. Stores: 2
 No. Storefiles: 2
 Data Locality: 1.0
 Storefile Size (MB): 0
 Storefile Index Size (MB): 0
 Memstore Size (MB): 0
 Root Index Size: 0
 Total Bloom Size: 0
 Total Index Size: 0
 Current Compacted Cells: 0
 Total Compacting Cells: 0


 Process finished with exit code 0


 **/