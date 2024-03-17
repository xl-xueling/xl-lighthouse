[["java:package:com.dtstep.lighthouse.common.ice"]]
module LightServer {
    sequence<byte> SequenceByte;
    ["java:type:java.util.ArrayList"]sequence<string> StringList;
    ["java:type:java.util.ArrayList"]sequence<long> LongList;
        interface RemoteLightServer {
                SequenceByte process(SequenceByte message);
                SequenceByte queryGroupInfo(string token);
        };
};