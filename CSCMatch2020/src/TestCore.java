import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

abstract public class TestCore {
	static private String facility = "";
	static private int failures = 0;
	static private PrintStream lastStream = null;

	static protected TestClass cscMatchClass = new TestClass("CSCMatch");
	static protected TestClass menuClass = new TestClass("Menu");
	static protected TestClass membershipClass = new TestClass("Membership");
	static protected TestClass memberClass = new TestClass("Member", String.class, int.class);
	static protected TestClass interestClass = new TestClass("Interest", String.class, int.class);
	static protected TestClass compatibilityClass = new TestClass("Compatibility", memberClass.getClazz(), memberClass.getClazz());
	static protected TestClass invalidInterestExceptionClass = new TestClass("InvalidInterestException", String.class);
	static protected TestClass invalidMemberExceptionClass = new TestClass("InvalidMemberException", String.class);
	
	abstract public void runTests();
	
	public void setFacility(String facility) {
		this.facility = facility;
	}

	static public void log(PrintStream stream, String prefix, String message) {
		// Work-around timing issue that can cause System.out and System.err
		// messages to be intermixed in Eclipse console.
		if (lastStream != stream) {
			if (lastStream != null) {
				lastStream.flush();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
			lastStream = stream;
		}
		stream.println(prefix + " " + (facility.equals("") ? "" : facility + " : " ) + message);
	}

	public void successMessage(String reason) {
		log(System.out, "+", reason);
	}
	
	public void errorMessage(String reason) {
		log(System.err, "-", reason);
	}

	public void pass(String reason) {
		successMessage(reason);
	}

	public void fail(String reason) {
		errorMessage(reason);
		failures++;
		throw new TestExit();
	}
	
	public void unexpected(Throwable e) {
		if (!e.getClass().equals(TestExit.class)) {
			e.printStackTrace();
			fail("unexpected exception " + e.getClass() + ", please report to instructor");
		} else {
			throw new TestExit();
		}
	}
	
	public void startTests(String facility) {
		if (!this.facility.equals(facility)) {
			this.facility = facility;
			successMessage("start tests");
		}
	}
	
	public void summarizeTests() {
		if (failures == 0) {
			successMessage("all tests passed");
		} else {
			errorMessage(failures + " tests failed");
		}
	}

	public String modifierNames(int requiredModifiers) {
		String mods = "";
		if ((requiredModifiers & Modifier.STATIC) != 0) {
			mods += "static ";
		}
		if ((requiredModifiers & Modifier.PUBLIC) != 0) {
			mods += "public ";
		}
		if ((requiredModifiers & Modifier.PRIVATE) != 0) {
			mods += "private ";
		}
		if ((requiredModifiers & Modifier.PROTECTED) != 0) {
			mods += "protected ";
		}
		mods = mods.trim();
		return mods;
	}
		
	public Field requiredVariable(TestClass tc, String name, int requiredModifiers, String type) {
		Field field = null;
		try {
			field = tc.getClazz().getDeclaredField(name);
			String fieldType = field.getGenericType().toString().replace("class ",  "").replace("java.lang.",  "");
			if (!fieldType.equals(type)) {
				fail(tc.getName() + " variable " + name + " must be of type " + type);
			}
			int modifiers = field.getModifiers();
			if (modifiers != requiredModifiers) {
				fail(tc.getName() + " variable " + name + " must be declared as " + modifierNames(requiredModifiers));
			}
		} catch (NoSuchFieldException e) {
			fail(tc.getName() + " variable " + name + " not declared");
		}
		field.setAccessible(true);
		return field;
	}
	
	public Method requiredMethod(TestClass tc, String name, int requiredModifiers, String returnType, Class<?> ... parameters) {
		Method method = null;
		
		try {
			method = tc.getClazz().getDeclaredMethod(name, parameters);
			if (method.getModifiers() != requiredModifiers) {
				fail(tc.getName() + " method " + method.getName() + " must be declared as " + modifierNames(requiredModifiers));
			}
			String methodReturnType = method.getGenericReturnType().toString().replace("class ",  "").replace("java.lang.",  "");
			if (!methodReturnType.equals(returnType)) {
				fail(tc.getName() + " method " + name + " must return type " + returnType);
			}
		} catch (NoSuchMethodException e) {
			fail(tc.getName() + " method " + name + " does not exist with " + e.getMessage());
		}
		
		return method;
	}
	
	public void requiredClass(TestClass tc) {
		if (tc.getClazz() == null) {
			fail(tc.getName() + " required class " + tc.getName() + " not defined or invalid");
		}
		
		if (tc.getConstructor() == null) {
			fail(tc.getName() + " required class " + tc.getName() + " missing constructor " + tc.getConstructorError());
		}
	}
}
