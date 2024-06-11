package com.cydeo.controller;

import com.cydeo.dto.UserDTO;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    RoleService roleService;
    UserService userService;

    public UserController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService= userService;
    }

    @GetMapping("/create")
    public String createUser(Model model){
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("users", userService.findAll());
        return "/user/create";
    }

    @PostMapping("/create")
    public String insertUser(@Valid  @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("users", userService.findAll());
            return "/user/create";//if we redirect, all the input will be deleted as we create new user
        }
        userService.save(user);
        return("redirect:/user/create");
    }

    @GetMapping("/update/{username}")//this need to be same with path variable param. we will clarify the username in the html page
    public String editUser(@PathVariable("username") String userId, Model model){
        model.addAttribute("user",userService.findById(userId));//form of the existing user
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("users", userService.findAll());

        return("/user/update");
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user")  UserDTO user){
        userService.update(user);
        return "redirect:/user/create";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String username){
        userService.deleteById(username);
        return "redirect:/user/create";
    }


}
