package runnerFiles;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features= {"src/test/resources"},
		glue= {"stepDefinitions"},
		plugin= {"pretty",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
		)
public class MyTestRunner {
	// some 
}
