# Cyber Security Base Project

This is a project work for University of Helsinki course [Cyber Security Base](https://cybersecuritybase.mooc.fi/).

## Problem

Idea is to implement web application Idea is to implement web application, which has at least 5 vulnerabilities from [OWASP Top ten most critical security risks](https://www.owasp.org/images/7/72/OWASP_Top_10-2017_%28en%29.pdf.pdf) report and provide steps to fix them. I decided to build simple blog service, in which registered users can post stories.

**Notice: This applications purpose is to demonstrate security vulnerabilities. This app should not be used in production environment, unless vulnerabilities fixed**

## Documentation

- [Vulnerability report](https://github.com/MiguelSombrero/cyber-security-base-project/blob/master/report.md)

- [User manual](https://github.com/MiguelSombrero/cyber-security-base-project/blob/master/manual.md)

## Tests

System level tests are executed with SpringBootTest for testing controllers, and with FluentLenium for testing views. Controllers have pretty good test coverage, but with views I only tested login and register pages. Services is also lacking unit tests.

## Commands

To clone project into your machine

    git clone https://github.com/MiguelSombrero/cyber-security-base-project.git

Start application in your favourite IDE or with command

    mvn spring-boot:run

## Environment

Application has been developed and tested in MacOS and with Java 8 version 1.8.0_161.