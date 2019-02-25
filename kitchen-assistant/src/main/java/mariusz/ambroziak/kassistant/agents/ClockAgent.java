package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.logging.Level;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;


public class ClockAgent extends BaseAgent {

	private static final String ClockAgentDescription = "Agent whose whole purpose is to measure time for other agents. "
			+ "";

	/**
	 * 
	 */

	
	private static float timePassed=0;

	ArrayList<AgentAddress> adresses;
	
	@Override
	protected void live() {
		while(true){
			timePassed+=1f;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.getExistingRoles(
					this.getExistingCommunities().first()
					, 
			this.getExistingGroups(
			this.getExistingCommunities().first()
			).first());
		}
	}

	public static float getTimePassed() {
		return timePassed;
	}

	@Override
	public String toString() {
		
		return super.toString()+"\n\n"+getTimePassed();
	}

	@Override
	protected void activate() {
//		timePassed=System.currentTimeMillis();
		setLogLevel(Level.FINEST);
		setDescription(ClockAgentDescription);


	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}

}
