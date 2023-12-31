// **********************************************************************
//
// Copyright (c) 2003-2018 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.6.5
//
// <auto-generated>
//
// Generated from file `lighthouse.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.dtstep.lighthouse.common.ice;

public interface _DataQueryInterfaceOperations
{
    String dataQuery(int statId, String dimens, long startTime, long endTime, Ice.Current __current);

    String dataQueryWithBatchList(int statId, String dimens, java.util.List<Long> batchTimeList, Ice.Current __current);

    String dataQueryWithDimensList(int statId, java.util.List<String> dimensList, long batchTime, Ice.Current __current);

    java.util.List<String> queryDimens(String token, String dimens, String lastDimens, int limit, Ice.Current __current);
}
