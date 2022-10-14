import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;

public class Test10 extends TestCore {
	public static void main(String[] args) {
		new Test10().runTests();
	}

	public void runTests() {
		new Test06().runTests();
		startTests(this.getClass().getName());
		try { testMembership(); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testMembership() {
		TestClass tc = membershipClass;
		Method addMember = null;
		Method iterator;
		Method findMember;

		requiredClass(tc);
		addMember = requiredMethod(tc, "addMember", Modifier.PUBLIC, "void", memberClass.getClazz());
		iterator = requiredMethod(tc, "iterator", Modifier.PUBLIC, "java.util.Iterator<Member>");
		findMember = requiredMethod(tc, "findMember", Modifier.PUBLIC, "Member", String.class);
		
		try {
			Object membership = membershipClass.newInstance();
			Object member = memberClass.newInstance("N1", 1);
			addMember.invoke(membership, member);
			member = memberClass.newInstance("N2", 2);
			addMember.invoke(membership, member);
			member = memberClass.newInstance("N3", 3);
			addMember.invoke(membership, member);
		
			String expected;
			String actual;
			
			Iterator<Object> iter = (Iterator<Object>) iterator.invoke(membership);
			int i = 0;
			while (iter.hasNext()) {
				Object o = iter.next();
				i++;
				expected = "N" + i + " - " + i;
				actual = o.toString();
				if (!expected.equals(actual)) {
					fail("Membership iterator pass " + i + " expected '" + expected + "' actual '" + actual + "'");
				}
			}
			
			pass("Membership toString returned\n" + membership.toString());
			
			member = findMember.invoke(membership, "N2");
			if (member == null) {
				fail("findMember failed to locate N2");
			}
			
			member = findMember.invoke(membership, "N9");
			if (member != null) {
				fail("findMember located non-existent N9");
			}
		} catch (Exception e) {
			unexpected(e);
		}
	}
}
