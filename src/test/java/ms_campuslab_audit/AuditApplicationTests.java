package ms_campuslab_audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledIfSystemProperty(
        named = "RUN_AUDIT_ORACLE_INTEGRATION_TESTS",
        matches = "true"
)
class AuditApplicationTests {

	@Test
	void contextLoads() {
	}

}
