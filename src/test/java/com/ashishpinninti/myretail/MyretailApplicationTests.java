package com.ashishpinninti.myretail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ashishpinninti.myretail.MyretailApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyretailApplication.class)
@WebAppConfiguration
public class MyretailApplicationTests {

	@Test
	public void contextLoads() {
	}

}
