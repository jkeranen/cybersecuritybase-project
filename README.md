# Cyber Security Base - Course Project I 

NOTE! Only four vulnerabilities implemented
 
**Task:**
- Create a web application that has at least five different flaws from the OWASP top ten list 
  https://www.owasp.org/index.php/Top_10_2013-Top_10 
- Starter code for the project is provided on Github at https://github.com/cybersecuritybase/cybersecuritybase-project.
- Source code is available in github at: https://github.com/jkeranen/cybersecuritybase-project

Following flaws has been implemented into the application:
- 2013-A1-Injection
- 2013-A4-Insecure Direct Object References
- 2013-A7-Missing Function Level Access Control

## 2013-A1-Injection
Untrusted data can be sent as part of a query.
### Identification
1. Open browser and go to address *http://localhost:8080* (you'll be redirected to *http://localhost:8080/login*).
2. Login with the following credentials: username: *david*; password: *lindley*.
3. Enter following string to Owner field "' or '1'='1" without double quotation marks.
4. All sign up data is shown to you. 
5. Enter following string to Owner field "' or 1=1" without double quotation marks.
6. An exception which contains the database query is shown to you.
>There was an unexpected error (type=Internal Server Error, status=500).
>org.hibernate.QueryException: expecting ''', found '<EOF>' [SELECT s FROM sec.project.domain.Signup s WHERE s.owner = '' 1 = 1']" 
### Fix
Use parametrized query instead of directly adding the parameter to the query string. 
 
        TypedQuery<Signup> query =
                entityManager.createQuery("SELECT s FROM Signup s WHERE s.owner = :owner", Signup.class);
        query.setParameter("owner", owner);

## 2013-A4-Insecure Direct Object References
Any authorized system user can easily access other user data by changing owner parameter value
### Identification
1. Open browser and go to address *http://localhost:8080* (you'll be redirected to *http://localhost:8080/login*).
2. Login with the following credentials: username: *bob*; password: *marley*.
3. On Signup page (/form) enter signup info (e.g. Name : *ziggy* Address: *jamaica*) and press Submit.
4. Entered signup information is shown to you. 
5. Press Logout. You'll be redirected to Login page.
6. Login with the following credentials: username: *tom*; password: *waits*.
7. On Signup page (/form) press Show my sign ups button. Empty signup info page is shown to you.
8. Change *http://localhost:8080/signups/tom* path in address bar to *http://localhost:8080/signups/bob* and press Enter.    
9. Bob's sign up information is shown to you.
### Fix
Instead of using *@PathVariable* directly to fetch the owner data check first if the *@PathVariable* 
matches with the user in *Authentication* object.

        if (authentication.isAuthenticated() && authentication.getName().equals(owner)) {
            model.addAttribute("signups", signupRepositoryCustom.findByOwner(owner));
            return "signups";
        } else {
            return "redirect:/form";
        }
## 2013-A7-Missing Function Level Access Control
Any authorized system user can access adminForm by giving correct path and from there perform admin actions.
### Identification
1. Open browser and go to address *http://localhost:8080* (you'll be redirected to *http://localhost:8080/login*).
2. Login with the following credentials: username: *bob*; password: *marley*.
3. On Signup page change the path in address bar from *http://localhost:8080/form* to *http://localhost:8080/adminForm*.
4. You are able to perform admin action on the page e.g. get tom's signup data by entering tom in the Owner field. 
### Fix
Authorization needs to be checked before showing the adminForm to the user i.e. is the user admin user.

        Account account = accountRepository.findByUsername(authentication.getName());
        if (authentication.isAuthenticated() && account.getRole().equals("ROLE_ADMIN")) {
            model.addAttribute("signups", signupRepositoryCustom.findByOwner(owner));
            return "adminForm";
        } else {
            return "redirect:/form";
        }

## 2013-A10-Unvalidated Redirects and Forwards
User can be redirected to any URL
### Identification
1. Open browser and go to address *http://localhost:8080* (you'll be redirected to *http://localhost:8080/login*).
2. Login with the following credentials: username: *bob*; password: *marley*.
3. Change *http://localhost:8080/form* path in address bar to *http://localhost:8080/signout?url=http://mooc.fi* and press Enter.
4. You are redirected to *mooc.fi* instead of login page.
### Fix
Don't use url parameter when calculating the destination
 
    public String signout() {
        return "redirect:/logout";
    }

