import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Membership implements Iterable<Member>, Serializable {
	private LinkedList<Member> members;
	
	public Membership() {
		members = new LinkedList<Member>();
	}
	public void addMember(Member member) {
		members.add(member);
		
	}
	public Iterator<Member> iterator() {
		return members.iterator();
	}
	public String toString() {
		String result = "";
		for (Member member: this) {
			result += member.getName() + "\n";
		}
		return result;
	}
	
	public Member findMember(String name) {
		for (Member member: this) {
			if (member.getName().equals(name)) {
				return member;
			}
		}
		return null;
	}
	
	public void save(String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.flush();
		oos.close();
	}
	
	public static Membership load(String fileName) throws IOException, ClassNotFoundException, FileNotFoundException {
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Membership membership = (Membership) ois.readObject();
		ois.close();
		return membership;
	}

}
