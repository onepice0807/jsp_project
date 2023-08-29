package com.ray.etc;

// 페이징 처리를 하기 위한 객체
public class Paginginfo_pointLog {
	private int totalPostCnt; // 전체 게시판의 글의 갯수
	private int viewPostCntPerPage = 10; // 1페이지당 보여줄 글의 갯수
	private int totalPageCnt; // 총 페이지 수
	private int startRowIndex; // 보여주기 시작할 글의 row index 번호
	private int pageNo; // 유저가 클릭한 페이지 번호

	private int pageCntPerBlock = 5; // 한개 블럭에 보여줄 페이지 번호의 갯수
	private int totalPagingBlockCnt; // 전체 페이징 블럭의 갯수
	private int pageBlockOfCurrentPage; // 현재 페이지가 속한 페이징 블럭 번호
	private int startNumCurrentPagingBlock; // 현재 페이징 블럭에서의 출력 시작 페이지 번호
	private int endNumOfCurrentBlock; // 현재 페이징 블럭에서 출력 끝 페이지 번호

	public void setEndNumOfCurrentBlock() {
		// 현재 페이징 블럭 끝 페이지 번호 = 현재 페이징 블럭번호 * pageCntPerBlock
		this.endNumOfCurrentBlock = this.pageBlockOfCurrentPage * this.pageCntPerBlock;
		
		if(this.endNumOfCurrentBlock > this.totalPageCnt) {
			this.endNumOfCurrentBlock = this.totalPageCnt;
		}
	}

	public int getEndNumOfCurrentBlock() {
		return endNumOfCurrentBlock;
	}

	public void setStartNumCurrentPagingBlock() {
		// 현재 페이징 블럭 시작페이지 번호 = ((현재 페이징 블럭번호 -1) *1 pageCntPerBlock) + 1
		this.startNumCurrentPagingBlock = ((this.pageBlockOfCurrentPage - 1) * this.pageCntPerBlock) + 1;

	}

	public int getStartNumCurrentPagingBlock() {
		return startNumCurrentPagingBlock;
	}

	public void setPageBlockOfCurrentPage() {
		// 현재 페이지번호 / pageCntPerBlock -> 나누어 떨어지지 않으면 올림
		if ((this.pageNo % this.pageCntPerBlock) == 0) {
			this.pageBlockOfCurrentPage = this.pageNo / this.pageCntPerBlock;
		} else {
			this.pageBlockOfCurrentPage = (int) (Math.ceil(this.pageNo / (double) this.pageCntPerBlock));
		}

	}

	public int getPageBlockOfCurrentPage() {
		return pageBlockOfCurrentPage;
	}

	public void setTotalPagingBlockCnt() {
		// 전체 페이징 블럭 갯수 = 전체 페이지 수/ pageCntPerBlock -> 나누어 떨어지지 않으면 + 1
		if ((this.totalPageCnt % this.pageCntPerBlock) == 0) {
			this.totalPagingBlockCnt = this.totalPageCnt / this.pageCntPerBlock;
		} else {
			this.totalPagingBlockCnt = (this.totalPageCnt / this.pageCntPerBlock) + 1;
		}

	}

	public int getTotalPagingBlockCnt() {
		return totalPagingBlockCnt;
	}

	public int getPageCntPerBlock() {
		return pageCntPerBlock;
	}

	public void setPageCntPerBlock(int pageCntPerBlock) {
		this.pageCntPerBlock = pageCntPerBlock;
	}

	public void setTotalPostCnt(int totalPostCnt) {
		this.totalPostCnt = totalPostCnt;
	}

	public int getTotalPostCnt() {
		return this.totalPostCnt;
	}

	public int getViewPostCntPerPage() {
		return viewPostCntPerPage;
	}

	public void setViewPostCntPerPage(int viewPostCntPerPage) {
		this.viewPostCntPerPage = viewPostCntPerPage;
	}

	public void setTotalPostCnt(int totalPostCnt, int viewPostCntPerPage) {
		// 총 페이지수는 = 게시판의 글수 / 한페이지당 보여줄 글의 갯수 -> 나누어떨어지지 않으면 + 1
		if ((totalPostCnt % viewPostCntPerPage) == 0) {
			this.totalPageCnt = totalPostCnt / viewPostCntPerPage;
		} else {
			this.totalPageCnt = (totalPostCnt / viewPostCntPerPage) + 1;
		}
	}

	public int getTotalPageCnt() {
		return totalPageCnt;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setStartRowIndex() {
		this.startRowIndex = (this.pageNo - 1) * this.viewPostCntPerPage;
	}

	public int getStartRowIndex() {
		return this.startRowIndex;

	}

	@Override
	public String toString() {
		return "Paginginfo [totalPostCnt=" + totalPostCnt + ", viewPostCntPerPage=" + viewPostCntPerPage
				+ ", totalPageCnt=" + totalPageCnt + ", startRowIndex=" + startRowIndex + ", pageNo=" + pageNo
				+ ", pageCntPerBlock=" + pageCntPerBlock + ", totalPagingBlockCnt=" + totalPagingBlockCnt
				+ ", pageBlockOfCurrentPage=" + pageBlockOfCurrentPage + ", startNumCurrentPagingBlock="
				+ startNumCurrentPagingBlock + ", endNumOfCurrentBlock=" + endNumOfCurrentBlock + "]";
	}

}
