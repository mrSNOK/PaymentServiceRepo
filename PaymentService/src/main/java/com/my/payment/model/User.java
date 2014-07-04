package com.my.payment.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	
	public User (){}
	///Regular constructor
	public User (String email, String password, Float balance, Role role){
		this.email = email;
		this.password = password;
		this.balance = balance;
		this.role = role;
	}
	///Constructor for tests
	public User (Integer id,Timestamp registred,String email, String password, Float balance, Role role){
		this.id = id;
		this.registred = registred;
		this.email = email;
		this.password = password;
		this.balance = balance;
		this.role = role;
	}
	
	@Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;
	
	@Column(name = "email", unique=true, nullable = false)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	///Currency format
	@Column(name = "balance", nullable = false, columnDefinition="Decimal(7,2) default '0.00'")
	private Float balance;
	
	@GeneratedValue
	@Column(name = "registred", nullable = false)
	private Date registred = new Date();
	///Many users can have one role so no need to cascade role on user
	@ManyToOne
    @JoinTable(name="users_roles",  
        joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")},  
        inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")}  
    )  
    private Role role;
	///One admin can have many operations so we need to cascade each operation on admin
	///Fetch type is lazy to avoid redundant data
	 @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)  
	    @JoinTable(name="admins_operations",   
	        joinColumns = {@JoinColumn(name="admin_id", referencedColumnName="id")},  
	        inverseJoinColumns = {@JoinColumn(name="operation_id", referencedColumnName="id")}  
	    ) 
	private Set<Operation> admin_operations;
	///One user can have many operations so we need to cascade each operation on user
	///Fetch type is lazy to avoid redundant data
	 @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)  
	    @JoinTable(name="users_operations",   
	        joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")},  
	        inverseJoinColumns = {@JoinColumn(name="operation_id", referencedColumnName="id")}  
	    ) 
	private Set<Operation> user_operations;
	///Hash code and equals methods does not include ID, because it is unique.
	///But entities with all other fields equal are equal
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((registred == null) ? 0 : registred.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (registred == null) {
			if (other.registred != null)
				return false;
		} else if (!registred.equals(other.registred))
			return false;
		return true;
	}

	public Set<Operation> getAdmin_operations() {
		return admin_operations;
	}

	public void setAdmin_operations(Set<Operation> admin_operations) {
		this.admin_operations = admin_operations;
	}

	public Set<Operation> getUser_operations() {
		return user_operations;
	}

	public void setUser_operations(Set<Operation> user_operations) {
		this.user_operations = user_operations;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Float getBalance() {
		return balance;
	}
	
	public Date getRegistred() {
		return registred;
	}

	public void setRegistred(Timestamp registred) {
		this.registred = registred;
	}

	public void setbalance(Float balance) {
		this.balance = balance;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
