
package ubc.cosc322;

import ygraph.ai.smartfox.games.amazons.HumanPlayer;
import java.util.ArrayList;
import java.util.Collection;
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
 * 
 * @author Yong Gao (yong.gao@ubc.ca) Jan 5, 2021
 */
public class COSC322Test extends GamePlayer {

	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;
	private String userName = null;
	private String passwd = null;
	String[][] stat = new String[10][10];

	/**
	 * The main method
	 * 
	 * @param args for name and passwd (current, any string would work)
	 */
	public static void main(String[] args) {
		COSC322Test player = new COSC322Test(args[0], args[1]);
		// HumanPlayer player = new HumanPlayer();

		if (player.getGameGUI() == null) {
			player.Go();
		} else {
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
	 * 
	 * @param userName
	 * @param passwd
	 */
	public COSC322Test(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;
		this.gamegui = new BaseGameGUI(this);
	}

	@Override
	public void onLogin() {
		userName = gameClient.getUserName();
		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}

	}

	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		switch (messageType) {
		case GameMessage.GAME_STATE_BOARD:
			establish(msgDetails.get(AmazonsGameMessage.GAME_STATE));
			// updates the gui
			gamegui.setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
			break;
		case GameMessage.GAME_ACTION_MOVE:
			upd(msgDetails);
			// updates gui
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
		return this.gamegui;
	}

	@Override
	public void connect() {
		gameClient = new GameClient(userName, passwd, this);
		System.out.println(gameClient);

	}

	//gets the initial board state usually only called once
	public int establish(Object object) {
		String list2 = object.toString();
		String[] list = list2.replaceAll("\\[", "").replaceAll("]", "").split(",");
		//removes uninportant info
		int j = 0;
		int row = 0;
		for (int i = 12; i < list.length; i += 1) {
			if (i % 11 != 0) {
				stat[row][j]= list[i];
				System.out.print(list[i] + "          ");
				if (j++ == 9) {
					row++;
					j = 0;
					System.out.println();
				}
			}
		}
		return 0;
	}

	//updates board state
	public void upd(Map msg) {
		ArrayList curr=(ArrayList) msg.get("queen-position-current");
		ArrayList next=(ArrayList) msg.get("queen-position-next");
		ArrayList arr = (ArrayList) msg.get("arrow-position");
		

		
		//moving queen to next pos
		String temp = this.stat[(int) curr.get(0)-1][(int) curr.get(1)-1];
		this.stat[(int) next.get(0)-1][(int) next.get(1)-1]=temp;
		//clearing current pos
		this.stat[(int) curr.get(0)-1][(int) curr.get(1)-1]="0";
		//setting arrow
		this.stat[(int) arr.get(0)-1][(int) arr.get(1)-1]="3";

		for (int i =0;i<10;i++) {
			for (int j = 0; j < 10;j++) {
				System.out.print(this.stat[i][j]+"           ");
			}
			System.out.println();
		}
		
		eval(this.stat,temp);
	}

	//performs evaluation on board state
	public int eval(String[][] state,String player) {
		int res=0;
		int queens = 0;
		for( int i = 0; i < 10;i++) {
			for (int j =0;j<10;j++) {
				//if is a player queen get score of queen
				if(this.stat[i][j].equals(player)) {
					res+=score(state,player,i,j);
					queens++;
				}
				//checks to see if all of the queens have been evaluated
				if(queens == 4)
					break;
			}
		}	
		System.out.println(queens);
		System.out.println("Board Score: " + res);
		return res;
	}
	
	public int score(String[][] state,String player,int y, int x) {
		int mult=0;
		//checks if any of the surrounding peices are opponents peices
		String opp;
		if(player.equals("1"))
			opp="2";
		else
			opp="1";

		if(x>0&&y>0&&state[x-1][y-1].equals(opp))
			mult++;
		else if(y>0&&state[x][y-1].equals(opp))
			mult++;
		else if(x<9&&y>0&&state[x+1][y-1].equals(opp))
			mult++;
		else if(x>0&&state[x-1][y].equals(opp))
			mult++;
		else if(x<9&&state[x+1][y].equals(opp))
			mult++;
		else if(x>0&&y<9&&state[x-1][y+1].equals(opp))
			mult++;
		else if(y<9&&state[x][y+1].equals(opp))
			mult++;
		else if(x<9&&y<9&&state[x+1][y+1].equals(opp))
			mult++;
		System.out.println("Score at: " +x+","+y+":"+mult);
		return mult;
		
	}
}// end of class
