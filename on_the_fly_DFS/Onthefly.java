package partial_DFS;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Onthefly {
	enum RESULT {
		TRUE, FALSE, UNDETERMINED
	}

	static Set<Pairs> pair_nonbisimulation = new HashSet<Pairs>();
	static LTS lts = new LTS();
	static int pairsnum = 0;
	static int loop = 0;

	public static void main(String[] args) throws IOException {
		RESULT result = RESULT.UNDETERMINED;
		lts.init();
		long start = System.nanoTime();
		long before = Runtime.getRuntime().freeMemory();
		while (!(result.equals(RESULT.TRUE) || result.equals(RESULT.FALSE))) {
			loop ++;
			result = partial_DFS();
		}
		long after = Runtime.getRuntime().freeMemory();
		long end = System.nanoTime();
		System.out.println(result);
		System.out.println(pairsnum);
		System.out.println(
				String.format("Time:%g ms\nMemory Usage:%d", ((double) (end - start)) / 1000 / 1000, before - after));
	}

	private static RESULT partial_DFS() {
		Set<Pairs> pair_visited = new HashSet<Pairs>();
		Set<Pairs> pair_visited_morethanonce = new HashSet<Pairs>();
		int number = 0;
		boolean stable = false;
		Pairs pair = new Pairs(lts.s0, lts.t0);
		Stack<Stack1Type> pair_to_process = new Stack<Stack1Type>();
		pair_to_process.push(new Stack1Type(pair, lts.succ(pair)));
		Stack<Stack2Type> pair_processed_bit_arrays = new Stack<Stack2Type>();
		pair_processed_bit_arrays.push(new Stack2Type(2));
		pair_processed_bit_arrays.push(new Stack2Type(lts.T_size(pair)));
		while (!pair_to_process.empty()) {
			stable = true;
			Stack1Type pair_processed_top = pair_to_process.peek();
			Stack2Type M = pair_processed_bit_arrays.peek();
			if (!pair_processed_top.succ.isEmpty()) {
				Pairs q = pair_processed_top.succ.remove(0);
				if (!(pair_visited.contains(q) || pair_nonbisimulation.contains(q))) {
					if (!pair_to_process.contains(new Stack1Type(q, null))) {
						//pairsnum++;
						if (!(lts.isFailed(q))) {
							pair_to_process.push(new Stack1Type(q, lts.succ(q)));
							pair_processed_bit_arrays.push(new Stack2Type(lts.T_size(q)));
							number++;
						}
					} else {
						pair_visited_morethanonce.add(q);
						M.setTrue(q.q1, q.q2);
					}
				} else {
					if (!(pair_nonbisimulation.contains(q))) {
						M.setTrue(q.q1, q.q2);

					}
				}
			} else {
				pair_to_process.pop();
				pair_processed_bit_arrays.pop();
				pair_visited.add(pair_processed_top.p);
				Stack2Type M2 = pair_processed_bit_arrays.peek();
				if (M.allTrue()) {
					
					M2.setTrue(pair_processed_top.p.q1, pair_processed_top.p.q2);
				} else {
				
					pair_nonbisimulation.add(pair_processed_top.p);
					if (pair_visited_morethanonce.contains(pair_processed_top.p))
						stable = false;
				}
			}
		}
		
		System.out.println("loop " + loop + " pair number:" + number);
		
		Stack2Type mInitialPair = pair_processed_bit_arrays.peek();
		
		if (!mInitialPair.get(lts.s0, lts.t0)) {
		     return RESULT.FALSE;
		} else if (stable)
			return RESULT.TRUE;
		else

			return RESULT.UNDETERMINED;

	}

}
