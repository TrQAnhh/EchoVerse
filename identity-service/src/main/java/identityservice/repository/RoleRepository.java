package identityservice.repository;

import identityservice.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Role r WHERE r.roleName = :roleName")
    void deleteByRoleName(String roleName);

    @Query("SELECT r FROM Role r WHERE r.roleName IN :roleNames")
    List<Role> findAllByRoleName(List<String> roleNames);

}
