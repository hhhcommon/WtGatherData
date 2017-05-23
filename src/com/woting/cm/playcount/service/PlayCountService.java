package com.woting.cm.playcount.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.spiritdata.framework.core.dao.mybatis.MybatisDAO;
import com.spiritdata.framework.core.model.BaseObject;
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
        int ret=-1;//失败
        try {
            
        } catch(Exception e) {
            e.printStackTrace();
            ret=-1;
        }
        return ret;
    }
}