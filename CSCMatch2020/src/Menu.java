import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
public class Menu {
	private Membership membership;
	private Scanner keyboard;
	public Menu() {
		membership = new Membership();
		keyboard = new Scanner(System.in);
	}
	public void run() {
		String option = "";
		int editCount = 0;
		boolean quit = false;
		do {
			System.out.println("a.  Load the Members");
			System.out.println("b.  Save the Members");
			System.out.println("c.  List All Members");
			System.out.println("d.  Add a Member");
			System.out.println("e.  Remove a Member");
			System.out.println("f.  List Member");
			System.out.println("g.  Add an Interest to a Member");
			System.out.println("h.  Quit");
			
			if (keyboard.hasNextLine()) {
				option = keyboard.nextLine();
				option = option.toLowerCase();

				switch (option) {
				case "a": loadMembers(); break;
				case "b": saveMembers(); editCount = 0; break;
				case "c": listAllMembers(); break;
				case "d": addMember(); editCount++; break;
				case "e": removeMember(); editCount++; break;
				case "f": listMember(); break;
				case "g": addInterest(); editCount++; break;
				case "h": 
					String choice = "";
					if (editCount > 0) {
						System.out.print("There are changes made that have not been save "
							+ "do you want to save before quitting? [Y/N] ");
							choice = keyboard.nextLine().toLowerCase();
							while (!(choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("n"))) {
								System.out.print("Please enter a valid option. Do you want to save before quitting? [Y/N] ");
								choice = keyboard.nextLine().toLowerCase();
							}
							if (choice.equalsIgnoreCase("y")) {
								System.out.println("Select option b to save.");
							} else {quit = true; System.out.println("Program terminated");}

					} else {quit = true; System.out.println("Program terminated");}  break; //Implement checking for edits.
				default: System.out.println("Invalid option. Please try a,b,c,d,e,f,g,h.");
				}
			}
		} while (!quit);

	}
	private void loadMembers() {
		String fileName;
		System.out.print("File name: ");
		fileName = keyboard.nextLine();
		try {
			membership = Membership.load(fileName);
		} catch (FileNotFoundException fnfe) {
			System.out.println("File not found. Please try a different file name!");
		} catch (IOException ioe) {
			System.out.println("File cannot be read. Please try a different file name!");
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		}

	}
	private void saveMembers() {
		String fileName;
		System.out.print("Enter file name: ");
		fileName = keyboard.nextLine();
		try {
		membership.save(fileName);
		} catch (IOException ioe) {
			System.out.println("File cound not be created. Please try again with a different file name!");
		}

	}
	private void listAllMembers() {
		System.out.println(membership);
	}
	private void addMember() {
		System.out.print("Name: "); 
		String name = keyboard.nextLine(); 
		if (membership.findMember(name) != null) { 
			System.out.println("That member already exists"); 
			return; 
		 } 
		System.out.print("Year (1-5): "); 
		while (!keyboard.hasNextInt()) {
			System.out.println("Year must be an integer");
			System.out.print("Year (1-5): "); 
			keyboard.next();
		}
		int year = keyboard.nextInt();
		
		keyboard.nextLine(); 
		  
		try { 
			Member member = new Member(name, year); 
			membership.addMember(member); 
		} catch (InvalidMemberException e) { 
			System.out.println(e.getMessage()); 
		} 
	} 
	private void removeMember() {
		System.out.print("Enter the name of the person you want to remove: ");
		String name = keyboard.nextLine();
		if (membership.findMember(name) == null) {
			System.out.println("Member not found please try again \n");
		}
		
		Iterator<Member> iter = membership.iterator();
		while (iter.hasNext()) {
			Member m = iter.next();
			if (m.equals(membership.findMember(name))) {
				System.out.println("Member: " + name + " has been found and removed.");
				iter.remove();
			}
		}
	}
	private void listMember() {
		LinkedList<Compatibility> compatibilities = new LinkedList<Compatibility>();
		Iterator<Member> iter = membership.iterator();

		String name = "";
		System.out.print("Enter the name of the member you want you list: ");
		name = keyboard.nextLine();
		Member member = membership.findMember(name);
		if (member == null) {
			System.out.println("This user does not exist.");
		} else {
			System.out.println(member + "Compatibilities: ");
			while (iter.hasNext()) {
				Member their = iter.next();
				Compatibility compatibility = new Compatibility(member, their);
				if (!member.getName().equals(their.getName())) compatibilities.add(compatibility);
			}
			compatibilities.sort(null);
			int count = 1;
			for (Compatibility compatibility: compatibilities) {
				if (count <= 5) {
				System.out.println(compatibility);
				}
				count++;
			}
			
		}
	}
	private void addInterest() {
		System.out.print("Enter the name of the person you want to add interest to: ");
		String name = keyboard.nextLine();
		while (membership.findMember(name) == null) {
			System.out.println("Member not found please try again");
			System.out.print("Enter the name of the person you want to add interest to: ");
			name = keyboard.nextLine();
		}
		Member member = membership.findMember(name);
		System.out.print("Enter your topic: ");
		String topic = keyboard.nextLine();
		System.out.print("Enter your interest level (1-10): ");
		while (!keyboard.hasNextInt()) {
			System.out.println("Interest level must be an integer");
			System.out.print("Enter your interest level (1-10): ");
			keyboard.next();
		}
		int level = keyboard.nextInt();
		keyboard.nextLine();
		try {
			Interest interest = new Interest(topic, level);
			member.addInterest(interest);
		} catch (InvalidInterestException e) {
			System.out.println(e.getMessage());
		}
		
	}
}
