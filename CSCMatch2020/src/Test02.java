import java.lang.reflect.Constructor;

public class Test02 extends TestCore {
	public static void main(String[] args) {
		new Test02().runTests();
	}

	public void runTests() {
		startTests(this.getClass().getName());
		try { testException(invalidInterestExceptionClass); } catch (TestExit e) {}
		try { testException(invalidMemberExceptionClass); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testException(TestClass tc) {
		String name = tc.getName();
		String testMessage = "***" + name + "***";
		
		requiredClass(tc);

		try {
			Exception e = (Exception) tc.newInstance(testMessage);
			throw e;
		} catch (Exception e) {
			if (name.equals(e.getClass().getName())) {
				if (!testMessage.equals(e.getMessage())) {
					fail("message thrown '" + testMessage + "' not message caught '" + e.getMessage() + "'");
				}
			} else {
				unexpected(e);
			}
		}
	}
}
