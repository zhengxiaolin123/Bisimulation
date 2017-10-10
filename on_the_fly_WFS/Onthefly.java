package partial_WFS;

import java.io.IOException;
import java.util.*;

public class Onthefly {
	enum RESULT {
		TRUE, FALSE, UNDETERMINED
	}

	static Set<Pairs> pair_nonbisimulation = new HashSet<Pairs>();
	static LTS lts = new LTS();
	static int loop = 0;

	public static void main(String[] args) throws IOException, InterruptedException {
		RESULT result = RESULT.UNDETERMINED;
		lts.init();
		long start = System.nanoTime();
		long before = Runtime.getRuntime().freeMemory();
		while (!(result.equals(RESULT.TRUE) || result.equals(RESULT.FALSE))) {
			loop ++;
			result = partial_WFS();
		}
		
		long after = Runtime.getRuntime().freeMemory();
		long end = System.nanoTime();
		System.out.println(result);
		System.out.println(
				String.format("Time:%g ms\nMemory Usage:%d", ((double) (end - start)) / 1000 / 1000, before - after));
	}

	private static RESULT partial_WFS() {
		if (lts.isFailed(new Pairs(lts.s0, lts.t0)))
			return RESULT.FALSE;
		int number = 0;
		
		Set<Pairs> pair_visited = new HashSet<Pairs>();
		Set<Pairs> pair_visited_morethanonce = new HashSet<Pairs>();
		
		Pairs pair = new Pairs(lts.s0, lts.t0);
		LinkedList<Stack1Type> queue1 = new LinkedList<Stack1Type>();
		queue1.add(new Stack1Type(pair, lts.succ(pair)));
		
		LinkedList<Stack2Type> queue2 = new LinkedList<Stack2Type>();
		Stack2Type mInitialPair = new Stack2Type(2, null, null);
		queue2.add(new Stack2Type(lts.T_size(pair), mInitialPair, pair));
		int index = 0;
		
		while (!queue1.isEmpty()) {
			Stack1Type queue1_top = queue1.get(0);
			Stack2Type M = queue2.get(index);
			if (!queue1_top.succ.isEmpty()) {
				Pairs q = queue1_top.succ.remove(0);
				if (!(pair_visited.contains(q) || pair_nonbisimulation.contains(q))) {
					if (!queue1.contains(new Stack1Type(q, null))) {
						if (!(lts.isFailed(q))) {
							queue1.add(new Stack1Type(q, lts.succ(q)));
							queue2.add(new Stack2Type(lts.T_size(q), M, q));
							number++;
						}
						else
						{
							pair_nonbisimulation.add(q);
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
				index++;
				queue1.remove(0);
				pair_visited.add(M.pair);
				
				Stack2Type tmp = M;
				if (tmp.allTrue()) {
					while (tmp.allTrue() && tmp.pair != null) {
						tmp.father.setTrue(tmp.pair.q1, tmp.pair.q2);
						tmp = tmp.father;
					}
				}
			}
		}

		System.out.println("loop " + loop + " pair number:" + number);
		
		if (!mInitialPair.get(lts.s0, lts.t0)) {
			return RESULT.FALSE;
		} 
		else
		{
			boolean stable = true;
			for(Stack2Type m : queue2)
			{
				if(!m.allTrue())
					pair_nonbisimulation.add(m.pair);
			}
			for(Pairs p : pair_visited_morethanonce)
			{
				if(pair_nonbisimulation.contains(p))
				{
					stable = false;
					break;
				}
			}
			if(stable)
			{
				for(Pairs p : pair_visited)
				{
					if(pair_nonbisimulation.contains(p))
					{
						stable = false;
						break;
					}
				}
			}
			
			if (stable)
				return RESULT.TRUE;
			else
				return RESULT.UNDETERMINED;
		}
	}

}
