# Vulnerability report

## Flaw 1 - Injection

### Description

In the application, user-supplied data related to Account object is validated on server. By some mistake, data related to Post object is validated only on client-side form (post.html). In every input field there is minimum and maximum length of the input, as well as boolean attribute is it required or not. For example, Title field in the post-page (Navigation bar > Write post) looks like this:

    <input class="form-control p-2" type="text" autofocus="true" name="title" placeholder="A title of your post" minlength="1" maxlength="50" required></input>

Attacker could bypass client-side validation and make request directly to the server. Since there is no validation of user input at the server, this could allow attacker to inject malicious code in the database.

Also, validation constraints aren't nearly strict enought. For example username have to be between 5-20 and password 5-100 characters. There is no constraints for the format; one could enter 5 blanks as a password and username. 

### Steps to fix

Always validate and sanitize user-supplied data on server. When creating Java domain objects, use annotations to specify the constraints of the attributes. Validations can be made with @Valid annotation. Fixed submitPost() method in PostController could look something like this

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public String submitPost(Authentication authentication, @Valid @ModelAttribute Post post, BindingResult result) {
        if (result.hasErrors()) {
            return "post";
        }
        Account account = accountService.getUser(authentication.getName());
        if (account == null) {
            return "redirect:/login";
        }
        postService.post(post, account);
        return "redirect:/posts";
    }

Increase passwords minimum required length at least to 8 characters. Add some constraints for the passwords format and sanitize the user input (trim extra white spaces). You could define those rules with @Pattern annotation, for example

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")

Pattern verifies that password is containing at least one number, one lower case and one upper case letter, one special character, contains no white space and is at least 8 characters long ([source](https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation)). You could also create your own custom validator class for more complex checks.

Validations has to be made in all methods, which are responsible for persisting user-supplied data.

## Flaw 2 - Cross-site Scripting (XSS)

### Description

Application is using Thymeleaf template engine. Thymeleaf is escaping data by default. However, one fragment in title.html is using *th:utext* instead of *th:text*.

    <div th:fragment="title(text)" class='row'>
        <div class='col jumbotron text-center mt-2'>
            <h1 th:utext="${text}">Title</h1>
        </div>
    </div>

While *th:text* expression escapes input, *th:utext* doesn't. This means that if attacker has managed to inject malicious command into the server, coming now back to user, browser would execute it. Attacker might, for example, be able to sniff users session id by command

    <script>alert(document.cookie);</script>

### Steps to fix

Always escape and sanitize user-supplied data in frontend, too. In this example it would have been sufficient fix just to use th:text instead of th:utext. Of course, this has to be done manually when not using template engines and frameworks which does this for you.

## Flaw 3 - Security misconfiguration

### Description

Application is using custom security configuration which is defined in SecurityConfiguration class. There are couple of alarming settings

    // enable H2-console for debugging
    http.csrf().disable();
    http.headers().frameOptions().sameOrigin();

It seems that developer has disabled default csrf() settings for testing purposes, which adds CSRF (cross-site request forgery) defense by adding CSRF-token into forms. 

### Steps to fix

Do not disable security settings in order to debug application. If you have to, use different security settings for development and production environments. Spring uses *application.properties* files for configuration profiles. Define *application-test.properties* file for test environment. When environment variable SPRING_PROFILES_ACTIVE is set to "test", Spring will look for *application-test.properties* file for configurations. Java classes can be set for test environment by annotating class

    @Profile("test")

## Flaw 4 - Broken access control

### Description

You can view your own profile in profile-page (Navigation bar > Profile) when you are logged in. As you can see in the browser, URL to request your profile page is */users/{username}*. You could insert any valid username in *{username}* parameter and get access to his/hers profile and all the sensitive data.

### Steps to fix

Create routers (controller) in such way, that they are not vulnerable to parameter tampering. Use long random parameters in path variables. In this case, it wouldn't be necessary even to use *{parameter}* in the path, since we are always requesting logged users profile. At minimum, always verify that user is authorized to make request. Fixed getUserProfile() method could look like this

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public String getUserProfile(Model model, Authentication authentication, @PathVariable Long id) {
        Account account = accountService.getUser(authentication.getName());
        if (account == null || account.getId() != id) {
            return "redirect:/login";
        }
        model.addAttribute("user", account);
        return "profile";
    }

There is also broken access control flaw in the deleteUser() and deletePost() methods; anyone could delete any user or post, if knowing it's id. This should be prevented for example in same way as above.

## Flaw 5 - Broken authentication

### Description

There is couple of alarming things on our application concerning authentication. 

#### Default admin user

If you look at the SecurityConfiguration class, you notice that application has default username "admin" with password "admin", and that user has ADMIN rights to the application. This user has access to */users* path, which will fetch all the registered users and show their personal information.

#### Default database password

There is no no database username and password set in *application.properties* file. This means, that Spring uses default username "sa" and password "". Anyone who has access to database, could easily get access to all the data by brute forcing default usernames and passwords.

### Steps to fix

Change default weak passwords to strong ones. In cryptography length is strength. Password which is used only programmatically (user doesn't have to remember it), like password in the database, should be veeeery looong.

## Flaw 6 - Sensitive data exposure

### Description

Flaw #4 and flaw #5 is also an examples of Sensitive data exposure. All personal data, such as full name, email, address etc. should be considered sensitive and should not show to third parties without permission.

### Steps to fix

First, fix broken access control and authentication problems. Also, consider is all this data really necessary for the application? For example, does it have to have feature for fetching all user data? Would it be safer to make these kind of queries with separate program or script?
