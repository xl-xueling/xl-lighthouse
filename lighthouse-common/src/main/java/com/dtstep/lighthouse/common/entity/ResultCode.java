package com.dtstep.lighthouse.common.entity;

public enum ResultCode {

    success("0","success"),

    paramValidateFailed("1","paramValidateFailed"),

    requiredParameterMissing("2","requiredParameterMissing"),

    requiredParameterMissingWithName("3","requiredParameterMissingWithName"),

    callerNotFound("4","callerNotFound"),

    unauthorized("401","unauthorized"),

    accessDenied("403","accessDenied"),

    elementNotFound("404","elementNotFound"),

    methodNotAllowed("405","methodNotAllowed"),

    systemError("500","systemError"),

    systemUnauthorized("1000","systemUnauthorized"),

    loginCheckFailed("1001","loginCheckFailed"),

    authRenewalFailed("1002","authRenewalFailed"),

    registerUserNameExist("1003","registerUserNameExist"),

    userPendApprove("1004","userPendApprove"),

    userStateUnAvailable("1006","userStateUnAvailable"),

    userStateUnAvailableRejected("1005","userStateUnAvailableRejected"),

    userStateUnAvailableFrozen("1006","userStateUnAvailableFrozen"),

    departDelErrorProjectExist("2001","departDelErrorProjectExist"),

    departDelErrorChildExist("2002","departDelErrorChildExist"),

    departCreateErrorLevelLimit("2003","departCreateErrorLevelLimit"),

    departDelErrorUserExist("2001","departDelErrorUserExist"),

    userDelErrorCannotDelCurrentUser("2004","userDelErrorCannotDelCurrentUser"),

    userDelErrorExistDepartPermission("2004","userDelErrorExistDepartPermission"),

    userDelErrorExistProjectPermission("2005","userDelErrorExistProjectPermission"),

    userDelErrorExistGroupPermission("2006","userDelErrorExistGroupPermission"),

    userDelErrorExistStatPermission("2007","userDelErrorExistStatPermission"),

    userDelErrorExistMetricPermission("2008","userDelErrorExistMetricPermission"),

    userChangePasswordWrong("2009","userChangePasswordWrong"),

    projectDelErrorGroupExist("2001","projectDelErrorGroupExist"),

    createGroupUnderProjectExceedLimit("2001","createGroupUnderProjectExceedLimit"),

    createGroupTokenExist("2001","createGroupTokenExist"),

    componentVerifyFormatError("2001","componentVerifyFormatError"),

    componentVerifyNonArrayStructure("2002","componentVerifyNonArrayStructure"),

    componentVerifyLevelLimit("2003","componentVerifyLevelLimit"),

    componentVerifyInvalidParams("2004","componentVerifyInvalidParams"),

    componentVerifyMissingParams("2004","componentVerifyMissingParams"),

    componentVerifyNotEmpty("2004","componentVerifyNotEmpty"),

    componentVerifyDuplicateValue("2004","componentVerifyDuplicateValue"),

    componentVerifyEmptyChildren("2004","componentVerifyEmptyChildren"),

    filterConfigDimensNotExist("2005","filterConfigDimensNotExist"),

    filterConfigDimensDuplicate("2009","filterConfigDimensDuplicate"),

    filterConfigDimensMissing("2006","filterConfigDimensMissing"),

    filterConfigLevelNotMatch("2007","filterConfigLevelNotMatch"),

    filterConfigComponentInvalid("2009","filterConfigComponentInvalid"),

    filterConfigDimensCannotBeEmpty("2005","filterConfigDimensCannotBeEmpty"),

    filterConfigLabelCannotBeEmpty("2006","filterConfigLabelCannotBeEmpty"),

    filterConfigConfigCannotBeEmpty("2008","filterConfigConfigCannotBeEmpty"),

    renderConfigConfigCannotBeEmpty("2008","renderConfigConfigCannotBeEmpty"),

    commonlyFiltersExceedLimit("2009","commonlyFiltersExceedLimit"),

    orderCreateRepeatSubmit("2301","orderCreateRepeatSubmit"),

    orderTypeNotExists("2302","orderTypeNotExists"),

    authAlreadyExists("2303","authAlreadyExists"),

    templateParserNoValidItem("2008","templateParserNoValidItem"),

    templateParserValidFailed("2008","templateParserValidFailed"),

    templateParserInValidAttrExist("2008","templateParserInValidAttrExist"),

    templateParserAttrCannotBeEmpty("2009","templateParserAttrCannotBeEmpty"),

    templateParserTitleCannotBeEmpty("2010","templateParserTitleCannotBeEmpty"),

    templateParserStatCannotBeEmpty("2010","templateParserStatCannotBeEmpty"),

    templateParserDimensCannotBeEmpty("2010","templateParserDimensCannotBeEmpty"),

