package partial_DFS;

import java.util.HashMap;
import java.util.Map;

public class Stack2Type {

	int size;
	Map<Integer, Boolean> map1 = new HashMap<Integer, Boolean>();
	Map<Integer, Boolean> map2 = new HashMap<Integer, Boolean>();

	public Stack2Type(int n) {
		size = n;
	}

	public void setTrue(int p1, int p2) {
		map1.put(p1, true);
		map2.put(p2, true);
	};

	public boolean get(int p1, int p2) {
		if (map1.containsKey(p1) && map2.containsKey(p2))
			return true;
		else
			return false;
	}

	public boolean allTrue() {
		if (map1.size() + map2.size() < size)
			return false;
		return true;
	}
}
