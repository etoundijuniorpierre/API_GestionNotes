package com.university.ManageNotes.data;

import com.university.ManageNotes.model.enums.AppRole;
import com.university.ManageNotes.model.Roles;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.RoleRepository;
import com.university.ManageNotes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Order(1)
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
    }
    
    private void createDefaultAdmin() {
        try {
            Users admin = userRepository.findByUsername("admin").orElse(null);
            
            if (admin == null) {
                // Create new admin
                admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("admin@university.edu");
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setIsActive(true);
                admin.setCreatedDate(Instant.now());
                admin.setLastModifiedDate(Instant.now());
                
                // Create or get ADMIN role
                Roles adminRole = getOrCreateAdminRole();
                admin.setRoles(Set.of(adminRole));
                
                userRepository.save(admin);
                System.out.println("✅ Default admin user created successfully");
                System.out.println("   Username: admin");
                System.out.println("   Password: admin");
            } else {
                // Update existing admin password
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setIsActive(true);
                admin.setLastModifiedDate(Instant.now());
                
                // Ensure admin has ADMIN role
                Roles adminRole = getOrCreateAdminRole();
                admin.setRoles(Set.of(adminRole));
                
                userRepository.save(admin);
                System.out.println("✅ Admin user updated successfully");
                System.out.println("   Username: admin");
                System.out.println("   Password: admin123");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to create/update admin user: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Roles getOrCreateAdminRole() {
        return roleRepository.findByAppRole(AppRole.ROLE_ADMIN)
            .orElseGet(() -> {
                Roles adminRole = new Roles();
                adminRole.setAppRole(AppRole.ROLE_ADMIN);
                return roleRepository.save(adminRole);
            });
    }
}