# Contributing to Personal Financial Tracker Mobile App

First off, thank you for considering contributing to the Personal Financial Tracker Mobile App! It's people like you that make this app such a great tool.

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the issue list as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

* **Use a clear and descriptive title**
* **Describe the exact steps which reproduce the problem**
* **Provide specific examples to demonstrate the steps**
* **Describe the behavior you observed after following the steps**
* **Explain which behavior you expected to see instead and why**
* **Include screenshots and animated GIFs if possible**
* **Include your environment details** (Android version, device model, app version)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

* **Use a clear and descriptive title**
* **Provide a step-by-step description of the suggested enhancement**
* **Provide specific examples to demonstrate the steps**
* **Describe the current behavior and expected behavior**
* **Explain why this enhancement would be useful**

### Pull Requests

* Fill in the required template
* Follow the Kotlin/Android styleguides
* End all files with a newline
* Avoid platform-dependent code
* Document new code based on the Documentation Styleguide

## Development Setup

### Prerequisites

- Android Studio (Latest version)
- JDK 11 or higher
- Android SDK (API 24 minimum)
- Git

### Getting Started

1. **Fork the repository**
   ```bash
   # Click the "Fork" button on GitHub
   ```

2. **Clone your fork**
   ```bash
   git clone https://github.com/YOUR-USERNAME/PersonalFinancialTracker_MobileApp.git
   cd PersonalFinancialTracker_MobileApp
   ```

3. **Add upstream remote**
   ```bash
   git remote add upstream https://github.com/Mapzoll/PersonalFinancialTracker_MobileApp.git
   ```

4. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   # or for bug fixes:
   git checkout -b fix/bug-name
   ```

5. **Open in Android Studio**
   - File → Open → Select the project folder
   - Wait for Gradle sync to complete

6. **Make your changes**
   - Write clean, readable code
   - Add tests for new functionality
   - Update documentation as needed

7. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

8. **Create a Pull Request**
   - Go to the original repository
   - Click "New Pull Request"
   - Select your branch
   - Fill in the PR template
   - Submit for review

## Styleguides

### Kotlin Code Style

* Use 4 spaces for indentation (not tabs)
* Use meaningful variable and function names
* Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
* Use `val` instead of `var` when possible
* Add type annotations for public APIs

**Example:**
```kotlin
// Good
fun calculateTotal(items: List<Item>): Double {
    return items.sumOf { it.price }
}

// Bad
fun calc(i: List<Item>): Double {
    var t = 0.0
    for (item in i) {
        t += item.price
    }
    return t
}
```

### Compose Code Style

* Use descriptive names for composable functions
* Keep composables small and focused
* Use proper lambda trailing syntax
* Add `@Composable` and `@Preview` annotations

**Example:**
```kotlin
@Composable
fun ExpenseCard(
    expense: Expense,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Text(expense.name)
    }
}

@Preview
@Composable
fun ExpenseCardPreview() {
    ExpenseCard(Expense("Groceries", 50.0))
}
```

### Commit Messages

* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
* Limit the first line to 72 characters or less
* Reference issues and pull requests liberally after the first line

**Format:**
```
Add feature: Brief description of changes

- Detailed explanation of what was changed
- Why this change was made
- Any relevant context or references

Fixes #123
```

### Documentation

* Write clear, concise comments for complex logic
* Use KDoc for public APIs
* Update README.md if behavior changes
* Update CHANGELOG.md with significant changes

**Example:**
```kotlin
/**
 * Calculates the total expense for a given category.
 *
 * @param category The category to calculate total for
 * @return The sum of all expenses in the category
 */
fun calculateCategoryTotal(category: String): Double {
    return expenses
        .filter { it.category == category }
        .sumOf { it.amount }
}
```

## Testing

* Write unit tests for new features
* Ensure existing tests pass
* Aim for at least 80% code coverage for critical features
* Use descriptive test names

**Test naming convention:**
```kotlin
@Test
fun shouldCalculateTotalExpensesCorrectly() {
    // Arrange
    val expenses = listOf(
        Expense("Food", 50.0),
        Expense("Transport", 30.0)
    )
    
    // Act
    val total = calculateTotal(expenses)
    
    // Assert
    assertEquals(80.0, total)
}
```

## Build & Test Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run all tests
./gradlew test

# Run instrumented tests on device/emulator
./gradlew connectedAndroidTest

# Check code quality
./gradlew lint

# Clean build
./gradlew clean
```

## Git Workflow

1. **Create a feature branch**
   ```bash
   git checkout -b feature/add-budget-tracking
   ```

2. **Make commits with clear messages**
   ```bash
   git commit -m "Add: Budget tracking feature"
   ```

3. **Keep your branch updated**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

4. **Push to your fork**
   ```bash
   git push origin feature/add-budget-tracking
   ```

5. **Create Pull Request on GitHub**

## Pull Request Process

1. Update the README.md with any new features or changes
2. Update the CHANGELOG.md following the format specified
3. Increase version numbers in gradle files if applicable
4. Ensure all tests pass locally
5. Request review from maintainers
6. Address any requested changes
7. Once approved, your PR will be merged

## Branch Naming Convention

* `feature/feature-name` - For new features
* `fix/bug-name` - For bug fixes
* `docs/description` - For documentation updates
* `refactor/description` - For refactoring
* `test/description` - For test additions

## Additional Notes

### Issue and Pull Request Labels

* `bug` - Something isn't working
* `enhancement` - New feature or request
* `documentation` - Improvements or additions to documentation
* `good first issue` - Good for newcomers
* `help wanted` - Extra attention is needed
* `question` - Further information is requested
* `wontfix` - This will not be worked on

### Community

* Join our discussions for questions and ideas
* Be respectful and constructive
* Help other contributors when you can
* Share knowledge and experience

## Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes for significant contributions
- CHANGELOG.md for major features

## Questions?

* Check existing [Issues](https://github.com/Mapzoll/PersonalFinancialTracker_MobileApp/issues)
* Read the [README.md](README.md)
* Review the [Documentation](docs/)
* Open a new discussion or issue

## License

By contributing to Personal Financial Tracker Mobile App, you agree that your contributions will be licensed under its MIT License.

---

Thank you for contributing! Your help is greatly appreciated! 🎉
