import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Test07 extends TestCore {
	public static void main(String[] args) {
		new Test07().runTests();
	}

	public void runTests() {
		startTests(this.getClass().getName());
		try { testMenu(); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testMenu() {
		TestClass tc = menuClass;
		Field membership = null;
		Field keyboard;
		
		requiredClass(tc);
		membership = requiredVariable(tc, "membership", Modifier.PRIVATE, "Membership");
		keyboard = requiredVariable(tc, "keyboard", Modifier.PRIVATE, "java.util.Scanner");
		requiredMethod(tc, "run", Modifier.PUBLIC, "void");
		requiredMethod(tc, "loadMembers", Modifier.PRIVATE, "void");
		requiredMethod(tc, "saveMembers", Modifier.PRIVATE, "void");
		requiredMethod(tc, "listAllMembers", Modifier.PRIVATE, "void");
		requiredMethod(tc, "addMember", Modifier.PRIVATE, "void");
		requiredMethod(tc, "removeMember", Modifier.PRIVATE, "void");
		requiredMethod(tc, "listMember", Modifier.PRIVATE, "void");
		requiredMethod(tc, "addInterest", Modifier.PRIVATE, "void");
		
		try {
			Object menu = menuClass.newInstance();
			Object m = membership.get(menu);
			if (m == null) {
				fail("membership not initialized");
			}
			Object k = keyboard.get(menu);
			if (k == null) {
				fail("keyboard not initialized");
			}
		} catch (Exception e) {
			unexpected(e);
		}
	}
}
