package checsim;

public class Pair1 {
	private int s;
	private int t;
	
	public Pair1(int s, int t)
	{
		this.s = s;
		this.t = t;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int r = new Integer(s).hashCode();
		return r*31;
	}

	@Override
	public boolean equals(Object  obj) {
		// TODO Auto-generated method stub
		Pair1 p = (Pair1) obj;
		return p.s == this.s&& p.t == this.t;
	}
	
	
}

