import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class FindFlowSize{
	public static HashMap<String, Integer> trackPacketData(String filename, HashSet<String> flowsToBeTracked){
		HashMap<String, Integer> tracker = new HashMap<String, Integer>();
		File file = new File(filename);
		try
		{
			Scanner scanner = new Scanner(file);
			String line;
			int linenumber = 0;
			String[] fields = new String[24];
			while (scanner.hasNextLine())
			{
				line = scanner.nextLine();
				/*if (linenumber++ == 0)
					continue;*/

				fields = line.split(",");

				String srcipString = fields[3];

				if (flowsToBeTracked.contains(srcipString)){
					if (tracker.containsKey(srcipString))
						tracker.put(srcipString, tracker.get(srcipString) + Integer.parseInt(fields[11]));
					else
						tracker.put(srcipString, Integer.parseInt(fields[11]));
				}
				
			}
			scanner.close();
			return tracker;
		}
		catch (FileNotFoundException e)
		{
			System.err.format("Exception occurred trying to read '%s'.", filename);
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args){
		// read the flows to be lost from a file mentioned in the command line and create a new stream with that flow lost
		HashSet<String> flowsToBeTracked = new HashSet<String>();
		ArrayList<String> flows = new ArrayList<String>();
		File file = new File(args[1]);
		try
		{
			Scanner scanner = new Scanner(file);
			String line;
			//int linenumber = 0;
			//String[] fields = new String[24];
			while (scanner.hasNextLine())
			{
				line = scanner.nextLine();
				flowsToBeTracked.add(line);
				flows.add(line);
			}
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			System.err.format("Exception occurred trying to read '%s'.", args[1]);
			e.printStackTrace();
			return;
		}

		HashMap<String, Integer> tracker = trackPacketData(args[0], flowsToBeTracked);

		for (String s : flows)
			System.out.println(s + "," + tracker.get(s) + ",");
	}
}