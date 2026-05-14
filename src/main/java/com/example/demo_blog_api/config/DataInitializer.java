package com.example.demo_blog_api.config;

import com.example.demo_blog_api.entity.Role;
import com.example.demo_blog_api.entity.RoleName;
import com.example.demo_blog_api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) {
        normalizeLegacyRolesTable();
        createRoleIfMissing(RoleName.ADMIN);
        createRoleIfMissing(RoleName.USER);
    }

    private void createRoleIfMissing(RoleName roleName) {
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            return roleRepository.save(role);
        });
    }

    private void normalizeLegacyRolesTable() {
        Integer idColumnCount = jdbcTemplate.queryForObject("""
                select count(*)
                from information_schema.columns
                where table_schema = database()
                  and table_name = 'roles'
                  and column_name = 'id'
                """, Integer.class);

        if (idColumnCount != null && idColumnCount > 0) {
            return;
        }

        List<String> legacyIdColumns = jdbcTemplate.queryForList("""
                select column_name
                from information_schema.columns
                where table_schema = database()
                  and table_name = 'roles'
                  and column_name <> 'name'
                limit 1
                """, String.class);

        if (legacyIdColumns.isEmpty()) {
            return;
        }

        String legacyColumn = legacyIdColumns.getFirst().replace("`", "``");
        jdbcTemplate.execute("alter table roles change column `" + legacyColumn + "` id bigint not null auto_increment");
    }
}
