package com.example.demo.api.jwt.entity;

public class IndexReport {
	/***
	 * 总人数
	 */
	private int userNum;
	/***
	 * 总社团数
	 */
	private int clubNum;
	/***
	 * 总活动
	 */
	private int activityNum;
	/***
	 * 今日新增所有人数
	 */
	private int todayUserNum;
	/***
	 * 今日新增社团数
	 */
	private int todayClubNum;
	/***
	 * 今日新增活动数
	 */
	private int todayActivityNum;
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public int getClubNum() {
		return clubNum;
	}
	public void setClubNum(int clubNum) {
		this.clubNum = clubNum;
	}
	public int getActivityNum() {
		return activityNum;
	}
	public void setActivityNum(int activityNum) {
		this.activityNum = activityNum;
	}
	public int getTodayUserNum() {
		return todayUserNum;
	}
	public void setTodayUserNum(int todayUserNum) {
		this.todayUserNum = todayUserNum;
	}
	public int getTodayClubNum() {
		return todayClubNum;
	}
	public void setTodayClubNum(int todayClubNum) {
		this.todayClubNum = todayClubNum;
	}
	public int getTodayActivityNum() {
		return todayActivityNum;
	}
	public void setTodayActivityNum(int todayActivityNum) {
		this.todayActivityNum = todayActivityNum;
	}
	
	
}
