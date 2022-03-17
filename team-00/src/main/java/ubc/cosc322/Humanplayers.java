
package ubc.cosc322;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 */
public class Humanplayers extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
    private String userName = null;
    private String passwd = null;
 
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	//COSC322Test2 player = new COSC322Test2("A","B");
    	HumanPlayer player =  new HumanPlayer();

    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
    /**
     * Any name and passwd;
     * @param userName
      * @param passwd
     */
    public Humanplayers(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	this.gamegui = new BaseGameGUI(this);
    }
 

    @Override
    public void onLogin() {
        userName = gameClient.getUserName(); 
         if(gamegui != null) { 
        	gamegui.setRoomInformation(gameClient.getRoomList()); 
        } 

    }

    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
        switch (messageType) {
        case GameMessage.GAME_STATE_BOARD:
        	//block(msgDetails);
            gamegui.setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
            break;
        case GameMessage.GAME_ACTION_MOVE:
        	block(msgDetails);
            gamegui.updateGameState(msgDetails);
            break;
        default:
            break;
        }
        return true;
    }    
    
    @Override
    public String userName() {
    	return this.userName;
    }

	@Override
	public GameClient getGameClient() {
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		return  this.gamegui;
	}

	@Override
	public void connect() {
    	gameClient = new GameClient(userName, passwd, this);	
 		System.out.println(gameClient);

	}
	
	public int block(Map<String, Object> msgDetails) {
		Object b =  msgDetails.values().toArray()[0];
		ArrayList list=(ArrayList) b;
		for(int i = 0; i < list.size();i+=1) {
			System.out.print(list.get(i)+"          ");
			if(i%10==0 && i != 0) {
				i++;
				System.out.println();
			}
		}
		System.out.println(list.size());
		return 0;
	}
}//end of class