    templateParserTitleLengthValidFailed("2011","templateParserTitleLengthValidFailed"),

    templateParserDimensNotExist("2012","templateParserDimensNotExist"),

    templateParserDimensValidFailed("2012","templateParserDimensValidFailed"),

    templateParserLimitValidFailed("2013","templateParserLimitValidFailed"),

    templateParserLimitValueExceed("2014","templateParserLimitValueExceed"),

    templateParserLimitMinuteNotSupport("2015","templateParserLimitMinuteNotSupport"),

    templateParserLimitDimensExistTogether("2016","templateParserLimitDimensExistTogether"),

    templateParserStatValidFailed("2017","templateParserStatValidFailed"),

    templateParserStateExceedLimit("2018","templateParserStateExceedLimit"),

    templateParserSeqTogether("2019","templateParserSeqTogether"),

    templateParserLimitSeqNotSupport("2020","templateParserLimitSeqNotSupport"),

    resourceDelExistSubElement("2021","resourceDelExistSubElement"),

    groupDelExistSubStat("2021","groupDelExistSubStat"),

    releasePermissionCurrentNotAllowed("2021","releasePermissionCurrentNotAllowed"),

    releasePermissionAdminAtLeastOne("2021","releasePermissionAdminAtLeastOne"),

    grantPermissionAdminExceedLimit("2021","grantPermissionAdminExceedLimit"),

    grantPermissionPublicLimit("2021","grantPermissionPublicLimit"),

    createRelationAlreadyExist("2023","createRelationAlreadyExist"),

    templateParserColumnNotExist("2024","templateParserColumnNotExist"),

    dimensColumnsExceedLimit("2025","dimensColumnsExceedLimit"),

    userStarMetricLimitExceed("3001","userStarMetricLimitExceed"),

    userStarProjectLimitExceed("3001","userStarProjectLimitExceed"),

    debugModeSwitchAlreadyTurnON("5001","debugModeSwitchAlreadyTurnON"),

    dataQueryMissingDimensParams("6001","dataQueryMissingDimensParams"),

    trackDebugModeExpired("7001","trackDebugModeExpired"),

    dataQueryLimitExceed("8001","dataQueryLimitExceed"),

    exportLimitExceed("9001","exportLimitExceed"),

    authorizationFailed("10001","authorizationFailed"),

    authorizationUnbindFailed("10002","authorizationUnbindFailed"),

    userStarViewLimitExceed("3001","userStarViewLimitExceed"),

    viewMissingRequestParam("11001","viewMissingRequestParam"),

    viewResponseNoData("11002","viewResponseNoData"),

    viewNoValidStatItem("11003","viewNoValidStatItem"),

    viewNotAuthorized("11004","viewNotAuthorized"),

    viewCategoryLimitExceed("11005","viewCategoryLimitExceed"),

    viewCategoryAtLeastOne("11006","viewCategoryAtLeastOne"),

    viewCategoryContainsSubItems("11007","viewCategoryContainsSubItems"),

    viewPublishStopped("11008","viewPublishStopped"),

    viewShareLinkStopped("11009","viewShareLinkStopped"),

    callerNameAlreadyExist("12001","callerNameAlreadyExist"),

    apiCallerNotExist("50001","apiCallerNotExist"),

    apiCallerKeyIncorrect("50002","apiCallerKeyIncorrect"),

    alarmTemplateCreateExceedLimit("60001","alarmTemplateCreateExceedLimit"),

    alarmSettingsServiceNotReachable ("70001","alarmSettingsServiceNotReachable"),

    alarmSettingsHttpsNotSupported ("70002","alarmSettingsHttpsNotSupported"),

    alarmSettingsTestingError ("70003","alarmSettingsTestingError"),

    alarmSettingsUniqueCodeExist ("70004","alarmSettingsUniqueCodeExist"),

    metricStructureReset("80004","metricStructureReset"),

    uploadErrorCreateDirectoryFailed("90001","uploadErrorCreateDirectoryFailed"),

    uploadErrorUnsupportedFileFormat("90002","uploadErrorUnsupportedFileFormat"),

    uploadErrorExceedLimit("90003","uploadErrorExceedLimit"),

    uploadError("90004","uploadError"),

    deleteFileError("90005","deleteFileError"),

    shortLinkNotExist("100001","shortLinkNotExist"),

    shortLinkInvalid("100002","shortLinkInvalid"),

    ;

    ResultCode(String code , String i18nLabel){
        this.code = code;
        this.i18nLabel = i18nLabel;
    }

    private String code;

    private String i18nLabel;

    private String[] params;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getI18nLabel() {
        return i18nLabel;
    }

    public void setI18nLabel(String i18nLabel) {
        this.i18nLabel = i18nLabel;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public static ResultCode getExtendResultCode(ResultCode resultCode, String ... params){
        resultCode.setParams(params);
        return resultCode;
    }
}
