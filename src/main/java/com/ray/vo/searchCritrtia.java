package com.ray.vo;

public class searchCritrtia {
	private String searchWord;
	private String searchType;

	public searchCritrtia(String searchWord, String searchType) {
		super();
		this.searchWord = searchWord;
		this.searchType = searchType;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	@Override
	public String toString() {
		return "searchCritrtia [searchWord=" + searchWord + ", searchType=" + searchType + "]";
	}

}
