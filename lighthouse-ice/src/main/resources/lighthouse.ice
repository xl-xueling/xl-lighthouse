[["java:package:com.dtstep.lighthouse.common.ice"]]
module LightHouseServer {
    sequence<byte> SequenceByte;
    struct SnpStatValue
	{
		long batchTime;
		double value;
		string dimens;
	};
    ["java:type:java.util.ArrayList"]sequence<SnpStatValue> StatValueList;
    ["java:type:java.util.ArrayList"]sequence<string> StringList;
    ["java:type:java.util.ArrayList"]sequence<long> LongList;
	interface Receiver{
		string logic(SequenceByte message);
	};

	interface Aux{
	    string queryGroupByToken(string token);
	    string queryStatById(int statId);	
	};


	interface DataQuery{
		string query(int statId,string dimens,long startTime,long endTime);
		string queryWithBatchList(int statId,string dimens,LongList batchTimeList);
		string queryWithDimensList(int statId,StringList dimensList,long batchTime);
		StringList queryDimens(string token,string dimensName,string lastDimensValue,int limit);
	};
};
