package agents;
import Algorithm.Segmentation_KMeans;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
public class AgentSlave1 extends Agent  {

	protected void setup() {
		
		System.out.println("Hi! i'm an Agent");
		System.out.println("My name  is : "+getLocalName());
		System.out.println("I prepare to achieve my goal");
	addBehaviour(new CyclicBehaviour() {
	@Override
	public void action() {
	try {
	MessageTemplate template=
	MessageTemplate.or(
	MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
	MessageTemplate.or(
	MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
	MessageTemplate.MatchPerformative(ACLMessage.REQUEST))
	);
	ACLMessage aclMessage=receive(template);
	if(aclMessage!=null){
	
	String src=aclMessage.getContent();
    int k = 2; 
    String m = "-1"; 
    int mode = 1; 
    if (m.equals("-c")) { 
        mode = 2; 
    } else if (m.equals("-c")) { 
        mode = 1; 
    } 
    // create new KMeans object 
    Segmentation_KMeans kmeans = new Segmentation_KMeans(); 
    // call the function to actually start the clustering 
    String newPath=kmeans.calculate(src, k,mode); 
    ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
	msg.setContent(newPath);
	msg.addReceiver(new AID("Master",AID.ISLOCALNAME));
	send(msg);
		
	}//end if()
		else{
		block();
		}
	
	} catch (Exception e) {
		e.printStackTrace();
		}

	
	}});
	
	}
	

}
