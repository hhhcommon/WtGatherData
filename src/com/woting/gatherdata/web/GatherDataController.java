package com.woting.gatherdata.web;

import java.sql.Timestamp;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.woting.gatherdata.mem.ApiGatherMemory;
import com.woting.gatherdata.ApiGatherUtils;
import com.woting.gatherdata.persis.pojo.ApiLogPo;

@Controller
public class GatherDataController {
    @RequestMapping(value="sendGatherData.do")
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request) {
        //数据收集处理==1
        ApiLogPo alPo=ApiGatherUtils.buildApiLogDataFromRequest(request);
        alPo.setApiName("2.1.1-passport/user/mlogin");
        alPo.setObjType("003");//设置为用户
        alPo.setDealFlag(1);//处理成功
        alPo.setOwnerType(201);
        alPo.setOwnerId("--");
        alPo.setApiName("TESTTEST");
        //数据收集处理=3
        alPo.setEndTime(new Timestamp(System.currentTimeMillis()));
        try {
            ApiGatherMemory.getInstance().put2Queue(alPo);
        } catch (InterruptedException e) {}
        return null;
    }
}