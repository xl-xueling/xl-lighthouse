[["java:package:com.dtstep.lighthouse.common.ice"]]
module LightServer {
    sequence<byte> SequenceByte;
    exception LightRpcException {
        string reason;
    }
    ["java:type:java.util.ArrayList"]sequence<string> StringList;
    ["java:type:java.util.ArrayList"]sequence<long> LongList;
        interface RemoteLightServer {
                SequenceByte process(SequenceByte message) throws LightRpcException;
                SequenceByte queryGroupInfo(string token) throws LightRpcException;
                SequenceByte queryStatInfo(int id) throws LightRpcException;
                SequenceByte dataDurationQuery(int statId, String dimensValue, long startTime, long endTime) throws LightRpcException;
                SequenceByte dataQuery(int statId, String dimensValue, LongList batchList) throws LightRpcException;
                SequenceByte dataDurationQueryWithDimensList(int statId, StringList dimensValueList, long startTime, long endTime) throws LightRpcException;
                SequenceByte dataDurationQueryWithDimensList(int statId, StringList dimensValueList, LongList batchList) throws LightRpcException;
                SequenceByte limitQuery(int statId, long batchTime) throws LightRpcException;
        };
};