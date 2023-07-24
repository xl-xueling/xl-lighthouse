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

public interface DataQueryInterfacePrx extends Ice.ObjectPrx
{
    public String dataQuery(int statId, String dimens, long startTime, long endTime);

    public String dataQuery(int statId, String dimens, long startTime, long endTime, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_dataQuery(int statId, String dimens, long startTime, long endTime);

    public Ice.AsyncResult begin_dataQuery(int statId, String dimens, long startTime, long endTime, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_dataQuery(int statId, String dimens, long startTime, long endTime, Ice.Callback __cb);

    public Ice.AsyncResult begin_dataQuery(int statId, String dimens, long startTime, long endTime, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_dataQuery(int statId, String dimens, long startTime, long endTime, Callback_DataQueryInterface_dataQuery __cb);

    public Ice.AsyncResult begin_dataQuery(int statId, String dimens, long startTime, long endTime, java.util.Map<String, String> __ctx, Callback_DataQueryInterface_dataQuery __cb);

    public Ice.AsyncResult begin_dataQuery(int statId,
                                           String dimens,
                                           long startTime,
                                           long endTime,
                                           IceInternal.Functional_GenericCallback1<String> __responseCb,
                                           IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_dataQuery(int statId,
                                           String dimens,
                                           long startTime,
                                           long endTime,
                                           IceInternal.Functional_GenericCallback1<String> __responseCb,
                                           IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                           IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_dataQuery(int statId,
                                           String dimens,
                                           long startTime,
                                           long endTime,
                                           java.util.Map<String, String> __ctx,
                                           IceInternal.Functional_GenericCallback1<String> __responseCb,
                                           IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_dataQuery(int statId,
                                           String dimens,
                                           long startTime,
                                           long endTime,
                                           java.util.Map<String, String> __ctx,
                                           IceInternal.Functional_GenericCallback1<String> __responseCb,
                                           IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                           IceInternal.Functional_BoolCallback __sentCb);

    public String end_dataQuery(Ice.AsyncResult __result);

    public String dataQueryWithBatchList(int statId, String dimens, java.util.List<Long> batchTimeList);

    public String dataQueryWithBatchList(int statId, String dimens, java.util.List<Long> batchTimeList, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId, String dimens, java.util.List<Long> batchTimeList);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId, String dimens, java.util.List<Long> batchTimeList, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId, String dimens, java.util.List<Long> batchTimeList, Ice.Callback __cb);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId, String dimens, java.util.List<Long> batchTimeList, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId, String dimens, java.util.List<Long> batchTimeList, Callback_DataQueryInterface_dataQueryWithBatchList __cb);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId, String dimens, java.util.List<Long> batchTimeList, java.util.Map<String, String> __ctx, Callback_DataQueryInterface_dataQueryWithBatchList __cb);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId,
                                                        String dimens,
                                                        java.util.List<Long> batchTimeList,
                                                        IceInternal.Functional_GenericCallback1<String> __responseCb,
                                                        IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId,
                                                        String dimens,
                                                        java.util.List<Long> batchTimeList,
                                                        IceInternal.Functional_GenericCallback1<String> __responseCb,
                                                        IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                                        IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId,
                                                        String dimens,
                                                        java.util.List<Long> batchTimeList,
                                                        java.util.Map<String, String> __ctx,
                                                        IceInternal.Functional_GenericCallback1<String> __responseCb,
                                                        IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_dataQueryWithBatchList(int statId,
                                                        String dimens,
                                                        java.util.List<Long> batchTimeList,
                                                        java.util.Map<String, String> __ctx,
                                                        IceInternal.Functional_GenericCallback1<String> __responseCb,
                                                        IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                                        IceInternal.Functional_BoolCallback __sentCb);

    public String end_dataQueryWithBatchList(Ice.AsyncResult __result);

    public String dataQueryWithDimensList(int statId, java.util.List<String> dimensList, long batchTime);

    public String dataQueryWithDimensList(int statId, java.util.List<String> dimensList, long batchTime, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId, java.util.List<String> dimensList, long batchTime);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId, java.util.List<String> dimensList, long batchTime, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId, java.util.List<String> dimensList, long batchTime, Ice.Callback __cb);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId, java.util.List<String> dimensList, long batchTime, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId, java.util.List<String> dimensList, long batchTime, Callback_DataQueryInterface_dataQueryWithDimensList __cb);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId, java.util.List<String> dimensList, long batchTime, java.util.Map<String, String> __ctx, Callback_DataQueryInterface_dataQueryWithDimensList __cb);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId,
                                                         java.util.List<String> dimensList,
                                                         long batchTime,
                                                         IceInternal.Functional_GenericCallback1<String> __responseCb,
                                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId,
                                                         java.util.List<String> dimensList,
                                                         long batchTime,
                                                         IceInternal.Functional_GenericCallback1<String> __responseCb,
                                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                                         IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId,
                                                         java.util.List<String> dimensList,
                                                         long batchTime,
                                                         java.util.Map<String, String> __ctx,
                                                         IceInternal.Functional_GenericCallback1<String> __responseCb,
                                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_dataQueryWithDimensList(int statId,
                                                         java.util.List<String> dimensList,
                                                         long batchTime,
                                                         java.util.Map<String, String> __ctx,
                                                         IceInternal.Functional_GenericCallback1<String> __responseCb,
                                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                                         IceInternal.Functional_BoolCallback __sentCb);

    public String end_dataQueryWithDimensList(Ice.AsyncResult __result);

    public java.util.List<String> queryDimens(String token, String dimens, String lastDimens, int limit);

    public java.util.List<String> queryDimens(String token, String dimens, String lastDimens, int limit, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_queryDimens(String token, String dimens, String lastDimens, int limit);

    public Ice.AsyncResult begin_queryDimens(String token, String dimens, String lastDimens, int limit, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_queryDimens(String token, String dimens, String lastDimens, int limit, Ice.Callback __cb);

    public Ice.AsyncResult begin_queryDimens(String token, String dimens, String lastDimens, int limit, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_queryDimens(String token, String dimens, String lastDimens, int limit, Callback_DataQueryInterface_queryDimens __cb);

    public Ice.AsyncResult begin_queryDimens(String token, String dimens, String lastDimens, int limit, java.util.Map<String, String> __ctx, Callback_DataQueryInterface_queryDimens __cb);

    public Ice.AsyncResult begin_queryDimens(String token,
                                             String dimens,
                                             String lastDimens,
                                             int limit,
                                             IceInternal.Functional_GenericCallback1<java.util.List<String>> __responseCb,
                                             IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_queryDimens(String token,
                                             String dimens,
                                             String lastDimens,
                                             int limit,
                                             IceInternal.Functional_GenericCallback1<java.util.List<String>> __responseCb,
                                             IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                             IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_queryDimens(String token,
                                             String dimens,
                                             String lastDimens,
                                             int limit,
                                             java.util.Map<String, String> __ctx,
                                             IceInternal.Functional_GenericCallback1<java.util.List<String>> __responseCb,
                                             IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_queryDimens(String token,
                                             String dimens,
                                             String lastDimens,
                                             int limit,
                                             java.util.Map<String, String> __ctx,
                                             IceInternal.Functional_GenericCallback1<java.util.List<String>> __responseCb,
                                             IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                             IceInternal.Functional_BoolCallback __sentCb);

    public java.util.List<String> end_queryDimens(Ice.AsyncResult __result);
}
