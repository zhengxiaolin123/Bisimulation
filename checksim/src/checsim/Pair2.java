package checsim;

public class Pair2 {
	private int s;
	private int newS;
	private int t;
	private int a;
	
	public Pair2 (int s, int newS, int t, int a)
	{
		this.s = s;
		this.newS = newS;
		this.t= t;
		this.a = a;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		
		Pair2 p = (Pair2) obj;
		return p.s == this.s && p.newS == this.newS && p.t == this.t && p.a == this.a;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int r = new Integer(s).hashCode();
		r = r*31+(new Integer(newS).hashCode());
		r = r*31+(new Integer(t).hashCode());
		return r*31+(new Integer(a).hashCode());
	}
	
}
