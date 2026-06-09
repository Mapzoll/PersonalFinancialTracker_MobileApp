# Security Policy

## Reporting Security Vulnerabilities

**Please do not disclose security vulnerabilities through public GitHub issues.**

If you discover a security vulnerability in the Personal Financial Tracker Mobile App, please report it by emailing [security contact - to be updated]. Please include:

1. Description of the vulnerability
2. Steps to reproduce the issue
3. Impact assessment
4. Suggested fix (if available)

We will acknowledge your report within 48 hours and provide an estimated timeline for the fix.

## Security Considerations

### Authentication & Authorization

- All user credentials are handled through Firebase Authentication
- Passwords are never stored locally
- Biometric authentication uses Android's BiometricPrompt API
- Sessions are managed securely with proper token expiration

### Data Protection

- Sensitive data (financial information) is encrypted both in transit and at rest
- Firebase Firestore enforces security rules for data access
- Local database (Room) can be encrypted using SQLCipher
- All network requests use HTTPS

### Permissions

The app requests only necessary permissions:
- `CAMERA` - For receipt capture
- `ACCESS_FINE_LOCATION` - For location-based tracking
- `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` - For file access
- `USE_BIOMETRIC` - For biometric authentication

### Dependencies

- All dependencies are regularly updated to their latest secure versions
- We monitor security advisories from:
  - Google Security Advisories
  - Firebase Security Updates
  - NVD (National Vulnerability Database)
  - Dependabot alerts

### Code Security

- No hardcoded secrets or API keys in the repository
- Sensitive configuration stored in secure Firebase console
- Code reviews mandatory for all pull requests
- Regular security audits of critical components

## Best Practices for Users

1. **Keep the app updated** - Always use the latest version
2. **Secure your device** - Use a strong unlock method
3. **Use biometric authentication** - When available
4. **Review permissions** - Only grant necessary permissions
5. **Report issues** - Report any suspicious behavior immediately

## Security Updates

- Security patches will be released as soon as possible
- Critical vulnerabilities will receive emergency releases
- All security updates will be documented in CHANGELOG.md
- Users will be notified through app updates

## Supported Versions

| Version | Supported          |
|---------|-------------------|
| 1.0.x   | ✅ Active Support |
| 0.2.x   | ⚠️ Limited Support |
| < 0.2   | ❌ Not Supported   |

## Compliance

This project follows:
- [OWASP Mobile Security](https://owasp.org/www-project-mobile-security/)
- [Google Play Security & Privacy Policies](https://play.google.com/about/developer-content-policy/)
- [Android Security & Privacy Best Practices](https://developer.android.com/privacy)

## Third-Party Dependencies

We regularly audit third-party dependencies for security vulnerabilities. See `CHANGELOG.md` for security-related updates.

## Contact

For security concerns, contact: [security contact - to be updated]

**Last Updated**: 2026-06-09
