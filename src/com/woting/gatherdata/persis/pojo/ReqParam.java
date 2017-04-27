package com.woting.gatherdata.persis.pojo;

import com.spiritdata.framework.core.model.BaseObject;

public class ReqParam extends BaseObject {
    private static final long serialVersionUID = -1518755052891055174L;

    private String CatalogType;
    private String CatalogId;
    private String FilterData;
    private String ResultType;
    private String PageType;
    private String MediaType;
    private String PerSize;
    private String PageSize;
    private String Page;
    private String BeginCatalogId;
    private String PageIndex;

    public String getCatalogType() {
        return CatalogType;
    }
    public void setCatalogType(String catalogType) {
        CatalogType = catalogType;
    }
    public String getCatalogId() {
        return CatalogId;
    }
    public void setCatalogId(String catalogId) {
        CatalogId = catalogId;
    }
    public String getFilterData() {
        return FilterData;
    }
    public void setFilterData(String filterData) {
        FilterData = filterData;
    }
    public String getResultType() {
        return ResultType;
    }
    public void setResultType(String resultType) {
        ResultType = resultType;
    }
    public String getPageType() {
        return PageType;
    }
    public void setPageType(String pageType) {
        PageType = pageType;
    }
    public String getMediaType() {
        return MediaType;
    }
    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }
    public String getPerSize() {
        return PerSize;
    }
    public void setPerSize(String perSize) {
        PerSize = perSize;
    }
    public String getPageSize() {
        return PageSize;
    }
    public void setPageSize(String pageSize) {
        PageSize = pageSize;
    }
    public String getPage() {
        return Page;
    }
    public void setPage(String page) {
        Page = page;
    }
    public String getBeginCatalogId() {
        return BeginCatalogId;
    }
    public void setBeginCatalogId(String beginCatalogId) {
        BeginCatalogId = beginCatalogId;
    }
    public String getPageIndex() {
        return PageIndex;
    }
    public void setPageIndex(String pageIndex) {
        PageIndex = pageIndex;
    }

    @Override
    public String toString() {
        return "ReqParam [CatalogType=" + CatalogType + ", CatalogId="
                + CatalogId + ", FilterData=" + FilterData + ", ResultType="
                + ResultType + ", PageType=" + PageType + ", MediaType="
                + MediaType + ", PerSize=" + PerSize + ", PageSize=" + PageSize
                + ", Page=" + Page + ", BeginCatalogId=" + BeginCatalogId
                + ", PageIndex=" + PageIndex + "]";
    }
}
