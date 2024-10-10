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
                SequenceByte dataDurationQuery(int statId, string dimensValue, long startTime, long endTime) throws LightRpcException;
                SequenceByte dataQuery(int statId, string dimensValue, LongList batchList) throws LightRpcException;
                SequenceByte dataDurationQueryWithDimensList(int statId, StringList dimensValueList, long startTime, long endTime) throws LightRpcException;
                SequenceByte dataQueryWithDimensList(int statId, StringList dimensValueList, LongList batchList) throws LightRpcException;
                SequenceByte limitQuery(int statId, long batchTime) throws LightRpcException;
		SequenceByte dataDurationQueryV2(string callerName,string callerKey,int statId, string dimensValue, long startTime, long endTime) throws LightRpcException; 
                SequenceByte dataQueryV2(string callerName,string callerKey,int statId, string dimensValue, LongList batchList) throws LightRpcException;
                SequenceByte dataDurationQueryWithDimensListV2(string callerName,string callerKey,int statId, StringList dimensValueList, long startTime, long endTime) throws LightRpcException;
                SequenceByte dataQueryWithDimensListV2(string callerName,string callerKey,int statId, StringList dimensValueList, LongList batchList) throws LightRpcException;
                SequenceByte limitQueryV2(string callerName,string callerKey,int statId, long batchTime) throws LightRpcException;
        };
};
