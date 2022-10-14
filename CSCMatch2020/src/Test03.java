import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Test03 extends TestCore {
	public static void main(String[] args) {
		new Test03().runTests();
	}

	public void runTests() {
		startTests(this.getClass().getName());
		try { testMember(); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testMember() {
		TestClass tc = memberClass;
		String name = "Member";
		Method setName = null;
		Method setYear = null;
		Method getName = null;
		Method getYear = null;
		Object member = null;
		String testName = "Test Name";
		String testName2 = "Test Name 2";
		int testYear = 1;
		String gotName;
		int gotYear;
		
		requiredClass(tc);
		requiredVariable(tc, "name", Modifier.PRIVATE, "String");
		requiredVariable(tc, "year", Modifier.PRIVATE, "int");
		
		try {
			member = tc.newInstance(testName, testYear);
		} catch (InvocationTargetException e1) {
			Throwable e2 = (Exception) e1.getCause();
			if (e2.getClass().equals(invalidMemberExceptionClass.getClazz())) {
				fail("constructor did not allow name to be set to '" + testName + "' and year to be set to " + testYear);
			} else {
				unexpected(e2);
			}
		} catch (Exception e) {
			unexpected(e);
		}
		
		try {
			Object o = tc.newInstance("", testYear);
			fail("constructor allowed name to be set to \"\"");
		} catch (InvocationTargetException e1) {
			Throwable e2 = (Exception) e1.getCause();
			if (e2.getClass().equals(invalidMemberExceptionClass.getClazz())) {
				// Setting name to "" was blocked
			} else {
				unexpected(e2);
			}
		} catch (Exception e) {
			unexpected(e);
		}
		
		for (int i = 0; i < 7; i++) {
			try {
				Object o = tc.newInstance(testName, i);
				if (i < 1 || i > 5) {
					fail("constructor allowed year to be set to " + i);
				}
			} catch (InvocationTargetException e1) {
				Throwable e2 = (Exception) e1.getCause();
				if (e2.getClass().equals(invalidMemberExceptionClass.getClazz())) {
					if (i >= 1 && i <= 5) {
						fail("constructor did not allow year to be set to " + i);
					}
				} else {
					unexpected(e2);
				}
			} catch (Exception e) {
				unexpected(e);
			}
		}
		
		try {
			setName = requiredMethod(tc, "setName", Modifier.PUBLIC, "void", String.class);
			getName = requiredMethod(tc, "getName", Modifier.PUBLIC, "String");
			setYear = requiredMethod(tc, "setYear", Modifier.PUBLIC, "void", int.class);
			getYear = requiredMethod(tc, "getYear", Modifier.PUBLIC, "int");
			
			gotName = (String) getName.invoke(member);
			gotYear = (int) getYear.invoke(member);
			
			if (!testName.equals(gotName)) {
				fail("constructed name '" + testName + "' getName '" + gotName + "'");
			}
			
			if (testYear != gotYear) {
				fail("constructed year '" + testYear + "' getYear '" + gotYear + "'");
			}

			testYear++;
			setName.invoke(member, testName2);
			setYear.invoke(member, testYear);

			gotName = (String) getName.invoke(member);
			gotYear = (int) getYear.invoke(member);
			
			if (!testName2.equals(gotName)) {
				fail("setName '" + testName2 + "' getName '" + gotName + "'");
			}
			
			if (testYear != gotYear) {
				fail("setYear year '" + testName + "' getYear '" + gotYear + "'");
			}

			Method toString = requiredMethod(tc, "toString", Modifier.PUBLIC, "String");
			toString.invoke(member);
		} catch (Exception e) {
			unexpected(e);
		}
		
		try {
			setName.invoke(member,  "");
			fail("setName allowed name to be changed to \"\"");
		} catch (InvocationTargetException e1) {
			Throwable e2 = (Exception) e1.getCause();
			if (!e2.getClass().equals(invalidMemberExceptionClass.getClazz())) {
				unexpected(e2);
			}
		} catch (Exception e) {
			unexpected(e);
		}
		
		try {
			setYear.invoke(member, 0);
			fail("setYear allowed year to be changed to 0");
		} catch (InvocationTargetException e1) {
			Throwable e2 = (Exception) e1.getCause();
			if (!e2.getClass().equals(invalidMemberExceptionClass.getClazz())) {
				unexpected(e2);
			}
		} catch (Exception e) {
			unexpected(e);
		}
	}
}
