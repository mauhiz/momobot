package net.mauhiz.fbook.puzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiarLiar extends ExportedRunnable {

	public static void main(String... args) throws IOException {
		new LiarLiar().run(args);
	}

	@Override
	public String getName() {
		return "liarliar";
	}
	
	static class Veteran {
		String name;
		final List<Veteran> accusers = new ArrayList<Veteran>();
		Boolean liar;
		static Veteran instance(Map<String, Veteran> members, String name) {
			Veteran v = members.get(name);
			if (v == null) {
				v = new Veteran();
				v.name = name;
				members.put(name, v);
			}
			return v;
		}
	}
	

	static int count(boolean liars, Collection<Veteran> members) {
		int c = 0;
		for (Veteran v : members) {
			if (v.liar == null) {
				return -1;
			} else if (v.liar.booleanValue() == liars) {
				c++;
			}
		}
		
		return c;
	}

	@Override
	protected void process(BufferedReader input, PrintWriter output) throws IOException {
		int n = Integer.parseInt(input.readLine());
		Map<String, Veteran> members = new HashMap<String, Veteran>(n);
		for (int i = 0; i < n; i++) {
			String[] accusation = input.readLine().split(" ");
			String accusedName = accusation[0];
			Veteran accused = Veteran.instance(members, accusedName);

			int m = Integer.parseInt(accusation[1]);
			for (int j = 0; j < m; j++) {
				String accuserName = input.readLine();
				Veteran accuser = Veteran.instance(members, accuserName);
				accused.accusers.add(accuser);
			}
		}
		
		// TODO bipartite graph analysis
		
		int countLiars = count(true, members.values());
		int countHonest = count(false, members.values());
		output.println(Math.max(countLiars, countHonest) + " " + Math.min(countLiars, countHonest));
	}

}
