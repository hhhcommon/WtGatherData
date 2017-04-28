package com.woting.gatherdata.web;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.util.RequestUtils;
import com.spiritdata.framework.util.StringUtils;
import com.woting.gatherdata.ApiGatherUtils;
import com.woting.gatherdata.mem.ApiGatherMemory;
import com.woting.gatherdata.persis.pojo.ApiLogPo;

@Controller
public class GatherDataController {
    @SuppressWarnings("unchecked")
    @RequestMapping(value="sendGatherData.do")
    @ResponseBody
    public Map<String,Object> gatherData(HttpServletRequest request) {
        //获取数据
        Map<String, Object> m = RequestUtils.getDataFromRequest(request);
        //1-获得原始数据
        String jsonParamStr=(m.get("data")==null?null:(String)m.get("data"));
        if (StringUtils.isNullOrEmptyOrSpace(jsonParamStr)) return null;
        List<String> l=null;
        try {
            l=(List<String>)JsonUtils.jsonToObj(jsonParamStr, List.class);
        } catch(Exception e) {
        }
        if (l==null) return null;

        //2解析原始数据
        for (String oneObj: l) {
            Map<String, Object> oneData=(Map<String, Object>) JsonUtils.jsonToObj(oneObj, HashMap.class);
            if (oneData==null) return null;
            ApiLogPo alPo=ApiGatherUtils.buildApiLogDataFromRequest(request);
            //从oneData获取数据，并填入alPo
            //=====================
            if (oneData.get("BeginTime")!=null) {
                alPo.setBeginTime(new Timestamp(Long.valueOf((String) oneData.get("BeginTime"))));
            }
            alPo.setDealFlag(1);// 处理成功
            alPo.setOwnerType(201);
            alPo.setMethod("POST");
            if (oneData.get("UserId")!=null) {
                alPo.setOwnerId((String)oneData.get("UserId"));//保存用户ID
            }
            if (oneData.get("ApiName")!=null) {
                alPo.setApiName((String)oneData.get("ApiName"));//保存事件类型
            }
            if (oneData.get("ObjId")!=null) {
                alPo.setObjId((String)oneData.get("ObjId"));
            }
            if (oneData.get("ObjType")!=null) {
                alPo.setObjType((String)oneData.get("ObjType"));
            }
            if (oneData.get("IMEI")!=null) {
                alPo.setDeviceId((String)oneData.get("IMEI"));
            }
            if (oneData.get("DeviceType")!=null) {
                alPo.setDeviceType(Integer.valueOf((String)oneData.get("DeviceType")));
            }
            if (oneData.get("ReqParam")!=null) {
                alPo.setReqParam((String)oneData.get("ReqParam"));
            }
            alPo.setEndTime(new Timestamp(System.currentTimeMillis()));
            try {
                ApiGatherMemory.getInstance().put2Queue(alPo);
            } catch (InterruptedException e) {}
        }
        return null;
    }
}