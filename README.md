# Banking System (Java, Test-Driven Development)

A command-driven banking engine built in Java with test-driven development. It processes text commands across three account types, separates command validation from business logic, and is backed by 355 JUnit tests with mutation testing.

SE 201 Software Engineering course project, September–December 2025.

| At a glance | |
|---|---|
| Account types | 3 (Checking, Savings, CD) |
| Commands | 5 (Create, Deposit, Withdraw, Transfer, Pass) |
| JUnit tests | 355, all passing |
| Line coverage (JaCoCo) | 98.5% (408 of 414 lines) |
| Mutation coverage (PIT) | 96.6% (281 of 291 mutants killed) |

## How it works

The program reads a list of text commands, processes them in order, and prints the final state of every open account followed by every command that was rejected.

Each command goes through two steps:

```
Input command
     │
     ▼
MasterControl ──► CommandValidator ──► Create / Deposit / Withdraw / Transfer / Pass validator
     │
     ├── valid   ──► CommandProcessor ──► Bank ──► Account (Checking / Savings / CD)
     │
     └── invalid ──► CommandStorage (logged and printed at the end)
```

Validation and execution are kept in separate classes. Validators only decide whether a command is legal and never change any balances. `CommandProcessor` only executes commands that have already passed validation. Each part can be tested on its own, and a rule change touches exactly one validator.

## Account types

| | Checking | Savings | CD (Certificate of Deposit) |
|---|---|---|---|
| Opening balance | $0 | $0 | Set at creation (must be > 0) |
| Max deposit | $1,000 | $2,500 | Deposits not allowed |
| Max withdrawal | $400 | $1,000, once per month | Full balance only, after 12 months |

## Commands

| Command | Format | Example |
|---|---|---|
| Create | `Create <checking\|savings> <id> <apr>`<br>`Create cd <id> <apr> <balance>` | `Create savings 12345678 2.5` |
| Deposit | `Deposit <id> <amount>` | `Deposit 12345678 500` |
| Withdraw | `Withdraw <id> <amount>` | `Withdraw 12345678 300` |
| Transfer | `Transfer <from-id> <to-id> <amount>` | `Transfer 12345678 87654321 250` |
| Pass | `Pass <months>` | `Pass 12` |

### Validation rules

- **IDs** must be exactly 8 digits and unique.
- **APR** must be between 0 and 10, inclusive.
- **Amounts** must be positive numbers. Non-numeric input is rejected, not crashed on.
- **Withdrawing more than the balance** is allowed within the limits above and empties the account to $0. Balances never go negative.
- **Transfers** require both accounts to exist and follow the source account's withdrawal rules. The destination has no deposit limit. A transfer out of Savings counts as that month's withdrawal.
- **Pass** accepts 1–60 months.
- **Command words** are case-insensitive (`create`, `CREATE`), except `Withdraw`, which must be capitalized exactly. The tests assert this behavior.

### What `Pass` does

For every account, in order:

1. Applies monthly compound interest: `balance += balance × (APR / 100) / 12`, once per month passed.
2. Resets the Savings once-per-month withdrawal limit.
3. Advances CD maturity.
4. Closes any account whose balance is below $25.

Example: $1,000 at 1.2% APR becomes $1,001.00 after 1 month and $1,012.07 after 12 months.

## Design

```
                Account  «abstract»
         id, apr, balance, transaction history
         deposit() · withdraw() · applyInterest()
                         ▲
          ┌──────────────┼──────────────┐
      Checking        Savings           CD
                 + monthly withdrawal  + 12-month maturity
                   flag
```

- **Inheritance and abstraction:** `Account` holds the shared state and behavior. `Checking`, `Savings` and `CD` extend it and add only their own rules.
- **Polymorphism:** `Bank` stores every account as an `Account` in a `HashMap` keyed by ID, so `Pass` applies interest to all of them in one loop.
- **Encapsulation:** internal lists are private and returned as copies.
- **Composition and dependency injection:** `MasterControl` receives its validator, processor and storage through its constructor, which lets tests assemble the system with real or substitute parts.

### Project structure

```
src/main/java/banking/
├── Account.java                    # Abstract base class
├── Checking.java                   # Checking account
├── Savings.java                    # Savings account with monthly withdrawal tracking
├── CD.java                         # Certificate of Deposit with maturity tracking
├── Transaction.java                # Record of a single deposit or withdrawal
├── Bank.java                       # Account storage and time progression
├── CommandValidator.java           # Routes each command to its validator
├── CreateCommandValidator.java
├── DepositCommandValidator.java
├── WithdrawCommandValidator.java
├── TransferCommandValidator.java
├── PassCommandValidator.java
├── CommandProcessor.java           # Executes validated commands
├── CommandStorage.java             # Stores invalid commands
└── MasterControl.java              # Runs the pipeline and builds the output
```

