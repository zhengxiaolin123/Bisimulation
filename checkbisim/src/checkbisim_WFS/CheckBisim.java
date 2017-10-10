package checkbisim_WFS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class CheckBisim {
	
	private Hashtable<Integer,Hashtable<Integer,List<Integer>>> lts_s = new Hashtable<Integer,Hashtable<Integer,List<Integer>>>();
	private Hashtable<Integer,Hashtable<Integer,List<Integer>>> lts_t = new Hashtable<Integer,Hashtable<Integer,List<Integer>>>();
	
	private int s0 = 0;
	private int t0 = 0;
	private int numStateOfDlts = 1;
	private int num0LabelState = 0;
	 static int pairsnum=0;
	public static void main(String[] args) {
		CheckBisim checkBisim = new CheckBisim();
		checkBisim.init();
		boolean result;
		long startTime = System.currentTimeMillis();
		result = checkBisim.check();
		long endTime = System.currentTimeMillis();
		if (result)
			System.out.println("YES");
		else
			System.out.println("No");
		System.out.println(pairsnum);
		System.out.println("The number of states of the DLTS:" + checkBisim.numStateOfDlts);
		System.out.println("The number of 0-label states of the DLTS:" + checkBisim.num0LabelState);
		System.out.println("Time:" + (endTime - startTime) + "ms");		
	}
	
	private boolean check() {
		List<StatePair> pairs = new ArrayList<StatePair>();
		Stack<Integer> dlts = new Stack<Integer>();
		Stack<Integer> pair_0_label = new Stack<Integer>();
		Set<Integer> pair_processed = new HashSet<Integer>();
		Map<Pair1,Integer> location_of_pair = new HashMap<Pair1,Integer>();
		Map<Pair2,Integer> candidateNum_t_to_s = new HashMap<Pair2,Integer>();
		Map<Pair2,Integer> candidateNum_s_to_t = new HashMap<Pair2,Integer>();
		pairs.add(new StatePair(s0,t0));
		location_of_pair.put(new Pair1(s0,t0), 0);
		dlts.push(0);
		while(!dlts.isEmpty()) {
			int index = dlts.pop();
			StatePair pair = pairs.get(index);
			int s = pair.s;
			int t = pair.t;
			Hashtable<Integer,List<Integer>> s_trantions = lts_s.get(s);
			Hashtable<Integer,List<Integer>> t_trantions = lts_t.get(t);
			if(s_trantions == null)
				s_trantions = new Hashtable<Integer,List<Integer>>();
			if(t_trantions == null)
				t_trantions = new Hashtable<Integer,List<Integer>>();
			
			Set<Integer> s_actions = s_trantions.keySet();
			Set<Integer> t_actions = t_trantions.keySet();
			boolean diff_action = false;
			Iterator<Integer> its = s_actions.iterator();
			while(its.hasNext())
			{
				int action = its.next();
				if(!t_actions.contains(action))
				{
					diff_action = true;
					break;
				}
			}
			Iterator<Integer> itt = t_actions.iterator();
			while(itt.hasNext())
			{
				int action = itt.next();
				if(!s_actions.contains(action))
				{
					diff_action = true;
					break;
				}
			}
			if(diff_action)
			{
				if(!pair_processed.contains(index))
				{
					pair_0_label.push(index);
					pair_processed.add(index);
					num0LabelState++;
				}
				if(index == 0)
					return false;
				
				continue;
			}
			
			Iterator<Integer> iter = s_actions.iterator();
			while(iter.hasNext())
			{
				int action = iter.next();
				int size_state_s = s_trantions.get(action).size();
				int size_state_t = t_trantions.get(action).size();
				boolean flag = true;
				for(int j=0;j<size_state_s;j++)
				{
					int newS = s_trantions.get(action).get(j);
					candidateNum_t_to_s.put(new Pair2(s,newS,t,action),size_state_t);

					for(int k=0;k<size_state_t;k++)
					{
						int newT = t_trantions.get(action).get(k);
						
						if(flag)
							candidateNum_s_to_t.put(new Pair2(t,newT,s,action),size_state_s);
						
						Integer location = location_of_pair.get(new Pair1(newS, newT));
						if(location == null)
						{
							location = pairs.size();
							pairs.add(new StatePair(newS,newT));
							pairsnum++;
							location_of_pair.put(new Pair1(newS, newT), location);
							dlts.push(location);
							numStateOfDlts++;
						}
						pairs.get(location).addParent(index,action);
					}
					flag = false;
				}
			}
		}
		
		while(!pair_0_label.empty())
		{
			int location = pair_0_label.pop();
			StatePair pair = pairs.get(location);
			for(int i=0;i<pair.parent_locations.size();i++)
			{
				int l = pair.parent_locations.get(i);
				StatePair parent = pairs.get(l);
				
				List<Integer> actions = pair.parent_actions.get(i);
				for(int j=0;j<actions.size();j++)
				{
					int action = actions.get(j);
					int count1 = candidateNum_t_to_s.get(new Pair2(parent.s,pair.s,parent.t,action));
					count1--;
					candidateNum_t_to_s.put(new Pair2(parent.s,pair.s,parent.t,action),count1);
					int count2 = candidateNum_s_to_t.get(new Pair2(parent.t,pair.t,parent.s,action));
					count2--;
					candidateNum_s_to_t.put(new Pair2(parent.t,pair.t,parent.s,action),count2);
					
					if(count1==0 || count2==0)
					{
						if(l==0)
							return false;
						if(!pair_processed.contains(l))
						{
							pair_0_label.push(l);
							pair_processed.add(l);
							num0LabelState++;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private void init() {
		readFile("D:/A/C/test1.txt", lts_s);
  		readFile("D:/A/C/test2.txt", lts_t);
	}
	
	private void readFile(String fileName, Hashtable<Integer,Hashtable<Integer,List<Integer>>> data) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String str;
			while((str=br.readLine())!=null){
				String num[]=str.split(" ");
				int a=Integer.parseInt(num[0]);
				Hashtable<Integer,List<Integer>> transitions = data.get(a);
				if(transitions == null)
					transitions = new Hashtable<Integer,List<Integer>>();
				
				int b=Integer.parseInt(num[1]);
				List<Integer> states = transitions.get(b);
				if(states == null)
					states = new ArrayList<Integer>();
				
				int c=Integer.parseInt(num[2]);
				states.add(c);
				
				transitions.put(b, states);
				data.put(a, transitions);
				}
			br.close();
			} catch (Exception e) {
				e.printStackTrace();
				}
		}
}
