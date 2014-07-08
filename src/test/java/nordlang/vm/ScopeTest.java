package nordlang.vm;

import nordlang.exceptions.DefinitionException;
import nordlang.exceptions.RunException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ScopeTest {

	@Test
	public void putNumberVariableWithCheck() throws DefinitionException {
		Scope scope = new Scope();
		assertFalse(scope.contains("abc"));
		TwinValue v = new TwinValue(0);
		scope.put("xyz", v);
		assertNotSame(v, scope.get("xyz"));
		assertTrue(scope.contains("xyz"));
		assertNotNull(scope.get("xyz").getIntVal());
		assertNull(scope.get("xyz").getStrVal());
		assertEquals(0, (int) scope.get("xyz").getIntVal());
		assertEquals("0", scope.get("xyz").toString());
		scope.set("xyz", new TwinValue("test"));
		assertNull(scope.get("xyz").getIntVal());
		assertEquals("test", scope.get("xyz").getStrVal());
	}

	@Test
	public void putStringVariableWithCheck() throws DefinitionException {
		Scope scope = new Scope();
		scope.put("xyz", new TwinValue("Hello, world"));
		assertTrue(scope.contains("xyz"));
		assertNotNull(scope.get("xyz").getStrVal());
		assertNull(scope.get("xyz").getIntVal());
		assertEquals("Hello, world", scope.get("xyz").getStrVal());
		assertEquals("Hello, world", scope.get("xyz").toString());
		scope.set("xyz", new TwinValue(123));
		assertNull(scope.get("xyz").getStrVal());
		assertEquals(new Integer(123), scope.get("xyz").getIntVal());
	}

	@Test(expected = DefinitionException.class)
	public void putVariableTwice() throws DefinitionException {
		Scope scope = new Scope();
		scope.put("xyz", new TwinValue(1));
		scope.put("xyz", new TwinValue(1));
	}

	@Test
	public void setExistingVariable() throws DefinitionException {
		Scope scope = new Scope();
		scope.put("xyz", new TwinValue(1));
		TwinValue v = new TwinValue(2);
		scope.set("xyz", v);
		assertNotSame(v, scope.get("xyz"));
		assertEquals(v.getStrVal(), scope.get("xyz").getStrVal());
	}

	@Test(expected = RunException.class)
	public void setUndefinedVariable() throws DefinitionException {
		Scope scope = new Scope();
		scope.set("xyz", new TwinValue("b"));
	}

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void setDefReservedName() throws DefinitionException {
		expectedException.expect(DefinitionException.class);
		expectedException.expectMessage(JUnitMatchers.containsString("'def' is reserved by environment"));
		Scope scope = new Scope();
		scope.put("def", new TwinValue("b"));
	}

	@Test
	public void setShowReservedName() throws DefinitionException {
		expectedException.expect(DefinitionException.class);
		expectedException.expectMessage(JUnitMatchers.containsString("'show' is reserved by environment"));
		Scope scope = new Scope();
		scope.put("show", new TwinValue("b"));
	}

	@Test
	public void removeByName() throws DefinitionException {
		Scope scope = new Scope();
		scope.put("b", new TwinValue(1));
		assertTrue(scope.contains("b"));
		scope.remove("b");
		assertFalse(scope.contains("b"));
	}

	@Test(expected = DefinitionException.class)
	public void removeUndefinedByName() throws DefinitionException {
		Scope scope = new Scope();
		scope.remove("b");
	}

}
