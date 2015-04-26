package app.shephertz.multiplayer;


import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import com.adanur.donttriangle.SceneManager;
import com.adanur.donttriangle.SceneManager.AllScenes;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;

public class WarpController {

	private static WarpController instance;
	
	private WarpClient warpClient;
	
	private String userName;
	private SceneManager scneNesne;
	
	private String roomId = "";
		
	private boolean isConnected = false;
	private boolean isNewRoomCreated = false;
	private boolean isGameStated = false;
	
	public WarpController() {
		initAppwarp();
		warpClient.addConnectionRequestListener(new ConnectionListener(this));
		warpClient.addZoneRequestListener(new ZoneListener(this));
		warpClient.addRoomRequestListener(new RoomListener(this));
		warpClient.addNotificationListener(new NotificationListener(this));
	}
	
	public static WarpController getInstance(){
		if(instance == null){
			instance = new WarpController();
		}
		return instance;
	}
	
	public void setActivity(SceneManager scneNesne){
		this.scneNesne = scneNesne;
	}
	
	public void startApp(String userName, SceneManager scneNesne) {
		this.userName = userName;
		warpClient.connectWithUserName(userName);
	}
	
	public void sendMove(String message) {
		warpClient.sendMove(message);
	}
	
	public void stopApp(){
		if(isConnected){
			warpClient.unsubscribeRoom(roomId);
			warpClient.leaveRoom(roomId);
			if(!isGameStated){
				warpClient.deleteRoom(roomId);
			}
		}
		warpClient.disconnect();
		isConnected = false;
		isNewRoomCreated = false;
		isGameStated = false;
	}
	
	private void initAppwarp(){
		try {
			WarpClient.initialize(WarpConstants.API_KEY, WarpConstants.SECRET_KEY);
			warpClient = WarpClient.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void onConnectDone(boolean status){
		log("onConnectDone: "+status);
		if(status){
			warpClient.joinRoomInRange(1, 1, false);
		}else{
			isConnected = false;
		}
	}
	
	public void onDisconnectDone(boolean status){
		
	}
	
	public void onJoinRoomDone(RoomEvent event){
		log("onJoinRoomDone: "+event.getResult());
		if(event.getResult()==WarpResponseResultCode.SUCCESS){
			warpClient.subscribeRoom(event.getData().getId());
		}else if(event.getResult()==WarpResponseResultCode.RESOURCE_NOT_FOUND){
			isNewRoomCreated = true;
			warpClient.createTurnRoom("DontTriangle", userName, 2, null, 30);
		}else{
			warpClient.disconnect();
		}
	}
	
	public void onSubscribeRoomDone(String roomId){
		if(roomId!=null){
			isConnected = true;
			if(isNewRoomCreated){// he must be first user
				scneNesne.waitUserText.oSprite.setVisible(true);
				scneNesne.gameStartText.oSprite.setVisible(false);
				//activity.updateNotification("Waiting for other user");
				Log.d("onSubscribeRoomDone", "Waiting for other user");
			}else{// he must be second user
				isGameStated = true;
				scneNesne.waitUserText.oSprite.setVisible(false);
				scneNesne.gameStartText.oSprite.setVisible(true);
				
				//activity.updateNotification("Game Started");
				Log.d("onSubscribeRoomDone", "Game Started");
				warpClient.startGame();
			}
			
		}else{
			warpClient.disconnect();
		}
	}
	
	public void onRoomCreated(String roomId){
		if(roomId!=null){
			this.roomId = roomId;
			warpClient.joinRoom(roomId);
		}else{
			warpClient.disconnect();
		}
	}
	
	public void onUserJoinedRoom(String roomId, String userName){
		if(isNewRoomCreated){
			isGameStated = true;
			warpClient.startGame();
			
		}
	}
	
	public void onUserLeftRoom(String roomId, String userName){
		if(isGameStated){
			isGameStated = false;
			warpClient.deleteRoom(roomId);//ben ekledim
			scneNesne.MulMetdsNsn.onEnemyLeft();
		}
	}
	
	public void onGameStarted(String sender, String roomId, String nextTurn){
		scneNesne.MulMetdsNsn.startGame(nextTurn);
		
		new Timer().schedule(new TimerTask() {   
		    @Override
		    public void run() {
		    	scneNesne.createMultiPlayerGameScene();
				scneNesne.setCurrentScene(AllScenes.MULTIGAME);
		    }
		}, 1000);
	}
	
	public void onGameStopped(String roomId, String userName){
		isGameStated = false;
	}

	public void onMoveCompleted(String moveData, String sender, String nextTurn){
			scneNesne.MulMetdsNsn.onMoveCompleted(moveData, sender, nextTurn);
		
	}


	private void log(String message){
		System.out.println(message);
	}
	
}
