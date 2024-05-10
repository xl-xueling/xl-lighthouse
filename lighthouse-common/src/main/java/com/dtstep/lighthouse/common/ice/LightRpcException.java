//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.10
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

public class LightRpcException extends com.zeroc.Ice.UserException
{
    public LightRpcException()
    {
        this.reason = "";
    }

    public LightRpcException(Throwable cause)
    {
        super(cause);
        this.reason = "";
    }

    public LightRpcException(String reason)
    {
        this.reason = reason;
    }

    public LightRpcException(String reason, Throwable cause)
    {
        super(cause);
        this.reason = reason;
    }

    public String ice_id()
    {
        return "com::dtstep::lighthouse::common::ice::LightRpcException";
    }

    public String reason;

    /** @hidden */
    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::com::dtstep::lighthouse::common::ice::LightRpcException", -1, true);
        ostr_.writeString(reason);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _readImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        reason = istr_.readString();
        istr_.endSlice();
    }

    /** @hidden */
    public static final long serialVersionUID = -2299495211219446597L;
}