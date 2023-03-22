package rest;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.openntf.xsp.nosql.mapping.extension.ViewQuery;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Sorts;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.Employee;
import model.util.PostUtil;


@Path("employees")
public class EmployeeResource {
	
	public static final int PAGE_LENGTH = 100;
	
	@Inject
	private Employee.Repository employees;
	
	@GET
	@RolesAllowed({ "*/O=SEB"})
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(description="Retrieves a list of all employee entities in the data store")
	public List<Employee> get() {
		System.out.println("@GET");		
		return employees.findAll().collect(Collectors.toList());
		//return employees.findAll(Sorts.sorts().asc("sid")).collect(Collectors.toList());		
	}
	
	/*@Path("/title/{title}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(description="Retrieves a list of all employees entities in the data store matching provided title")
	public List<Employee> getByTitle(@PathParam("title") String title) {
		System.out.println("@GET - getByTitle");
		System.out.println("param value = " + title);
		ViewQuery query = ViewQuery.query().category(title);
		System.out.println("query = " + query.getCategory());
		return employees.findByTitle(query).collect(Collectors.toList());
	}*/
	
	@Path("/title/{title}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(description="Retrieves a list of all employees entities in the data store matching provided title")
	public List<Employee> getByTitle(@PathParam("title") String title) {	
		System.out.println("@GET - getByTitle");
		System.out.println("param value = " + title);
		
			
		
		//Working:
		/*ViewQuery query = ViewQuery.query().category(title);
		return employees.findByTitle(query).collect(Collectors.toList());*/
		
		ViewQuery query = ViewQuery.query().key(title, true);			
		return employees.findByTitle(query).collect(Collectors.toList());
		
		//return employees.findByTitle(title, Sorts.sorts().asc("sid")).collect(Collectors.toList());		
	}
	
	@Path("/recent")	
	@GET
	//
	//@RolesAllowed({ "*/O=SEB", "LocalDomainAdmins", "[Admin]" })
	@RolesAllowed({ "Anonymous"})
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(description="Retrieves a list of all employees entities in the data store")
	public List<Employee> findRecent() {	
		System.out.println("@GET - findRecent");
		// Figure out the starting point
		String startParam = "0";
		int start = Math.max(PostUtil.parseStartParam(startParam), 0);
		int page = start / PAGE_LENGTH + 1;
		return employees.findRecent(Pagination.page(page).size(PAGE_LENGTH)).collect(Collectors.toList()); 
	}
	
	
	
	@Path("/age/{age}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(description="Retrieves a list of all employees entities in the data store matching provided age")
	public List<Employee> getByAge(@PathParam("age") int age) {
		System.out.println("@GET - getByAge");
		System.out.println("param value = " + age);		
		return employees.findByAge(age, Sorts.sorts().asc("sid")).collect(Collectors.toList());
	}	
	
	@Path("/department/{department}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(description="Retrieves a list of all employees entities in the data store matching provided department")
	public List<Employee> get(@PathParam("department") String department) {		
		System.out.println("make query with param department = " + department);
		ViewQuery query = ViewQuery.query().category(department);		
		return employees.findByDepartment(query).collect(Collectors.toList());
	}
		
	
	/*@Path("/department/{department}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(description="Retrieves a list of all employees entities in the data store matching provided department")
	public List<Employee> get(@PathParam("department") String department) {		
		
		System.out.println("string -> department = " + department);
		return employees.findByDepartment(department).collect(Collectors.toList());
	}*/
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Employee create(@Valid Employee employee) {
		System.out.println("@POST");
		employee.setId(null);
		return employees.save(employee);
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Employee getEmployee(@PathParam("id") String id) {
		System.out.println("@GET id");
		return employees.findById(id)
			.orElseThrow(() -> new NotFoundException(MessageFormat.format("Could not find employee for ID {0}", id)));
	}
	
	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Employee update(@PathParam("id") String id, @Valid Employee employee) {
		System.out.println("@PUT id");
		employee.setId(id);
		return employees.save(employee);
	}
	
	@Path("{id}")
	@DELETE
	public void delete(@PathParam("id") String id) {
		System.out.println("@Delete id");
		employees.deleteById(id);
	}
}
