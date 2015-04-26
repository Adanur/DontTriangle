package com.adanur.donttriangle;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;

import android.util.Log;
import app.shephertz.multiplayer.WarpController;

public class MultiplayerMetods {
	SceneManager scnManNesne;

	public MultiplayerMetods(SceneManager scnManNesne) {
		this.scnManNesne = scnManNesne;
	}

	public void getIndex(int i, int j) {

		if (i != j) {
			scnManNesne.cizilenCizgiArray[i][j] = true;
			scnManNesne.cizilenCizgiArray[j][i] = true;
			updateUI(i, j);
			checkGameComplete(i, j);
		}

	}

	public void updateUI(int i, int j) {
		if (i != j) {

			scnManNesne.drawLine(scnManNesne.aSprite[i].getX(),
					scnManNesne.aSprite[i].getY(),
					scnManNesne.aSprite[j].getX(),
					scnManNesne.aSprite[j].getY(), 5);
			scnManNesne.cizilenCizgiArray[j][i] = true;
			scnManNesne.cizilenCizgiArray[i][j] = true;
		}

	}

	public void checkGameComplete(int i, int j) {
		boolean r = scnManNesne.gameOverTest();
		scnManNesne.ucgen = scnManNesne.triControl.fonkTriangleControl();
		if (r == true) {
			if (scnManNesne.ucgen != null) {
				WarpController.getInstance()
						.sendMove(i + "#" + j + "/" + "Win");
				scnManNesne.createGameOverScene();

				return;
			} else {
				scnManNesne.createGameWinScene();
				scnManNesne.multiPlayerGameScene
						.setChildScene(scnManNesne.gameWinScene);
				WarpController.getInstance().sendMove(
						i + "#" + j + "/" + "Loose");
				return;
			}
		}
		WarpController.getInstance().sendMove(i + "#" + j + "/" + "");
	}

	public void onMoveCompleted(final String moveData, final String sender,
			final String nextTurn) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("var",""+moveData+" "+sender+" "+Util.UserName);
				if (!sender.equals(Util.UserName) && moveData.length() > 0) {
					final int i = Integer.parseInt(moveData.substring(0,
							moveData.indexOf('#')));
					final int j = Integer.parseInt(moveData.substring(
							moveData.indexOf('#') + 1, moveData.indexOf('/')));
					final String data = moveData.substring(
							moveData.indexOf('#') + 1, moveData.length());
					
					updateUI(i, j);

					if (data.trim().length() > 0) {
						if (data.indexOf("Win") != -1) {
							scnManNesne.isGameStarted = false;
							scnManNesne.createGameWinScene();
							scnManNesne.multiPlayerGameScene
									.setChildScene(scnManNesne.gameWinScene);
						}
						if (data.indexOf("Loose") != -1) {
							scnManNesne.isGameStarted = false;
							scnManNesne.createGameOverScene();
						}
					}

				} else {
					// empty move
					
				}
				//if (moveData.length() > 0) {			
				if (nextTurn.equals(Util.UserName)) {
					if (moveData.equals("")) {
						//kazandi. Cunku karsi taraf oynamadý.
					}
					else{
					scnManNesne.isUserTurn = true;
					if (scnManNesne.yourTurn.oSprite!=null && !scnManNesne.oyunBitti) {
						scnManNesne.yourTurn.oSprite.setVisible(true);
						scnManNesne.rivalsTurn.oSprite.setVisible(false);
						}
					}
					// ("Your Turn");
				} else {
					if (moveData.equals("")) {
						scnManNesne.createGameOverScene();
						try {
							WarpClient.getInstance().disconnect();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
					scnManNesne.isUserTurn = false;
					if (scnManNesne.yourTurn.oSprite!=null && !scnManNesne.oyunBitti) {
						scnManNesne.yourTurn.oSprite.setVisible(false);
						scnManNesne.rivalsTurn.oSprite.setVisible(true);
						}
					// ("Enemy Turn");
					}
				}
			//	}
			}
		}).start();

	}
	public void onEnemyLeft(){
		if(scnManNesne.isGameStarted){
			new Thread(new Runnable() {
				@Override
				public void run() {
					scnManNesne.createGameWinScene();
					scnManNesne.multiPlayerGameScene
							.setChildScene(scnManNesne.gameWinScene);
					//showResultDialog("Congrats! You Win\nEnemy Left");
				}
			}).start();
		}
	}

	public void startGame(final String turnUser) {
		scnManNesne.isGameStarted = true;
		Log.i("turnUser",turnUser);
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (turnUser.equals(Util.UserName)) {//turnUser sen isen sýra sende
					scnManNesne.isUserTurn = true;
					if (scnManNesne.yourTurn.oSprite!=null && !scnManNesne.oyunBitti) {
					scnManNesne.yourTurn.oSprite.setVisible(true);
					scnManNesne.rivalsTurn.oSprite.setVisible(false);
					}
					// notificationTextView.setText("Your Turn");
				} else {
					scnManNesne.isUserTurn = false;
					if (scnManNesne.yourTurn.oSprite!=null && !scnManNesne.oyunBitti) {
					scnManNesne.yourTurn.oSprite.setVisible(false);
					scnManNesne.rivalsTurn.oSprite.setVisible(true);
					}
					// notificationTextView.setText("Enemy Turn");
				}

			}
		}).start();

	}
}