## Testing

The code was written test-first (red, green, refactor). Each production class has its own test class.

| Test type | What it covers | Examples |
|---|---|---|
| Unit | One class at a time | Account math, CD maturity, each validator's rules |
| Integration | Real objects working together | `CommandProcessorTest`, `MasterControlTest`: command list in, final report out |
| Boundary | Values at the exact edge of each rule | $400 vs $400.01 withdrawals, 7/8/9-digit IDs, APR 0 and 10, CD at 11 vs 12 months |

Tests per class: CreateCommandValidator 50, WithdrawCommandValidator 50, CommandProcessor 45, Bank 34, DepositCommandValidator 31, Account 29, TransferCommandValidator 27, CommandValidator 19, CD 17, PassCommandValidator 16, Transaction 13, Savings 10, CommandStorage 7, MasterControl 4, Checking 3.

### Coverage

- **Line coverage, 98.5%:** 408 of 414 lines run during the tests. The core money logic (`Account`, `Bank`, `Savings`, `CD`) is at 100%. Most of the uncovered lines are defensive branches that validation makes unreachable.
- **Mutation coverage, 96.6%:** PIT made 291 small changes to the code, such as flipping `<=` to `<` on the $400 withdrawal limit, negating conditions and removing method calls. The test suite failed on 281 of them. Of the 10 that survived, one is an equivalent mutant (`balance < 0` vs `balance <= 0` when clamping to zero), and most of the rest sit behind overlapping validation checks.

Line coverage shows that the tests run the code. Mutation coverage shows that the tests would catch a bug in it.

## Continuous integration

This project was developed in the course's GitLab instance and later uploaded to GitHub. The GitLab pipeline in [`.gitlab-ci.yml`](.gitlab-ci.yml) ran on every commit inside the `gradle:6.6.1-jdk11` Docker image:

| Stage | Job | Purpose |
|---|---|---|
| build | `build` | Compile, caching dependencies between runs |
| test | `test` | Run all JUnit tests and generate the JaCoCo coverage report |
| test | `mutation-coverage` | Run PIT and publish its HTML report |
| test | `code_quality` | SonarJava static analysis through CodeClimate; findings fail the pipeline |
| deploy | `coverage-jdk11` | Convert JaCoCo output to Cobertura so GitLab shows line coverage in merge requests |

## Running it

Requires JDK 11 and Gradle 6.6–7.6. (Gradle 8+ is not supported by this build file, and PIT 1.5.2 needs Java 11 bytecode.)

```bash
gradle test                    # run all tests
gradle test jacocoTestReport   # line coverage  -> builds/jacoco/jacoco.xml
gradle pitest                  # mutation tests -> build/reports/pitest/
```

Or use the same Docker image as the CI pipeline:

```bash
docker run --rm -v "$PWD":/proj -w /proj gradle:6.6.1-jdk11 gradle test jacocoTestReport pitest
```

## Example

```
Create savings 12345678 0.6
Create checking 98765432 0.01
Deposit 12345678 700
Deposit 98765432 300
Transfer 98765432 12345678 300
Pass 1
Create cd 23456789 1.2 2000
```

Result:

- Savings 12345678: $1,000.50 (700 + 300 transferred + one month of interest)
- Checking 98765432: closed (balance fell below $25 after the transfer)
- CD 23456789: $2,000.00

## Known limitations

These are deliberate scope choices for a course project, and the changes I would make next:

- **Money is stored as `double`.** Floating-point rounding builds up when interest compounds. A production system would use `BigDecimal` or whole cents.
- **Validators use `instanceof` checks** to apply per-account limits. Moving the limits into overridable methods on each account (for example `getMaxWithdrawal()`) would let new account types be added without editing every validator.
- **The ID check is duplicated** across four validators and could be moved into a shared helper.
- **Storage is in memory only.** There is no persistence and no support for concurrent users.

## Tech stack

| Tool | Used for |
|---|---|
| Java 11 | Application code |
| JUnit 5 | Unit, integration and boundary tests |
| Gradle | Build, dependency management and test tasks |
| JaCoCo | Line coverage |
| PIT (Pitest) 1.5.2 | Mutation testing |
| GitLab CI/CD + Docker | Automated pipeline on every commit |
| SonarJava (CodeClimate) | Static code-quality analysis |

## Author

Thu Rein Linn
