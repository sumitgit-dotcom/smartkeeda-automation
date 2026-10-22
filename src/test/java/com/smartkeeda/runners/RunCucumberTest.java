package com.smartkeeda.runners;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
	    features = "src/test/resources/features",
	    glue = "com.smartkeeda",
	    plugin = {
	        "pretty",
	        "html:target/cucumber-reports/cucumber.html"
	    },
	    monochrome = true
	)

public class RunCucumberTest extends AbstractTestNGCucumberTests {

	
	@Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
