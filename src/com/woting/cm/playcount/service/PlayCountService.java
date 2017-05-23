package com.woting.cm.playcount.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.spiritdata.framework.core.dao.mybatis.MybatisDAO;
import com.spiritdata.framework.core.model.BaseObject;
import com.spiritdata.framework.util.SequenceUUID;
import com.woting.cm.core.media.MediaType;

@Service
public class PlayCountService {
    @Resource(name="defaultDAO")
    private MybatisDAO<BaseObject> contentDao;
    @Resource(name="cacheDAO")
    private MybatisDAO<BaseObject> cacheDao;

    @PostConstruct
    public void initParam() {
        contentDao.setNamespace("WT_CONTENT");
        cacheDao.setNamespace("CACHEDB");
    }
    /**
     * 设置播放次数
     * @param mt 节目类型
     * @param contentId 节目Id
     * @param playCount 播放次数
     * @param publisher 发布组织名称
     * @return 0=节目不存在；1=设置成功；-1设置失败
     */
    public int setPlayCount(MediaType mt, String contentId, long playCount, String publisher) {
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("tableName", mt.getTabName());
        param.put("contentId", contentId);
        param.put("playCount", playCount);
        param.put("publisher", publisher);

        int ret=-1;//失败
        try {
            //判断节目是否存在
            int c=contentDao.getCount("existContent", param);
            if (c!=1) return 0;//节目不存在

            //设置播放次数
            c=contentDao.getCount("existPlayCount", param);
            if (c==1) contentDao.update("setPlayCount", param);
            else if (c==0) {
                param.put("id", SequenceUUID.getPureUUID());
                contentDao.insert("insertPlayCount", param);
            } else {//多于一个删除掉
                contentDao.delete("deleteDuplicate", param);
                contentDao.insert("insertPlayCount", param);
            }
            adjustCachePlayCount(mt, contentId);
        } catch(Exception e) {
            e.printStackTrace();
            ret=-1;
        }
        return ret;
    }

    /**
     * 我听平台，播放次数自增一
     * @param 节目类型
     * @param contentId 节目Id
     * @return 0=节目不存在；1=自增成功；-1自增失败
     */
    public int increamPlayCount(MediaType mt, String contentId) {
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("tableName", mt.getTabName());
        param.put("contentId", contentId);
        param.put("publisher", "我听科技");

        int ret=-1;//失败
        try {
            //判断节目是否存在
            int c=contentDao.getCount("existContent", param);
            if (c!=1) return 0;//节目不存在

            //设置播放次数
            c=contentDao.getCount("existPlayCount", param);
            if (c==1) contentDao.update("increamPlayCount", param);
            else if (c==0) {
                param.put("id", SequenceUUID.getPureUUID());
                param.put("playCount", 1l);
                contentDao.insert("insertPlayCount", param);
            } else {//多于一个删除掉
                long l=contentDao.queryForObjectAutoTranform("getMaxPlayCount", param);
                contentDao.delete("deleteDuplicate", param);
                param.put("playCount", l+1);
                contentDao.insert("insertPlayCount", param);
            }
            adjustCachePlayCount(mt, contentId);
        } catch(Exception e) {
            e.printStackTrace();
            ret=-1;
        }
        return ret;
    }

    /*
     * 调整CacheDB中的播放次数
     * @param mt 节目类型
     * @param contentId 节目Id
     * @return 0=没有播放次数，无法调整；1=调整成功；-1=调整失败
     */
    private int adjustCachePlayCount(MediaType mt, String contentId) {
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("tableName", mt.getTabName());
        param.put("contentId", contentId);

        int ret=-1;//失败
        try {
            //判断节目是否存在
            List<Long> pl=contentDao.queryForListAutoTranform("getPlayCountListByContent", param);
            if (pl==null||pl.isEmpty()) return 0;
            Long maxPlayCount=0l;
            for (Long p: pl) if (p>maxPlayCount) maxPlayCount=p;
            param.put("playCount", maxPlayCount);

            param.put("cacheId", mt.getTabName()+"_"+contentId+"_PLAYCOUNT");
            int c=cacheDao.getCount("existContent", param);
            if (c==1) cacheDao.update("setPlayCount", param);
            else if (c==0) {
                cacheDao.insert("insertPlayCount", param);
            } else {//多于一个删除掉
                cacheDao.delete("deleteDuplicate", param);
                cacheDao.insert("insertPlayCount", param);
            }
        } catch(Exception e) {
            e.printStackTrace();
            ret=-1;
        }
        return ret;
    }
}