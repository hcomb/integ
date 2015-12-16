package eu.hcomb.riot;

public class Run {

	public static void main(String[] args) throws Exception {
		
		String cmd = args[0];

		if("get".equals(cmd))
			GetFromQueue.main(args);
		else if("put".equals(cmd))
			WriteToQueue.main(args);
		else{
			System.out.println(" * cannot handle command: " + cmd);
		}
		
		
	}
}
