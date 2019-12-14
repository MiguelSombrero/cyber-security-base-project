# User manual

Application starts a server into path http://localhost:8080.

## Create an account and login

At default anonymous user is redirected to login page. Application has default admin user, with username "admin" and password "admin". This user is only loaded in memory (not in db), and can be used for navigating the pages, but cannot be used for example for posting in the forum.

Go to register page (Navigation bar > Register) to register your account. Username and password must be at least 5 characters long. After you have created an account, you are redirected to login page, where you can login to application.

## Read posts

After login, you are redirected to main page, where you can see news and posts. You can also navigate to that page from navigation (Navigation bar > Register). News are just hardcoded text for filling the page. Posts are fetched from the database and are shown sorted with creation time, in descending order (last first). In your own posts there is also delete button, for deleting post.

## Write post

You can write to the forum by going post page (Navigation bar > Write post). After post you are redirected to main page, where you can see your post first.

## Profile

You can view you profile by going profile page (Navigation bar > Profile). Profile page shows your own posts, where you can delete them one by one. At the moment profile page has no other functionalities, but to delete posts and delete you account (even your motto is hardcoded).

If you delete your account, all your posts are deleted from the database and you are logged out of the application.

## Admin profile

If you are logged in the application as "admin", you are seeing one extra option in the navigation bar, Users. This link leads you to the users page, where you can view data of all the users that has registered an account. This path (/users) is secured and only accessible to users with role ADMIN.