import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;

public class Test06 extends TestCore {
	public static void main(String[] args) {
		new Test06().runTests();
	}

	public void runTests() {
		startTests(this.getClass().getName());
		try { testMembership(); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testMembership() {
		TestClass tc = membershipClass;
		String name = "Membership";
		Method addMember = null;
		Field members = null;
		String testName = "Test Name";
		String testName2 = "Test Name 2";
		int testYear = 1;
		String gotName;
		int gotYear;
		
		requiredClass(tc);
		members = requiredVariable(tc, "members", Modifier.PRIVATE, "java.util.LinkedList<Member>");
		addMember = requiredMethod(tc, "addMember", Modifier.PUBLIC, "void", memberClass.getClazz());
		
		try {
			Object membership = membershipClass.newInstance();
			LinkedList<Object> m = (LinkedList<Object>) members.get(membership);
			if (m == null) {
				fail("constructor failed to initialize interests");
			}
			if (m.size() != 0) {
				fail("members is not empty right after construction");
			}
			Object member = memberClass.newInstance("Test Name 1", 1);
			addMember.invoke(membership, member);
			if (m.size() != 1) {
				fail("addMember failed to add member 1");
			}
			member = memberClass.newInstance("Test Name 2", 2);
			addMember.invoke(membership, member);
			if (m.size() != 2) {
				fail("addMember failed to add member 2");
			}
		} catch (Exception e) {
			unexpected(e);
		}
	}
}
