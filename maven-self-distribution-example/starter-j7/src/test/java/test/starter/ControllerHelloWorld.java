package test.starter;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import starter.StarterInitializer;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerHelloWorld {

	@Before
	public void setUp() throws Exception {
		bsController.isInitialized(
				StarterInitializer.applicationProperties(),
				StarterInitializer.otherProperties()
		);
	  } 
	
	@Test
	public void test1HelloWorldInstance() {
		i_action action = null;
		try{
			action = bsController.getActionInstance("hello", null, null);
		}catch(Exception e){			
		}
		
		Assert.assertNotNull(action);
		Assert.assertNotNull(action.getCurrent_redirect());
		Assert.assertEquals(action.getCurrent_redirect().get_uri(),"/starter/pages/hello.html");
		
		assertThat(action, instanceOf(starter.components.HelloWorld.class));
		
		i_action action1 = null;
		try{
			action1 = bsController.getActionInstance("hello","resource", null, null);
		}catch(Exception e){			
		}
		Assert.assertNotNull(action1);
		

/*	
  		starter.components.HelloWorld instance = (starter.components.HelloWorld)action;	
		Assert.assertNotNull(instance.getNetwork());
		Assert.assertNotNull(network.getNetwork().obtainInstance());
		Assert.assertEquals(network.getDefaultNetwork(),"Interpolation");
		
		Assert.assertEquals(network.getNetwork().obtainInstance().getNeurons().length,10);
		Assert.assertEquals(network.getNetwork().obtainInstance().getSynapses().length,23);
		
		network.change("type", "XOR");
		Assert.assertEquals(network.getNetwork().obtainInstance().getNeurons().length,5);
		Assert.assertEquals(network.getNetwork().obtainInstance().getSynapses().length,6);	
		
		network.change("type", "CUST");
		Assert.assertEquals(network.getNetwork().obtainInstance().getNeurons().length,2);
		Assert.assertEquals(network.getNetwork().obtainInstance().getSynapses().length,1);		
*/		
	}
	
	
	@After
	public void clean() throws Exception {
		bsController.clean();
	}

}
