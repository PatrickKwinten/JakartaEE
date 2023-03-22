package model;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.openntf.xsp.nosql.communication.driver.DominoConstants;
import org.openntf.xsp.nosql.mapping.extension.DominoRepository;
import org.openntf.xsp.nosql.mapping.extension.ViewDocuments;
import org.openntf.xsp.nosql.mapping.extension.ViewQuery;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Sorts;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import org.eclipse.jnosql.communication.driver.attachment.EntityAttachment;


@Schema(description = "Represents an individual employee within the system")
@Entity("employee")//domino form name
public class Employee extends AbstractAttachmentEntity implements Comparable<Employee> {
	
	public interface Repository extends DominoRepository<Employee, String> {
		
		public static final String VIEW_EMPLOYEES  = "vw_employees";
		@ViewDocuments(value=VIEW_EMPLOYEES, maxLevel = 0)
		Stream<Employee> findAll();			
						
		public static final String VIEW_EMPLOYEES_RECENT  = "vw_employees_recent";		
		@ViewDocuments(value=VIEW_EMPLOYEES_RECENT, maxLevel=0)
		//@ViewDocuments(value=VIEW_EMPLOYEES, maxLevel = 0)
		Stream<Employee> findRecent(Pagination pagination);		
				
		public static final String VIEW_BY_DEPARTMENT = "vw_employees_department";		
		@ViewDocuments(VIEW_BY_DEPARTMENT)
		Stream<Employee> findByDepartment(ViewQuery viewQuery);				
		
		//public static final String VIEW_BY_TITLE = "vw_employees_title";
		public static final String VIEW_BY_TITLE = "vw_employees_title_cat";
		@ViewDocuments(VIEW_BY_TITLE)		
		//Stream<Employee> findByTitle(String title, Sorts asc);
		Stream<Employee> findByTitle(ViewQuery query);

		public static final String VIEW_EMPLOYEES_AGE  = "vw_employees_age";		
		@ViewDocuments(value=VIEW_EMPLOYEES_AGE, maxLevel=0)
		Stream<Employee> findByAge(int age, Sorts asc);
				
		
		/*public static final String VIEW_EMPLOYEES_AGE  = "vw_employees_age";		
		@ViewEntries(value=VIEW_EMPLOYEES_AGE, maxLevel=0)
		Stream<Employee> findByAge(Sorts sorts, );	*/	
		
		/*@ViewDocuments(value=VIEW_EMPLOYEES, maxLevel = 0)
		Stream<Employee> findByDepartment(String department);*/
		
	}
	
	private @Id String id;
	@Schema(description="The employee's full name", example="Foo Fooson")
	//@NotEmpty means must have at least 1 character
	private @Column @NotEmpty String sid;
	@Schema(description="The employee's security ID", example="s1971")
	private @Column @NotEmpty String name;
	@Schema(description="The employee's job title", example="CTO")
	private @Column @NotEmpty String title;
	@Schema(description="The title for employee", example="mr/mrs")
	private @Column @NotEmpty String department;
	@Schema(description="The employee's current age", example="80")
	private @Column @Min(1) int age;
	
	@Column(DominoConstants.FIELD_MDATE)
	private Instant modified;
	@Column(DominoConstants.FIELD_CDATE)
	private Instant created;
	@Column(DominoConstants.FIELD_REPLICAID)
	private String replicaId;
	@Column(DominoConstants.FIELD_ATTACHMENTS)
    private List<EntityAttachment> attachments;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public Instant getModified() {
		return modified;
	}
	public void setModified(Instant modified) {
		this.modified = modified;
	}
	
	public Instant getCreated() {
		return created;
	}
	public void setCreated(Instant created) {
		this.created = created;
	}
	
	@Override
	public List<EntityAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<EntityAttachment> attachments) {
		this.attachments = attachments;
	}
	
	@Override
	public int compareTo(Employee arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String getDocumentId() {
		return id;
	}
	public void setDocumentId(String documentId) {
		this.id = documentId;
	}
	
	@Override
	public String getReplicaId() {
		return replicaId;
	}
	public void setReplicaId(String replicaId) {
		this.replicaId = replicaId;
	}
	
	
	
}
