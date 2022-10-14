import java.util.Iterator;

public class Compatibility implements Comparable<Compatibility> {
	private Member their;
	private int score;
	public Compatibility(Member my, Member their) {
		this.their = their;
		Iterator<Interest> theirIter = their.iterator();
		while (theirIter.hasNext()) {
			boolean matched = false;
			Interest theirInt = theirIter.next();
			String theirTopic = theirInt.getTopic();
			Iterator<Interest> myIter = my.iterator();
			while (myIter.hasNext()) {
				Interest myInt = myIter.next();
				String myTopic = myInt.getTopic();
				if (theirTopic.equalsIgnoreCase(myTopic)) {
					score = score + (myInt.getLevel() * theirInt.getLevel());
					matched = true;
				}
			}
			if (!matched) {
				score =  (int) score + theirInt.getLevel()/2;
			}
		}
	}
	public int getScore() {
		return score;
	}
	public Member getTheir() {
		return their;
	}
	public String toString() {
		String result = "";
		result = their.getName() + " " + getScore();
 		return result;
	}
	public int compareTo(Compatibility o) {
		if (score == o.getScore()) {
			return their.getName().compareTo(o.getTheir().getName());
		} else {
			return o.getScore() - score;
		}
	}

}
