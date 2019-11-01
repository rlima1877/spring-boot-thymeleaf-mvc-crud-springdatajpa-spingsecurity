package com.luv2code.springboot.thymeleafdemo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	private EmployeeService employeeService;

	public EmployeeController(EmployeeService theEmployeeService) {
		employeeService = theEmployeeService;
	}

	// add mapping for "/list"

	@GetMapping("/list")
	public String listEmployees(Model theModel) {

		// get employees from db
		List<Employee> theEmployees = employeeService.findAll();

		// add to the spring model
		theModel.addAttribute("employees", theEmployees);

		return "employees/list-employees";
	}

	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {

		// create model attribute to bind form data
		Employee theEmployee = new Employee();

		theModel.addAttribute("employee", theEmployee);

		return "employees/employee-form";
	}

	/*
	 * This controller will handle saving an employee or updating an employee. How?
	 * 
	 * ADDING: If we click the "Add" button this will load the employee-form.html
	 * with empty fields, since we're adding a new object. The employee-form.html
	 * doesn't have a field for ID, so the user will send all fields but the ID.
	 * When we pass an Employee object to the save() method, it checks if ID is
	 * present, if it is not present, it will do an add.
	 * 
	 * UPDATING: If we are updating an Employee, we will click the button "Update"
	 * from the list-employees.html. That button will direct the user to the
	 * employee-form.html, when the form loads it will grab the data from the row
	 * that was clicked populate the form. The trick is that the ID field will also
	 * be passed, but we are hiding it at the html level so the user doesn't tinker
	 * with it. So once we make the updates and hit save, it will hit this method,
	 * but now we are actually passing the ID so the repository method knows that we
	 * need to do an update
	 * 
	 */
	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {

		// save the employee
		employeeService.save(theEmployee);

		// use a redirect to prevent duplicate submissions
		return "redirect:/employees/list";
	}

	// when user click update button it will re-direct the call the below URL and
	// hit this controller.
	// http://localhost:8080/employees/showFormForUpdate?employeeId=27

	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("employeeId") int theId, Model theModel) {

		// get the employee from the database
		Employee theEmployee = employeeService.findById(theId);

		// set employee as a model attribute to pre-populate the form
		theModel.addAttribute("employee", theEmployee);

		// when we return, below page will load with the model
		return "employees/employee-form";
	}

	@GetMapping("/delete")
	public String deleteEmployee(@RequestParam("employeeId") int theId) {

		employeeService.deleteById(theId);

		// when we return, below page will load with the model
		return "redirect:/employees/list";
	}

	@GetMapping("/search")
	public String delete(@RequestParam("employeeName") String theName,
						 Model theModel) {
		
		// delete the employee
		List<Employee> theEmployees = employeeService.searchBy(theName);
		
		// add to the spring model
		theModel.addAttribute("employees", theEmployees);
		
		// send to /employees/list
		return "/employees/list-employees";
		
	}

}
