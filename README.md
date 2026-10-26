# QUICK RASHI Android launcher (V 14.2.5)

Simple Android browser launcher for the specified Google Apps Script URL.

## Build for free with GitHub Actions
1. Create a GitHub repository (private is recommended), e.g. `quick-rashi-android`.
2. Upload **the project contents** (not the ZIP as a single file), including hidden `.github/workflows/build-apk.yml`.
3. Open Actions > Build QUICK RASHI APK > Run workflow.
4. When green, open run > Artifacts > QUICK_RASHI_V14_2_5_APK and download ZIP containing `app-debug.apk`.
5. Install the APK on Android (allow installation from your file manager if prompted).

## Behavior
- Uses Chrome Custom Tabs (or compatible default browser) for Chrome login, saved passwords, autofill, cookies, camera/browser permission handling.
- Opens only the provided Google Apps Script URL on app launch; links opened by that web app behave as normal browser links.
- Android Back returns to previous browser page or exits the tab.
- Requires internet, Chrome / compatible browser installed. No Android camera permission requested by launcher; camera permission is managed by browser and website.
- Embedded Google Apps Script camera iframe may still be blocked. Use the separately hosted HTTPS camera-scanner page in the web app.

## Notes
- This project has not been compiled in the ChatGPT environment because Android SDK/Gradle were unavailable. The GitHub workflow performs the build.
- Debug APK is signed with Android debug key, suitable for direct testing (not Play Store publication).
- Chrome Custom Tabs is browser-based: it does NOT embed Chrome invisibly or guarantee a fully address-bar-free experience.
