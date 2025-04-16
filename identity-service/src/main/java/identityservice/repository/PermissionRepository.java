package identityservice.repository;

import identityservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Permission findByPermissionName(String permissionName);
}
