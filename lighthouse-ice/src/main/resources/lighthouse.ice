[["java:package:com.dtstep.lighthouse.common.ice"]]
module LightServer {
    sequence<byte> SequenceByte;
    ["java:type:java.util.ArrayList"]sequence<string> StringList;
    ["java:type:java.util.ArrayList"]sequence<long> LongList;
        interface RemoteLightServer {
                string process(SequenceByte message);
                string queryGroupInfo(string token);
        };
};