package com.woting.gatherdata.persis.pojo;

import com.spiritdata.framework.core.model.BaseObject;

/**
 * 数据收集对象
 */
public class Data extends BaseObject {
    private static final long serialVersionUID = -8023806688959183216L;

    private String BeginTime;//打开时间
    private String ApiName;//是一个固定的值：L-open
    private String ObjType;
    private ReqParam ReqParam;
    private String ObjId;
    private String UserId;
    private String IMEI;
    private String DeviceType;
    private String PCDType;
    private String ScreenSize;
    private String longitude;
    private String latitude;
    private String Region;
    public String getBeginTime() {
        return BeginTime;
    }
    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }
    public String getApiName() {
        return ApiName;
    }
    public void setApiName(String apiName) {
        ApiName = apiName;
    }
    public String getObjType() {
        return ObjType;
    }
    public void setObjType(String objType) {
        ObjType = objType;
    }
    public ReqParam getReqParam() {
        return ReqParam;
    }
    public void setReqParam(ReqParam reqParam) {
        ReqParam = reqParam;
    }
    public String getObjId() {
        return ObjId;
    }
    public void setObjId(String objId) {
        ObjId = objId;
    }
    public String getUserId() {
        return UserId;
    }
    public void setUserId(String userId) {
        UserId = userId;
    }
    public String getIMEI() {
        return IMEI;
    }
    public void setIMEI(String iMEI) {
        IMEI = iMEI;
    }
    public String getDeviceType() {
        return DeviceType;
    }
    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }
    public String getPCDType() {
        return PCDType;
    }
    public void setPCDType(String pCDType) {
        PCDType = pCDType;
    }
    public String getScreenSize() {
        return ScreenSize;
    }
    public void setScreenSize(String screenSize) {
        ScreenSize = screenSize;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String gPS_longitude) {
        longitude = gPS_longitude;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String gPS_latitude) {
        latitude = gPS_latitude;
    }
    public String getRegion() {
        return Region;
    }
    public void setRegion(String region) {
        Region = region;
    }
}
