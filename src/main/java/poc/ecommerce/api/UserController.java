package poc.ecommerce.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import poc.ecommerce.model.Role;
import poc.ecommerce.model.User;
import poc.ecommerce.service.SecurityService;
import poc.ecommerce.service.UserService;
import poc.ecommerce.validator.UserValidator;

/**
 * User login/register controller
 * 
 * @author Agustín
 *
 */
@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserValidator userValidator;

	@GetMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("userForm", new User());

		return "registration";
	}

	@PostMapping("/registration")
	public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
		userValidator.validate(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return "registration";
		}
		if (userForm.getRole() != null && userForm.getRole().equals(Role.ROLE_ADMIN.name())) {
			userForm.setRole(Role.ROLE_ADMIN.name());
		} else {
			userForm.setRole(Role.ROLE_USER.name());
		}
		userService.save(userForm);

		securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

		return "redirect:/welcome";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "login";
		}

		securityService.autoLogin(userForm.getUsername(), userForm.getPassword());

		return "redirect:/welcome";
	}

	@GetMapping("/login")
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}

	@GetMapping({ "/", "/welcome" })
	public String welcome(Model model) {
		return "welcome";
	}

}
