package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Slave3Container {

		public static void main(String[] args) {
			try {
			Runtime runtime=Runtime.instance();
			ProfileImpl profileImpl=new ProfileImpl(false);
			profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
			AgentContainer agentContainer=runtime.createAgentContainer(profileImpl);
			AgentController
			agentController=agentContainer.createNewAgent("Slave3",
			"agents.AgentSlave3", new Object[]{});
			agentController.start();
			} catch (Exception e) {
			e.printStackTrace();
			} }
	
}
