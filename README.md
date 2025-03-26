**Rule Engine Service**

**Overview**

The Rule Engine Service applies business rules to PaymentTransaction entities dynamically. It supports enrichment and routing rules, which can be managed through CRUD operations.

**Features:**

   * CRUD operations for Business Rules.
    
   * Rule Execution Engine for processing transactions.
    
   * Spring Cache integration for performance optimization.
    
   * Uses SpEL (Spring Expression Language) for dynamic condition and action execution.
    
   * H2 in-memory database for development.

**Technology Stack:**

   * Java 21
    
   * Spring Boot 3
    
   * Spring Data JPA
    
   * H2 Database (for development)
    
   * Spring Cache

**Installation & Setup:**

**Prerequisites**

   * Java 21

   * Maven 3.6.3+

**How It Works:**

   1-Business Rules are defined with conditions and actions.

   2-When a transaction is received, the Rule Engine:

       * Evaluates Enrichment Rules (all matching rules are applied).

       * Evaluates Routing Rules (only the highest-priority matching rule is applied).

       * Note multiple conditions work with "AND" , "&&" operator to apply actions.

   3-The modified transaction is returned along with applied rule insights.

