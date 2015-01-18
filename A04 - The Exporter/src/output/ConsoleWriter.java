package output;

import java.util.ArrayList;

public class ConsoleWriter implements Writer {

	@Override
	public void write(ArrayList<String> results) {
		for (int i = 0; i < results.size(); i++)	{
			System.out.println(results.get(i));
		}
	}
}
