package com.adanur.donttriangle;

public class CreateThread implements Runnable{

	SceneManager scNesne;
	final String turnUser;
	public CreateThread (SceneManager scNesne,final String turnUser) {
	    this.scNesne = scNesne;
	    this.turnUser=turnUser;
	  }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(turnUser.equals(Util.UserName)){
			scNesne.isUserTurn = true;
			//notificationTextView.setText("Your Turn");
		}else{
			scNesne.isUserTurn = false;
			//notificationTextView.setText("Enemy Turn");
		}
	}

}
