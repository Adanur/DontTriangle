package com.adanur.donttriangle;

public class TriangleControl {
	private int koorCount;
	private boolean[][] cizilenCizgiArray = new boolean[koorCount][koorCount];
	
	public TriangleControl(int koorCount, boolean[][] cizilenCizgiArray) {
		this.koorCount = koorCount;
		this.cizilenCizgiArray = cizilenCizgiArray;
	}
	
	public int[] fonkTriangleControl(){
		
		int[] ucgen=new int[3];
		for (int i = 0; i < koorCount; i++) {
			for (int j = 0; j < koorCount; j++) {
				if(i!=j){
					if(cizilenCizgiArray[i][j]==true){
						for (int k = 0; k < koorCount; k++) {
							if (cizilenCizgiArray[j][k]==true) {
								if (cizilenCizgiArray[k][i]==true) {
									ucgen[0]=i;
									ucgen[1]=j;
									ucgen[2]=k;
									return ucgen;
								}
							}
						}
					}
					
				}
			}
		}
		
		return null;
		
	}
	
	
}
