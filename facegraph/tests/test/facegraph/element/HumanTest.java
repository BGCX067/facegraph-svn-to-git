package test.facegraph.element;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.Mockito;
import org.neo4j.graphdb.Node;

import facegraph.element.Human;

public class HumanTest {

	Mockito _mockery = new Mockito();
	
	@Test
	public void testFriendWith() 
	{
		Human humanMock = _mockery.mock(Human.class);
		
	}

	@Test
	public void testLike() {
		fail("Not yet implemented");
	}

	@Test
	public void testNodeAsHuman() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProperty() {
		fail("Not yet implemented");
	}

	@Test
	public void testAsNode() {
		fail("Not yet implemented");
	}

}
