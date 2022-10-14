import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;

public class Test21 extends TestCore {
	public static void main(String[] args) {
		new Test21().runTests();
	}

	public void runTests() {
		startTests(this.getClass().getName());
		try { testCompatibility(); } catch (TestExit e) {}
		summarizeTests();
	}

	public void testCompatibility() {
		TestClass tc = compatibilityClass;
		Method addInterest;
		Object memberA;
		Object memberB;
		Object memberC;
		
		requiredClass(tc);
		requiredClass(memberClass);
		requiredClass(interestClass);
		addInterest = requiredMethod(memberClass, "addInterest", Modifier.PUBLIC, "void", interestClass.getClazz());
		
		try {
			memberA = memberClass.newInstance("Member A", 1);
			memberB = memberClass.newInstance("Member B", 2);
			memberC = memberClass.newInstance("Member C", 3);
			
			addInterest.invoke(memberA,  interestClass.newInstance("Topic 1", 2));
			addInterest.invoke(memberA,  interestClass.newInstance("Topic 2", 3));
			addInterest.invoke(memberA,  interestClass.newInstance("Topic 3", 5));
			
			addInterest.invoke(memberB,  interestClass.newInstance("Topic 1", 5));
			addInterest.invoke(memberB,  interestClass.newInstance("Topic 3", 3));
			addInterest.invoke(memberB,  interestClass.newInstance("Topic 4", 7));
			addInterest.invoke(memberB,  interestClass.newInstance("Topic 5", 1));
			addInterest.invoke(memberB,  interestClass.newInstance("Topic 6", 9));
			
			addInterest.invoke(memberC,  interestClass.newInstance("Topic 1", 5));
			addInterest.invoke(memberC,  interestClass.newInstance("Topic 2", 10));

			testScore(memberA, memberB, 32);
			testScore(memberA, memberC, 40);
			testScore(memberB, memberA, 26);
			testScore(memberB, memberC, 30);
			testScore(memberC, memberA, 42);
			testScore(memberC, memberB, 33);
		} catch (Exception e) {
			unexpected(e);
		}
	}
	
	public void testScore(Object my, Object their, int expected) {
		try {
			Method getName = requiredMethod(memberClass, "getName",  Modifier.PUBLIC, "String");
			Method getScore = requiredMethod(compatibilityClass, "getScore", Modifier.PUBLIC, "int");
			Object c = compatibilityClass.newInstance(my, their);
			int actual = (int) getScore.invoke(c); 
			if (actual != expected) {
				fail("score from " + getName.invoke(my) + " to " + getName.invoke(their) + " expected " + expected + " got " + actual);
			} else {
				pass("score from " + getName.invoke(my) + " to " + getName.invoke(their) + " correct at " + actual);
			}
		} catch (TestExit e) {
			// ignore failure exit request
		} catch (Exception e) {
			try {
				unexpected(e);
			} catch (TestExit e2) {
				// ignore failure exit request
			}
		}
	}
}
