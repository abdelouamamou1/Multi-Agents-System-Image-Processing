package agents;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class AgentMaster extends Agent {
    private String[] ImageName=new String[3];

    private String[] sender=new String[3];
    int i=0;
    String OriginalImage;
    
	protected void setup() {
	
		System.out.println("Hi! i'm an Agent");
		System.out.println("My name  is : "+getLocalName());
		System.out.println("I prepare to achieve my goal");
	
		//JFRAME-----------------------------
		JFrame frame = new JFrame("Multi-Agents System & Image Processing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contentPane = new JPanel();
     
        contentPane.setOpaque(true);
        contentPane.setLayout(null);

       
        JButton btn = new JButton("Choose the image");

        btn.setBounds(225,10,150,30);
        contentPane.add(btn);

        JLabel label11 = new JLabel(" Original Image", JLabel.CENTER);
        label11.setSize(300, 50);
        label11.setLocation(-10, 35);

        JLabel label12 = new JLabel();
     
        label12.setSize(250, 249);
        label12.setLocation(10,80);
        contentPane.add(label11);
        contentPane.add(label12);
        
        JLabel label21 = new JLabel("Segmented Image", JLabel.RIGHT);
        label21.setSize(300, 50);
        label21.setLocation(230,35);
        JLabel label22 = new JLabel();
        label22.setSize(250,249);
        label22.setLocation(350,80);
        contentPane.add(label21);
        contentPane.add(label22);
        
        JLabel label31 = new JLabel("Image Contours", JLabel.CENTER);
        label31.setSize(300,50);
        label31.setLocation(-10, 330);
       
        JLabel label32=new JLabel();
       
        label32.setSize(250, 249);
        label32.setLocation(10,370);
        contentPane.add(label31);
        contentPane.add(label32);
 
        JLabel label41 = new JLabel("Bright Image", JLabel.RIGHT);
        label41.setSize(300,50);
        label41.setLocation(220, 330);
       
    
        JLabel label42=new JLabel();
      
        label42.setSize(250, 249);
        label42.setLocation(350,370);
        contentPane.add(label41);
        contentPane.add(label42);
        btn.addActionListener(new ActionListener() {
        
            @Override
            public void actionPerformed(ActionEvent e) {
            
     
            	this.SendMsg();

            	
            }

            
            //GetPath allows to get the path of chosen image
            private String GetPath() {
				String path = "";
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				int returnValue = jfc.showOpenDialog(null);


				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					path=selectedFile.getAbsolutePath();
				}
	
				return path;
			}

            //SendMsg() function sends paths of images to each slave
			private void SendMsg() { 

				String path="";
	    	       path=this.GetPath();
	    	       OriginalImage=path;
	    	     
	    		   for(int j=1;j<4;j++) {

	    		     	ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
	    				String nameAgent="Slave"+j;

	    				msg.addReceiver(new AID(nameAgent,AID.ISLOCALNAME));
	    				msg.setContent(path);	
	    				send(msg);
	    		
	    				addBehaviour(new CyclicBehaviour() {	
	    					@Override
	    					public void action() {
	    						
	    							MessageTemplate template=
	    							MessageTemplate.or(
	    							MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
	    							MessageTemplate.or(
	    							MessageTemplate.MatchPerformative(ACLMessage.INFORM),
	    							MessageTemplate.MatchPerformative(ACLMessage.REQUEST))
	    							);
	    							
	    							ACLMessage aclMessage=receive(template);
	    							if(aclMessage!=null){
	    							
	    							ImageName[i]=aclMessage.getContent();
	    							sender[i]=aclMessage.getSender().getLocalName();
	  
	    							i++;
	    					
	    						    	if(i==3) {
	    						    	
	    						    	    label12.setIcon(new ImageIcon(OriginalImage));
		    								for (int k=0;k<3;k++) {
		    								
		    									if(sender[k].equals("Slave1")) {
		    										  										
		    					
		    		    						        label22.setIcon(new ImageIcon(ImageName[k]));
		    		    						       
		    									}
		    									if(sender[k].equals("Slave2")) {
		    									
		   
		    		    						        label32.setIcon(new ImageIcon(ImageName[k]));
		    						            	
		    						            	
		    									}
		    						            
		    									if(sender[k].equals("Slave3")) {
		    										
		 
		    		    						      label42.setIcon(new ImageIcon(ImageName[k]));
		    		    						
		    						            }
		    							   
		    						       
		    								}
	    						      
	    						     
	    						    	 ImageName[0]= null;
			    					  	 ImageName[1]= null;
			    					  	 ImageName[2]= null;
	
	    						         i=0;
		
	    							}
	    							
	    							}
	    						 	
	    							else{
	    							block();
	    							}
	    							
	    					}
	    					});
	    			
			}

			}

        });
            frame.setContentPane(contentPane);
        	frame.setSize(640,665);
	        frame.setLocationByPlatform(true);
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
    
	}
	
}
