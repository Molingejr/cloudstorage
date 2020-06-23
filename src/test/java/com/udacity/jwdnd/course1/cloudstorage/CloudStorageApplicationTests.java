package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	final String saveButton = "/div[@class='modal-dialog']" +
			"/div[@class='modal-content']" +
			"/div[@class='modal-footer']" +
			"/button[text()='Save changes']";

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test that verifies that an unauthorized user can only access the login and signup pages
	@Test
	@Order(2)
	public void unauthorizeUser() {
		// Try to access home
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());

		// Try to access result
		driver.get("http://localhost:" + this.port + "/result");
		Assertions.assertEquals("Login", driver.getTitle());

		// Try to access signup
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		// Try to access login
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test that signs up a new user, logs in, verifies that the home page is accessible,
	// logs out, and verifies that the home page is no longer accessible.
	@Test
	@Order(3)
	public void authorizedUser() throws InterruptedException {
		// Test user signup
		driver.get("http://localhost:" + this.port + "/signup");
		WebDriverWait wait = new WebDriverWait(driver, 2);
		WebElement firstName =  wait.until(ExpectedConditions.elementToBeClickable(By.id("inputFirstName")));
		WebElement lastName = driver.findElement(By.id("inputLastName"));
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement signUp = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Sign Up']")));
		firstName.sendKeys("Mikel");
		lastName.sendKeys("Anderson");
		username.sendKeys("mikel");
		password.sendKeys("Anderson@mikel32");
		signUp.click();
		String signupUrl = "http://localhost:" + this.port + "/signup?success";
		String expectedUrl = driver.getCurrentUrl();
		Assertions.assertEquals(expectedUrl, signupUrl);

		// Test user login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement login_username = driver.findElement(By.id("inputUsername"));
		WebElement login_password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		login_username.sendKeys("mikel");
		login_password.sendKeys("Anderson@mikel32");
		login.click();
		String homeUrl = "http://localhost:" + this.port + "/home";
		String currentUrl = driver.getCurrentUrl();
		Assertions.assertEquals(currentUrl, homeUrl);

		// Test User logout and verity access to home page
		WebElement logout = driver.findElement(By.xpath("//button[text()='Logout']"));
		logout.click();
		Thread.sleep(60);
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test that creates a note, and verifies it is displayed.
	@Test
	@Order(4)
	public void createNote(){
		// login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		password.sendKeys("Anderson@mikel32");
		login.click();

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Locate and click on 'Add a New Note' button
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		driver.findElement(By.linkText("Notes")).click();
		driver.switchTo().activeElement();

		WebElement addNewNote = driver.findElement(By.xpath("//button[text()= '\n" +
				"                            + Add a New Note\n" +
				"                        ']"));

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"//div[@id='nav-notes']/button[@class='btn btn-info float-right']")));
		addNewNote.click();

		// New Note Modal popup and fill it and click save
		String addNewNoteModal = driver.getWindowHandle();
		driver.switchTo().window(addNewNoteModal);

		WebElement noteTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		WebElement noteDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description")));
		WebElement saveNote = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[@id='noteModal']"+saveButton)));
		noteTitle.sendKeys("My Test Note");
		noteDescription.sendKeys("My test note description");
		saveNote.click();

		// Validate success URL
		String currentUrl = driver.getCurrentUrl();
		String actualUrl = "http://localhost:" + this.port + "/result?success";
		Assertions.assertEquals(currentUrl, actualUrl);

		// Verify display of the new note
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		continueLink.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		driver.findElement(By.linkText("Notes")).click();
		driver.switchTo().activeElement();
		WebElement noteHeaderTitle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/thead/tr/th[text()='Title']")));

		WebElement newNoteTitle = driver.findElement(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
						"/table[@id='userTable']/tbody/tr/th[text()='My Test Note']"));
		WebElement newNoteDescription = driver.findElement(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/td[text()='My test note description']"));
		Assertions.assertEquals(newNoteTitle.getText(), "My Test Note");
		Assertions.assertEquals(newNoteDescription.getText(), "My test note description");

	}

	// Test that edits an existing note and verifies that the changes are displayed.
	@Test
	@Order(5)
	public void editNote(){
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		password.sendKeys("Anderson@mikel32");
		login.click();

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Locate Note section
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		driver.findElement(By.linkText("Notes")).click();
		driver.switchTo().activeElement();
		WebElement editNote = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/td/button[text()='Edit']")));
		editNote.click();

		String editNoteModal = driver.getWindowHandle();
		driver.switchTo().window(editNoteModal);

		WebElement noteTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		WebElement noteDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description")));
		WebElement saveNote = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[@id='noteModal']"+saveButton)));

		noteTitle.click();
		noteTitle.clear();
		noteTitle.sendKeys("New Test Title");

		noteDescription.click();
		noteDescription.clear();
		noteDescription.sendKeys("New Test Description");

		saveNote.click();

		String expectedUrl=driver.getCurrentUrl();
		String actualUrl = "http://localhost:" + this.port + "/result?success";
		Assertions.assertEquals(expectedUrl, actualUrl);

		// Verify display of the edited note
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		continueLink.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		driver.findElement(By.linkText("Notes")).click();
		driver.switchTo().activeElement();
		WebElement noteHeaderTitle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/thead/tr/th[text()='Title']")));

		WebElement editedNoteTitle = driver.findElement(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/th[text()='New Test Title']"));
		WebElement editedNoteDescription = driver.findElement(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/td[text()='New Test Description']"));
		Assertions.assertEquals(editedNoteTitle.getText(), "New Test Title");
		Assertions.assertEquals(editedNoteDescription.getText(), "New Test Description");
	}

	// Test that deletes a note and verifies that the note is no longer displayed
	@Test
	@Order(6)
	public void deleteNote(){
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		password.sendKeys("Anderson@mikel32");
		login.click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		// Locate note section
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		driver.findElement(By.linkText("Notes")).click();
		driver.switchTo().activeElement();

		WebElement deleteNote = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/td/a[text()='Delete']")));
		deleteNote.click();
		String expectedUrl=driver.getCurrentUrl();
		String actualUrl = "http://localhost:" + this.port + "/result?success";
		Assertions.assertEquals(expectedUrl, actualUrl);

		// Verify that deleted note no longer display
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		continueLink.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		driver.findElement(By.linkText("Notes")).click();
		driver.switchTo().activeElement();
		WebElement noteHeaderTitle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/thead/tr/th[text()='Title']")));

		WebElement table = driver.findElement(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody"));
		List<WebElement> rows = table.findElements(By.xpath("./tr"));
		Assertions.assertEquals(0, rows.size());
	}

	// Test that creates a set of credentials, verifies that they are displayed,
	// and verifies that the displayed password is encrypted
	@Test
	@Order(7)
	public void createCredential(){
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		password.sendKeys("Anderson@mikel32");
		login.click();

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Locate credentials section and click add new credential
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		driver.findElement(By.linkText("Credentials")).click();
		driver.switchTo().activeElement();
		WebElement addCredential = driver.findElement(By.xpath("//button[text()= '\n" +
				"                            + Add a New Credential\n" +
				"                        ']"));


		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/button[@class='btn btn-info float-right']")));
		addCredential.click();

		// Credential Modal pop-up and save new credential
		String addCredentialModal = driver.getWindowHandle();
		driver.switchTo().window(addCredentialModal);

		WebElement url = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		WebElement credentialUsername = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
		WebElement credentialPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));

		WebDriverWait wait2 = new WebDriverWait(driver, 2);
		WebElement saveNote = wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='credentialModal']"+saveButton)));

		url.sendKeys("http://mytesturl.com");
		credentialUsername.sendKeys("anthony");
		credentialPassword.sendKeys("Anthony@345");
		saveNote.click();

		// Test success
		String expectedUrl=driver.getCurrentUrl();
		String actualUrl = "http://localhost:" + this.port + "/result?success";
		Assertions.assertEquals(expectedUrl, actualUrl);

		// Verify display of newly created credentials
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		continueLink.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		driver.findElement(By.linkText("Credentials")).click();
		driver.switchTo().activeElement();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/button[@class='btn btn-info float-right']")));

	}

	// Test that views an existing set of credentials, verifies that the viewable password is unencrypted,
	// edits the credentials, and verifies that the changes are displayed
	@Test
	@Order(8)
	public void editCredential(){
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		password.sendKeys("Anderson@mikel32");
		login.click();

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Find credentials and click edit
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		driver.findElement(By.linkText("Credentials")).click();
		driver.switchTo().activeElement();


		WebElement editCredential = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/div[@class='table-responsive']" +
				"/table[@id='credentialTable']/tbody/tr/td/button[text()='Edit']")));
		editCredential.click();

		// Credential modal pop-up and fill data and click save
		String editCredentialModal = driver.getWindowHandle();
		driver.switchTo().window(editCredentialModal);

		WebElement url = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		WebElement credentialUsername = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
		WebElement credentialPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));
		WebElement saveCredential = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='credentialModal']"+saveButton)));

		url.click();
		url.clear();
		url.sendKeys("http://mynewurl.com");

		credentialUsername.click();
		credentialUsername.clear();
		credentialUsername.sendKeys("jerry");

		credentialPassword.click();
		credentialPassword.clear();
		credentialPassword.sendKeys("jerry@123");

		saveCredential.click();

		String expectedUrl=driver.getCurrentUrl();
		String actualUrl = "http://localhost:" + this.port + "/result?success";
		Assertions.assertEquals(expectedUrl, actualUrl);

		// Verify display of edited credentials
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		continueLink.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		driver.findElement(By.linkText("Credentials")).click();
		driver.switchTo().activeElement();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/button[@class='btn btn-info float-right']")));
	}

	@Test
	@Order(9)
	public void deleteCredential(){
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		password.sendKeys("Anderson@mikel32");
		login.click();

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Go to credential tab and delete credentials
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		driver.findElement(By.linkText("Credentials")).click();
		driver.switchTo().activeElement();

		WebElement deleteCredentials = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/div[@class='table-responsive']" +
				"/table[@id='credentialTable']/tbody/tr/td/a[text()='Delete']")));

		deleteCredentials.click();
		String expectedUrl=driver.getCurrentUrl();
		String actualUrl = "http://localhost:" + this.port + "/result?success";
		Assertions.assertEquals(expectedUrl, actualUrl);

		// Verify display of deleted credentials
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		continueLink.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		driver.findElement(By.linkText("Credentials")).click();
		driver.switchTo().activeElement();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/button[@class='btn btn-info float-right']")));

	}

}
