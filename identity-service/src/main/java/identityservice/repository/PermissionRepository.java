package identityservice.repository;

import identityservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    void deleteByPermissionName(String permissionName);

    @Query("SELECT p FROM Permission p WHERE p.permissionName IN :permissionNames")
    List<Permission> findAllByPermissionNameIn(Set<String> permissionNames);
}
