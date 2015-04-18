package com.adanur.donttriangle;

public class PointsKoordinat {
	
	private int[][] koor;
	
	public PointsKoordinat(int pointCount){
			int aci = 360 / pointCount;
			koor = new int[pointCount][2];
			for (int i = 0; i < pointCount; i++) {
				koor[i][0] = (int) (200 * (Math.cos(Math.toRadians(i * aci))) + 240);
				koor[i][1] = (int) (200 * (Math.sin(Math.toRadians(i * aci))) + 400);
			}
		
	}
	public int[][] getKoor() {
		return koor;
	}	

}
