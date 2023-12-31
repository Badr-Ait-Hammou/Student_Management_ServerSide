package sessions;

import java.util.List;

import dao.IDao;
import dao.IDaoLocal;
import entities.Filiere;
import entities.Role;
import entities.Student;
import entities.User;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless(name = "studentService")
public class StudentService implements IDao<Student>,IDaoLocal<Student>{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@PermitAll
	  public Student create(Student o) {
        if (o.getFiliere() != null && o.getFiliere().getId() == 0) {
            entityManager.persist(o.getFiliere());
        }
        entityManager.persist(o);
        return o;
    }

	@Override
	@PermitAll
	public Student update(Student o) {
		
	    Student st = entityManager.find(Student.class, o.getId());
	    
	    if (st == null) throw new RuntimeException("Student not found");
	    
        st.setLogin(st.getLogin());
        st.setPassword(st.getPassword());
        st.setFiliere(st.getFiliere());
        st.setFirstName(st.getFirstName());
        st.setLastName(st.getLastName());
        st.setTelephone(st.getTelephone());
        st.setRoles(st.getRoles());
	    
	    entityManager.merge(st);
	    
	    return st;
	}

	@Override
	@PermitAll
	public boolean delete(Student o) {
	    Student st = entityManager.find(Student.class, o.getId());
	    
	    if (st == null) throw new RuntimeException("Student not found");
	    entityManager.remove(st);
	    return true;
	}

	@Override
	@PermitAll
	public Student findById(int id) {
		Student st=entityManager.find(Student.class, id);
		if(st == null) throw new RuntimeException("student not found");
		return st;
	}

	@Override
	@PermitAll
	public List<Student> findAll() {
		Query query=entityManager.createQuery("select s from Student s");
		// TODO Auto-generated method stub
		return query.getResultList();
	}
	
	@PermitAll
	public List<Student> findAllByFiliere(Filiere filiere) {
		Query query=entityManager.createQuery("select s from Student s where s.filiere =: filiere");
		query.setParameter("filiere", filiere);
		// TODO Auto-generated method stub
		return query.getResultList();
	}

	@Override
	@PermitAll
	public void affect(Role r, User u) {
		r=entityManager.merge(r);
		u=entityManager.merge(u);
		u.getRoles().add(r);
		entityManager.merge(u);
		
	}

}