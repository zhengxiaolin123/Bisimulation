package partial_WFS;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LTS {

	int s0 = 0, t0 = 0, n1, n2;

	private Hashtable<Integer, Hashtable<Integer, List<Integer>>> lts_s = new Hashtable<Integer, Hashtable<Integer, List<Integer>>>();
	private Hashtable<Integer, Hashtable<Integer, List<Integer>>> lts_t = new Hashtable<Integer, Hashtable<Integer, List<Integer>>>();

	public void init() throws IOException {

		readFile("D:/A/C/test1.txt", lts_s);
  		readFile("D:/A/C/test2.txt", lts_t);	
	}

	private void readFile(String fileName, Hashtable<Integer, Hashtable<Integer, List<Integer>>> data) {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String str;
			while ((str = br.readLine()) != null) {
				String num[] = str.split(" ");
				int a = Integer.parseInt(num[0]);

				Hashtable<Integer, List<Integer>> transitions = data.get(a);
				if (transitions == null)
					transitions = new Hashtable<Integer, List<Integer>>();

				int b = Integer.parseInt(num[1]);
				List<Integer> states = transitions.get(b);
				if (states == null)
					states = new ArrayList<Integer>();

				int c = Integer.parseInt(num[2]);
				states.add(c);

				transitions.put(b, states);
				data.put(a, transitions);

			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int T_size(Pairs p) {
		Set<Integer> nset1 = new HashSet<Integer>();
		Hashtable<Integer, List<Integer>> p1list = lts_s.get(p.q1);
		if (p1list == null)
			p1list = new Hashtable<Integer, List<Integer>>();
		Set key1 = p1list.keySet();
		if (key1 == null)
			key1 = new HashSet();
		Iterator it = key1.iterator();
		while (it.hasNext()) {
			List<Integer> p1_states = p1list.get(it.next());
			if (p1_states == null)
				p1_states = new ArrayList<Integer>();
			for (int i = 0; i < p1_states.size(); i++) {
				nset1.add(p1_states.get(i));

			}
		}

		Set<Integer> nset2 = new HashSet<Integer>();

		Hashtable<Integer, List<Integer>> p2list = lts_t.get(p.q2);
		if (p2list == null)
			p2list = new Hashtable<Integer, List<Integer>>();
		Set key2 = p2list.keySet();
		if (key2 == null)
			key2 = new HashSet();
		Iterator jt = key2.iterator();
		while (jt.hasNext()) {
			List<Integer> p2_states = p2list.get(jt.next());

			for (int i = 0; i < p2_states.size(); i++) {
				nset2.add(p2_states.get(i));
			}
		}

		return (nset1.size() + nset2.size());
	}

	public List<Integer> T(int p) {
		return null;
	}

	public List<Pairs> succ(Pairs p) {
		List<Pairs> succL = new LinkedList<Pairs>();
		Hashtable<Integer, List<Integer>> p1list = lts_s.get(p.q1);
		if (p1list == null) {
			p1list = new Hashtable<Integer, List<Integer>>();
		}
		Hashtable<Integer, List<Integer>> p2list = lts_t.get(p.q2);
		if (p2list == null) {
			p2list = new Hashtable<Integer, List<Integer>>();
		}
		Set key_1 = p1list.keySet();
		Set key_2 = p2list.keySet();
		Iterator p1 = key_1.iterator();

		while (p1.hasNext()) {
			int action = (int) p1.next();

			List<Integer> state_q1 = p1list.get(action);
			if (state_q1 == null) {
				state_q1 = new ArrayList<Integer>();
			}
			Iterator p2 = key_2.iterator();
			while (p2.hasNext()) {
				int action1 = (int) p2.next();

				List<Integer> state_q2 = p2list.get(action1);

				if (state_q2 == null) {
					state_q2 = new ArrayList<Integer>();
				}
				if (action1 == action) {

					for (int i = 0; i < state_q1.size(); i++) {
						for (int j = 0; j < state_q2.size(); j++) {
							succL.add(new Pairs(state_q1.get(i), state_q2.get(j)));
						}
					}
				}

			}

		}

		return succL;
	}

	public boolean isFailed(Pairs p) {
		Hashtable<Integer, List<Integer>> ac_lts_s = lts_s.get(p.q1);
		Hashtable<Integer, List<Integer>> ac_lts_t = lts_t.get(p.q2);
		if (ac_lts_s == null)
			ac_lts_s = new Hashtable<Integer, List<Integer>>();
		Set s1 = ac_lts_s.keySet();
		if (ac_lts_t == null)
			ac_lts_t = new Hashtable<Integer, List<Integer>>();
		Set s2 = ac_lts_t.keySet();
		if ((s1 == null) && (s2 == null)) {
			return false;
		}
		if ((s1 == null) && (s2 != null)) {
			return true;
		}
		if ((s1 != null) && (s2 == null)) {
			return true;
		}
		if (s1.equals(s2)) {
			s1 = null;
			s2 = null;
			return false;
		} else {
			s1 = null;
			s2 = null;
			return true;
		}
	}

}
