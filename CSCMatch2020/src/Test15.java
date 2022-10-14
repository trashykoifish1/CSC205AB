import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;

public class Test15 extends TestCore {
	public static void main(String[] args) {
		new Test15().runTests();
	}

	public void runTests() {
		startTests(this.getClass().getName());
		try { testMembership(); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testMembership() {
		TestClass tc = memberClass;
		String name = tc.getName();
		Method addInterest = null;
		Method iterator;
		Method findInterest;

		requiredClass(tc);
		addInterest = requiredMethod(tc, "addInterest", Modifier.PUBLIC, "void", interestClass.getClazz());
		iterator = requiredMethod(tc, "iterator", Modifier.PUBLIC, "java.util.Iterator<Interest>");
		findInterest = requiredMethod(tc, "findInterest", Modifier.PUBLIC, "Interest", String.class);
		
		try {
			Object member = memberClass.newInstance("Test Name", 1);
			Object interest = interestClass.newInstance("I1", 1);
			addInterest.invoke(member, interest);
			interest = interestClass.newInstance("I2", 2);
			addInterest.invoke(member, interest);
			interest = interestClass.newInstance("I3", 3);
			addInterest.invoke(member, interest);
		
			String expected;
			String actual;
			
			Iterator<Object> q = (Iterator<Object>) iterator.invoke(member);
			int i = 0;
			while (q.hasNext()) {
				Object o = q.next();
				i++;
				expected = "I" + i + " - " + i;
				actual = o.toString();
				if (!expected.equals(actual)) {
					fail("Member iterator pass " + i + " expected '" + expected + "' actual '" + actual + "'");
				}
			}
			
			pass("Member toString returned\n" + member.toString());
			
			interest = findInterest.invoke(member, "I2");
			if (interest == null) {
				fail("findInterest failed to locate I2");
			}
			
			interest = findInterest.invoke(member, "I9");
			if (interest != null) {
				fail("findInterest located non-existent I9");
			}
		} catch (Exception e) {
			unexpected(e);
		}
	}
}
