# Vulnerability report

Link to [repository](https://github.com/MiguelSombrero/cyber-security-base-project)

## Flaw 1 - Injection

### Description

In the application, user-supplied data is validated by client-side form. In every input field there is minimum and maximum length of the input, as well as boolean attribute is it required or not. For example Username field in the login-page looks like this:

    <input class="form-control p-2" type="text" autofocus="true" name="username" placeholder="Username" minlength="1" maxlength="20" required/>

However, validation is only performed at client, not in the server. Malicious user could bypass client-side validation and make request directly to the server. Since there is no validation of user input at the server, this could allow attacker to inject malicious code to server.

### Steps to fix

Always validate user-supplied data on server. When creating Java domain objects, use annotations to specify the constraints of the attributes. These annotations can be found from package *javax.validation.constraints*. For example, in our applications Account class attributes could be annotated like this

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    @Size(min = 5, max = 20)
    private String username;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

Since Account class is also annotated as @Entity, these constraints would apply also in the database when created. Validations can be made with @Valid annotation, in controller which takes care of creating the user. Fixed method could look something like this

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        accountService.registerUser(account);
        return "redirect:/login";
    }

Validations has to be made in all methods, which are responsible for persisting user-supplied data.

## Flaw 2 - Cross-site Scripting (XSS)

### Description

Application is using Thymeleaf for creating Java templates. Thymeleaf is escaping all the data coming from the server by default. However, one fragment in title.html is using *th:utext* instead of *th:text*.

    <div th:fragment="title(text)" class='row'>
        <div class='col jumbotron text-center mt-2'>
            <h1 th:utext="${text}">Title</h1>
        </div>
    </div>

While *th:text* expression escapes input, *th:utext* doesn't. This means that if attacker has managed to inject malicious code into the server, coming now back to user, browser would execute it as it is. Attacker might, for example, be able to sniff users session id by command

    <script>alert(document.cookie);</script>

### Steps to fix

Always escape and sanitize user-supplied data. In this example it would have been sufficient fix just to use th:text instead of th:utext. Of course, this has to be done manually when not using template engines and frameworks which does this for you.


## Flaw 3 - Using components with known vulnerabilities

**MUISTIINPANO - POISTA: Määritä jonkin käytetyn lisäosan versionumero pom.xml tiedostossa siten, että versio on vanha ja sisältää haavoittuvuuksia.**

### Description

### Steps to fix

## Flaw 4 - Broken access control

**MUISTIINPANO - POISTA: Jätä security configuraatiossa määrittelemättä mitä sivuja voi avata kirjautumatta järjestelmään. Lisää järjestelmään jokin toiminta, jonka vain kirjatunut käyttäjä saa mähdä.**

### Description

### Steps to fix

## Flaw 5 - Security misconfiguration

**MUISTIINPANO - POISTA: tee jokin selvästi huono tietoturvakonfigurointi, joka mahdollistaa hyökkäyksen. Disabloi esimerkiksi csrf-hyökkäyksen esto tai cookies httpOnly.**

### Description

### Steps to fix

## Flaw 6 - Sensitive data exposure

**MUISTIINPANO - POISTA: Käyttäjän profiilisivulle pääsee syöttämällä suoraan osoitekenttää /profile/{username}.. Profiilisivulla on tietoa, joka saa näkyä vain käyttäjälle.**

### Description

### Steps to fix