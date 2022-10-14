import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Test04 extends TestCore {
	public static void main(String[] args) {
		new Test04().runTests();
	}

	public void runTests() {
		startTests(this.getClass().getName());
		try { testInterest(); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testInterest() {
		TestClass tc = interestClass;
		String name = "Interest";
		Class<?> clazz = null;
		Constructor<?> constructor = null;
		Method setTopic = null;
		Method setLevel = null;
		Method getTopic = null;
		Method getLevel = null;
		Object interest = null;
		String testTopic = "Test Topic";
		String testTopic2 = "Test topic2";
		int testLevel = 1;
		String gotTopic;
		int gotLevel;
		
		requiredClass(tc);
		requiredVariable(tc, "topic", Modifier.PRIVATE, "String");
		requiredVariable(tc, "level", Modifier.PRIVATE, "int");
		
		try {
			interest = tc.newInstance(testTopic, testLevel);
		} catch (InvocationTargetException e1) {
			Throwable e2 = (Exception) e1.getCause();
			if (e2.getClass().equals(invalidInterestExceptionClass.getClazz())) {
				fail("constructor did not allow topic to be set to '" + testTopic + "' and level to be set to " + testLevel);
			} else {
				unexpected(e2);
			}
		} catch (Exception e) {
			unexpected(e);
		}
		
		try {
			Object o = tc.newInstance("", testLevel);
			fail("constructor allowed topic to be set to \"\"");
		} catch (InvocationTargetException e1) {
			Throwable e2 = (Exception) e1.getCause();
			if (e2.getClass().equals(invalidInterestExceptionClass.getClazz())) {
				// Setting name to "" was blocked
			} else {
				unexpected(e2);
			}
		} catch (Exception e) {
			unexpected(e);
		}
		
		for (int i = -1; i < 12; i++) {
			try {
				Object o = tc.newInstance(testTopic, i);
				if (i < 0 || i > 10) {
					fail("constructor allowed level to be set to " + i);
				}
			} catch (InvocationTargetException e1) {
				Throwable e2 = (Exception) e1.getCause();
				if (e2.getClass().equals(invalidInterestExceptionClass.getClazz())) {
					if (i >= 0 && i <= 10) {
						fail("constructor did not allow level to be set to " + i);
					}
				} else {
					unexpected(e2);
				}
			} catch (Exception e) {
				unexpected(e);
			}
		}

		try {
			setTopic = requiredMethod(tc, "setTopic", Modifier.PUBLIC, "void", String.class);
			getTopic = requiredMethod(tc, "getTopic", Modifier.PUBLIC, "String");
			setLevel = requiredMethod(tc, "setLevel", Modifier.PUBLIC, "void", int.class);
			getLevel = requiredMethod(tc, "getLevel", Modifier.PUBLIC, "int");
			
			gotTopic = (String) getTopic.invoke(interest);
			gotLevel = (int) getLevel.invoke(interest);
			
			if (!testTopic.equals(gotTopic)) {
				fail("constructed name '" + testTopic + "' getTopic '" + gotTopic + "'");
			}
			
			if (testLevel != gotLevel) {
				fail("constructed level '" + testLevel + "' getLevel '" + gotLevel + "'");
			}

			testLevel++;
			setTopic.invoke(interest, testTopic2);
			setLevel.invoke(interest, testLevel);

			gotTopic = (String) getTopic.invoke(interest);
			gotLevel = (int) getLevel.invoke(interest);
			
			if (!testTopic2.equals(gotTopic)) {
				fail("setTopic '" + testTopic2 + "' getTopic '" + gotTopic + "'");
			}
			
			if (testLevel != gotLevel) {
				fail("setLevel level '" + testTopic + "' getLevel '" + gotLevel + "'");
			}

			Method toString = requiredMethod(tc, "toString", Modifier.PUBLIC, "String");
			toString.invoke(interest);
		} catch (Exception e) {
			unexpected(e);
		}
		
		try {
			setTopic.invoke(interest,  "");
			fail("setTopic allowed name to be changed to \"\"");
		} catch (InvocationTargetException e1) {
			Throwable e2 = (Exception) e1.getCause();
			if (!e2.getClass().equals(invalidInterestExceptionClass.getClazz())) {
				unexpected(e2);
			}
		} catch (Exception e) {
			unexpected(e);
		}
		
		try {
			setLevel.invoke(interest, -1);
			fail("setLevel allowed level to be changed to -1");
		} catch (InvocationTargetException e1) {
			Throwable e2 = (Exception) e1.getCause();
			if (!e2.getClass().equals(invalidInterestExceptionClass.getClazz())) {
				unexpected(e2);
			}
		} catch (Exception e) {
			unexpected(e);
		}
	}
}
