package com.woting.gatherdata.web;

import java.sql.Timestamp;
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
import com.woting.gatherdata.persis.pojo.Data;

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
        List<Data> l=null;
        try {
            l=(List<Data>)JsonUtils.jsonToObj(jsonParamStr, List.class);
        } catch(Exception e) {
        }
        if (l==null) return null;

        //2解析原始数据
        for (Data oneData: l) {
            ApiLogPo alPo=ApiGatherUtils.buildApiLogDataFromRequest(request);
            //从oneData获取数据，并填入alPo
            //=====================
            alPo.setBeginTime(new Timestamp(Long.valueOf(oneData.getBeginTime())));
            alPo.setDealFlag(1);// 处理成功
            alPo.setOwnerType(201);
            alPo.setOwnerId("--");
            alPo.setApiName(oneData.getApiName());
            alPo.setObjId(oneData.getObjId());
            alPo.setObjType(oneData.getObjType());
            alPo.setDeviceId(oneData.getIMEI());
            alPo.setDeviceType(Integer.valueOf(oneData.getDeviceType()));
            if (oneData.getReqParam()!=null) alPo.setReqParam(oneData.getReqParam().toString());
            alPo.setEndTime(new Timestamp(System.currentTimeMillis()));
            try {
                ApiGatherMemory.getInstance().put2Queue(alPo);
            } catch (InterruptedException e) {}
        }
        return null;
    }
}