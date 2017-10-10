package partial_WFS;

public class Pairs {
	int q1, q2;

	public Pairs(int p, int q) {
		q1 = p;
		q2 = q;

	}

	public boolean equal(Pairs p) {
		if ((q1 == p.q1) && (q2 == p.q2)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Pairs)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		return this.q1 == ((Pairs) obj).q1 && this.q2 == ((Pairs) obj).q2;

	}

	public int hashCode() {
		return (String.valueOf(q1) + String.valueOf(q2)).hashCode();
	}

}