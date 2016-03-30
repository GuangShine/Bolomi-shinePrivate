package com.serotonin.mango.vo;


import com.serotonin.json.JsonRemoteEntity;

@JsonRemoteEntity
public  class PageVO {

	private int InfoCount;
	private int NowPage;
	private int PageSize;
	private int PageCount;

	
	public void setInfoCount(int infoCount) {
		InfoCount = infoCount;
	}
	public void setNowPage(int nowPage) {
		NowPage = nowPage;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	public void setPageCount(int pageCount) {
		PageCount = pageCount;
	}
	public int getInfoCount() {
		return InfoCount;
	}
	public int getNowPage() {
		return NowPage;
	}
	public int getPageSize() {
		return PageSize;
	}
	public int getPageCount() {
		return PageCount;
	}

	
}
