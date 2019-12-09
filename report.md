# Vulnerability report

Link to [repository](https://github.com/MiguelSombrero/cyber-security-base-project)

## Flaw 1 - Injection

### Description

In the application, user-supplied data related to Account object is validated on server. By some mistake, data related to Post object is validated only on client-side form (post.html). In every input field there is minimum and maximum length of the input, as well as boolean attribute is it required or not. For example, Title field in the post-page (Navigation bar > Write post) looks like this:

    <input class="form-control p-2" type="text" autofocus="true" name="title" placeholder="A title of your post" minlength="1" maxlength="50" required></input>

Malicious user could bypass client-side validation and make request directly to the server. Since there is no validation of user input at the server, this could allow attacker to inject malicious code in the database.

### Steps to fix

Always validate user-supplied data on server. When creating Java domain objects, use annotations to specify the constraints of the attributes. These annotations can be found from package *javax.validation.constraints*. For example, in our applications Post class attributes could be annotated like this

    private LocalDateTime created;

    @NotNull
    @Size(min = 1, max = 50)
    private String title;

    @NotNull
    @Size(min = 1, max = 5000)
    private String content;

Note that attribute *created* is set on the server and is not user-input. Since Post class is annotated as @Entity, these constraints would apply also in the database. Validations can be made with @Valid annotation in controller which takes care of creating the post. Fixed submitPost() method could look something like this

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

Validations has to be made in all methods, which are responsible for persisting user-supplied data.

## Flaw 2 - Cross-site Scripting (XSS)

### Description

Application is using Thymeleaf for Java templates. Thymeleaf is escaping all the data coming from the server by default. However, one fragment in title.html is using *th:utext* instead of *th:text*.

    <div th:fragment="title(text)" class='row'>
        <div class='col jumbotron text-center mt-2'>
            <h1 th:utext="${text}">Title</h1>
        </div>
    </div>

While *th:text* expression escapes input, *th:utext* doesn't. This means that if attacker has managed to inject malicious command into the server, coming now back to user, browser would execute it. Attacker might, for example, be able to sniff users session id by command

    <script>alert(document.cookie);</script>

### Steps to fix

Always escape and sanitize user-supplied data. In this example it would have been sufficient fix just to use th:text instead of th:utext. Of course, this has to be done manually when not using template engines and frameworks which does this for you.

## Flaw 3 - Security misconfiguration

### Description

Application is using custom security configuration which is defined in SecurityConfiguration class. There are couple of alarming settings

    // enable H2-console for debugging
    http.csrf().disable();
    http.headers().frameOptions().sameOrigin();

It seems that developer is disabled default csrf() settings, which adds CSRF (cross-site request forgery) defense by adding CSRF-token into forms. 

### Steps to fix

Do not disable security settings in order to debug application. If you have to, use different security settings for development and production environments. Spring uses *application.properties* files for configuration profiles. Define *application-test.properties* file for test environment. Spring uses this file when environment variable SPRING_PROFILES_ACTIVE="test". Java classes can be set for test environment by annotating class

    @Profile("test")

## Flaw 4 - Sensitive data exposure

### Description

You can view your own profile in profile-page (Navigation bar > Profile) when you are logged in. As you can see in the browser, URL to request your profile page is */users/{username}*. You could insert any valid username in *{username}* parameter and get access to his/hers profile and all the sensitive data.

### Steps to fix

Create routers (controller) in such way, that they are not vulnerable to parameter tampering. Use long random parameters in path variables. Fixed getUserProfile() method could look like this

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public String getUserProfile(Model model, Authentication authentication, @PathVariable Long id) {
        Account account = accountService.getUser(authentication.getName());
        if (account == null || account.getId() != id) {
            return "redirect:/login";
        }
        model.addAttribute("user", account);
        return "profile";
    }

This is also an example of broken access control flaw.

## Flaw 5 - Broken access control

### Description

User can delete posts with Delete button, which is rendered only on those posts, for which user is an author. In PostControllers deletePost() method there is no user authorizing. Malicious user could make delete request directly to the server and remove other users posts, if knowing posts id.

### Steps to fix


