# Banking System

A command-driven banking application built in Java that simulates core banking operations including account management, transactions, and interest calculations.

## Project Overview

This banking system allows users to manage multiple types of bank accounts through text-based commands. The system handles account creation, deposits, withdrawals, transfers, and time progression with automatic interest calculation and account maintenance.

## Features

### Account Types
- **Checking Account**: Standard account with up to $400 withdrawal limit per transaction
- **Savings Account**: Interest-bearing account with up to $1,000 withdrawal limit (once per month)
- **Certificate of Deposit (CD)**: High-yield account requiring 12-month maturity period before withdrawal

### Core Operations
- **Create**: Open new bank accounts with specified APR (Annual Percentage Rate)
- **Deposit**: Add funds to accounts (limits: Checking $1,000, Savings $2,500 per deposit)
- **Withdraw**: Remove funds from accounts with account-specific restrictions
- **Transfer**: Move money between two accounts
- **Pass**: Advance time by 1-60 months, triggering interest accrual and account maintenance

### Automated Features
- Monthly interest calculation and compounding
- Automatic account closure for balances below $25
- Savings account withdrawal limit reset each month
- CD maturity tracking

## Technical Architecture

### Design Patterns
- **Command Pattern**: Separates command validation, processing, and storage
- **Inheritance**: Account hierarchy with shared behavior and specialized implementations
- **Dependency Injection**: Loose coupling between components for testability

### Project Structure
```
banking/
├── Account.java                    # Abstract base class
├── Checking.java                   # Checking account implementation
├── Savings.java                    # Savings account with withdrawal tracking
├── CD.java                         # Certificate of Deposit with maturity
├── Bank.java                       # Account repository and time operations
├── CommandValidator.java           # Command validation router
├── CreateCommandValidator.java     # Account creation validation
├── DepositCommandValidator.java    # Deposit validation with limits
├── WithdrawCommandValidator.java   # Withdrawal validation with rules
├── TransferCommandValidator.java   # Transfer validation
├── PassCommandValidator.java       # Time progression validation
├── CommandProcessor.java           # Command execution handler
├── CommandStorage.java             # Invalid command tracker
├── MasterControl.java              # Main controller and output generator
└── Transaction.java                # Transaction record keeper
```

## Command Format

### Create Account
```
Create checking <8-digit-id> <apr>
Create savings <8-digit-id> <apr>
Create cd <8-digit-id> <apr> <initial-balance>
```
- APR must be between 0.0 and 10.0
- CD initial balance must be greater than 0
- Account IDs must be unique

**Examples:**
```
Create checking 12345678 1.5
Create savings 87654321 2.5
Create cd 11111111 3.0 5000
```

### Deposit
```
Deposit <8-digit-id> <amount>
```
- Checking: Maximum $1,000 per deposit
- Savings: Maximum $2,500 per deposit
- CD: Deposits not allowed

**Examples:**
```
Deposit 12345678 500
Deposit 87654321 2000
```

### Withdraw
```
Withdraw <8-digit-id> <amount>
```
- Checking: Maximum $400 per withdrawal
- Savings: Maximum $1,000 per withdrawal, once per month
- CD: Full balance only, after 12 months maturity

**Examples:**
```
Withdraw 12345678 300
Withdraw 87654321 800
```

### Transfer
```
Transfer <from-id> <to-id> <amount>
```
- Follows withdrawal rules for source account
- No deposit limits for destination account

**Examples:**
```
Transfer 12345678 87654321 250
```

### Pass Time
```
Pass <months>
```
- Months must be between 1 and 60
- Applies interest to all accounts
- Removes accounts with balance < $25
- Resets savings withdrawal limits

**Examples:**
```
Pass 1
Pass 12
```

## Interest Calculation

Interest is calculated monthly using the formula:
```
Monthly Interest = (Balance × APR / 100) / 12
```

Interest compounds, meaning each month's interest is added to the principal before calculating the next month's interest.

**Example:**
- Account balance: $1,000
- APR: 1.2%
- After 1 month: $1,001.00
- After 12 months: $1,012.07 (compounded)

## Validation Rules

### Account Creation
- IDs must be exactly 8 digits
- IDs must be unique across all accounts
- APR must be 0-10 (inclusive)
- Commands are case-insensitive (e.g., "Create" or "create")

### Deposits
- Amount must be positive
- Must respect account-specific limits
- Account must exist

### Withdrawals
- Amount must be positive
- Cannot exceed account balance (balance goes to $0 if insufficient)
- Savings accounts: Only once per month
- CD accounts: Only after 12 months, full balance only

### Transfers
- Both accounts must exist
- Follows withdrawal rules for source account
- Amount must be positive

## Testing

The project includes comprehensive test coverage:
- **Line Coverage**: 98%
- **Mutation Coverage**: 95%
- **Total Tests**: 300+

### Test Categories
- Unit tests for each account type
- Validation tests for all command types
- Integration tests for command processing
- Edge case and boundary condition tests
- Mutation testing with PIT framework

### Running Tests
```bash
# Run all tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport

# Run mutation testing
./gradlew pitest
```

## Technology Stack

- **Language**: Java 11+
- **Testing**: JUnit 5
- **Build Tool**: Gradle
- **Mutation Testing**: PIT (Pitest)
- **Code Coverage**: JaCoCo

## Key Learning Outcomes

### Software Engineering Concepts
- Test-Driven Development (TDD)
- Object-Oriented Programming principles
- Design patterns and software architecture
- Input validation and error handling
- Code quality metrics and testing strategies

### Technical Skills
- Java inheritance and polymorphism
- Unit testing with JUnit
- Mutation testing for robust code
- Command pattern implementation
- Dependency injection for testability

## Example Usage Scenario

```
# Create accounts
Create savings 12345678 0.6
Create checking 98765432 0.01

# Make deposits
Deposit 12345678 700
Deposit 98765432 300

# Transfer money
Transfer 98765432 12345678 300

# Advance time by 1 month
Pass 1

# Create CD after time passes
Create cd 23456789 1.2 2000

# Final state:
# Savings 12345678: Balance $1000.50 (700 + 300 transfer + interest)
# Checking 98765432: Removed (balance $0 after transfer)
# CD 23456789: Balance $2000.00
```

## Future Enhancements

Potential improvements for the system:
- Persistent storage (database integration)
- User authentication and security
- Transaction history reporting
- Account statements and summaries
- Web-based or GUI interface
- Multi-currency support
- Loan and credit features

## Author

Thu Rein Linn

## License

This project is created for educational purposes as part of SE201 coursework.

---

*Last Updated: December 2024*
