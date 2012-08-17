package nl.kaninefaten.jersey.spring;


import org.springframework.stereotype.Component;

@Component(value="SpringSampleBeanImpl")
public class SpringSampleBeanImpl implements SpringSampleBean{

	@Override
	public String hello() {
		return "hello";
		
	}
	
}
