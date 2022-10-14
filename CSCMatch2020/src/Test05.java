import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;

public class Test05 extends TestCore {
	public static void main(String[] args) {
		new Test05().runTests();
	}

	public void runTests() {
		new Test04().testInterest();
		startTests(this.getClass().getName());
		try { testMember(); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testMember() {
		String name = "Member";
		Field interests;
		Method addInterest = null;
		

		requiredClass(memberClass);
		requiredClass(interestClass);
		interests = requiredVariable(memberClass, "interests", Modifier.PRIVATE, "java.util.LinkedList<Interest>");
		addInterest = requiredMethod(memberClass, "addInterest", Modifier.PUBLIC, "void", interestClass.getClazz());
		
		try {
			Object member = memberClass.newInstance("Test Name", 1);
			LinkedList<Object> i = (LinkedList<Object>) interests.get(member);
			if (i == null) {
				fail("constructor failed to initialize interests");
			}
			if (i.size() != 0) {
				fail("interests is not empty right after construction");
			}
			Object interest = interestClass.newInstance("Test Topic 1", 1);
			addInterest.invoke(member, interest);
			if (i.size() != 1) {
				fail("addInterest failed to add interest 1");
			}
			interest = interestClass.newInstance("Test Topic 2", 2);
			addInterest.invoke(member, interest);
			if (i.size() != 2) {
				fail("addInterest failed to add interest 2");
			}
		} catch (Exception e) {
			unexpected(e);
		}
	}
}
