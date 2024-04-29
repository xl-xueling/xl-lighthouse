[["java:package:com.dtstep.lighthouse.common.ice"]]
module LightServer {
    sequence<byte> SequenceByte;
    exception LightRpcException {
        string reason;
    }
    ["java:type:java.util.ArrayList"]sequence<string> StringList;
    ["java:type:java.util.ArrayList"]sequence<long> LongList;
        interface RemoteLightServer {
                SequenceByte process(SequenceByte message);
                SequenceByte queryGroupInfo(string token);
                SequenceByte queryStatInfo(int id);
                SequenceByte dataDurationQuery(int statId, String dimensValue, long startTime, long endTime);
                SequenceByte dataQuery(int statId, String dimensValue, LongList batchList);
                SequenceByte dataDurationQueryWithDimensList(int statId, StringList dimensValueList, long startTime, long endTime);
                SequenceByte dataDurationQueryWithDimensList(int statId, StringList dimensValueList, LongList batchList);
                SequenceByte limitQuery(int statId, long batchTime);
        };
};