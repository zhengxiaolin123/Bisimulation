package checsim;

import java.util.ArrayList;
import java.util.List;

public class StatePair {
	int s;
	int t;
	List<Integer> parent_locations = new ArrayList<Integer>();
	List<List<Integer>> parent_actions = new ArrayList<List<Integer>>();
	
	public StatePair(int s, int t)
	{
		this.s = s;
		this.t = t;
	}
	public void addParent(int  p,int action)
	{
		if(this.parent_locations.contains(p))
		{
			int index = this.parent_locations.indexOf(p);
			if(!parent_actions.get(index).contains(action))
				parent_actions.get(index).add(action);
		}
		else
		{
			this.parent_locations.add(p);
			List<Integer> actions = new ArrayList<Integer>();
			actions.add(action);
			parent_actions.add(actions);
		}
		
	}
}
