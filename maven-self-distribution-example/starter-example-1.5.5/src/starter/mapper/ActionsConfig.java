package starter.mapper;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionMapping;


@ActionMapping (
		memoryInContainer_streams="true",
		
		redirects={},
		actions={
			@Action (
					path="operator",
					redirect = "/menuCreator"
			)					
		}
		
	)



public class ActionsConfig {

}
