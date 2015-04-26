package com.adanur.donttriangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayedLevel {
	SceneManager scnNesnesi;
	private Random randomSayi;
	
	public PlayedLevel(SceneManager scnNesne) {
		this.scnNesnesi = scnNesne;
	}
	
	public void createPlayedLevel(int hamle,int noktaCount){
		List<String> cizilmeyenCizgiList = new ArrayList<String>();
		List<Integer> cizilecekCizgiList = new ArrayList<Integer>();
		cizilmeyenCizgiList.clear();
		String veri="";
		scnNesnesi.hamleliOyunMu=true;
		for (int i = 0; i < noktaCount; i++) {
			for (int j = 0; j < noktaCount; j++) {
				if (j>i) {
					veri=""+i+"-"+j;
					cizilmeyenCizgiList.add(veri);
				}
			}
		}
		while (cizilmeyenCizgiList.size() > 0) {

			randomSayi = new Random();
			int sayi =1+ randomSayi.nextInt(cizilmeyenCizgiList.size());
			String cizilecek = cizilmeyenCizgiList.get(sayi-1);

			String[] parts = cizilecek.split("-");
			int part1 = Integer.valueOf(parts[0]);
			int part2 = Integer.valueOf(parts[1]);

			scnNesnesi.cizilenCizgiArray[part1][part2] = true;
			scnNesnesi.cizilenCizgiArray[part2][part1] = true;
			if (scnNesnesi.triControl.fonkTriangleControl() == null) {
				cizilecekCizgiList.add(part1);
				cizilecekCizgiList.add(part2);
				cizilmeyenCizgiList.remove(sayi-1);
			} else {
				scnNesnesi.cizilenCizgiArray[part1][part2] = false;
				scnNesnesi.cizilenCizgiArray[part2][part1] = false;
				cizilmeyenCizgiList.remove(sayi-1);
			}
		}
		for (int i = 1; i < hamle*2; i=i+2) {
			scnNesnesi.cizilenCizgiArray[cizilecekCizgiList.get(cizilecekCizgiList.size()-i)][cizilecekCizgiList.get(cizilecekCizgiList.size()-(i+1))] = false;
			scnNesnesi.cizilenCizgiArray[cizilecekCizgiList.get(cizilecekCizgiList.size()-(i+1))][cizilecekCizgiList.get(cizilecekCizgiList.size()-i)] = false;
		}
		for (int i = 0; i < cizilecekCizgiList.size() - (hamle * 2); i = i + 2) {
			scnNesnesi.drawLine(scnNesnesi.koordinatlar[cizilecekCizgiList.get(i)][0],
					scnNesnesi.koordinatlar[cizilecekCizgiList.get(i)][1],
					scnNesnesi.koordinatlar[cizilecekCizgiList.get(i + 1)][0],
					scnNesnesi.koordinatlar[cizilecekCizgiList.get(i + 1)][1], 10);
		}
		
		
	}

}
