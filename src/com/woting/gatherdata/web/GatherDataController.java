package com.woting.gatherdata.web;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.util.RequestUtils;
import com.spiritdata.framework.util.StringUtils;
import com.woting.cm.core.media.MediaType;
import com.woting.cm.playcount.service.PlayCountService;
import com.woting.gatherdata.ApiGatherUtils;
import com.woting.gatherdata.mem.ApiGatherMemory;
import com.woting.gatherdata.persis.pojo.ApiLogPo;

@Controller
public class GatherDataController {
    @Resource
    private PlayCountService playCountService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value="sendGatherData.do")
    @ResponseBody
    public Map<String,Object> gatherData(HttpServletRequest request) {
        //获取数据
        Map<String, Object> m = RequestUtils.getDataFromRequest(request);
        if (m==null) return null;
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
            if (oneData.get("UserId")!=null && !oneData.get("UserId").equals("")) {
                alPo.setOwnerId(oneData.get("UserId").toString());//保存用户ID
            }
            if (oneData.get("ApiName")!=null && !oneData.get("ApiName").equals("")) {
                alPo.setApiName(oneData.get("ApiName").toString());//保存事件类型
            }
            if (oneData.get("ObjId")!=null && !oneData.get("ObjId").equals("")) {
                alPo.setObjId(oneData.get("ObjId").toString());
            }
            if (oneData.get("ObjType")!=null && !oneData.get("ObjType").equals("")) {
                alPo.setObjType(oneData.get("ObjType").toString());
            }
            if (oneData.get("IMEI")!=null && !oneData.get("IMEI").equals("")) {
                alPo.setDeviceId(oneData.get("IMEI").toString());
            }
            if (oneData.get("DeviceType")!=null && !oneData.get("DeviceType").equals("")) {
                alPo.setDeviceType(Integer.valueOf(oneData.get("DeviceType").toString()));
            }
            if (oneData.get("ReqParam")!=null && !oneData.get("ReqParam").equals("")) {
                alPo.setReqParam(oneData.get("ReqParam").toString());
            }
            if (oneData.get("UserId")!=null && !oneData.get("UserId").equals("")) {
                alPo.setOwnerId(oneData.get("UserId").toString());
            } else {
                alPo.setOwnerId("0");
            }
//            if (oneData.get("EndTime")!=null && !oneData.get("EndTime").equals("")) {
//                alPo.setEndTime(Timestamp.valueOf(oneData.get("EndTime").toString()));
//            }
            String apiName=alPo.getApiName();
            if (apiName.equals("E-play")||apiName.equals("E-pause")||apiName.equals("E-close")) {//在这种情况下，EndTime是播放偏移量
                //处理播放时间
                long offsetTime=0;
                try {
                    offsetTime=Long.parseLong(oneData.get("EndTime")+"");
                    offsetTime=offsetTime*1000;
                    if (offsetTime==0) offsetTime=100;
                } catch(Exception e){}
                alPo.setEndTime(new Timestamp(offsetTime));
            } else {
                alPo.setEndTime(new Timestamp(System.currentTimeMillis()));
            }
            try {
                ApiGatherMemory.getInstance().put2Queue(alPo);
            } catch (InterruptedException e) {}
        }
        return null;
    }

    @RequestMapping(value="setPlayCount.do")
    @ResponseBody
    public Map<String,Object> setPlayCount(HttpServletRequest request) {
        //获取数据
        Map<String, Object> m = RequestUtils.getDataFromRequest(request);
        Map<String,Object> retMap=new HashMap<String, Object>();
        if (m==null) {
            retMap.put("ReturnType", "0000");
            retMap.put("Message", "无法获取需要的参数");
            return retMap;
        }
        //1-得到服务标识
        String serverName=(m.get("ServerName")==null?null:m.get("ServerName")+"");
        int pcdType=-1;
        try {pcdType=Integer.parseInt(m.get("PCDType")+"");} catch(Exception e) {pcdType=-1;}
        if (pcdType!=0||!"抓取服务".equals(serverName)) {
            retMap.put("ReturnType", "2001");
            retMap.put("Message", "没有服务标识不合法：ServerName="+m.get("ServerName")+";PCDType="+m.get("PCDType"));
            return retMap;
        }
        //2-得到节目类型
        String mediaType=(m.get("MediaType")==null?null:m.get("MediaType")+"");
        MediaType MT=MediaType.buildByTypeName(mediaType);
        if (MT==MediaType.ERR) {
            retMap.put("ReturnType", "2002");
            retMap.put("Message", "节目类型不合法：MediaType="+m.get("MediaType"));
            return retMap;
        }
        //3-得到节目Id
        String contentId=(m.get("ContentId")==null?null:m.get("ContentId")+"");
        if (StringUtils.isNullOrEmptyOrSpace(contentId)) {
            retMap.put("ReturnType", "2003");
            retMap.put("Message", "节目Id不能为空");
            return retMap;
        }
        //4-得到播放次数
        long playCount=-1l;
        try {playCount=Long.parseLong(m.get("PlayCount")+"");} catch(Exception e) {playCount=-1l;}
        if (playCount==-1l) {
            retMap.put("ReturnType", "2004");
            retMap.put("Message", "播放次数不合法：PlayCount="+m.get("PlayCount"));
            return retMap;
        }
        //5-得到发布组织
        String publisher=(m.get("Publisher")==null?null:m.get("Publisher")+"");
        if (StringUtils.isNullOrEmptyOrSpace(publisher)) {
            retMap.put("ReturnType", "2005");
            retMap.put("Message", "发布组织名称不能为空");
            return retMap;
        }
        int flag=playCountService.setPlayCount(MT, contentId, playCount, publisher);
        if (flag==0) {
            retMap.put("ReturnType", "1003");
            retMap.put("Message", "节目不存在");
        } else if (flag==1) {
            retMap.put("ReturnType", "1001");
            retMap.put("Message", "调整成功");
        } else {
            retMap.put("ReturnType", "1002");
            retMap.put("Message", "调整失败");
        }
        return retMap;
    }
}
