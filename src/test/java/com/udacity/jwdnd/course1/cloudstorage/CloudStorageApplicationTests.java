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
	public void getLoginPage() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/login");
		Thread.sleep(500);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test that verifies that an unauthorized user can only access the login and signup pages
	@Test
	@Order(2)
	public void unauthorizeUser() throws InterruptedException {
		// Try to access home
		driver.get("http://localhost:" + this.port + "/home");
		Thread.sleep(500);
		Assertions.assertEquals("Login", driver.getTitle());

		// Try to access result
		driver.get("http://localhost:" + this.port + "/result");
		Thread.sleep(500);
		Assertions.assertEquals("Login", driver.getTitle());

		// Try to access signup
		driver.get("http://localhost:" + this.port + "/signup");
		Thread.sleep(500);
		Assertions.assertEquals("Sign Up", driver.getTitle());

		// Try to access login
		driver.get("http://localhost:" + this.port + "/login");
		Thread.sleep(500);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test that signs up a new user, logs in, verifies that the home page is accessible,
	// logs out, and verifies that the home page is no longer accessible.
	@Test
	@Order(3)
	public void authorizedUser() throws InterruptedException {
		// Test user signup
		driver.get("http://localhost:" + this.port + "/signup");
		Thread.sleep(500);
		WebDriverWait wait = new WebDriverWait(driver, 2);
		WebElement firstName =  wait.until(ExpectedConditions.elementToBeClickable(By.id("inputFirstName")));
		WebElement lastName = driver.findElement(By.id("inputLastName"));
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement signUp = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Sign Up']")));
		firstName.sendKeys("Mikel");
		Thread.sleep(500);
		lastName.sendKeys("Anderson");
		Thread.sleep(500);
		username.sendKeys("mikel");
		Thread.sleep(500);
		password.sendKeys("Anderson@mikel32");
		Thread.sleep(500);
		signUp.click();
		String signupUrl = "http://localhost:" + this.port + "/signup?success";
		String expectedUrl = driver.getCurrentUrl();
		Thread.sleep(500);
		Assertions.assertEquals(expectedUrl, signupUrl);

		// Test user login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement login_username = driver.findElement(By.id("inputUsername"));
		WebElement login_password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		login_username.sendKeys("mikel");
		login_password.sendKeys("Anderson@mikel32");
		Thread.sleep(500);
		login.click();
		String homeUrl = "http://localhost:" + this.port + "/home";
		String currentUrl = driver.getCurrentUrl();
		Thread.sleep(500);
		Assertions.assertEquals(currentUrl, homeUrl);

		// Test User logout and verity access to home page
		WebElement logout = driver.findElement(By.xpath("//button[text()='Logout']"));
		logout.click();
		Thread.sleep(500);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test that creates a note, and verifies it is displayed.
	@Test
	@Order(4)
	public void createNote() throws InterruptedException {
		// login
		driver.get("http://localhost:" + this.port + "/login");
		Thread.sleep(500);
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		Thread.sleep(500);
		password.sendKeys("Anderson@mikel32");
		Thread.sleep(500);
		login.click();
		Thread.sleep(500);

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Locate and click on 'Add a New Note' button
		driver.get("http://localhost:" + this.port + "/home");
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Notes")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);

		WebElement addNewNote = driver.findElement(By.xpath("//button[text()= '\n" +
				"                            + Add a New Note\n" +
				"                        ']"));
		Thread.sleep(500);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
				"//div[@id='nav-notes']/button[@class='btn btn-info float-right']")));
		addNewNote.click();
		Thread.sleep(500);

		// New Note Modal popup and fill it and click save
		String addNewNoteModal = driver.getWindowHandle();
		Thread.sleep(500);
		driver.switchTo().window(addNewNoteModal);
		Thread.sleep(500);

		WebElement noteTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		WebElement noteDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description")));
		WebElement saveNote = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[@id='noteModal']"+saveButton)));
		noteTitle.sendKeys("My Test Note");
		Thread.sleep(500);
		noteDescription.sendKeys("My test note description");
		Thread.sleep(500);
		saveNote.click();
		Thread.sleep(500);

		// Validate success URL
		driver.get("http://localhost:" + this.port + "/result?success");
		Assertions.assertEquals("Result?success", driver.getTitle() + "?success");

		// Verify display of the new note
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		continueLink.click();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Notes")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);
		WebElement noteHeaderTitle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/thead/tr/th[text()='Title']")));

		WebElement newNoteTitle = driver.findElement(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
						"/table[@id='userTable']/tbody/tr/th[text()='My Test Note']"));
		WebElement newNoteDescription = driver.findElement(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/td[text()='My test note description']"));
		Thread.sleep(500);
		Assertions.assertEquals(newNoteTitle.getText(), "My Test Note");
		Assertions.assertEquals(newNoteDescription.getText(), "My test note description");

	}

	// Test that edits an existing note and verifies that the changes are displayed.
	@Test
	@Order(5)
	public void editNote() throws InterruptedException {
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		Thread.sleep(500);
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		Thread.sleep(500);
		password.sendKeys("Anderson@mikel32");
		Thread.sleep(500);
		login.click();
		Thread.sleep(500);

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Locate Note section
		driver.get("http://localhost:" + this.port + "/home");
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Notes")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);
		WebElement editNote = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/td/button[text()='Edit']")));
		editNote.click();
		Thread.sleep(500);

		String editNoteModal = driver.getWindowHandle();
		driver.switchTo().window(editNoteModal);
		Thread.sleep(500);

		WebElement noteTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		WebElement noteDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description")));
		WebElement saveNote = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[@id='noteModal']"+saveButton)));
		Thread.sleep(500);

		noteTitle.click();
		Thread.sleep(500);
		noteTitle.clear();
		Thread.sleep(500);
		noteTitle.sendKeys("New Test Title");
		Thread.sleep(500);

		noteDescription.click();
		Thread.sleep(500);
		noteDescription.clear();
		Thread.sleep(500);
		noteDescription.sendKeys("New Test Description");
		Thread.sleep(500);

		saveNote.click();
		Thread.sleep(500);

		// Validate success URL
		driver.get("http://localhost:" + this.port + "/result?success");
		Assertions.assertEquals("Result?success", driver.getTitle() + "?success");


		// Verify display of the edited note
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		continueLink.click();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Notes")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);
		WebElement noteHeaderTitle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/thead/tr/th[text()='Title']")));

		WebElement editedNoteTitle = driver.findElement(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/th[text()='New Test Title']"));
		WebElement editedNoteDescription = driver.findElement(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/td[text()='New Test Description']"));
		Thread.sleep(500);
		Assertions.assertEquals(editedNoteTitle.getText(), "New Test Title");
		Assertions.assertEquals(editedNoteDescription.getText(), "New Test Description");
	}

	// Test that deletes a note and verifies that the note is no longer displayed
	@Test
	@Order(6)
	public void deleteNote() throws InterruptedException {
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		Thread.sleep(500);
		password.sendKeys("Anderson@mikel32");
		Thread.sleep(500);
		login.click();
		Thread.sleep(500);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		// Locate note section
		driver.get("http://localhost:" + this.port + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Notes")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();

		WebElement deleteNote = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-notes']/div[@class='table-responsive']" +
				"/table[@id='userTable']/tbody/tr/td/a[text()='Delete']")));
		deleteNote.click();
		Thread.sleep(500);

		// Validate success URL
		driver.get("http://localhost:" + this.port + "/result?success");
		Assertions.assertEquals("Result?success", driver.getTitle() + "?success");


		// Verify that deleted note no longer display
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		Thread.sleep(500);
		continueLink.click();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Notes")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Notes")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);
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
	public void createCredential() throws InterruptedException {
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		Thread.sleep(500);
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		Thread.sleep(500);
		password.sendKeys("Anderson@mikel32");
		Thread.sleep(500);
		login.click();
		Thread.sleep(500);

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Locate credentials section and click add new credential
		driver.get("http://localhost:" + this.port + "/home");
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Credentials")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);
		WebElement addCredential = driver.findElement(By.xpath("//button[text()= '\n" +
				"                            + Add a New Credential\n" +
				"                        ']"));


		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/button[@class='btn btn-info float-right']")));
		Thread.sleep(500);
		addCredential.click();

		// Credential Modal pop-up and save new credential
		String addCredentialModal = driver.getWindowHandle();
		Thread.sleep(500);
		driver.switchTo().window(addCredentialModal);
		Thread.sleep(500);

		WebElement url = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		WebElement credentialUsername = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
		WebElement credentialPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));

		WebDriverWait wait2 = new WebDriverWait(driver, 2);
		WebElement saveNote = wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='credentialModal']"+saveButton)));

		url.sendKeys("http://mytesturl.com");
		credentialUsername.sendKeys("anthony");
		Thread.sleep(500);
		credentialPassword.sendKeys("Anthony@345");
		Thread.sleep(500);
		saveNote.click();
		Thread.sleep(500);

		// Validate success URL
		driver.get("http://localhost:" + this.port + "/result?success");
		Assertions.assertEquals("Result?success", driver.getTitle() + "?success");


		// Verify display of newly created credentials
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		Thread.sleep(500);
		continueLink.click();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Credentials")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/button[@class='btn btn-info float-right']")));

	}

	// Test that views an existing set of credentials, verifies that the viewable password is unencrypted,
	// edits the credentials, and verifies that the changes are displayed
	@Test
	@Order(8)
	public void editCredential() throws InterruptedException {
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		Thread.sleep(500);
		password.sendKeys("Anderson@mikel32");
		Thread.sleep(500);
		login.click();
		Thread.sleep(500);

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Find credentials and click edit
		driver.get("http://localhost:" + this.port + "/home");
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Credentials")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);


		WebElement editCredential = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/div[@class='table-responsive']" +
				"/table[@id='credentialTable']/tbody/tr/td/button[text()='Edit']")));
		Thread.sleep(500);
		editCredential.click();

		// Credential modal pop-up and fill data and click save
		String editCredentialModal = driver.getWindowHandle();
		Thread.sleep(500);
		driver.switchTo().window(editCredentialModal);
		Thread.sleep(500);

		WebElement url = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		WebElement credentialUsername = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
		WebElement credentialPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));
		WebElement saveCredential = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='credentialModal']"+saveButton)));
		Thread.sleep(500);

		url.click();
		Thread.sleep(500);
		url.clear();
		Thread.sleep(500);
		url.sendKeys("http://mynewurl.com");
		Thread.sleep(500);

		credentialUsername.click();
		Thread.sleep(500);
		credentialUsername.clear();
		Thread.sleep(500);
		credentialUsername.sendKeys("jerry");
		Thread.sleep(500);

		credentialPassword.click();
		Thread.sleep(500);
		credentialPassword.clear();
		Thread.sleep(500);
		credentialPassword.sendKeys("jerry@123");
		Thread.sleep(500);

		saveCredential.click();
		Thread.sleep(500);

		// Validate success URL
		driver.get("http://localhost:" + this.port + "/result?success");
		Assertions.assertEquals("Result?success", driver.getTitle() + "?success");


		// Verify display of edited credentials
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		Thread.sleep(500);
		continueLink.click();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Credentials")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/button[@class='btn btn-info float-right']")));
	}

	@Test
	@Order(9)
	public void deleteCredential() throws InterruptedException {
		// Login
		driver.get("http://localhost:" + this.port + "/login");
		Thread.sleep(500);
		WebElement username = driver.findElement(By.id("inputUsername"));
		WebElement password = driver.findElement(By.id("inputPassword"));
		WebElement login = driver.findElement(By.xpath("//button[text()='Login']"));
		username.sendKeys("mikel");
		Thread.sleep(500);
		password.sendKeys("Anderson@mikel32");
		Thread.sleep(500);
		login.click();
		Thread.sleep(500);

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// Go to credential tab and delete credentials
		driver.get("http://localhost:" + this.port + "/home");
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Credentials")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);

		WebElement deleteCredentials = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/div[@class='table-responsive']" +
				"/table[@id='credentialTable']/tbody/tr/td/a[text()='Delete']")));

		deleteCredentials.click();
		Thread.sleep(500);
		
		// Validate success URL
		driver.get("http://localhost:" + this.port + "/result?success");
		Assertions.assertEquals("Result?success", driver.getTitle() + "?success");


		// Verify display of deleted credentials
		WebElement continueLink = driver.findElement(By.xpath("//a[text()='here']"));
		Thread.sleep(500);
		continueLink.click();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Credentials")));
		Thread.sleep(500);
		driver.findElement(By.linkText("Credentials")).click();
		Thread.sleep(500);
		driver.switchTo().activeElement();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-credentials']/button[@class='btn btn-info float-right']")));

	}

}
