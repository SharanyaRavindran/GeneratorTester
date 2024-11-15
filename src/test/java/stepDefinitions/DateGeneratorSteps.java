package stepDefinitions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class DateGeneratorSteps {

	WebDriver driver = new ChromeDriver();
	// getting all the web elements
	By dateTxtArea = By.id("generatedRandomDateTextArea");
	By generateRandomDateBtn = By.xpath("button[class = 'button is-primary is-large mt-4']");
	By copyBtn = By.xpath("use[href='#copy']");
	By downloadBtn = By.xpath("use[href='#downloadfile']");
	By count = By.id("count");
	By dateFormat = By.id("format");
	By customDateFormat = By.id("custom-format");
	By startDate = By.id("start");
	By endDate = By.id("end");

	int countOfDates;
	ArrayList<String> datesList;
	String startDatestr;
	String endDatestr;
	String formatOption;

	@Given("The user is on the date generator application")
	public void the_user_is_on_the_date_generator_application() {
		driver.get("https://codebeautify.org/generate-random-date");
		driver.manage().window().maximize();
	}

	@When("Enter {int} in How many dates to generate? text box")
	public void enter_count_in_text_box(Integer int1) {
		countOfDates = int1;
		driver.findElement(count).clear();
		driver.findElement(count).sendKeys(int1.toString());

	}

	@Then("Verify the number of dates generated.")
	public void verify_the_number_of_dates_generated() {
		String dateStr = driver.findElement(dateTxtArea).getAttribute("value");
		ArrayList<String> dates = new ArrayList<>(Arrays.asList(dateStr.split("\\r?\\n")));
		Assert.assertTrue(dates.size() == countOfDates);
	}

	@And("Check the dates generated")
	public void check_the_dates_generated() {
		String dateStr = driver.findElement(dateTxtArea).getAttribute("value");
		datesList = new ArrayList<>(Arrays.asList(dateStr.split("\\r?\\n")));
	}

	@Then("No duplicate date should be generated")
	public void no_duplicate_date_should_be_generated() {
		HashSet<String> setOfDates = new HashSet<>();
		for (String d : datesList) {
			if (!setOfDates.add(d)) {
				Assert.assertTrue(false);
			}
		}
	}

	@Then("The date generated should be a valid date")
	public void the_date_generated_should_be_a_valid_date() {
		Select formatSelect = new Select(driver.findElement(dateFormat));
		String format = formatSelect.getFirstSelectedOption().getText();

		if (format != null && format.equals("MM-DD-YYYY"))
			format = "MM-dd-yyyy";

		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(format);
		for (String dateStr : datesList) {
			LocalDate randomDate = LocalDate.parse(dateStr, formatter1);
			if (randomDate.isLeapYear() && randomDate.getMonthValue() == 2) {
				Assert.assertTrue("leap year should not have dates greater than 29", randomDate.getDayOfMonth() <= 29);
			} else if (randomDate.getMonthValue() == 2) {
				Assert.assertTrue("Non leap year should not have dates greater than 28",
						randomDate.getDayOfMonth() < 29);
			} else if (randomDate.getMonthValue() == 1 || randomDate.getMonthValue() == 3
					|| randomDate.getMonthValue() == 5 || randomDate.getMonthValue() == 7
					|| randomDate.getMonthValue() == 8 || randomDate.getMonthValue() == 10
					|| randomDate.getMonthValue() == 12) {
				Assert.assertTrue("Check dates for months Jan, Mar, May, Jul, Aug, Oct, Dec",
						randomDate.getDayOfMonth() <= 31);
			} else {
				Assert.assertTrue("Check dates for months Apr, Jun, Sep, Nov", randomDate.getDayOfMonth() <= 30);
			}
		}
	}

	@When("Enter {string} and {string}")
	public void enter_start_and_end_dates(String startDatestr, String endDatestr) {
		this.startDatestr = startDatestr;
		this.endDatestr = endDatestr;
		driver.findElement(startDate).sendKeys(startDatestr);
		driver.findElement(endDate).sendKeys(endDatestr);
	}

	@Then("The dates generated should be in range of start and end dates")
	public void the_dates_generated_should_be_in_range_of_start_and_end_dates() {
		check_the_dates_generated();
		Select formatSelect = new Select(driver.findElement(dateFormat));
		String format = formatSelect.getFirstSelectedOption().getText();

		if (format != null && format.equals("MM-DD-YYYY"))
			format = "MM-dd-yyyy";

		// String format = driver.findElement(dateFormat).getText();
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(format);
//		System.out.println("==========startDatestr  "+startDatestr + " === endDatestr == "+endDatestr);
		LocalDate startD = LocalDate.parse(startDatestr, formatter1);
		LocalDate endD = LocalDate.parse(endDatestr, formatter1);
		for (String dateStr : datesList) {
			LocalDate randomDate = LocalDate.parse(dateStr, formatter1);
			System.out.println(
					"==== Start Date: " + startD + "  ==== End DAte: " + endD + " === Testing Date: " + dateStr);
			Assert.assertTrue(randomDate.isAfter(startD) && randomDate.isBefore(endD));
		}
	}

	@When("Select {string} in Date Output Format dropdown")
	public void select_format_in_dropdown(String formatOption) {
		this.formatOption = formatOption;
		Select formatSelect = new Select(driver.findElement(dateFormat));
		formatSelect.selectByValue(formatOption);
	}

	@Then("The dates generated should be in the format selected")
	public void the_dates_generated_should_be_in_the_format_selected() {
		check_the_dates_generated();

		if (formatOption != null && formatOption.equals("yyyy-dd-mm-hh-mm-ss"))
			formatOption = "yyyy-dd-MM HH:mm:ss";
		else if (formatOption != null && formatOption.equals("mm-dd-yyyy-hh-mm-ss"))
			formatOption = "MM-dd-yyyy HH:mm:ss";
		else if (formatOption != null && formatOption.equals("mm-dd-yyyy"))
			formatOption = "MM-dd-yyyy";
		System.out.println("formatOption == " + formatOption);
		for (String d : datesList) {

			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(formatOption);
			LocalDate date2 = LocalDate.parse(d, formatter1);
			System.out.println("formatter1===" + formatter1);
			System.out.println("date2===" + date2);
			System.out.println("date generated == " + d);
			Assert.assertTrue(d.equals(date2.toString()));
		}

	}

	@After(order = 0)
	public void closeBrowser() {
		driver.close();
		driver.quit();
	}

}
