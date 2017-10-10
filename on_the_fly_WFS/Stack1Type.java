package partial_WFS;

import java.util.LinkedList;
//import java.util.LinkedList;
import java.util.List;

public class Stack1Type {
	Pairs p;
	List<Pairs> succ = new LinkedList<Pairs>();

	public Stack1Type(Pairs p1, List<Pairs> l) {
		p = p1;
		succ = l;
	}

	@Override
	public boolean equals(Object obj) {
		Stack1Type st = (Stack1Type) obj;
		if ((p.q1 == st.p.q1) && (p.q2 == st.p.q2)) {
			return true;
		} else {
			return false;
		}

	}
}
