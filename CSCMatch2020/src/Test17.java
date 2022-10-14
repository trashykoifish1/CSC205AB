import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;

public class Test17 extends TestCore {
	public static void main(String[] args) {
		new Test17().runTests();
	}

	public void runTests() {
		new Test15().runTests();
		startTests(this.getClass().getName());
		try { testMembership(); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testMembership() {
		TestClass tc = memberClass;
		String name = tc.getName();
		Method addInterest = null;
		Method iterator;
		Method getTopic;
		Method getLevel;

		requiredClass(tc);
		addInterest = requiredMethod(tc, "addInterest", Modifier.PUBLIC, "void", interestClass.getClazz());
		iterator = requiredMethod(tc, "iterator", Modifier.PUBLIC, "java.util.Iterator<Interest>");
		getTopic = requiredMethod(interestClass, "getTopic", Modifier.PUBLIC, "String");
		getLevel = requiredMethod(interestClass, "getLevel", Modifier.PUBLIC, "int");
		
		try {
			Object member = memberClass.newInstance("Test Name", 1);
			Object interest = interestClass.newInstance("Java", 2);
			addInterest.invoke(member, interest);
			interest = interestClass.newInstance("Java", 4);
			addInterest.invoke(member, interest);
			interest = interestClass.newInstance("Java", 6);
			addInterest.invoke(member, interest);
		
			String expected;
			String actual;
			
			Iterator<Object> q = (Iterator<Object>) iterator.invoke(member);
			int i = 0;
			boolean java2 = false;
			boolean java4 = false;
			boolean java6 = false;
			
			while (q.hasNext()) {
				Object o = q.next();
				i++;
				String topic = (String) getTopic.invoke(o);
				if (topic.equals("Java")) {
					int level = (int) getLevel.invoke(o);
					if (level == 2) {
						java2 = true;
					} else if (level == 4) {
						java4 = true;
					} else if (level == 6) {
						java6 = true;
					} else {
						fail("Interest returned Java level that wasn't 2, 4, or 6: " + level);
					}
				} else {
					fail("Interest returned topic that wasn't Java: " + topic);
				}
			}
			
			if (java2 && java4 && java6) {
				fail("Interest returned Java at 2, 4, and 6 instead of just 6");
			}
			
			if (i > 1) {
				fail("Interest returned " + i + " values instead of just 1");
			}
			
			if (!java6) {
				fail("Interest did not return Java at 6");
			}
		} catch (Exception e) {
			unexpected(e);
		}
	}
}
