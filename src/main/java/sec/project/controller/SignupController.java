package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.SignupRepository;
import sec.project.repository.SignupRepositoryImpl;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private SignupRepositoryImpl signupRepositoryCustom;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("*")
    public String defaultMapping(Authentication authentication) {
        Account account = accountRepository.findByUsername(authentication.getName());
        if (account.getRole().equals("ROLE_USER")) {
            return "redirect:/form";
        } else {
            return "redirect:/adminForm";
        }
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm()
    {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(Authentication authentication, Model model,
                             @RequestParam String name, @RequestParam String address) {
        Account account = accountRepository.findByUsername(authentication.getName());
        Signup signup = new Signup(name, address, account.getUsername());
        signupRepository.save(signup);
        model.addAttribute("signup", signup);
        return "done";
    }

    /*
     * @todo A4-Insecure Direct Object References
     * @todo Any authorized system user can access adminForm by giving correct path
     */
    @RequestMapping(value = "/adminForm", method = RequestMethod.GET)
    public String loadAdminForm() {
        return "adminForm";
    }

    /*
     * @todo A4-Insecure Direct Object References
     * @todo Any authorized system user can easily access other user data by changing owner parameter value
     */
    @RequestMapping(value = "/signups/{owner}", method = RequestMethod.GET)
    public String getSignupData(Model model, @PathVariable String owner) {
        model.addAttribute("signups", signupRepositoryCustom.findByOwner(owner));
        return "signups";
    }

    @RequestMapping(value = "/mysignups", method = RequestMethod.GET)
    public String getSignupOwner(Authentication authentication, Model model) {
        Account account = accountRepository.findByUsername(authentication.getName());
        return "redirect:/signups/" + account.getUsername();
    }

    @RequestMapping(value = "/signups", method = RequestMethod.POST)
    public String getSignups(Model model, @RequestParam String owner) {
        model.addAttribute("signups", signupRepositoryCustom.findByOwner(owner));
        return "adminForm";
    }
}
