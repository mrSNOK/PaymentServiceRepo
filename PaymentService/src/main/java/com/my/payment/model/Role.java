package com.my.payment.model;



import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class Role {
	
	public Role(){}
	///Regular constructor
	public Role(String role){
		this.role = role;
	}
	///Constructor for tests
	public Role(Integer id,String role){
		this.id = id;
		this.role = role;
	}
	@Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;
	
	@Column(name = "role", unique = true, nullable = false)
	private String role;
	///One role can have many users so we need to cascade each user on role
	///Fetch type is lazy to avoid redundant data	
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)  
    @JoinTable(name="users_roles",   
        joinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")},  
        inverseJoinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")}  
    )  
    private Set<User> roleUsers;
	///Hash code and equals methods does not include ID, because it is unique.
	///But entities with all other fields equal are equal
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		Role other = (Role) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Set<User> getUserRoles() {
		return roleUsers;
	}

	public void setUserRoles(Set<User> userRoles) {
		this.roleUsers = userRoles;
	}
    
    
}
