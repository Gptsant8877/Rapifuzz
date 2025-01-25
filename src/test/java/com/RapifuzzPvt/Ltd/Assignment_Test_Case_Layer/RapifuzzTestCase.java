package com.RapifuzzPvt.Ltd.Assignment_Test_Case_Layer;


import java.io.IOException;

import org.testng.annotations.Test;

import com.RapifuzzPvt.Ltd.Assignment.Page_Layer.Rapifuzz;



public class RapifuzzTestCase extends BaseTest {

	@Test
	public void verifyDestination() throws IOException {
		
		Rapifuzz rapifuzz=new Rapifuzz(gm);
		rapifuzz.validateDestination();
		
	}
	
}
