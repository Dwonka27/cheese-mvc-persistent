package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "view")
    public String index (Model model) {

        model.addAttribute("menues", menuDao.findAll());
        model.addAttribute("title", "Menu");

        return "menu/index";
    }


    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String DisplayViewMenu(Model model, @PathVariable int id ) {

        model.addAttribute("menu", menuDao.findOne(id));
        model.addAttribute("title", "Menu");

        return "menu/view";

    }

    @RequestMapping(value = "add-item/{id}", method = RequestMethod.GET)
    public String AddMenuItemItemForm (Model model, @PathVariable int id, Menu menu, AddMenuItemform form) {

        model.addAttribute("menu", menuDao.findOne(id));
        model.addAttribute("title", "Add item to menu: ");
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("form", form);

        return "menu/add-item";
    }

    @RequestMapping(value = "add-item/{id}", method = RequestMethod.POST)
    public String ProcessMenuItemForm(
            @ModelAttribute @Valid Menu newMenu, Cheese theCheese,  Errors errors, Model model, AddMenuItemform form) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add menu");
            return "menu/add";
        }

        theCheese = cheeseDao.findOne(form.getCheeseId());
        newMenu = menuDao.findOne(form.getMenuId());

        menuDao.save(newMenu);
        return "redirect:/menu/view/" + newMenu.getId();
    }


    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        model.addAttribute("Menues", menuDao.findAll());

        return "menu/add";
    }


    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String ProcessAddMenuForm(
            @ModelAttribute @Valid Menu newMenu, Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add menu");
            return "menu/add-item";
        }

        menuDao.save(newMenu);
        return "redirect:/menu/view/" + newMenu.getId();
    }




}
